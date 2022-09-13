package com.hermes.lotcenter.domain;

import com.beicai.common.DateTimeUtil;
import com.hermes.lotcenter.domain.dto.StrategyResultDTO;
import com.hermes.lotcenter.domain.rpc.criteria.DoBetCriteria;
import com.hermes.lotcenter.domain.rpc.response.DoBetResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.domain.rpc.response.MemberInfoResponse;
import com.hermes.lotcenter.domain.strategy.SpittleStrategyImpl;
import com.hermes.lotcenter.entity.UserBetRecordEntity;
import com.hermes.lotcenter.entity.UserBetTaskEntity;
import com.hermes.lotcenter.infrastructure.enums.BetDataIdEnum;
import com.hermes.lotcenter.infrastructure.enums.BetTaskStatusEnum;
import com.hermes.lotcenter.infrastructure.enums.StrategyResultStatusEnum;
import com.hermes.lotcenter.infrastructure.enums.UserBetRecordStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/12.
 */
@Slf4j
@Component
public class DoBetTaskDomain {

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


    public void doBet(String taskNo) {

        MemberInfoResponse memberInfoResponse = lotteryBetDomain.memberInfo(taskNo);
        UserBetTaskEntity userBetTask = userBetTaskDomain.query(taskNo);

        String accountName = memberInfoResponse.getAccount();
        Double accountAmount = memberInfoResponse.getMoney();
        String serverTime = memberInfoResponse.getServerTime();
        log.info("[DoBetTask]触发时间 = " + DateTimeUtil.nowStr());
        log.info("serverTime = " + serverTime);
        log.info("accountName = " + accountName + ",accountAmount = " + accountAmount);

        if (!memberInfoResponse.getLogin() || Objects.isNull(userBetTask)) {
            log.info("userBetTask is null or User Login false");

        } else {
            String lotCode = userBetTask.getCode();
            LotteryOptResponse lotteryOptResponse = lotteryBetDomain.lotteryOpt(lotCode);
            log.info("userBetTask = " + userBetTask);
            LotteryOptResponse.Last last = lotteryOptResponse.getLast();
            LotteryOptResponse.Current current = lotteryOptResponse.getCurrent();
            log.info("last = " + last);
            log.info("current = " + current);
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
                            log.info("[Adjust][UpdateLastBetRecord] updateLastBetRecord = " + updateLastBetRecord + ",updateBetRecord modify " + updateBetRecord);
                        }
                    });

            Integer userBetTaskStatus = userBetTask.getStatus();
            BetTaskStatusEnum betTaskStatusEnum = BetTaskStatusEnum.fromCode(userBetTaskStatus);
            if (BetTaskStatusEnum.DOING.equals(userBetTaskStatus)) {
                log.info("[任务]执行中........");
                if (lotteryOptResponse.isSuccess()) {

                    //0、初始化批次投注信息，账号初试金额、该批次当前回撤、当前盈利、是否触发了停止任务：投注次数、投注金额、止损或止盈；
                    //1、更新上一期开奖的中奖结果 last
                    String lastQiHao = last.getQiHao();
                    UserBetRecordEntity lastBetRecord = userBetRecordDomain.query(taskNo, lotCode, lastQiHao);

                    UserBetRecordEntity updateLastBetRecord = doBetDomain.doLottery(last, lastBetRecord);
                    int updateBetRecord = userBetRecordDomain.updateBetRecord(taskNo, lotCode, lastQiHao, updateLastBetRecord);
                    log.info("[UpdateLastBetRecord] updateLastBetRecord = " + updateLastBetRecord + ",updateBetRecord modify " + updateBetRecord);
                    //2、计算本期应该投注的金额和号码 current
                    //查询最新状态的上一期投注记录信息
                    UserBetRecordEntity updatedLastBetRecord = userBetRecordDomain.query(taskNo, lotCode, lastQiHao);
                    StrategyResultDTO strategyResultDTO = spittleStrategy.strategy(last, updatedLastBetRecord, current, userBetTask);
                    log.info("strategyResultDTO = " + strategyResultDTO);
                    if (StrategyResultStatusEnum.SUCCESS.equals(strategyResultDTO.getStatus())) {
                        UserBetRecordEntity betRecordEntity = doBetDomain.toBetRecord(strategyResultDTO, userBetTask);
                        int insertBetRecord = userBetRecordDomain.insert(betRecordEntity);
                        log.info("[InsertBetRecord] betRecord = " + betRecordEntity + ",insertBetRecord add " + insertBetRecord);
                        if (insertBetRecord > 0) {
                            //插入投注记录成功则发送投注请求
                            // sleep in milliseconds
                            int sleepSeconds = RandomUtils.nextInt(5, 25);
                            log.info("准备发送投注请求，" + sleepSeconds + " 秒后执行......");
                            try {
                                Thread.sleep(sleepSeconds * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String qiHao = strategyResultDTO.getQiHao();
                            String haoMa = strategyResultDTO.getHaoMa();
                            Double money = strategyResultDTO.getMoney();
                            BetDataIdEnum dataIdEnum = BetDataIdEnum.fromName(haoMa);
                            DoBetCriteria criteria = doBetDomain.buildCriteria(lotCode, qiHao, money, dataIdEnum);
                            DoBetResponse response = lotteryBetDomain.doBet(taskNo, criteria);
                            log.info("发送投注请求Response=" + response);
                        }
                    }
                }

            } else {
                log.info("[任务]未执行，BetTaskStatus：" + betTaskStatusEnum.getDesc());
            }
        }
    }
}
