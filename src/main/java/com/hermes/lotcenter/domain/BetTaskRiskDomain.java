package com.hermes.lotcenter.domain;

import com.hermes.lotcenter.domain.dto.BetTaskStatsDTO;
import com.hermes.lotcenter.domain.dto.RiskResultDTO;
import com.hermes.lotcenter.entity.UserBetTaskEntity;
import com.hermes.lotcenter.infrastructure.enums.RiskResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/11.
 * <p>
 * 投注风险服务领域
 */
@Component
public class BetTaskRiskDomain {

    @Autowired
    private UserBetTaskDomain userBetTaskDomain;

    @Autowired
    private UserBetRecordDomain userBetRecordDomain;

    public RiskResultDTO checkBetRisk(String taskNo) {

        UserBetTaskEntity userBetTask = userBetTaskDomain.query(taskNo);
        String code = Objects.nonNull(userBetTask) ? userBetTask.getCode() : null;
        return checkBetRisk(taskNo, code, userBetTask);
    }

    public RiskResultDTO checkBetRisk(String taskNo, String code, UserBetTaskEntity userBetTask) {

        RiskResultDTO riskResultDTO = new RiskResultDTO();
        BetTaskStatsDTO betTaskStatsDTO = userBetRecordDomain.queryBetTaskStats(taskNo, code);
        riskResultDTO.setBetTaskStats(betTaskStatsDTO);
        if (Objects.isNull(betTaskStatsDTO) || Objects.isNull(userBetTask)) {
            riskResultDTO.setMessage("BetTaskStats控制校验异常");
            riskResultDTO.setStatus(RiskResultEnum.RISK.getCode());
            return riskResultDTO;
        }
        Integer betTimes = betTaskStatsDTO.getBetTimes();
        Double totalBuyMoney = betTaskStatsDTO.getTotalBuyMoney();
        Double totalWinMoney = betTaskStatsDTO.getTotalWinMoney();

        Integer maxTimes = userBetTask.getMaxTimes();
        Double maxAmount = userBetTask.getMaxAmount();
        Double stopLossAmount = userBetTask.getStopLossAmount();
        Double stopProfitAmount = userBetTask.getStopProfitAmount();
        if (betTimes >= maxTimes) {
            riskResultDTO.setMessage("最大投注次数校验异常");
            riskResultDTO.setStatus(RiskResultEnum.RISK.getCode());
            return riskResultDTO;
        }
        if (totalBuyMoney >= maxAmount) {
            riskResultDTO.setMessage("最大投注金额校验异常");
            riskResultDTO.setStatus(RiskResultEnum.RISK.getCode());
            return riskResultDTO;
        }
        if (totalBuyMoney > totalWinMoney) {
            //亏了
            Double lossAmount = totalBuyMoney - totalWinMoney;
            if (lossAmount > stopLossAmount) {
                riskResultDTO.setMessage("止损金额校验异常");
                riskResultDTO.setStatus(RiskResultEnum.RISK.getCode());
                return riskResultDTO;
            }
        } else {
            Double profitAmount = totalWinMoney - totalBuyMoney;
            if (profitAmount > stopProfitAmount) {
                riskResultDTO.setMessage("止盈金额校验异常");
                riskResultDTO.setStatus(RiskResultEnum.RISK.getCode());
                return riskResultDTO;
            }
        }
        //正常
        riskResultDTO.setStatus(RiskResultEnum.NORMAL.getCode());
        return riskResultDTO;
    }
}
