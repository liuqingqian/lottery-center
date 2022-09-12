package com.hermes.lotcenter.service;

import com.beicai.common.restful.Response;
import com.hermes.lotcenter.application.web.criteria.LotteryRecordCriteria;
import com.hermes.lotcenter.domain.dto.LotCycleMatchingScoreDTO;

import java.util.List;

/**
 * Created by liuqingqian on 2022/9/12.
 */
public interface AnalysisLotteryRecordService {

    Response<List<LotCycleMatchingScoreDTO>> groupMatchingScore(LotteryRecordCriteria criteria);

}
