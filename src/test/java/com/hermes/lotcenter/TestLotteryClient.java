package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.rpc.LotteryClient;
import com.hermes.lotcenter.domain.rpc.criteria.LotteryOptCriteria;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
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
    public void testLotteryOpt(){
        String lotCode = "FFK3";
        LotteryOptCriteria lotteryOptCriteria = new LotteryOptCriteria();
        lotteryOptCriteria.setLotCode(lotCode);

        LotteryOptResponse lotteryOptResponse = lotteryClient.lotteryOpt(lotteryOptCriteria);

        System.out.println("lotteryOptResponse = " + lotteryOptResponse);

    }
}
