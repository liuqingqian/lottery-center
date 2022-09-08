package com.hermes.lotcenter.domain.rpc.fallback;

import com.hermes.lotcenter.domain.rpc.LotteryBetClient;
import com.hermes.lotcenter.domain.rpc.response.DoBetResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Slf4j
@Component
public class LotteryBetClientFallbackFactory implements FallbackFactory<LotteryBetClient> {
    @Override
    public LotteryBetClient create(Throwable throwable) {
        return new LotteryBetClient() {

            @Override
            public DoBetResponse doBet(Map<String, ?> formCriteria) {
                log.error("rpc LotteryBetClient doBet error:{}", throwable.getMessage());
                return null;
            }
        };
    }
}
