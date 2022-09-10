package com.hermes.lotcenter.domain.rpc;

import com.hermes.lotcenter.domain.LotteryConfig;
import com.hermes.lotcenter.domain.rpc.fallback.LotteryClientFallbackFactory;
import com.hermes.lotcenter.domain.rpc.response.DoBetResponse;
import com.hermes.lotcenter.infrastructure.configuration.FeignFormSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@FeignClient(
        name = "LC-LotteryBetClient",
        url = LotteryConfig.ORIGIN,
        configuration = {FeignFormSupportConfig.class},
        fallbackFactory = LotteryClientFallbackFactory.class
)
@Deprecated
public interface LotteryBetClient {

    //TODO:用FeignClient发送 From表单数据未设置成功，后续再研究，目前代替方案使用LotteryBetDomain，NetUtil2发送完成；
    @PostMapping(value = "/lotteryV2Bet/dolotV2Bet.do", consumes = APPLICATION_FORM_URLENCODED_VALUE,
            headers = {"Referer=" + LotteryConfig.Referer.LOTTERY_INDEX, "X-Requested-With=XMLHttpRequest",
                    "Cookie=SESSION=" + LotteryConfig.SESSION})
    DoBetResponse doBet(Map<String, ?> formCriteria);
}
