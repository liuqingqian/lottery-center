package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.AnalysisMockDomain;
import com.hermes.lotcenter.domain.dto.AnalysisMockLotRecordDTO;
import com.hermes.lotcenter.infrastructure.enums.TradingStatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liuqingqian on 2022/9/6.
 * <p>
 * 统计分析模拟投注记录
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAnalysisMockDomain {

    @Autowired
    private AnalysisMockDomain analysisMockDomain;


    @Test
    public void testAnalysisDayMockLotSpittleSize() {
        //顺投大小策略
        //1440
        //初试账户金额，单位：元
        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);
        String lotCode = "FFK3";
        String startDate = "2022-09-05";
        int startCycleGroup = 18;//18（1021）、20（1141））、22（1261）
        int endCycleGroup = 24;
        int cycleTime = 60;
        int stopLossAmount = 3000;
        int stopProfitAmount = 10000;

        AnalysisMockLotRecordDTO analysisMockLotRecordDTO = analysisMockDomain.analysisDayMockLotSpittleSize(lotCode, startDate,
                startCycleGroup, endCycleGroup, cycleTime,
                initAccountAmount, lotStrategy,
                stopLossAmount, stopProfitAmount);

        System.out.println("analysisMockLotRecordDTO = " + analysisMockLotRecordDTO);
    }

    @Test
    public void testAnalysisDayMockLotSpittleSingleDouble() {
        //顺投单双策略
        //1440
        //初试账户金额，单位：元
        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);
        String lotCode = "FFK3";
        String startDate = "2022-09-08";
        int startCycleGroup = 18;//18（1021）、20（1141））、22（1261）
        int endCycleGroup = 24;
        int cycleTime = 60;
        int stopLossAmount = 3000;
        int stopProfitAmount = 5000;

        AnalysisMockLotRecordDTO analysisMockLotRecordDTO = analysisMockDomain.analysisDayMockLotSpittleSingleDouble(lotCode, startDate,
                startCycleGroup, endCycleGroup, cycleTime,
                initAccountAmount, lotStrategy,
                stopLossAmount, stopProfitAmount);

        System.out.println("analysisMockLotRecordDTO = " + analysisMockLotRecordDTO);
    }

    @Test
    public void testAnalysisMonthMockLotSpittleSize() {
        //顺投大小策略
        //1440
        //初试账户金额，单位：元
        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);
        String lotCode = "FFK3";
        String startMonth = "2022-08";
        int startCycleGroup = 20;//18（1021）、20（1141））、22（1261）
        int endCycleGroup = 24;
        int cycleTime = 60;
        int stopLossAmount = 3000;
        int stopProfitAmount = 10000;

        List<AnalysisMockLotRecordDTO> analysisMockLotRecords = analysisMockDomain.analysisMonthMockLotSpittleSize(lotCode, startMonth,
                startCycleGroup, endCycleGroup, cycleTime,
                initAccountAmount, lotStrategy,
                stopLossAmount, stopProfitAmount);
        int total = analysisMockLotRecords.size();
        int winningTimes = analysisMockLotRecords.stream()
                .filter(analysisMockLotRecordDTO -> TradingStatusEnum.STOP_PROFIT.equals(analysisMockLotRecordDTO.getTradingStatus()))
                .collect(Collectors.toList()).size();

        Double winningRateDouble = new BigDecimal(winningTimes).divide(new BigDecimal(total), 4, BigDecimal.ROUND_DOWN).doubleValue();
        String winningRate = (winningRateDouble * 100) + "%";
        System.out.println("winningRate = " + winningRate);
        System.out.println("total = " + total + ",winningTimes = " + winningTimes);
        System.out.println("analysisMockLotRecords = " + analysisMockLotRecords);
    }

    @Test
    public void testAnalysisMonthMockLotSpittleSingleDouble() {
        //顺投单双策略
        //1440
        //初试账户金额，单位：元
        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);
        String lotCode = "FFK3";
        String startMonth = "2022-07";
        int startCycleGroup = 18;//18（1021）、20（1141））、22（1261）
        int endCycleGroup = 24;
        int cycleTime = 60;
        int stopLossAmount = 3000;
        int stopProfitAmount = 5000;

        List<AnalysisMockLotRecordDTO> analysisMockLotRecords = analysisMockDomain.analysisMonthMockLotSpittleSingleDouble(lotCode, startMonth,
                startCycleGroup, endCycleGroup, cycleTime,
                initAccountAmount, lotStrategy,
                stopLossAmount, stopProfitAmount);
        int total = analysisMockLotRecords.size();
        int winningTimes = analysisMockLotRecords.stream()
                .filter(analysisMockLotRecordDTO -> TradingStatusEnum.STOP_PROFIT.equals(analysisMockLotRecordDTO.getTradingStatus()))
                .collect(Collectors.toList()).size();
        int lossTimes = total - winningTimes;
        int monthProfit = winningTimes * stopProfitAmount - lossTimes * stopLossAmount;

        Double winningRateDouble = new BigDecimal(winningTimes).divide(new BigDecimal(total), 4, BigDecimal.ROUND_DOWN).doubleValue();
        String winningRate = (winningRateDouble * 100) + "%";
        System.out.println("winningRate = " + winningRate);
        System.out.println("total = " + total + ",winningTimes = " + winningTimes);

        System.out.println("monthProfit = " + monthProfit);
        System.out.println("analysisMockLotRecords = " + analysisMockLotRecords);
    }

}
