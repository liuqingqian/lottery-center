package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.DoBetDomain;
import com.hermes.lotcenter.domain.LotteryBetDomain;
import com.hermes.lotcenter.domain.rpc.criteria.DoBetCriteria;
import com.hermes.lotcenter.domain.rpc.response.DoBetResponse;
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
public class TestLotteryBetDomain {

    @Autowired
    private LotteryBetDomain lotteryBetDomain;

    @Autowired
    private DoBetDomain doBetDomain;

    @Test
    public void testDoBet() {
        String lotCode = "FFK3";
        String qiHao = "202209081437";
        Integer money = 1;
        String name = "双";
        BetDataIdEnum dataIdEnum = BetDataIdEnum.fromName(name);
        DoBetCriteria criteria = doBetDomain.buildCriteria(lotCode, qiHao, money, dataIdEnum);
        DoBetResponse response = lotteryBetDomain.doBet(criteria);

        Assert.assertEquals(response.getMsg(), true, response.getSuccess());
        System.out.println("response= " + response);
    }
}
