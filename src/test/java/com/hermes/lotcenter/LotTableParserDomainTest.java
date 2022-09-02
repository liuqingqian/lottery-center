package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.FileOptDomain;
import com.hermes.lotcenter.domain.LotTableParserDomain;
import com.hermes.lotcenter.domain.dto.LotRecordDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by liuqingqian on 2022/8/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LotTableParserDomainTest {

    @Autowired
    private LotTableParserDomain lotTableParserDomain;

    @Autowired
    private FileOptDomain fileOptDomain;

    @Test
    public void testParserLotTable() {
        String lotCode = "FFK3";
        String startDate = "2022-09-02";
//        String lotCode = "K41";
//        String startDate = "2022-08-29";
        String rawContent = fileOptDomain.readLotFile(lotCode, startDate);
        Assert.assertNotNull("读取开奖结果文件内容为空", rawContent);

        List<LotRecordDTO> lotRecordList = lotTableParserDomain.parseLotTable(lotCode, rawContent);
        Assert.assertNotNull("读取开奖结果列表为空", lotRecordList);
        Assert.assertTrue("读取开奖结果列表无数据", lotRecordList.size() > 0);
        System.out.println("lotRecordList = " + lotRecordList);
    }

}
