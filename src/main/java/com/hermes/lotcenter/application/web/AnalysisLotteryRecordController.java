package com.hermes.lotcenter.application.web;

import com.beicai.common.ValidationUtil;
import com.beicai.common.restful.ErrorCode;
import com.beicai.common.restful.Response;
import com.hermes.lotcenter.application.web.criteria.LotteryRecordCriteria;
import com.hermes.lotcenter.domain.dto.LotCycleMatchingScoreDTO;
import com.hermes.lotcenter.service.AnalysisLotteryRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuqingqian on 2022/9/12.
 */
@RestController
@RequestMapping("/hermes/analysis")
@Api(value = "彩种开奖记录统计分析接口", tags = "彩种开奖记录统计分析接口")
public class AnalysisLotteryRecordController {

    @Autowired
    private AnalysisLotteryRecordService analysisLotteryRecordService;

    @GetMapping("/record/hotspot/cycle-matching-score")
    @ApiOperation(value = "申请动态组卷试卷接口")
    public Response<List<LotCycleMatchingScoreDTO>> groupMatchingScore(@SpringQueryMap LotteryRecordCriteria criteria) {
        String validate = ValidationUtil.validate(criteria);
        if (StringUtils.isNotBlank(validate)) {
            return Response.error(ErrorCode.PARAM_INVALID, validate);
        }
        return analysisLotteryRecordService.groupMatchingScore(criteria);
    }
}
