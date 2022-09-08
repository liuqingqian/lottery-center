package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.DoBetDomain;
import com.hermes.lotcenter.domain.rpc.LotteryClient;
import com.hermes.lotcenter.domain.rpc.criteria.DoBetCriteria;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryLastCriteria;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryOptCriteria;
import com.hermes.lotcenter.domain.rpc.response.DoBetResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.infrastructure.enums.BetDataIdEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLotteryClient {

    @Autowired
    private LotteryClient lotteryClient;

    @Autowired
    private DoBetDomain doBetDomain;


    @Test
    public void testLotteryOpt() {
        String lotCode = "FFK3";

        LotteryOptCriteria lotteryOptCriteria = new LotteryOptCriteria();
        lotteryOptCriteria.setLotCode(lotCode);

        LotteryOptResponse response = lotteryClient.lotteryOpt(lotteryOptCriteria);

        Assert.assertEquals(response.getMsg(), true, response.getSuccess());
        System.out.println("response= " + response);

    }

    @Test
    public void testLotteryLast() {
        String lotCode = "FFK3";
        String qiHao = "202209071316";
        LotteryLastCriteria lotteryLastCriteria = new LotteryLastCriteria();
        lotteryLastCriteria.setLotCode(lotCode);
        lotteryLastCriteria.setQiHao(qiHao);


        LotteryLastResponse response = lotteryClient.lotteryLast(lotteryLastCriteria);

        Assert.assertEquals(response.getMsg(), true, response.getSuccess());
        System.out.println("response= " + response);
    }




}
