package com.hermes.lotcenter.domain.rpc;

import com.hermes.lotcenter.domain.LotteryConfig;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryLastCriteria;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryOptCriteria;
import com.hermes.lotcenter.domain.rpc.fallback.LotteryClientFallbackFactory;
import com.hermes.lotcenter.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.domain.rpc.response.MemberInfoResponse;
import com.hermes.lotcenter.infrastructure.configuration.FeignConverterConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
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

    @PostMapping(value = "/meminfo.do", headers = {"Referer=" + Referer.LOTTERY_INDEX, "X-Requested-With=XMLHttpRequest",
            "Cookie=SESSION=" + LotteryConfig.SESSION})
    MemberInfoResponse memberInfo();

    @PostMapping(value = "/lotteryV2/lotV2Op.do", headers = {"Referer=" + Referer.LOTTERY_INDEX, "X-Requested-With=XMLHttpRequest"})
    LotteryOptResponse lotteryOpt(@SpringQueryMap LotteryOptCriteria criteria);


    @PostMapping(value = "/lotteryV2/lotLast.do", headers = {"Referer=" + Referer.LOTTERY_INDEX, "X-Requested-With=XMLHttpRequest"})
    LotteryLastResponse lotteryLast(@SpringQueryMap LotteryLastCriteria criteria);
}
