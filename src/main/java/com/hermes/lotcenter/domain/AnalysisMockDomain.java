package com.hermes.lotcenter.domain;

import com.google.common.collect.Lists;
import com.hermes.lotcenter.domain.dto.AnalysisMockLotRecordDTO;
import com.hermes.lotcenter.domain.dto.MockLotRecordDTO;
import com.hermes.lotcenter.infrastructure.enums.MockLotStatusEnum;
import com.hermes.lotcenter.infrastructure.enums.TradingStatusEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/6.
 * <p>
 * 以日为单位模拟统计完整周期的投注信息
 */
@Component
public class AnalysisMockDomain {

    @Autowired
    private MockLotteryDomain mockLotteryDomain;

    //TODO:统计模拟指定日期、时间分组和时间周期的投注记录
    //TODO:统计模拟指定月份、时间分组和时间周期的投注记录
    //TODO:统计模拟指定日期集合、时间分组和时间周期的投注记录

    public AnalysisMockLotRecordDTO analysisDayMockLotSpittleSize(String lotCode, String startDate,
                                                                  int startCycleGroup, int endCycleGroup, int cycleTime,
                                                                  Integer initAccountAmount, List<Integer> lotStrategy,
                                                                  Integer stopLossAmount, Integer stopProfitAmount) {
        List<MockLotRecordDTO> mockLotRecords = mockLotteryDomain.mockSpittleSize(lotCode, startDate, startCycleGroup, endCycleGroup, cycleTime, initAccountAmount, lotStrategy);

        return analysisDayMockLot(lotCode, startDate, mockLotRecords, initAccountAmount, stopLossAmount, stopProfitAmount);
    }

    public AnalysisMockLotRecordDTO analysisDayMockLotSpittleSingleDouble(String lotCode, String startDate,
                                                                          int startCycleGroup, int endCycleGroup, int cycleTime,
                                                                          Integer initAccountAmount, List<Integer> lotStrategy,
                                                                          Integer stopLossAmount, Integer stopProfitAmount) {
        List<MockLotRecordDTO> mockLotRecords = mockLotteryDomain.mockSpittleSingleDouble(lotCode, startDate, startCycleGroup, endCycleGroup, cycleTime, initAccountAmount, lotStrategy);

        return analysisDayMockLot(lotCode, startDate, mockLotRecords, initAccountAmount, stopLossAmount, stopProfitAmount);
    }


    public List<AnalysisMockLotRecordDTO> analysisMonthMockLotSpittleSize(String lotCode, String startMonth,
                                                                          int startCycleGroup, int endCycleGroup, int cycleTime,
                                                                          Integer initAccountAmount, List<Integer> lotStrategy,
                                                                          Integer stopLossAmount, Integer stopProfitAmount) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date dateMonth = null;
        try {
            dateMonth = dateFormat.parse(startMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateMonth);
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String format = "%s-%02d";
        List<AnalysisMockLotRecordDTO> analysisMockLotRecords = Lists.newArrayList();
        for (int i = 1; i <= daysOfMonth; i++) {
            String startDate = String.format(format, startMonth, i);
            AnalysisMockLotRecordDTO analysisMockLotRecordDTO = analysisDayMockLotSpittleSize(lotCode, startDate,
                    startCycleGroup, endCycleGroup, cycleTime,
                    initAccountAmount, lotStrategy,
                    stopLossAmount, stopProfitAmount);
            if (Objects.nonNull(analysisMockLotRecordDTO)) {
                analysisMockLotRecords.add(analysisMockLotRecordDTO);
            }
        }
        return analysisMockLotRecords;
    }

    public List<AnalysisMockLotRecordDTO> analysisMonthMockLotSpittleSingleDouble(String lotCode, String startMonth,
                                                                                  int startCycleGroup, int endCycleGroup, int cycleTime,
                                                                                  Integer initAccountAmount, List<Integer> lotStrategy,
                                                                                  Integer stopLossAmount, Integer stopProfitAmount) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date dateMonth = null;
        try {
            dateMonth = dateFormat.parse(startMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateMonth);
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String format = "%s-%02d";
        List<AnalysisMockLotRecordDTO> analysisMockLotRecords = Lists.newArrayList();
        for (int i = 1; i <= daysOfMonth; i++) {
            String startDate = String.format(format, startMonth, i);
            AnalysisMockLotRecordDTO analysisMockLotRecordDTO = analysisDayMockLotSpittleSingleDouble(lotCode, startDate,
                    startCycleGroup, endCycleGroup, cycleTime,
                    initAccountAmount, lotStrategy,
                    stopLossAmount, stopProfitAmount);
            if (Objects.nonNull(analysisMockLotRecordDTO)) {
                analysisMockLotRecords.add(analysisMockLotRecordDTO);
            }
        }
        return analysisMockLotRecords;
    }

    public List<AnalysisMockLotRecordDTO> analysisDaysMockLot() {
        return null;
    }


    public AnalysisMockLotRecordDTO analysisDayMockLot(String lotCode, String startDate,
                                                       List<MockLotRecordDTO> mockLotRecords,
                                                       Integer initAccountAmount, Integer stopLossAmount, Integer stopProfitAmount) {
        if (initAccountAmount < stopLossAmount) {
            throw new RuntimeException("账号金额不能小于止损金额");
        }
        AnalysisMockLotRecordDTO analysisMockLotRecordDTO = new AnalysisMockLotRecordDTO();
        analysisMockLotRecordDTO.setCode(lotCode);
        analysisMockLotRecordDTO.setDate(startDate);
        analysisMockLotRecordDTO.setLotNo(1);
        analysisMockLotRecordDTO.setStopLossAmount(stopLossAmount);
        analysisMockLotRecordDTO.setStopProfitAmount(stopProfitAmount);

        //default
        analysisMockLotRecordDTO.setTradingStatus(TradingStatusEnum.STOP.getCode());
        if (CollectionUtils.isEmpty(mockLotRecords)) {
            return analysisMockLotRecordDTO;
        }

        Integer stopLoss = initAccountAmount - stopLossAmount;
        Integer stopProfit = initAccountAmount + stopProfitAmount;
        boolean triggerStop = false;
        Integer winTimes = 0;
        Integer lossTimes = 0;
        Integer totalDrawDownAmount = 0;
        Integer totalProfitAmount = 0;
        Integer maxDrawDown = 0;
        Integer maxProfit = 0;
        for (int i = 0; i < mockLotRecords.size(); i++) {
            MockLotRecordDTO mockLotRecordDTO = mockLotRecords.get(i);
            Integer accountAmount = mockLotRecordDTO.getAccountAmount();
            if (accountAmount < stopLoss) {
                //止损
                analysisMockLotRecordDTO.setTradingStatus(TradingStatusEnum.STOP_LOSS.getCode());
                triggerStop = true;
            }
            if (accountAmount >= stopProfit) {
                //止盈
                analysisMockLotRecordDTO.setTradingStatus(TradingStatusEnum.STOP_PROFIT.getCode());
                triggerStop = true;
            }
            if (triggerStop) {
                //如果触发止损，本次不需要投注，最后一次投注是上一次；
                analysisMockLotRecordDTO.setTimes(i);
                break;
            }
            //成功、失败次数
            if (MockLotStatusEnum.WINNING_LOT.equals(mockLotRecordDTO.getLotStatus())) {
                totalProfitAmount = totalProfitAmount + mockLotRecordDTO.getAmount();
                winTimes++;
            } else if (MockLotStatusEnum.LOSING_LOT.equals(mockLotRecordDTO.getLotStatus())) {
                totalDrawDownAmount = totalDrawDownAmount + mockLotRecordDTO.getAmount();
                lossTimes++;
            }
            if (accountAmount < initAccountAmount) {
                //浮亏
                int drawDown = initAccountAmount - accountAmount;
                if (drawDown > maxDrawDown) {
                    maxDrawDown = drawDown;
                }
            } else {
                //浮盈
                int profit = accountAmount - initAccountAmount;
                if (profit > maxProfit) {
                    maxProfit = profit;
                }
            }
        }

        Integer totalAmount = totalProfitAmount + totalDrawDownAmount;
        analysisMockLotRecordDTO.setWinTimes(winTimes);
        analysisMockLotRecordDTO.setLossTimes(lossTimes);
        analysisMockLotRecordDTO.setTotalAmount(totalAmount);
        analysisMockLotRecordDTO.setTotalProfitAmount(totalProfitAmount);
        analysisMockLotRecordDTO.setTotalDrawDownAmount(totalDrawDownAmount);
        analysisMockLotRecordDTO.setMaxProfit(maxProfit);
        analysisMockLotRecordDTO.setMaxDrawDown(maxDrawDown);


        //全局
        analysisMockLotRecordDTO.setMockLotRecords(mockLotRecords);
        Integer minAccountAmount = mockLotRecords.stream()
                .filter(mockLotRecordDTO -> initAccountAmount > mockLotRecordDTO.getAccountAmount())
                .mapToInt(MockLotRecordDTO::getAccountAmount).min().orElse(0);
        Integer maxAccountAmount = mockLotRecords.stream()
                .filter(mockLotRecordDTO -> initAccountAmount < mockLotRecordDTO.getAccountAmount())
                .mapToInt(MockLotRecordDTO::getAccountAmount).max().orElse(0);
        analysisMockLotRecordDTO.setGlobalMaxProfit(maxAccountAmount - initAccountAmount);
        analysisMockLotRecordDTO.setGlobalMaxDrawDown(initAccountAmount - minAccountAmount);

        return analysisMockLotRecordDTO;
    }

}
