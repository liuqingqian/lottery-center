package com.hermes.lotcenter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hermes.lotcenter.domain.AnalysisLotteryDomain;
import com.hermes.lotcenter.domain.LotteryRecordDomain;
import com.hermes.lotcenter.domain.dto.DayCycleHotTimeDTO;
import com.hermes.lotcenter.domain.dto.DayTimeHotDTO;
import com.hermes.lotcenter.domain.dto.LotCycleMatchingScoreDTO;
import com.hermes.lotcenter.domain.dto.LotMatchingScoreDTO;
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
 * Created by liuqingqian on 2022/9/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAnalysisLotteryDomain {

    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;

    @Autowired
    private AnalysisLotteryDomain analysisLotteryDomain;

    @Test
    public void testMatchingScoreList() {
        String lotCode = "FFK3";
        String startDate = "2022-09-03";
        List<LotteryRecordEntity> lotteryRecordEntities = lotteryRecordDomain.queryListByDate(lotCode, startDate);
        List<LotMatchingScoreDTO> lotMatchingScoreList = analysisLotteryDomain.matchingScoreList(lotteryRecordEntities);

        Assert.assertNotNull("查询开奖记录连号分值信息列表为空", lotMatchingScoreList);
        Assert.assertTrue("查询开奖记录连号分值信息列表无数据", lotMatchingScoreList.size() > 0);
        System.out.println("lotMatchingScoreList = " + lotMatchingScoreList);
    }


    @Test
    public void testGroupMatchingScore() {
        String lotCode = "FFK3";
        String startDate = "2022-09-09";
        List<LotMatchingScoreDTO> lotMatchingScoreList = analysisLotteryDomain.matchingScoreList(lotteryRecordDomain.queryListByDate(lotCode, startDate));
        int totalSize = lotMatchingScoreList.size();
        int cycleTime = 60;

        Map<Integer, LotCycleMatchingScoreDTO> cycleMatchingScoreMap = Maps.newHashMap();
        for (int i = 0; i < totalSize; i++) {
            LotMatchingScoreDTO matchingScoreDTO = lotMatchingScoreList.get(i);
            Integer cycleGroup = (i / cycleTime + 1);
            LotCycleMatchingScoreDTO cycleMatchingScoreDTO = cycleMatchingScoreMap.get(cycleGroup);
            if (Objects.isNull(cycleMatchingScoreDTO)) {
                cycleMatchingScoreDTO = new LotCycleMatchingScoreDTO();
                cycleMatchingScoreDTO.setDate(startDate);
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
        cycleMatchingScoreMap.values().stream()
                .sorted(Comparator.comparing(LotCycleMatchingScoreDTO::getCycleGroup))
                .forEach(cycleMatchingScoreDTO -> {
                    System.out.println(cycleMatchingScoreDTO);
                });
    }

    @Test
    public void testDayMatchingScore() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String lotCode = "FFK3";
        String startMonth = "2022-09";
        Date dateMonth = dateFormat.parse(startMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateMonth);
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String format = "%s-%02d";
        List<DayTimeHotDTO> dayTimeHots = Lists.newArrayList();
        for (int i = 1; i <= 9; i++) {
            DayTimeHotDTO dayTimeHot = new DayTimeHotDTO();
            String startDate = String.format(format, startMonth, i);
            System.out.println("startDate = " + startDate);
            List<LotMatchingScoreDTO> lotMatchingScoreList = analysisLotteryDomain.matchingScoreList(lotteryRecordDomain.queryListByDate(lotCode, startDate));
            int totalSize = lotMatchingScoreList.size();
            int cycleTime = 60;

            Map<Integer, LotCycleMatchingScoreDTO> cycleMatchingScoreMap = Maps.newHashMap();
            for (int j = 0; j < totalSize; j++) {
                LotMatchingScoreDTO matchingScoreDTO = lotMatchingScoreList.get(j);
                Integer cycleGroup = (j / cycleTime + 1);
                LotCycleMatchingScoreDTO cycleMatchingScoreDTO = cycleMatchingScoreMap.get(cycleGroup);
                if (Objects.isNull(cycleMatchingScoreDTO)) {
                    cycleMatchingScoreDTO = new LotCycleMatchingScoreDTO();
                    cycleMatchingScoreDTO.setDate(startDate);
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
            List<LotCycleMatchingScoreDTO> cycleMatchingScoreList = cycleMatchingScoreMap.values().stream()
                    .sorted(Comparator.comparing(LotCycleMatchingScoreDTO::getCycleGroup))
                    .collect(Collectors.toList());
            dayTimeHot.setCode(lotCode);
            dayTimeHot.setDate(startDate);
            dayTimeHot.setMatchingScoreList(cycleMatchingScoreList);
            int daySizeMatchingScore = cycleMatchingScoreList.stream()
                    .mapToInt(LotCycleMatchingScoreDTO::getSizeMatchingScore)
                    .sum();
            int daySingleDoubleMatchingScore = cycleMatchingScoreList.stream()
                    .mapToInt(LotCycleMatchingScoreDTO::getSingleDoubleMatchingScore)
                    .sum();
            dayTimeHot.setSizeMatchingScore(daySizeMatchingScore);
            dayTimeHot.setSingleDoubleMatchingScore(daySingleDoubleMatchingScore);
            dayTimeHots.add(dayTimeHot);
        }

        List<DayCycleHotTimeDTO> dayCycleHotTimes = Lists.newArrayList();

        List<LotCycleMatchingScoreDTO> lotCycleMatchingScores = dayTimeHots.stream()
                .map(DayTimeHotDTO::getMatchingScoreList)
                .flatMap(Collection::stream).filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Integer, List<LotCycleMatchingScoreDTO>> groupListMap = lotCycleMatchingScores.stream()
                .collect(Collectors.groupingBy(LotCycleMatchingScoreDTO::getCycleGroup));
        groupListMap.entrySet().stream()
                .forEach(groupListEntry -> {
                    Integer cycleGroup = groupListEntry.getKey();
                    List<LotCycleMatchingScoreDTO> groupCycleMatchingScores = groupListEntry.getValue();
                    LotCycleMatchingScoreDTO first = groupCycleMatchingScores.stream().findFirst().orElse(null);
                    DayCycleHotTimeDTO dayCycleHotTimeDTO = new DayCycleHotTimeDTO();
                    dayCycleHotTimeDTO.setCycleGroup(cycleGroup);
                    dayCycleHotTimeDTO.setCycleTime(first.getCycleTime());
                    //热度转换，设置一个阈值，大于阈值则认为有效热度得1分否则0分，该有效热度需要满足回踩该时间周期时满足两个条件：1、不可大于最大回撤；2、满足对应盈亏比
                    Integer sizeHot = groupCycleMatchingScores.stream()
                            .mapToInt(lotCycleMatchingScore -> lotCycleMatchingScore.getSizeMatchingScore() >= 60 ? 1 : 0)
                            .sum();
                    Integer singleDoubleHot = groupCycleMatchingScores.stream()
                            .mapToInt(lotCycleMatchingScore -> lotCycleMatchingScore.getSingleDoubleMatchingScore() >= 60 ? 1 : 0)
                            .sum();
                    dayCycleHotTimeDTO.setSizeHot(sizeHot);
                    dayCycleHotTimeDTO.setSingleDoubleHot(singleDoubleHot);
                    dayCycleHotTimes.add(dayCycleHotTimeDTO);
                });

        System.out.println("dayCycleHotTimes = " + dayCycleHotTimes);
    }

}
