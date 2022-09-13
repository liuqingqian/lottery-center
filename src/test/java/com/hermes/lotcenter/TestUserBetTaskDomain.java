package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.UserBetTaskDomain;
import com.hermes.lotcenter.domain.dto.SpittleStrategyDataDTO;
import com.hermes.lotcenter.entity.UserBetTaskEntity;
import com.hermes.lotcenter.infrastructure.enums.BetTaskStatusEnum;
import com.hermes.lotcenter.infrastructure.enums.SidedTypeEnum;
import com.hermes.lotcenter.infrastructure.enums.StrategyTypeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserBetTaskDomain {

    @Autowired
    private UserBetTaskDomain userBetTaskDomain;


    @Test
    public void testInsertSmall() {
        String taskNo = "T20220913H000001";
        String lotCode = "FFK3";
        Long accountId = 946912L;
        String accountName = "lh1384888";
        Double stopLossAmount = 300.00D;
        Double stopProfitAmount = 500.00D;
        Integer maxTimes = 500;
        Double maxAmount = 20000.00D;
        Integer strategyType = StrategyTypeEnum.POSITIVE_SPITTLE.getCode();
        SpittleStrategyDataDTO spittleStrategyDataDTO = new SpittleStrategyDataDTO();
        List<Double> strategy = Arrays.asList(50D, 40D, 30D, 25D, 25D);
        spittleStrategyDataDTO.setStrategy(strategy);
        spittleStrategyDataDTO.setSidedType(SidedTypeEnum.SINGLE_DOUBLE.getCode());
        String sessionId = "c356f3b3-ae5e-4391-8187-9015f76fa6f9";
        UserBetTaskEntity userBetTaskEntity = new UserBetTaskEntity();
        userBetTaskEntity.setTaskNo(taskNo);
        userBetTaskEntity.setCode(lotCode);
        userBetTaskEntity.setAccountName(accountName);
        userBetTaskEntity.setAccountId(accountId);
        userBetTaskEntity.setStopLossAmount(stopLossAmount);
        userBetTaskEntity.setStopProfitAmount(stopProfitAmount);
        userBetTaskEntity.setMaxAmount(maxAmount);
        userBetTaskEntity.setMaxTimes(maxTimes);
        userBetTaskEntity.setStrategyType(strategyType);
        userBetTaskEntity.setStrategyData(spittleStrategyDataDTO);
        userBetTaskEntity.setSessionId(sessionId);
        userBetTaskEntity.setHeaders("");
        userBetTaskEntity.setStatus(BetTaskStatusEnum.DOING.getCode());

        int insert = userBetTaskDomain.insert(userBetTaskEntity);
        Assert.assertTrue("插入投注任务失败", insert > 0);
        System.out.println("userBetTaskEntity = " + userBetTaskEntity);
    }

    @Test
    public void testInsertSmall2() {
        String taskNo = "T202209110000005";
        String lotCode = "FFK3";
        Long accountId = 946902L;
        String accountName = "kongkong888";
        Double stopLossAmount = 600.00D;
        Double stopProfitAmount = 1000.00D;
        Integer maxTimes = 500;
        Double maxAmount = 40000.00D;
        Integer strategyType = StrategyTypeEnum.POSITIVE_SPITTLE.getCode();
        SpittleStrategyDataDTO spittleStrategyDataDTO = new SpittleStrategyDataDTO();
        List<Double> strategy = Arrays.asList(100D, 80D, 60D, 50D, 50D);
        spittleStrategyDataDTO.setStrategy(strategy);
        spittleStrategyDataDTO.setSidedType(SidedTypeEnum.SINGLE_DOUBLE.getCode());
        String sessionId = "fca6a4d4-822b-4e71-a076-bbdc49a37291";
        UserBetTaskEntity userBetTaskEntity = new UserBetTaskEntity();
        userBetTaskEntity.setTaskNo(taskNo);
        userBetTaskEntity.setCode(lotCode);
        userBetTaskEntity.setAccountName(accountName);
        userBetTaskEntity.setAccountId(accountId);
        userBetTaskEntity.setStopLossAmount(stopLossAmount);
        userBetTaskEntity.setStopProfitAmount(stopProfitAmount);
        userBetTaskEntity.setMaxAmount(maxAmount);
        userBetTaskEntity.setMaxTimes(maxTimes);
        userBetTaskEntity.setStrategyType(strategyType);
        userBetTaskEntity.setStrategyData(spittleStrategyDataDTO);
        userBetTaskEntity.setSessionId(sessionId);
        userBetTaskEntity.setHeaders("");
        userBetTaskEntity.setStatus(BetTaskStatusEnum.DOING.getCode());

        int insert = userBetTaskDomain.insert(userBetTaskEntity);
        Assert.assertTrue("插入投注任务失败", insert > 0);
        System.out.println("userBetTaskEntity = " + userBetTaskEntity);
    }


    @Test
    public void testInsertMedium() {
        String taskNo = "T202209130000001";
        String lotCode = "FFK3";
        Long accountId = 946902L;
        String accountName = "kongkong888";
        Double stopLossAmount = 1200.00D;
        Double stopProfitAmount = 2000.00D;
        Integer maxTimes = 500;
        Double maxAmount = 80000.00D;
        Integer strategyType = StrategyTypeEnum.POSITIVE_SPITTLE.getCode();
        SpittleStrategyDataDTO spittleStrategyDataDTO = new SpittleStrategyDataDTO();
        List<Double> strategy = Arrays.asList(200D, 160D, 120D, 100D, 100D);
        spittleStrategyDataDTO.setStrategy(strategy);
        spittleStrategyDataDTO.setSidedType(SidedTypeEnum.SINGLE_DOUBLE.getCode());
        String sessionId = "a6f27e66-f403-432d-884b-58275c7bbf91";
        UserBetTaskEntity userBetTaskEntity = new UserBetTaskEntity();
        userBetTaskEntity.setTaskNo(taskNo);
        userBetTaskEntity.setCode(lotCode);
        userBetTaskEntity.setAccountName(accountName);
        userBetTaskEntity.setAccountId(accountId);
        userBetTaskEntity.setStopLossAmount(stopLossAmount);
        userBetTaskEntity.setStopProfitAmount(stopProfitAmount);
        userBetTaskEntity.setMaxAmount(maxAmount);
        userBetTaskEntity.setMaxTimes(maxTimes);
        userBetTaskEntity.setStrategyType(strategyType);
        userBetTaskEntity.setStrategyData(spittleStrategyDataDTO);
        userBetTaskEntity.setSessionId(sessionId);
        userBetTaskEntity.setHeaders("");
        userBetTaskEntity.setStatus(BetTaskStatusEnum.DOING.getCode());

        int insert = userBetTaskDomain.insert(userBetTaskEntity);
        Assert.assertTrue("插入投注任务失败", insert > 0);
        System.out.println("userBetTaskEntity = " + userBetTaskEntity);
    }

    @Test
    public void testInsertBig() {
        String taskNo = "T202209110000004";
        String lotCode = "FFK3";
        Long accountId = 946902L;
        String accountName = "kongkong888";
        Double stopLossAmount = 3000.00D;
        Double stopProfitAmount = 5000.00D;
        Integer maxTimes = 500;
        Double maxAmount = 200000.00D;
        Integer strategyType = StrategyTypeEnum.POSITIVE_SPITTLE.getCode();
        SpittleStrategyDataDTO spittleStrategyDataDTO = new SpittleStrategyDataDTO();
        List<Double> strategy = Arrays.asList(500D, 400D, 300D, 250D, 250D);
        spittleStrategyDataDTO.setStrategy(strategy);
        spittleStrategyDataDTO.setSidedType(SidedTypeEnum.SINGLE_DOUBLE.getCode());
        String sessionId = "fca6a4d4-822b-4e71-a076-bbdc49a37291";
        UserBetTaskEntity userBetTaskEntity = new UserBetTaskEntity();
        userBetTaskEntity.setTaskNo(taskNo);
        userBetTaskEntity.setCode(lotCode);
        userBetTaskEntity.setAccountName(accountName);
        userBetTaskEntity.setAccountId(accountId);
        userBetTaskEntity.setStopLossAmount(stopLossAmount);
        userBetTaskEntity.setStopProfitAmount(stopProfitAmount);
        userBetTaskEntity.setMaxAmount(maxAmount);
        userBetTaskEntity.setMaxTimes(maxTimes);
        userBetTaskEntity.setStrategyType(strategyType);
        userBetTaskEntity.setStrategyData(spittleStrategyDataDTO);
        userBetTaskEntity.setSessionId(sessionId);
        userBetTaskEntity.setHeaders("");
        userBetTaskEntity.setStatus(BetTaskStatusEnum.DOING.getCode());

        int insert = userBetTaskDomain.insert(userBetTaskEntity);
        Assert.assertTrue("插入投注任务失败", insert > 0);
        System.out.println("userBetTaskEntity = " + userBetTaskEntity);
    }

    @Test
    public void testQuery() {
        String taskNo = "T202209090000001";
        UserBetTaskEntity userBetTaskEntity = userBetTaskDomain.query(taskNo);
        Assert.assertNotNull("查询投注任务为空", userBetTaskEntity);
        System.out.println("userBetTaskEntity = " + userBetTaskEntity);
    }

    @Test
    public void testUpdateTaskStatus() {
        String taskNo = "T202209110000001";
        Integer status = BetTaskStatusEnum.DOING.getCode();
        int updateTaskStatus = userBetTaskDomain.updateTaskStatus(taskNo, status);
        Assert.assertTrue("更新投注任务状态失败", updateTaskStatus > 0);
        System.out.println("updateTaskStatus = " + updateTaskStatus);
    }

    @Test
    public void testUpdateTaskSession() {
        String taskNo = "T202209110000007";
        String sessionId = "660bf3bb-5b72-49b9-bbd3-d4ed0700aaa7";
//        String sessionId = LotteryConfig.SESSION;
        int updateTaskStatus = userBetTaskDomain.updateTaskSession(taskNo, sessionId);
        Assert.assertTrue("更新投注任务会话ID失败", updateTaskStatus > 0);
        System.out.println("updateTaskStatus = " + updateTaskStatus);
    }

    @Test
    public void testUpdateTaskStrategy() {
        String taskNo = "T202209110000001";
        Integer strategyType = StrategyTypeEnum.POSITIVE_SPITTLE.getCode();
        SpittleStrategyDataDTO spittleStrategyDataDTO = new SpittleStrategyDataDTO();
        List<Double> strategy = Arrays.asList(5D, 4D, 3D, 2D, 2D);
        spittleStrategyDataDTO.setStrategy(strategy);
        spittleStrategyDataDTO.setSidedType(SidedTypeEnum.SINGLE_DOUBLE.getCode());
        int updateTask = userBetTaskDomain.updateTaskStrategy(taskNo, strategyType, spittleStrategyDataDTO);
        Assert.assertTrue("更新投注任务策略失败", updateTask > 0);
        System.out.println("updateTask = " + updateTask);

    }


}
