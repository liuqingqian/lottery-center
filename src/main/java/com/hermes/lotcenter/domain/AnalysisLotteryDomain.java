package com.hermes.lotcenter.domain;

import com.google.common.collect.Lists;
import com.hermes.lotcenter.domain.dto.LotMatchingScoreDTO;
import com.hermes.lotcenter.entity.LotteryRecordEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by liuqingqian on 2022/9/4.
 */
@Component
public class AnalysisLotteryDomain {

    public List<LotMatchingScoreDTO> matchingScoreList(List<LotteryRecordEntity> lotteryRecordEntities) {

        if (CollectionUtils.isEmpty(lotteryRecordEntities)) {
            return Lists.newArrayList();
        }
        Map<String, LotteryRecordEntity> dbRecordEntityMap = lotteryRecordEntities.stream()
                .collect(Collectors.toMap(LotteryRecordEntity::getPeriodNumber, item -> item, (k1, k2) -> k2));

        List<LotMatchingScoreDTO> matchingScoreList = lotteryRecordEntities.stream()
                .map(lotteryRecordEntity -> toLotMatchingScoreDTO(lotteryRecordEntity, dbRecordEntityMap))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return matchingScoreList;
    }

    private LotMatchingScoreDTO toLotMatchingScoreDTO(LotteryRecordEntity lotteryRecordEntity, Map<String, LotteryRecordEntity> dbRecordEntityMap) {
        if (Objects.isNull(lotteryRecordEntity)) {
            return null;
        }
        String periodNumber = lotteryRecordEntity.getPeriodNumber();
        Integer size = lotteryRecordEntity.getSize();
        Integer singleDouble = lotteryRecordEntity.getSingleDouble();
        LotMatchingScoreDTO matchingScore = new LotMatchingScoreDTO();
        matchingScore.setCode(lotteryRecordEntity.getCode());
        matchingScore.setPeriodNumber(periodNumber);
        matchingScore.setLotTime(lotteryRecordEntity.getLotTime());

        BigDecimal period = new BigDecimal(periodNumber);

        //TODO:暂时简单处理，头和尾的上一期我们默认没有；
        //上期期数
        String prevPeriodNo = period.add(new BigDecimal(-1)).toString();
        //下期期数
        String nextPeriodNo = period.add(new BigDecimal(1)).toString();
        LotteryRecordEntity prevEntity = dbRecordEntityMap.get(prevPeriodNo);
        LotteryRecordEntity nextEntity = dbRecordEntityMap.get(nextPeriodNo);
        int sizeMatchingScore = 0;
        int singleDoubleMatchingScore = 0;
        if (Objects.nonNull(prevEntity)) {
            if (Objects.equals(prevEntity.getSize(), size)) {
                sizeMatchingScore++;
            }
            if (Objects.equals(prevEntity.getSingleDouble(), singleDouble)) {
                singleDoubleMatchingScore++;
            }
        }
        if (Objects.nonNull(nextEntity)) {
            if (Objects.equals(nextEntity.getSize(), size)) {
                sizeMatchingScore++;
            }
            if (Objects.equals(nextEntity.getSingleDouble(), singleDouble)) {
                singleDoubleMatchingScore++;
            }
        }
        matchingScore.setSizeMatchingScore(sizeMatchingScore);
        matchingScore.setSingleDoubleMatchingScore(singleDoubleMatchingScore);
        return matchingScore;
    }

    public static void main(String[] args) {
        String periodNumber = "202209040001";

        BigDecimal period = new BigDecimal(periodNumber);

        //上期期数
        String prevPeriodNo = period.add(new BigDecimal(-1)).toString();
        //下期期数
        String nextPeriodNo = period.add(new BigDecimal(1)).toString();
        System.out.println("period = " + period);
        System.out.println("prevPeriodNo = " + prevPeriodNo);
        System.out.println("nextPeriodNo = " + nextPeriodNo);
    }
}
