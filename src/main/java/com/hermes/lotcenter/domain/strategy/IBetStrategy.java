package com.hermes.lotcenter.domain.strategy;

import com.hermes.lotcenter.domain.dto.StrategyResultDTO;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.entity.UserBetRecordEntity;
import com.hermes.lotcenter.entity.UserBetTaskEntity;

/**
 * Created by liuqingqian on 2022/9/9.
 */
public interface IBetStrategy {

    StrategyResultDTO strategy(LotteryOptResponse.Last last, UserBetRecordEntity lastBetRecord, LotteryOptResponse.Current current, UserBetTaskEntity userBetTask);
}
