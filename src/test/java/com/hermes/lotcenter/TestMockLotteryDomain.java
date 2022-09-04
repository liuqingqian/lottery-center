package com.hermes.lotcenter;

import com.google.common.collect.Maps;
import com.hermes.lotcenter.domain.LotteryRecordDomain;
import com.hermes.lotcenter.domain.dto.MockLotRecordDTO;
import com.hermes.lotcenter.entity.LotteryRecordEntity;
import com.hermes.lotcenter.infrastructure.enums.LotSingleDoubleEnum;
import com.hermes.lotcenter.infrastructure.enums.LotSizeEnum;
import com.hermes.lotcenter.infrastructure.enums.MockLotStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by liuqingqian on 2022/9/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMockLotteryDomain {

    private static final String FORMAT = "%s%04d";

    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;


    @Test
    public void mockLotterySpittleSize() {
        //顺投大小策略
        //1440
        //初试账户金额，单位：元
        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);


        String lotCode = "FFK3";
        String startDate = "2022-08-21";
        List<LotteryRecordEntity> lotteryRecordEntities = lotteryRecordDomain.queryListByDate(lotCode, startDate);
        Assert.assertNotNull("查询开奖记录列表为空", lotteryRecordEntities);
        Assert.assertTrue("查询开奖记录列表无数据", lotteryRecordEntities.size() > 0);
        System.out.println("lotteryRecordEntities size = " + lotteryRecordEntities.size());

        Map<String, LotteryRecordEntity> recordEntityMap = lotteryRecordEntities.stream()
                .collect(Collectors.toMap(LotteryRecordEntity::getPeriodNumber, item -> item, (k1, k2) -> k2));


        Map<String, MockLotRecordDTO> mockLotRecordMap = Maps.newHashMap();

        //全局保存最近投注记录
        MockLotRecordDTO lastMockLot = null;

        String prefix = startDate.replace("-", "");
        int totalPeriod = lotteryRecordEntities.size();
//        int startPeriodIndex = 1;
//        int endPeriodIndex = totalPeriod;

        int startCycleGroup = 18;//18、20、22
        int endCycleGroup = 24;
        int cycleTime = 60;
        int startPeriodIndex = (startCycleGroup - 1) * cycleTime;
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
        Collection<MockLotRecordDTO> mockLotRecordList = mockLotRecordMap.values().stream()
                .sorted(Comparator.comparing(MockLotRecordDTO::getPeriodNumber))
                .collect(Collectors.toList());

        System.out.println("mockLotRecordList = " + mockLotRecordList);
    }

    @Test
    public void mockLotterySpittleSingleDouble() {
        //顺投单双策略
        //1440
        //初试账户金额，单位：元
        Integer initAccountAmount = 10000;
//        List<Integer> lotStrategy = Arrays.asList(5, 4, 3, 2, 2);
//        List<Integer> lotStrategy = Arrays.asList(100, 80, 60, 50, 50);
//        List<Integer> lotStrategy = Arrays.asList(300, 240, 180, 150, 150);
        List<Integer> lotStrategy = Arrays.asList(500, 400, 300, 250, 250);


        String lotCode = "FFK3";
        String startDate = "2022-08-31";
        List<LotteryRecordEntity> lotteryRecordEntities = lotteryRecordDomain.queryListByDate(lotCode, startDate);
        Assert.assertNotNull("查询开奖记录列表为空", lotteryRecordEntities);
        Assert.assertTrue("查询开奖记录列表无数据", lotteryRecordEntities.size() > 0);
        System.out.println("lotteryRecordEntities size = " + lotteryRecordEntities.size());

        Map<String, LotteryRecordEntity> recordEntityMap = lotteryRecordEntities.stream()
                .collect(Collectors.toMap(LotteryRecordEntity::getPeriodNumber, item -> item, (k1, k2) -> k2));


        Map<String, MockLotRecordDTO> mockLotRecordMap = Maps.newHashMap();

        //全局保存最近投注记录
        MockLotRecordDTO lastMockLot = null;

        String prefix = startDate.replace("-", "");
        int totalPeriod = lotteryRecordEntities.size();
//        int startPeriodIndex = 1;
//        int endPeriodIndex = totalPeriod;
        int startCycleGroup = 18;//18（1021）、20（1141））、22（1261）
        int endCycleGroup = 24;
        int cycleTime = 60;
        int startPeriodIndex = (startCycleGroup - 1) * cycleTime;
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
        Collection<MockLotRecordDTO> mockLotRecordList = mockLotRecordMap.values().stream()
                .sorted(Comparator.comparing(MockLotRecordDTO::getPeriodNumber))
                .collect(Collectors.toList());

        System.out.println("mockLotRecordList = " + mockLotRecordList);
    }
//    LotCycleMatchingScoreDTO(cycleGroup=1, cycleTime=60, sizeMatchingScore=62, singleDoubleMatchingScore=53)
//    LotCycleMatchingScoreDTO(cycleGroup=2, cycleTime=60, sizeMatchingScore=58, singleDoubleMatchingScore=66)
//    LotCycleMatchingScoreDTO(cycleGroup=3, cycleTime=60, sizeMatchingScore=68, singleDoubleMatchingScore=62)
//    LotCycleMatchingScoreDTO(cycleGroup=4, cycleTime=60, sizeMatchingScore=71, singleDoubleMatchingScore=76)
//    LotCycleMatchingScoreDTO(cycleGroup=5, cycleTime=60, sizeMatchingScore=54, singleDoubleMatchingScore=47)
//    LotCycleMatchingScoreDTO(cycleGroup=6, cycleTime=60, sizeMatchingScore=58, singleDoubleMatchingScore=59)
//    LotCycleMatchingScoreDTO(cycleGroup=7, cycleTime=60, sizeMatchingScore=60, singleDoubleMatchingScore=58)
//    LotCycleMatchingScoreDTO(cycleGroup=8, cycleTime=60, sizeMatchingScore=66, singleDoubleMatchingScore=61)
//    LotCycleMatchingScoreDTO(cycleGroup=9, cycleTime=60, sizeMatchingScore=67, singleDoubleMatchingScore=49)
//    LotCycleMatchingScoreDTO(cycleGroup=10, cycleTime=60, sizeMatchingScore=44, singleDoubleMatchingScore=45)
//    LotCycleMatchingScoreDTO(cycleGroup=11, cycleTime=60, sizeMatchingScore=67, singleDoubleMatchingScore=61)
//    LotCycleMatchingScoreDTO(cycleGroup=12, cycleTime=60, sizeMatchingScore=59, singleDoubleMatchingScore=61)
//    LotCycleMatchingScoreDTO(cycleGroup=13, cycleTime=60, sizeMatchingScore=52, singleDoubleMatchingScore=77)
//    LotCycleMatchingScoreDTO(cycleGroup=14, cycleTime=60, sizeMatchingScore=57, singleDoubleMatchingScore=72)
//    LotCycleMatchingScoreDTO(cycleGroup=15, cycleTime=60, sizeMatchingScore=53, singleDoubleMatchingScore=57)
//    LotCycleMatchingScoreDTO(cycleGroup=16, cycleTime=60, sizeMatchingScore=76, singleDoubleMatchingScore=67)
//    LotCycleMatchingScoreDTO(cycleGroup=17, cycleTime=60, sizeMatchingScore=57, singleDoubleMatchingScore=72)
//    LotCycleMatchingScoreDTO(cycleGroup=18, cycleTime=60, sizeMatchingScore=64, singleDoubleMatchingScore=71)
//    LotCycleMatchingScoreDTO(cycleGroup=19, cycleTime=60, sizeMatchingScore=60, singleDoubleMatchingScore=44)
//    LotCycleMatchingScoreDTO(cycleGroup=20, cycleTime=60, sizeMatchingScore=51, singleDoubleMatchingScore=62)
//    LotCycleMatchingScoreDTO(cycleGroup=21, cycleTime=60, sizeMatchingScore=47, singleDoubleMatchingScore=60)
//    LotCycleMatchingScoreDTO(cycleGroup=22, cycleTime=60, sizeMatchingScore=72, singleDoubleMatchingScore=63)
//    LotCycleMatchingScoreDTO(cycleGroup=23, cycleTime=60, sizeMatchingScore=60, singleDoubleMatchingScore=62)
//    LotCycleMatchingScoreDTO(cycleGroup=24, cycleTime=60, sizeMatchingScore=57, singleDoubleMatchingScore=57)

}
