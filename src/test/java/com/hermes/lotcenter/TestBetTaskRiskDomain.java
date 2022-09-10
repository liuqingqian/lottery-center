package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.BetTaskRiskDomain;
import com.hermes.lotcenter.domain.dto.RiskResultDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by liuqingqian on 2022/9/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBetTaskRiskDomain {

    @Autowired
    private BetTaskRiskDomain betTaskRiskDomain;


    @Test
    public void testCheckBetRisk() {
        String taskNo = "T202209090000001";
        RiskResultDTO riskResultDTO = betTaskRiskDomain.checkBetRisk(taskNo);
        Assert.assertNotNull("检查风险结果信息为空", riskResultDTO);
        System.out.println("riskResultDTO = " + riskResultDTO);
    }

}
