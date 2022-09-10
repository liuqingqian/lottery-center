package com.hermes.lotcenter.domain.strategy;

import com.hermes.lotcenter.domain.DoBetDomain;
import com.hermes.lotcenter.domain.UserBetRecordDomain;
import com.hermes.lotcenter.domain.dto.SpittleStrategyDataDTO;
import com.hermes.lotcenter.domain.dto.StrategyResultDTO;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.entity.UserBetRecordEntity;
import com.hermes.lotcenter.entity.UserBetTaskEntity;
import com.hermes.lotcenter.infrastructure.enums.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/9.
 * <p>
 * 唾沫投注策略
 */
@Component
public class SpittleStrategyImpl implements IBetStrategy {

    @Autowired
    private DoBetDomain doBetDomain;

    @Autowired
    private UserBetRecordDomain userBetRecordDomain;

    @Override
    public StrategyResultDTO strategy(LotteryOptResponse.Last last, UserBetRecordEntity lastBetRecord, LotteryOptResponse.Current current, UserBetTaskEntity userBetTask) {
        StrategyResultDTO resultDTO = new StrategyResultDTO();
        if (Objects.isNull(last) || Objects.isNull(current) || Objects.isNull(userBetTask)) {
            resultDTO.setMessage("空值参数校验异常");
            resultDTO.setStatus(StrategyResultStatusEnum.FAIL.getCode());
            return resultDTO;
        }
        Integer taskStatus = userBetTask.getStatus();
        if (BetTaskStatusEnum.DOING.notEquals(taskStatus)) {
            resultDTO.setMessage("TaskStatus校验异常");
            resultDTO.setStatus(StrategyResultStatusEnum.FAIL.getCode());
            return resultDTO;
        }
        Integer strategyType = userBetTask.getStrategyType();
        StrategyTypeEnum strategyTypeEnum = StrategyTypeEnum.fromCode(strategyType);
        if (Objects.isNull(strategyTypeEnum) ||
                !(StrategyTypeEnum.POSITIVE_SPITTLE == strategyTypeEnum || StrategyTypeEnum.POSITIVE_SPITTLE == strategyTypeEnum)) {
            resultDTO.setMessage("StrategyType校验异常");
            resultDTO.setStatus(StrategyResultStatusEnum.FAIL.getCode());
            return resultDTO;
        }
        SpittleStrategyDataDTO spittleStrategy = userBetTask.getStrategyData();
        if (Objects.isNull(spittleStrategy) || Objects.isNull(spittleStrategy.getSidedType()) || CollectionUtils.isEmpty(spittleStrategy.getStrategy())) {
            resultDTO.setMessage("StrategyData校验异常");
            resultDTO.setStatus(StrategyResultStatusEnum.FAIL.getCode());
            return resultDTO;
        }
        String lastLotteryHaoMa = last.getHaoMa();
        List<String> lastSidedNames = doBetDomain.convertToName(lastLotteryHaoMa);
        if (CollectionUtils.isEmpty(lastSidedNames)) {
            resultDTO.setMessage("SidedEnum校验异常");
            resultDTO.setStatus(StrategyResultStatusEnum.FAIL.getCode());
            return resultDTO;
        }
        //是否豹子，豹子跳过
        boolean isDual = SidedEnum.DUAL.getName().equals(lastSidedNames.get(0));
        if (isDual) {
            resultDTO.setMessage("豹子跳过");
            resultDTO.setStatus(StrategyResultStatusEnum.SKIP.getCode());
            return resultDTO;
        }

        String taskNo = userBetTask.getTaskNo();
        String code = userBetTask.getCode();
        //判断任务是否中断
//        List<UserBetRecordEntity> userBetRecordEntities = userBetRecordDomain.queryByTaskList(taskNo, code);


        Double betAmount;
        List<Double> strategy = spittleStrategy.getStrategy();
        if (Objects.isNull(lastBetRecord)) {
            //没有上一期投注记录
            betAmount = strategy.get(0);
        } else {
            Double lastBuyMoney = lastBetRecord.getBuyMoney();
            if (strategy.contains(lastBuyMoney) && UserBetRecordStatusEnum.WINNING_LOT.equals(lastBetRecord.getStatus())) {
                //上期中奖，根据上期投注金额计算出当期投注金额
                int prevAmountIndexOf = strategy.indexOf(lastBuyMoney);
                //如果上期策略金额往后还有策略金额，则取策略金额，否则取最后配置的策略金额；
                int currentAmountIndexOf = (prevAmountIndexOf + 1) < strategy.size() ? prevAmountIndexOf + 1 : strategy.size() - 1;
                betAmount = strategy.get(currentAmountIndexOf);
            } else {
                //未中奖或者策略中不包含投注金额时取第一个策略金额
                betAmount = strategy.get(0);
            }
        }
        SidedTypeEnum sidedTypeEnum = SidedTypeEnum.fromCode(spittleStrategy.getSidedType());
        String betHaoMa = null;
        switch (sidedTypeEnum) {
            case SIZE:
                if (StrategyTypeEnum.POSITIVE_SPITTLE == strategyTypeEnum) {
                    betHaoMa = doBetDomain.getSizeSidedName(lastSidedNames);
                } else if (StrategyTypeEnum.NEGATIVE_SPITTLE == strategyTypeEnum) {
                    betHaoMa = doBetDomain.getNegativeSizeSidedName(lastSidedNames);
                }
                break;
            case SINGLE_DOUBLE:
                if (StrategyTypeEnum.POSITIVE_SPITTLE == strategyTypeEnum) {
                    betHaoMa = doBetDomain.getSingleDoubleSidedName(lastSidedNames);
                } else if (StrategyTypeEnum.NEGATIVE_SPITTLE == strategyTypeEnum) {
                    betHaoMa = doBetDomain.getNegativeSingleDoubleSidedName(lastSidedNames);
                }
                break;
        }
        resultDTO.setCode(userBetTask.getCode());
        resultDTO.setQiHao(current.getQiHao());
        resultDTO.setActiveTime(current.getActiveTime());
        resultDTO.setAgo(current.getAgo());
        resultDTO.setHaoMa(betHaoMa);
        resultDTO.setMoney(betAmount);
        resultDTO.setStatus(StrategyResultStatusEnum.SUCCESS.getCode());
        return resultDTO;
    }
}
