package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.rpc.LotteryClient;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryLastCriteria;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryOptCriteria;
import com.hermes.lotcenter.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.domain.rpc.response.MemberInfoResponse;
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

    @Test
    public void testMemberInfo() {
        MemberInfoResponse response = lotteryClient.memberInfo();

        Assert.assertEquals("获取会员状态信息失败", true, response.isSuccess());
        System.out.println("response= " + response);
    }


    @Test
    public void testLotteryOpt() {
        String lotCode = "FFK3";

        LotteryOptCriteria lotteryOptCriteria = new LotteryOptCriteria();
        lotteryOptCriteria.setLotCode(lotCode);

        LotteryOptResponse response = lotteryClient.lotteryOpt(lotteryOptCriteria);

        Assert.assertEquals(response.getMsg(), true, response.isSuccess());
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

        Assert.assertEquals(response.getMsg(), true, response.isSuccess());
        System.out.println("response= " + response);
    }


}
