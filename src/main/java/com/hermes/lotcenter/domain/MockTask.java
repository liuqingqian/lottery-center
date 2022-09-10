package com.hermes.lotcenter.domain;

import com.beicai.common.DateTimeUtil;
import com.hermes.lotcenter.domain.dto.StrategyResultDTO;
import com.hermes.lotcenter.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.domain.rpc.response.MemberInfoResponse;
import com.hermes.lotcenter.domain.strategy.SpittleStrategyImpl;
import com.hermes.lotcenter.entity.UserBetRecordEntity;
import com.hermes.lotcenter.entity.UserBetTaskEntity;
import com.hermes.lotcenter.infrastructure.enums.BetTaskStatusEnum;
import com.hermes.lotcenter.infrastructure.enums.StrategyResultStatusEnum;
import com.hermes.lotcenter.infrastructure.enums.UserBetRecordStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Component
public class MockTask {

    @Autowired
    private UserBetTaskDomain userBetTaskDomain;

    @Autowired
    private LotteryBetDomain lotteryBetDomain;

    @Autowired
    private UserBetRecordDomain userBetRecordDomain;

    @Autowired
    private DoBetDomain doBetDomain;

    @Autowired
    private SpittleStrategyImpl spittleStrategy;


    @Scheduled(cron = "4 * * * * ?")
    public void task() {

        String lotCode = "FFK3";
        String taskNo = "T202209090000001";

        MemberInfoResponse memberInfoResponse = lotteryBetDomain.memberInfo(taskNo);
        UserBetTaskEntity userBetTask = userBetTaskDomain.query(taskNo);
        LotteryOptResponse lotteryOptResponse = lotteryBetDomain.lotteryOpt(lotCode);

        String accountName = memberInfoResponse.getAccount();
        Double accountAmount = memberInfoResponse.getMoney();
        String serverTime = memberInfoResponse.getServerTime();
        System.out.println("触发时间 = " + DateTimeUtil.nowStr());
        System.out.println("serverTime = " + serverTime);
        System.out.println("accountName = " + accountName + ",accountAmount = " + accountAmount);

        if (Objects.isNull(userBetTask)) {
            System.out.println("userBetTask is null");

        } else {
            System.out.println("userBetTask = " + userBetTask);
            LotteryOptResponse.Last last = lotteryOptResponse.getLast();
            LotteryOptResponse.Current current = lotteryOptResponse.getCurrent();
            System.out.println("last = " + last);
            System.out.println("current = " + current);
            //处理上一次中断导致未开奖状态的投注记录
            List<UserBetRecordEntity> notLotEntities = userBetRecordDomain.queryByTaskStatusList(taskNo, lotCode, UserBetRecordStatusEnum.NOT_LOT.getCode());
            notLotEntities.stream()
                    .filter(userBetRecordEntity -> !userBetRecordEntity.getPeriodNumber().equals(last.getQiHao()))
                    .forEach(notLotBetRecord -> {
                        String code = notLotBetRecord.getCode();
                        String periodNumber = notLotBetRecord.getPeriodNumber();
                        LotteryLastResponse lotteryLastResponse = lotteryBetDomain.lotteryLast(code, periodNumber);
                        if (Objects.nonNull(lotteryLastResponse) && lotteryLastResponse.isSuccess()) {
                            UserBetRecordEntity lastBetRecord = userBetRecordDomain.query(taskNo, code, periodNumber);
                            LotteryLastResponse.Last lastLast = lotteryLastResponse.getLast();
                            LotteryOptResponse.Last lastOpt = new LotteryOptResponse.Last();
                            lastOpt.setQiHao(lastLast.getQiHao());
                            lastOpt.setHaoMa(lastLast.getHaoMa());
                            UserBetRecordEntity updateLastBetRecord = doBetDomain.doLottery(lastOpt, lastBetRecord);
                            int updateBetRecord = userBetRecordDomain.updateBetRecord(taskNo, lotCode, periodNumber, updateLastBetRecord);
                            System.out.println("[Adjust][UpdateLastBetRecord] updateLastBetRecord = " + updateLastBetRecord + ",updateBetRecord modify " + updateBetRecord);
                        }
                    });

            Integer userBetTaskStatus = userBetTask.getStatus();
            BetTaskStatusEnum betTaskStatusEnum = BetTaskStatusEnum.fromCode(userBetTaskStatus);
            if (BetTaskStatusEnum.DOING.equals(userBetTaskStatus)) {
                System.out.println("[任务]执行中........");
                if (lotteryOptResponse.isSuccess()) {

                    //0、初始化批次投注信息，账号初试金额、该批次当前回撤、当前盈利、是否触发了停止任务：投注次数、投注金额、止损或止盈；
                    //1、更新上一期开奖的中奖结果 last
                    String lastQiHao = last.getQiHao();
                    UserBetRecordEntity lastBetRecord = userBetRecordDomain.query(taskNo, lotCode, lastQiHao);

                    UserBetRecordEntity updateLastBetRecord = doBetDomain.doLottery(last, lastBetRecord);
                    int updateBetRecord = userBetRecordDomain.updateBetRecord(taskNo, lotCode, lastQiHao, updateLastBetRecord);
                    System.out.println("[UpdateLastBetRecord] updateLastBetRecord = " + updateLastBetRecord + ",updateBetRecord modify " + updateBetRecord);
                    //2、计算本期应该投注的金额和号码 current
                    //查询最新状态的上一期投注记录信息
                    UserBetRecordEntity updatedLastBetRecord = userBetRecordDomain.query(taskNo, lotCode, lastQiHao);
                    StrategyResultDTO strategyResultDTO = spittleStrategy.strategy(last, updatedLastBetRecord, current, userBetTask);
                    System.out.println("strategyResultDTO = " + strategyResultDTO);
                    if (StrategyResultStatusEnum.SUCCESS.equals(strategyResultDTO.getStatus())) {
                        UserBetRecordEntity betRecordEntity = doBetDomain.toBetRecord(strategyResultDTO, userBetTask);
                        int insertBetRecord = userBetRecordDomain.insert(betRecordEntity);
                        System.out.println("[InsertBetRecord] betRecord = " + betRecordEntity + ",insertBetRecord add " + insertBetRecord);
                        if (insertBetRecord > 0) {
                            //插入投注记录成功则发送投注请求
                            System.out.println("模拟发送投注请求");
                        }
                    }
                }

            } else {
                System.out.println("[任务]未执行，BetTaskStatus：" + betTaskStatusEnum.getDesc());
            }
        }
    }
}
