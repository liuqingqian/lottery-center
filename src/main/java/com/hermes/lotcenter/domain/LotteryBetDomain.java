package com.hermes.lotcenter.domain;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Maps;
import com.hermes.lotcenter.domain.rpc.LotteryClient;
import com.hermes.lotcenter.domain.rpc.criteria.DoBetCriteria;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryLastCriteria;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryOptCriteria;
import com.hermes.lotcenter.domain.rpc.response.DoBetResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.domain.rpc.response.MemberInfoResponse;
import com.hermes.lotcenter.entity.UserBetTaskEntity;
import com.hermes.lotcenter.infrastructure.utils.JSONTextUtil;
import com.hermes.lotcenter.infrastructure.utils.NetUtil2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LotteryClient lotteryClient;

    @Autowired
    private UserBetTaskDomain userBetTaskDomain;

//    public MemberInfoResponse memberInfo() {
//        return lotteryClient.memberInfo();
//    }

    public LotteryOptResponse lotteryOpt(String lotCode) {

        if (StringUtils.isBlank(lotCode)) {
            return null;
        }
        LotteryOptCriteria criteria = new LotteryOptCriteria();
        criteria.setLotCode(lotCode);
        return lotteryClient.lotteryOpt(criteria);
    }

    public LotteryLastResponse lotteryLast(String lotCode, String qiHao) {
        if (StringUtils.isBlank(lotCode) || StringUtils.isBlank(qiHao)) {
            return null;
        }
        LotteryLastCriteria criteria = new LotteryLastCriteria();
        criteria.setLotCode(lotCode);
        criteria.setQiHao(qiHao);

        return lotteryClient.lotteryLast(criteria);
    }

    public MemberInfoResponse memberInfo(String taskNo) {
        NetUtil2.Response res = NetUtil2.post(getMemberUrl(), Maps.newHashMap(), getHeaders(taskNo));

        return JSONTextUtil.parseObject(res.rawContent, MemberInfoResponse.class);
    }

    public DoBetResponse doBet(String taskNo, DoBetCriteria criteria) {
        if (Objects.isNull(criteria)) {
            return null;
        }
        Map<String, ?> formCriteria = BeanUtil.beanToMap(criteria);
        NetUtil2.Response res = NetUtil2.post(getBetUrl(), formCriteria, getHeaders(taskNo));

        return JSONTextUtil.parseObject(res.rawContent, DoBetResponse.class);
    }


    //----------------------------------------------------------------------------------------
    private Map<String, String> getHeaders(String taskNo) {
        Map<String, String> headers = new HashMap<>();


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
        UserBetTaskEntity userBetTask = userBetTaskDomain.query(taskNo);
        if (Objects.nonNull(userBetTask) && StringUtils.isNotBlank(userBetTask.getSessionId())) {
            headers.put("Cookie", "SESSION=" + userBetTask.getSessionId());
        }
        return headers;
    }

    private String getBetUrl() {
        return LotteryConfig.ORIGIN + "/lotteryV2Bet/dolotV2Bet.do";
    }

    private String getMemberUrl() {
        return LotteryConfig.ORIGIN + "/meminfo.do";
    }

}
