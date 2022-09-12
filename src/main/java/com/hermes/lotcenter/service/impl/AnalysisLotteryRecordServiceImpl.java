package com.hermes.lotcenter.service.impl;

import com.beicai.common.DateTimeUtil;
import com.beicai.common.restful.Response;
import com.google.common.collect.Maps;
import com.hermes.lotcenter.application.web.criteria.LotteryRecordCriteria;
import com.hermes.lotcenter.domain.AnalysisLotteryDomain;
import com.hermes.lotcenter.domain.LotteryRecordDomain;
import com.hermes.lotcenter.domain.dto.LotCycleMatchingScoreDTO;
import com.hermes.lotcenter.domain.dto.LotMatchingScoreDTO;
import com.hermes.lotcenter.service.AnalysisLotteryRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by liuqingqian on 2022/9/12.
 */
@Service
@Slf4j
public class AnalysisLotteryRecordServiceImpl implements AnalysisLotteryRecordService {
    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;

    @Autowired
    private AnalysisLotteryDomain analysisLotteryDomain;

    @Override
    public Response<List<LotCycleMatchingScoreDTO>> groupMatchingScore(LotteryRecordCriteria criteria) {

        String code = criteria.getCode();
        String date = "";//yyyy-MM-dd
        if (StringUtils.isBlank(criteria.getDate())) {
            date = DateTimeUtil.nowStr(DateTimeUtil.yyyy_MM_dd);
        } else {
            date = criteria.getDate();
        }
        List<LotMatchingScoreDTO> lotMatchingScoreList = analysisLotteryDomain.matchingScoreList(lotteryRecordDomain.queryListByDate(code, date));
        int totalSize = lotMatchingScoreList.size();
        int cycleTime = criteria.getCycleTime();

        Map<Integer, LotCycleMatchingScoreDTO> cycleMatchingScoreMap = Maps.newHashMap();
        for (int i = 0; i < totalSize; i++) {
            LotMatchingScoreDTO matchingScoreDTO = lotMatchingScoreList.get(i);
            Integer cycleGroup = (i / cycleTime + 1);
            LotCycleMatchingScoreDTO cycleMatchingScoreDTO = cycleMatchingScoreMap.get(cycleGroup);
            if (Objects.isNull(cycleMatchingScoreDTO)) {
                cycleMatchingScoreDTO = new LotCycleMatchingScoreDTO();
                cycleMatchingScoreDTO.setDate(date);
                cycleMatchingScoreDTO.setCycleGroup(cycleGroup);
                cycleMatchingScoreDTO.setCycleTime(cycleTime);
                cycleMatchingScoreDTO.setSizeMatchingScore(0);
                cycleMatchingScoreDTO.setSingleDoubleMatchingScore(0);
            }
            Integer sizeMatchingScore = cycleMatchingScoreDTO.getSizeMatchingScore();
            Integer singleDoubleMatchingScore = cycleMatchingScoreDTO.getSingleDoubleMatchingScore();
            cycleMatchingScoreDTO.setSizeMatchingScore(sizeMatchingScore + matchingScoreDTO.getSizeMatchingScore());
            cycleMatchingScoreDTO.setSingleDoubleMatchingScore(singleDoubleMatchingScore + matchingScoreDTO.getSingleDoubleMatchingScore());
            cycleMatchingScoreMap.put(cycleGroup, cycleMatchingScoreDTO);
        }
        List<LotCycleMatchingScoreDTO> matchingScoreList = cycleMatchingScoreMap.values().stream()
                .sorted(Comparator.comparing(LotCycleMatchingScoreDTO::getCycleGroup))
                .collect(Collectors.toList());

        return Response.ok(matchingScoreList);
    }
}
