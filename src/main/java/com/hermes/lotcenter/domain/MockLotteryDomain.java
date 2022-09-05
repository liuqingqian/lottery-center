package com.hermes.lotcenter.domain;

import com.google.common.collect.Maps;
import com.hermes.lotcenter.domain.dto.MockLotRecordDTO;
import com.hermes.lotcenter.entity.LotteryRecordEntity;
import com.hermes.lotcenter.infrastructure.enums.LotSingleDoubleEnum;
import com.hermes.lotcenter.infrastructure.enums.LotSizeEnum;
import com.hermes.lotcenter.infrastructure.enums.MockLotStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by liuqingqian on 2022/9/2.
 * <p>
 * 模拟投注服务领域
 */
@Component
public class MockLotteryDomain {

    private static final String FORMAT = "%s%04d";
    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;


    //TODO:模拟指定日期、时间分组和时间周期的投注记录
    public List<MockLotRecordDTO> mockSpittleSize(String lotCode, String startDate, int startCycleGroup, int endCycleGroup, int cycleTime, Integer initAccountAmount, List<Integer> lotStrategy) {
        //顺投大小策略
        //1440
        //初试账户金额，单位：元
//        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
//        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);


//        String lotCode = "FFK3";
//        String startDate = "2022-09-05";
        List<LotteryRecordEntity> lotteryRecordEntities = lotteryRecordDomain.queryListByDate(lotCode, startDate);

        Map<String, LotteryRecordEntity> recordEntityMap = lotteryRecordEntities.stream()
                .collect(Collectors.toMap(LotteryRecordEntity::getPeriodNumber, item -> item, (k1, k2) -> k2));


        Map<String, MockLotRecordDTO> mockLotRecordMap = Maps.newHashMap();

        //全局保存最近投注记录
        MockLotRecordDTO lastMockLot = null;

        String prefix = startDate.replace("-", "");
        int totalPeriod = lotteryRecordEntities.size();
//        int startPeriodIndex = 1;
//        int endPeriodIndex = totalPeriod;

//        int startCycleGroup = 18;//18、20、22
//        int endCycleGroup = 24;
//        int cycleTime = 60;
        int startPeriodIndex = (startCycleGroup - 1) * cycleTime + 1;
        int endPeriodIndex = totalPeriod < endCycleGroup * cycleTime ? totalPeriod : endCycleGroup * cycleTime;
        for (int periodIndex = startPeriodIndex; periodIndex <= endPeriodIndex; periodIndex++) {
            //当期期数
            String currentPeriodNo = String.format(FORMAT, prefix, periodIndex);
            //上期期数
            String prevPeriodNo = String.format(FORMAT, prefix, (periodIndex - 1));
            //下期期数
            String nextPeriodNo = String.format(FORMAT, prefix, (periodIndex + 1));
            System.out.print("prevPeriodNo = " + prevPeriodNo);
            System.out.print(",currentPeriodNo = " + currentPeriodNo);
            System.out.println(",nextPeriodNo = " + nextPeriodNo);


            //本期已开奖的记录信息
            LotteryRecordEntity currentRecord = recordEntityMap.get(currentPeriodNo);
            //下期已开奖的记录信息
            LotteryRecordEntity nextRecord = recordEntityMap.get(nextPeriodNo);
            LotSizeEnum currentLotSizeEnum = LotSizeEnum.fromCode(currentRecord.getSize());
            if (Objects.isNull(nextRecord) || LotSizeEnum.DUAL == currentLotSizeEnum) {
                //当天最后一期跳过或豹子，则跳过一期
                continue;
            }
            //本期预投注的记录信息
            MockLotRecordDTO currentMockLot = new MockLotRecordDTO();
            currentMockLot.setCode(lotCode);
            currentMockLot.setPeriodNumber(nextPeriodNo);
            //1、计算本期投注金额；
            Integer currentAmount = null;
            //上期投注就是本期开奖
            MockLotRecordDTO prevMockLot = mockLotRecordMap.get(currentPeriodNo);
            //没有上期或者未中奖，当期投注金额=初始资金投注；模拟暂不考虑未开奖的情况
            if (Objects.isNull(prevMockLot) || MockLotStatusEnum.LOSING_LOT.equals(prevMockLot.getLotStatus())) {
                currentAmount = lotStrategy.get(0);

            } else if (MockLotStatusEnum.WINNING_LOT.equals(prevMockLot.getLotStatus())) {
                //上期中奖，根据上期投注金额计算出当期投注金额
                Integer prevAmount = prevMockLot.getAmount();
                int prevAmountIndexOf = lotStrategy.indexOf(prevAmount);
                //如果上期策略金额往后还有策略金额，则取策略金额，否则取最后配置的策略金额；
                int currentAmountIndexOf = (prevAmountIndexOf + 1) < lotStrategy.size() ? prevAmountIndexOf + 1 : lotStrategy.size() - 1;
                currentAmount = lotStrategy.get(currentAmountIndexOf);
            }
            currentMockLot.setAmount(currentAmount);

            //2、本期投什么？
            currentMockLot.setLotSizeTxt(currentLotSizeEnum.getDesc());

            //3、本期开出了什么？
            LotSizeEnum nextRecordSizeEnum = LotSizeEnum.fromCode(nextRecord.getSize());
            currentMockLot.setSizeTxt(nextRecordSizeEnum.getDesc());

            //3、本期中奖状态,暂不考虑未开奖的情况
            if (currentLotSizeEnum.equals(nextRecord.getSize())) {
                currentMockLot.setLotStatus(MockLotStatusEnum.WINNING_LOT.getCode());
            } else {
                currentMockLot.setLotStatus(MockLotStatusEnum.LOSING_LOT.getCode());
            }

            //4、账户动态金额
            if (Objects.isNull(lastMockLot)) {
                //账号当前金额减本次投注金额
                Integer accountAmount = initAccountAmount - currentMockLot.getAmount();
                currentMockLot.setAccountAmount(accountAmount);
            } else {
                Integer accountAmount = lastMockLot.getAccountAmount() - currentMockLot.getAmount();
                //中奖了则加上期次投注金额，既上期中奖金额
                if (MockLotStatusEnum.WINNING_LOT.equals(lastMockLot.getLotStatus())) {
                    accountAmount = accountAmount + lastMockLot.getAmount() * 2;
                }
                currentMockLot.setAccountAmount(accountAmount);
            }
            //本期投注赋给全局的最后一次投注记录；
            lastMockLot = currentMockLot;
            //本期投注记录存进集合中
            mockLotRecordMap.put(currentMockLot.getPeriodNumber(), currentMockLot);
        }
        List<MockLotRecordDTO> mockLotRecordList = mockLotRecordMap.values().stream()
                .sorted(Comparator.comparing(MockLotRecordDTO::getPeriodNumber))
                .collect(Collectors.toList());

        return mockLotRecordList;
    }


    public List<MockLotRecordDTO> mockSpittleSingleDouble(String lotCode, String startDate, int startCycleGroup, int endCycleGroup, int cycleTime, Integer initAccountAmount, List<Integer> lotStrategy) {
        //顺投单双策略
        //1440
        //初试账户金额，单位：元
//        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
//        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);


//        String lotCode = "FFK3";
//        String startDate = "2022-09-05";
        List<LotteryRecordEntity> lotteryRecordEntities = lotteryRecordDomain.queryListByDate(lotCode, startDate);

        Map<String, LotteryRecordEntity> recordEntityMap = lotteryRecordEntities.stream()
                .collect(Collectors.toMap(LotteryRecordEntity::getPeriodNumber, item -> item, (k1, k2) -> k2));

        Map<String, MockLotRecordDTO> mockLotRecordMap = Maps.newHashMap();
        //全局保存最近投注记录
        MockLotRecordDTO lastMockLot = null;

        String prefix = startDate.replace("-", "");
        int totalPeriod = lotteryRecordEntities.size();
//        int startPeriodIndex = 1;
//        int endPeriodIndex = totalPeriod;
//        int startCycleGroup = 22;//18（1021）、20（1141））、22（1261）
//        int endCycleGroup = 24;
//        int cycleTime = 60;
        int startPeriodIndex = (startCycleGroup - 1) * cycleTime + 1;
        int endPeriodIndex = endCycleGroup * cycleTime;
        for (int periodIndex = startPeriodIndex; periodIndex <= endPeriodIndex; periodIndex++) {
            //当期期数
            String currentPeriodNo = String.format(FORMAT, prefix, periodIndex);
            //上期期数
            String prevPeriodNo = String.format(FORMAT, prefix, (periodIndex - 1));
            //下期期数
            String nextPeriodNo = String.format(FORMAT, prefix, (periodIndex + 1));
            System.out.print("prevPeriodNo = " + prevPeriodNo);
            System.out.print(",currentPeriodNo = " + currentPeriodNo);
            System.out.println(",nextPeriodNo = " + nextPeriodNo);


            //本期已开奖的记录信息
            LotteryRecordEntity currentRecord = recordEntityMap.get(currentPeriodNo);
            //下期已开奖的记录信息
            LotteryRecordEntity nextRecord = recordEntityMap.get(nextPeriodNo);
            LotSingleDoubleEnum currentLotSingleDoubleEnum = LotSingleDoubleEnum.fromCode(currentRecord.getSingleDouble());
            if (Objects.isNull(nextRecord) || LotSingleDoubleEnum.DUAL == currentLotSingleDoubleEnum) {
                //当天最后一期跳过或豹子，则跳过一期
                continue;
            }
            //本期预投注的记录信息
            MockLotRecordDTO currentMockLot = new MockLotRecordDTO();
            currentMockLot.setCode(lotCode);
            currentMockLot.setPeriodNumber(nextPeriodNo);
            //1、计算本期投注金额；
            Integer currentAmount = null;
            //上期投注就是本期开奖
            MockLotRecordDTO prevMockLot = mockLotRecordMap.get(currentPeriodNo);
            //没有上期或者未中奖，当期投注金额=初始资金投注；模拟暂不考虑未开奖的情况
            if (Objects.isNull(prevMockLot) || MockLotStatusEnum.LOSING_LOT.equals(prevMockLot.getLotStatus())) {
                currentAmount = lotStrategy.get(0);

            } else if (MockLotStatusEnum.WINNING_LOT.equals(prevMockLot.getLotStatus())) {
                //上期中奖，根据上期投注金额计算出当期投注金额
                Integer prevAmount = prevMockLot.getAmount();
                int prevAmountIndexOf = lotStrategy.indexOf(prevAmount);
                //如果上期策略金额往后还有策略金额，则取策略金额，否则取最后配置的策略金额；
                int currentAmountIndexOf = (prevAmountIndexOf + 1) < lotStrategy.size() ? prevAmountIndexOf + 1 : lotStrategy.size() - 1;
                currentAmount = lotStrategy.get(currentAmountIndexOf);
            }
            currentMockLot.setAmount(currentAmount);

            //2、本期投什么？
            currentMockLot.setLotSingleDoubleTxt(currentLotSingleDoubleEnum.getDesc());

            //3、本期开出了什么？
            LotSingleDoubleEnum nextRecordSingleDoubleEnum = LotSingleDoubleEnum.fromCode(nextRecord.getSingleDouble());
            currentMockLot.setSingleDoubleTxt(nextRecordSingleDoubleEnum.getDesc());

            //3、本期中奖状态,暂不考虑未开奖的情况
            if (currentLotSingleDoubleEnum.equals(nextRecord.getSingleDouble())) {
                currentMockLot.setLotStatus(MockLotStatusEnum.WINNING_LOT.getCode());
            } else {
                currentMockLot.setLotStatus(MockLotStatusEnum.LOSING_LOT.getCode());
            }

            //4、账户动态金额
            if (Objects.isNull(lastMockLot)) {
                //账号当前金额减本次投注金额
                Integer accountAmount = initAccountAmount - currentMockLot.getAmount();
                currentMockLot.setAccountAmount(accountAmount);
            } else {
                Integer accountAmount = lastMockLot.getAccountAmount() - currentMockLot.getAmount();
                //中奖了则加上期次投注金额，既上期中奖金额
                if (MockLotStatusEnum.WINNING_LOT.equals(lastMockLot.getLotStatus())) {
                    accountAmount = accountAmount + lastMockLot.getAmount() * 2;
                }
                currentMockLot.setAccountAmount(accountAmount);
            }
            //本期投注赋给全局的最后一次投注记录；
            lastMockLot = currentMockLot;
            //本期投注记录存进集合中
            mockLotRecordMap.put(currentMockLot.getPeriodNumber(), currentMockLot);
        }
        List<MockLotRecordDTO> mockLotRecordList = mockLotRecordMap.values().stream()
                .sorted(Comparator.comparing(MockLotRecordDTO::getPeriodNumber))
                .collect(Collectors.toList());

        return mockLotRecordList;
    }

}
