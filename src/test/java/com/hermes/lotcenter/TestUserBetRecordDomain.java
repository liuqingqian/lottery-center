package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.UserBetRecordDomain;
import com.hermes.lotcenter.domain.dto.BetTaskStatsDTO;
import com.hermes.lotcenter.entity.UserBetRecordEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserBetRecordDomain {

    @Autowired
    private UserBetRecordDomain userBetRecordDomain;


    @Test
    public void testInsert() {
//        {
//                "account": "kongkong888",
//                "buyMoney": 1.00,
//                "buyZhuShu": 1,
//                "haoMa": "单",
//                "lotName": "极速快三",
//                "lotType": 10,
//                "lotteryHaoMa": "3,3,4",
//                "odds": 1.980000,
//                "openTime": 1662658263029,
//                "orderId": "L22090904556",
//                "playCode": "daxiaoshaibao",
//                "playName": "骰宝",
//                "stationId": 111,
//                "status": 3,
//                "winMoney": 0.00,
//                "winZhuShu": 0
//        }
        String taskNo = "T202209090000000";
        String lotCode = "FFK3";
        String periodNumber = "202209090092";
        Long accountId = 946902L;
        String accountName = "kongkong888";
        Double buyMoney = 1.00D;
        Integer buyZhuShu = 1;
        String haoMa = "单";
        String lotName = "极速快三";
        Integer lotType = 10;
        Integer stationId = 111;
        Double odds = 1.98D;
        String playCode = "daxiaoshaibao";
        String playName = "骰宝";

        UserBetRecordEntity betRecordEntity = new UserBetRecordEntity();
        betRecordEntity.setTaskNo(taskNo);
        betRecordEntity.setCode(lotCode);
        betRecordEntity.setPeriodNumber(periodNumber);
        betRecordEntity.setAccountName(accountName);
        betRecordEntity.setAccountId(accountId);
        betRecordEntity.setBuyMoney(buyMoney);
        betRecordEntity.setBuyZhuShu(buyZhuShu);
        betRecordEntity.setHaoMa(haoMa);
        betRecordEntity.setLotName(lotName);
        betRecordEntity.setLotType(lotType);
        betRecordEntity.setStationId(stationId);
        betRecordEntity.setOdds(odds);
        betRecordEntity.setPlayCode(playCode);
        betRecordEntity.setPlayName(playName);

        int insert = userBetRecordDomain.insert(betRecordEntity);
        Assert.assertTrue("插入投注记录失败", insert > 0);
        System.out.println("betRecordEntity = " + betRecordEntity);
    }

    @Test
    public void testQuery() {

        String taskNo = "T202209090000000";
        String lotCode = "FFK3";
        String periodNumber = "202209090092";
        UserBetRecordEntity betRecordEntity = userBetRecordDomain.query(taskNo, lotCode, periodNumber);
        Assert.assertNotNull("查询投注记录为空", betRecordEntity);
        System.out.println("betRecordEntity = " + betRecordEntity);
    }

    @Test
    public void testQueryByBatchList() {

        String taskNo = "T202209090000000";
        String lotCode = "FFK3";
        List<UserBetRecordEntity> betRecordEntities = userBetRecordDomain.queryByTaskList(taskNo, lotCode);

        Assert.assertNotNull("查询投注记录列表为空", betRecordEntities);
        Assert.assertTrue("查询投注记录列表无数据", betRecordEntities.size() > 0);
        System.out.println("betRecordEntities = " + betRecordEntities);
    }


    @Test
    public void testQueryByPeriodList() {

        String lotCode = "FFK3";
        String periodNumber = "202209090092";
        List<UserBetRecordEntity> betRecordEntities = userBetRecordDomain.queryByPeriodList(lotCode, periodNumber);

        Assert.assertNotNull("查询投注记录列表为空", betRecordEntities);
        Assert.assertTrue("查询投注记录列表无数据", betRecordEntities.size() > 0);
        System.out.println("betRecordEntities = " + betRecordEntities);
    }

    @Test
    public void testQueryBetTaskStats() {
        String taskNo = "T202209090000001";
        String lotCode = "FFK3";
        BetTaskStatsDTO betTaskStatsDTO = userBetRecordDomain.queryBetTaskStats(taskNo, lotCode);
        Assert.assertNotNull("查询投注统计数据信息为空", betTaskStatsDTO);
        System.out.println("betTaskStatsDTO = " + betTaskStatsDTO);
    }
}
