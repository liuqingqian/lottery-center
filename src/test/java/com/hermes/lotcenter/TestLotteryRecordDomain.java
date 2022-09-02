package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.FileOptDomain;
import com.hermes.lotcenter.domain.LotTableParserDomain;
import com.hermes.lotcenter.domain.LotteryRecordDomain;
import com.hermes.lotcenter.domain.dto.LotRecordDTO;
import com.hermes.lotcenter.entity.LotteryRecordEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by liuqingqian on 2022/9/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLotteryRecordDomain {

    @Autowired
    private LotTableParserDomain lotTableParserDomain;

    @Autowired
    private FileOptDomain fileOptDomain;

    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;

    @Test
    public void testBatchInsert() {
        String lotCode = "FFK3";
        String startDate = "2022-08-31";
//        String lotCode = "K41";
//        String startDate = "2022-08-29";
        String rawContent = fileOptDomain.readLotFile(lotCode, startDate);
        Assert.assertNotNull("读取开奖结果文件内容为空", rawContent);

        List<LotRecordDTO> lotRecordList = lotTableParserDomain.parseLotTable(lotCode, rawContent);
        Assert.assertNotNull("读取开奖结果列表为空", lotRecordList);
        Assert.assertTrue("读取开奖结果列表无数据", lotRecordList.size() > 0);


        List<LotteryRecordEntity> lotteryRecordEntities = lotRecordList.stream()
                .map(lotteryRecordDomain::toLotteryRecordEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("lotteryRecordEntities = " + lotteryRecordEntities);
        boolean batchInsert = lotteryRecordDomain.batchInsert(lotteryRecordEntities);
        Assert.assertTrue("批量插入失败", batchInsert);
    }

    @Test
    public void testQuery() {
        String lotCode = "FFK3";
        String periodNumber = "202209010025";
        LotteryRecordEntity lotteryRecordEntity = lotteryRecordDomain.query(lotCode, periodNumber);
        Assert.assertNotNull("查询开奖记录为空", lotteryRecordEntity);
        System.out.println("lotteryRecordEntity = " + lotteryRecordEntity);
    }

    @Test
    public void testQueryList() {
        String lotCode = "FFK3";
        String startDate = "2022-08-31";
        List<LotteryRecordEntity> lotteryRecordEntities = lotteryRecordDomain.queryListByDate(lotCode, startDate);
        Assert.assertNotNull("查询开奖记录列表为空", lotteryRecordEntities);
        Assert.assertTrue("查询开奖记录列表无数据", lotteryRecordEntities.size() > 0);
        System.out.println("lotteryRecordEntities = " + lotteryRecordEntities);
    }
}
