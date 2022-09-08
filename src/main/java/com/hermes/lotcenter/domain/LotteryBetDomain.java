package com.hermes.lotcenter.domain;

import cn.hutool.core.bean.BeanUtil;
import com.hermes.lotcenter.domain.rpc.criteria.DoBetCriteria;
import com.hermes.lotcenter.domain.rpc.response.DoBetResponse;
import com.hermes.lotcenter.infrastructure.utils.JSONTextUtil;
import com.hermes.lotcenter.infrastructure.utils.NetUtil2;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/8.
 * 投注服务领域
 */
@Component
public class LotteryBetDomain {

    public DoBetResponse doBet(DoBetCriteria criteria) {
        if (Objects.isNull(criteria)) {
            return null;
        }
        Map<String, ?> formCriteria = BeanUtil.beanToMap(criteria);
        NetUtil2.Response res = NetUtil2.post(url, formCriteria, headers);

        return JSONTextUtil.parseObject(res.rawContent, DoBetResponse.class);
    }


    private static Map<String, String> headers = new HashMap<>();

    private static String url;

    static {

        url = LotteryConfig.ORIGIN + "/lotteryV2Bet/dolotV2Bet.do";

        String referer = LotteryConfig.ORIGIN + "/lotteryV2/index.do";
        headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Host", LotteryConfig.HOST);
        headers.put("Origin", LotteryConfig.ORIGIN);
        headers.put("Pragma", "no-cache");
        headers.put("Cache-Control", "no-cache");
        headers.put("Proxy-Connection", "keep-alive");
        headers.put("Referer", referer);
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
        headers.put("X-Requested-With", "XMLHttpRequest");
//        Cookie=SESSION=77954a18-d501-46e5-b9e8-aefb529513a1
        headers.put("Cookie", "SESSION=77954a18-d501-46e5-b9e8-aefb529513a1");
    }
}
