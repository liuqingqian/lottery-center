package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.DoBetDomain;
import com.hermes.lotcenter.domain.rpc.LotteryBetClient;
import com.hermes.lotcenter.domain.rpc.response.DoBetResponse;
import com.hermes.lotcenter.infrastructure.enums.BetDataIdEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLotteryBetClient {
    @Autowired
    private LotteryBetClient lotteryBetClient;

    @Autowired
    private DoBetDomain doBetDomain;

    @Test
    public void testDoBet() {

        String lotCode = "FFK3";
        String qiHao = "202209071316";
        Integer money = 1;
        String name = "双";
        BetDataIdEnum dataIdEnum = BetDataIdEnum.fromName(name);
        Map<String, ?> criteria = doBetDomain.buildFromCriteria(lotCode, qiHao, money, dataIdEnum);
        DoBetResponse response = lotteryBetClient.doBet(criteria);
        Assert.assertEquals(response.getMsg(), true, response.isSuccess());
        System.out.println("response= " + response);
    }
}
