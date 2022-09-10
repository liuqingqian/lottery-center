package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.FileOptDomain;
import com.hermes.lotcenter.domain.LotTableParserDomain;
import com.hermes.lotcenter.domain.LotteryConfig;
import com.hermes.lotcenter.domain.LotteryRecordDomain;
import com.hermes.lotcenter.domain.dto.LotRecordDTO;
import com.hermes.lotcenter.entity.LotteryRecordEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
        String startDate = "2022-09-09";
//        String lotCode = "K41";
//        String startDate = "2022-08-29";
        String rawContent = fileOptDomain.readLotFile(lotCode, startDate);
        Assert.assertNotNull("读取开奖结果文件内容为空", rawContent);

        List<LotRecordDTO> lotRecordList = lotTableParserDomain.parseLotTable(lotCode, rawContent);
        Assert.assertNotNull("读取开奖结果列表为空", lotRecordList);
        Assert.assertTrue("读取开奖结果列表无数据", lotRecordList.size() > 0);
        //检查数据库，只补齐需要插入的数据
        List<LotteryRecordEntity> dbRecordEntities = lotteryRecordDomain.queryFileListByDate(lotCode, startDate);
        Map<String, LotteryRecordEntity> dbRecordEntityMap = dbRecordEntities.stream()
                .collect(Collectors.toMap(LotteryRecordEntity::getPeriodNumber, item -> item, (k1, k2) -> k2));


        List<LotteryRecordEntity> lotteryRecordEntities = lotRecordList.stream()
                //DB中没有的记录才插入；
                .filter(lotRecordDTO -> !dbRecordEntityMap.keySet().contains(lotRecordDTO.getPeriodNumber()))
                .map(lotteryRecordDomain::toLotteryRecordEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("lotteryRecordEntities = " + lotteryRecordEntities);
        boolean batchInsert = lotteryRecordDomain.batchInsert(lotteryRecordEntities);
        Assert.assertTrue("批量插入失败", batchInsert);
    }

    @Test
    public void testMonthBatchInsert() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String lotCode = "FFK3";
        String startMonth = "2022-01";
        Date dateMonth = dateFormat.parse(startMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateMonth);
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String format = "%s-%02d";
        for (int i = 1; i <= daysOfMonth; i++) {
            String startDate = String.format(format, startMonth, i);
            System.out.println("startDate = " + startDate);
            String url = LotteryConfig.RESULT_85_URL;
            Map<String, Object> params = new HashMap<>();
            params.put("lotCode", lotCode);
            params.put("startDate", startDate);
            String rawContent = fileOptDomain.readLotFile(lotCode, startDate);
            Assert.assertNotNull("读取开奖结果文件内容为空", rawContent);

            List<LotRecordDTO> lotRecordList = lotTableParserDomain.parseLotTable(lotCode, rawContent);
            Assert.assertNotNull("读取开奖结果列表为空", lotRecordList);
            Assert.assertTrue("读取开奖结果列表无数据", lotRecordList.size() > 0);

            //检查数据库，只补齐需要插入的数据
            List<LotteryRecordEntity> dbRecordEntities = lotteryRecordDomain.queryFileListByDate(lotCode, startDate);
            Map<String, LotteryRecordEntity> dbRecordEntityMap = dbRecordEntities.stream()
                    .collect(Collectors.toMap(LotteryRecordEntity::getPeriodNumber, item -> item, (k1, k2) -> k2));

            List<LotteryRecordEntity> lotteryRecordEntities = lotRecordList.stream()
                    //DB中没有的记录才插入；
                    .filter(lotRecordDTO -> !dbRecordEntityMap.keySet().contains(lotRecordDTO.getPeriodNumber()))
                    .map(lotteryRecordDomain::toLotteryRecordEntity)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            System.out.println("lotteryRecordEntities.size = " + lotteryRecordEntities.size());
            boolean batchInsert = lotteryRecordDomain.batchInsert(lotteryRecordEntities);
            System.out.println("batchInsert = " + batchInsert);
        }

        System.out.println("daysOfMonth = " + daysOfMonth);

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

    @Test
    public void testQueryFileListByDate() {
        String lotCode = "FFK3";
        String startDate = "2022-09-03";
        List<LotteryRecordEntity> lotteryRecordEntities = lotteryRecordDomain.queryFileListByDate(lotCode, startDate);
        Assert.assertNotNull("查询开奖记录列表为空", lotteryRecordEntities);
        Assert.assertTrue("查询开奖记录列表无数据", lotteryRecordEntities.size() > 0);
        System.out.println("lotteryRecordEntities = " + lotteryRecordEntities);
    }
}
