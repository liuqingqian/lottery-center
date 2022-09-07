package com.hermes.lotcenter.domain.rpc;

import com.hermes.lotcenter.domain.LotteryConfig;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryOptCriteria;
import com.hermes.lotcenter.domain.rpc.fallback.LotteryClientFallbackFactory;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.infrastructure.configuration.FeignConverterConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import static com.hermes.lotcenter.domain.LotteryConfig.Referer;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@FeignClient(
        name = "LC-LotteryClient",
        url = LotteryConfig.ORIGIN,
        configuration = {FeignConverterConfiguration.class},
        fallbackFactory = LotteryClientFallbackFactory.class
)
public interface LotteryClient {


    @PostMapping(value = "/lotteryV2/lotV2Op.do", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE},
            headers = {"Referer=" + Referer.LOTTERY_OPT, "X-Requested-With=XMLHttpRequest"})
    LotteryOptResponse lotteryOpt(LotteryOptCriteria criteria);
}
