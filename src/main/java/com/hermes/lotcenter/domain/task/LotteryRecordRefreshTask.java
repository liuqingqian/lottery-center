package com.hermes.lotcenter.domain.task;

import com.beicai.common.DateTimeUtil;
import com.hermes.lotcenter.domain.*;
import com.hermes.lotcenter.domain.dto.LotRecordDTO;
import com.hermes.lotcenter.entity.LotteryRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by liuqingqian on 2022/9/12.
 */
@Component
public class LotteryRecordRefreshTask {

    @Autowired
    private DownloadLotDomain downloadLotDomain;

    @Autowired
    private FileOptDomain fileOptDomain;

    @Autowired
    private LotTableParserDomain lotTableParserDomain;

    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;

    @Scheduled(cron = "10 */3 * * * ?")
    public void refreshTask() {
        String url = LotteryConfig.RESULT_85_URL;
        Map<String, Object> params = new HashMap<>();
        String lotCode = "FFK3";
        System.out.println("[RefreshTask]触发时间 = " + DateTimeUtil.nowStr());
        String startDate = DateTimeUtil.nowStr(DateTimeUtil.yyyy_MM_dd);
        System.out.println("startDate = " + startDate);
        params.put("lotCode", lotCode);
        params.put("startDate", startDate);
        String rawContent = downloadLotDomain.getHtmlRawContent(url, params);
        boolean writeLotFile = fileOptDomain.writeLotFile(rawContent, lotCode, startDate);

        String rawFileContent = fileOptDomain.readLotFile(lotCode, startDate);

        List<LotRecordDTO> lotRecordList = lotTableParserDomain.parseLotTable(lotCode, rawFileContent);
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
        System.out.println("batchInsert size: " + lotteryRecordEntities.size() + " ,Insert " + batchInsert);
    }
}
