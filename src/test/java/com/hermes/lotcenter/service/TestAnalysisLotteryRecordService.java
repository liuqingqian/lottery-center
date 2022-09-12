package com.hermes.lotcenter.service;

import com.beicai.common.restful.ErrorCode;
import com.beicai.common.restful.Response;
import com.hermes.lotcenter.application.web.criteria.LotteryRecordCriteria;
import com.hermes.lotcenter.domain.dto.LotCycleMatchingScoreDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by liuqingqian on 2022/9/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAnalysisLotteryRecordService {

    @Autowired
    private AnalysisLotteryRecordService analysisLotteryRecordService;

    @Test
    public void testGroupMatchingScore() {
        String lotCode = "FFK3";
        LotteryRecordCriteria criteria = new LotteryRecordCriteria();
        criteria.setCode(lotCode);
        Response<List<LotCycleMatchingScoreDTO>> response = analysisLotteryRecordService.groupMatchingScore(criteria);
        Assert.assertEquals(response.getErrorMsg(), ErrorCode.OK.getCode(), response.getErrorCode());
        System.out.println("response.getResults() = " + response.getResults());
    }

}
