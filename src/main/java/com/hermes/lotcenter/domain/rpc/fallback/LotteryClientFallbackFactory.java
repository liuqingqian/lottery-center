package com.hermes.lotcenter.domain.rpc.fallback;

import com.hermes.lotcenter.domain.rpc.LotteryClient;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryLastCriteria;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryOptCriteria;
import com.hermes.lotcenter.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.domain.rpc.response.MemberInfoResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Slf4j
@Component
public class LotteryClientFallbackFactory implements FallbackFactory<LotteryClient> {
    @Override
    public LotteryClient create(Throwable throwable) {
        return new LotteryClient() {
            @Override
            public MemberInfoResponse memberInfo() {
                log.error("rpc LotteryClient memberInfo error:{}", throwable.getMessage());
                return null;
            }

            @Override
            public LotteryOptResponse lotteryOpt(LotteryOptCriteria criteria) {
                log.error("rpc LotteryClient lotteryOpt error:{}", throwable.getMessage());
                return null;
            }

            @Override
            public LotteryLastResponse lotteryLast(LotteryLastCriteria criteria) {
                log.error("rpc LotteryClient lotteryLast error:{}", throwable.getMessage());
                return null;
            }
        };
    }
}