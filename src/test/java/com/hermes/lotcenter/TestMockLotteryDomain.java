package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.LotteryRecordDomain;
import com.hermes.lotcenter.domain.MockLotteryDomain;
import com.hermes.lotcenter.domain.dto.MockLotRecordDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liuqingqian on 2022/9/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMockLotteryDomain {

    private static final String FORMAT = "%s%04d";

    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;

    @Autowired
    private MockLotteryDomain mockLotteryDomain;

    @Test
    public void mockSpittleSize() {
        //顺投大小策略
        //1440
        //初试账户金额，单位：元
        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);
//        List<Integer> lotStrategy = Arrays.asList(1000, 800, 600, 500, 500);
        String lotCode = "FFK3";
        String startDate = "2022-09-09";
        int startCycleGroup = 18;//18（1021）、20（1141））、22（1261）
        int endCycleGroup = 24;
        int cycleTime = 60;
        List<MockLotRecordDTO> mockLotRecords = mockLotteryDomain.mockSpittleSize(lotCode, startDate, startCycleGroup, endCycleGroup, cycleTime, initAccountAmount, lotStrategy);

        Assert.assertNotNull("模拟[顺投大小策略]投注记录信息列表为空", mockLotRecords);
        Assert.assertTrue("模拟[顺投大小策略]投注记录信息列表无数据", mockLotRecords.size() > 0);
        System.out.println("mockLotRecords = " + mockLotRecords);
    }

    @Test
    public void mockSpittleSingleDouble() {
        //顺投单双策略
        //1440
        //初试账户金额，单位：元
        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);
//        List<Integer> lotStrategy = Arrays.asList(1000, 800, 600, 500, 500);
        String lotCode = "FFK3";
        String startDate = "2022-09-09";
        int startCycleGroup = 1;//18（1021）、20（1141））、22（1261）
        int endCycleGroup = 7;
        int cycleTime = 60;
        List<MockLotRecordDTO> mockLotRecords = mockLotteryDomain.mockSpittleSingleDouble(lotCode, startDate, startCycleGroup, endCycleGroup, cycleTime, initAccountAmount, lotStrategy);
        Assert.assertNotNull("模拟[顺投单双策略]投注记录信息列表为空", mockLotRecords);
        Assert.assertTrue("模拟[顺投单双策略]记录信息列表无数据", mockLotRecords.size() > 0);
        System.out.println("mockLotRecords = " + mockLotRecords);
    }
}
