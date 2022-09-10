package com.hermes.lotcenter.domain;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.hermes.lotcenter.domain.dto.BetDataDTO;
import com.hermes.lotcenter.domain.dto.StrategyResultDTO;
import com.hermes.lotcenter.domain.rpc.criteria.DoBetCriteria;
import com.hermes.lotcenter.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotcenter.entity.UserBetRecordEntity;
import com.hermes.lotcenter.entity.UserBetTaskEntity;
import com.hermes.lotcenter.infrastructure.enums.BetDataIdEnum;
import com.hermes.lotcenter.infrastructure.enums.SidedEnum;
import com.hermes.lotcenter.infrastructure.enums.UserBetRecordStatusEnum;
import com.hermes.lotcenter.infrastructure.utils.JSONTextUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/8.
 * <p>
 * 投注服务领域
 */
@Component
public class DoBetDomain {

    /**
     * 投注相关的通用配置信息
     */
    //玩法类型：骰宝 daxiaoshaibao
    private static final String SHAIBAO = "daxiaoshaibao";
    //彩种类型@10:快三
    private static final int KS_LOT_TYPE = 10;
    //快三站点ID
    private static final int STATION_ID = 111;
    //赔率ID
    private static final int BET_ODDS_ID = 60219;
    //赔率
    private static final double BET_RATE = 1.98;
    //最大投注
    private static final int BET_MAX = 500000;
    //最小投注
    private static final int BET_MIN = 1;

    public DoBetCriteria buildCriteria(String lotCode, String qiHao, Double money, BetDataIdEnum dataIdEnum) {
        if (Objects.isNull(money) || Objects.isNull(dataIdEnum)) {
            return null;
        }
        if (money < 1) {
            throw new RuntimeException("投注金额不能小于1元");
        }

        DoBetCriteria doBetCriteria = new DoBetCriteria();
        doBetCriteria.setGroupCode(SHAIBAO);
        doBetCriteria.setPlayCode(SHAIBAO);
        doBetCriteria.setLotType(KS_LOT_TYPE);
        doBetCriteria.setStId(STATION_ID);
        doBetCriteria.setLotCode(lotCode);
        doBetCriteria.setQiHao(qiHao);
        //data
        BetDataDTO betDataDTO = buildBetData(money, dataIdEnum);
        if (Objects.nonNull(betDataDTO)) {
            String dataJson = JSONTextUtil.toJsonString(Lists.newArrayList(betDataDTO));
            doBetCriteria.setData(dataJson);
        }
        return doBetCriteria;
    }


    public UserBetRecordEntity toBetRecord(StrategyResultDTO strategyResultDTO, UserBetTaskEntity userBetTask) {
        if (Objects.isNull(strategyResultDTO) || Objects.isNull(userBetTask)) {
            return null;
        }
        UserBetRecordEntity betRecordEntity = new UserBetRecordEntity();
        betRecordEntity.setTaskNo(userBetTask.getTaskNo());
        betRecordEntity.setAccountId(userBetTask.getAccountId());
        betRecordEntity.setAccountName(userBetTask.getAccountName());
        betRecordEntity.setCode(strategyResultDTO.getCode());
        betRecordEntity.setPeriodNumber(strategyResultDTO.getQiHao());
        betRecordEntity.setHaoMa(strategyResultDTO.getHaoMa());
        betRecordEntity.setBuyMoney(strategyResultDTO.getMoney());
        betRecordEntity.setOpenTime(strategyResultDTO.getActiveTime());
        betRecordEntity.setStatus(UserBetRecordStatusEnum.NOT_LOT.getCode());
        //硬编码设置快三的固定参数
        betRecordEntity.setBuyZhuShu(1);
        betRecordEntity.setLotName("极速快三");
        betRecordEntity.setLotType(10);
        betRecordEntity.setOdds(1.98D);
        betRecordEntity.setPlayCode("daxiaoshaibao");
        betRecordEntity.setPlayName("骰宝");
        betRecordEntity.setStationId(111);

        return betRecordEntity;
    }

    public Map<String, ?> buildFromCriteria(String lotCode, String qiHao, Double money, BetDataIdEnum dataIdEnum) {
        DoBetCriteria doBetCriteria = buildCriteria(lotCode, qiHao, money, dataIdEnum);
        return BeanUtil.beanToMap(doBetCriteria);
    }

    public BetDataDTO buildBetData(Double money, BetDataIdEnum dataIdEnum) {
        if (Objects.isNull(money) || Objects.isNull(dataIdEnum)) {
            return null;
        }
        if (money < 1) {
            throw new RuntimeException("投注金额不能小于1元");
        }
        BetDataDTO betDataDTO = new BetDataDTO();
        betDataDTO.setMoney(String.valueOf(money));
        betDataDTO.setId(String.valueOf(dataIdEnum.getId()));
        betDataDTO.setName(dataIdEnum.getName());
        betDataDTO.setOddsId(String.valueOf(BET_ODDS_ID));
        betDataDTO.setRate(String.valueOf(BET_RATE));
        betDataDTO.setMax(String.valueOf(BET_MAX));
        betDataDTO.setMin(String.valueOf(BET_MIN));
        betDataDTO.setPlayCode(null);
        return betDataDTO;

    }

    //开奖返回需要修改的投注记录对象
    public UserBetRecordEntity doLottery(LotteryOptResponse.Last last, UserBetRecordEntity lastBetRecord) {
        if (Objects.isNull(last) || Objects.isNull(lastBetRecord)) {
            return null;
        }
        UserBetRecordEntity updateLastBetRecord = new UserBetRecordEntity();
        updateLastBetRecord.setTaskNo(lastBetRecord.getTaskNo());
        updateLastBetRecord.setCode(lastBetRecord.getCode());
        updateLastBetRecord.setPeriodNumber(lastBetRecord.getPeriodNumber());
        String lastLotteryHaoMa = last.getHaoMa();
        List<String> sidedNames = convertToName(lastLotteryHaoMa);
        boolean isLastBingo = sidedNames.contains(lastBetRecord.getHaoMa());
        if (isLastBingo) {
            Double winMoney = lastBetRecord.getBuyMoney() * lastBetRecord.getOdds();
            updateLastBetRecord.setWinMoney(winMoney);
            updateLastBetRecord.setWinZhuShu(1);
        }
        Integer betRecordStatus = isLastBingo ? UserBetRecordStatusEnum.WINNING_LOT.getCode() : UserBetRecordStatusEnum.LOSING_LOT.getCode();
        updateLastBetRecord.setLotteryHaoMa(lastLotteryHaoMa);
        updateLastBetRecord.setStatus(betRecordStatus);

        return updateLastBetRecord;
    }

    public List<String> convertToName(String lotteryHaoMa) {
        //haoMa=1,5,6 --> 单、双、大、小、豹
        if (StringUtils.isBlank(lotteryHaoMa)) {
            return Lists.newArrayList();
        }
        String[] diceArray = lotteryHaoMa.trim().split(",");
        if (Objects.isNull(diceArray) || diceArray.length != 3) {
            return Lists.newArrayList();
        }
        Integer dice1 = Integer.valueOf(diceArray[0].trim());
        Integer dice2 = Integer.valueOf(diceArray[1].trim());
        Integer dice3 = Integer.valueOf(diceArray[2].trim());

        return convertDice(dice1, dice2, dice3);
    }

    //    大小：三个开奖号码总和值11~17 为大；总和值4~10 为小；若三个号码相同、通吃[大]、[小]各注。
//    单双：三个开奖号码总和值5,7,9,11,13,15,17为单；总和值4,6,8,10,12,14,16为双；若三个号码相同、通吃[单]、[双]各注。
    public List<String> convertDice(Integer dice1, Integer dice2, Integer dice3) {
        if (Objects.isNull(dice1) || Objects.isNull(dice2) || Objects.isNull(dice3)) {
            return Lists.newArrayList();
        }
        if (Objects.equals(dice1, dice2) && Objects.equals(dice2, dice3)) {
            return Lists.newArrayList(SidedEnum.DUAL.getName());
        }
        Integer sum = dice1 + dice2 + dice3;
        String size = sum > 10 ? SidedEnum.BIG.getName() : SidedEnum.SMALL.getName();
        String singleDouble = sum % 2 == 0 ? SidedEnum.EVEN.getName() : SidedEnum.ODD.getName();
        return Lists.newArrayList(size, singleDouble);
    }

    public String convertSizeSidedName(String lotteryHaoMa) {
        List<String> sidedNames = convertToName(lotteryHaoMa);
        return getSizeSidedName(sidedNames);
    }

    public String convertSingleDoubleSidedName(String lotteryHaoMa) {
        List<String> sidedNames = convertToName(lotteryHaoMa);
        return getSingleDoubleSidedName(sidedNames);
    }

    public String getSizeSidedName(List<String> sidedNames) {
        if (CollectionUtils.isEmpty(sidedNames) || sidedNames.size() == 1) {
            return null;
        }
        if (sidedNames.contains(SidedEnum.BIG.getName())) {
            return SidedEnum.BIG.getName();
        } else if (sidedNames.contains(SidedEnum.SMALL.getName())) {
            return SidedEnum.SMALL.getName();
        } else {
            return null;
        }
    }

    public String getSingleDoubleSidedName(List<String> sidedNames) {
        if (CollectionUtils.isEmpty(sidedNames) || sidedNames.size() == 1) {
            return null;
        }
        if (sidedNames.contains(SidedEnum.ODD.getName())) {
            return SidedEnum.ODD.getName();
        } else if (sidedNames.contains(SidedEnum.EVEN.getName())) {
            return SidedEnum.EVEN.getName();
        } else {
            return null;
        }
    }

    public String getNegativeSizeSidedName(List<String> sidedNames) {
        if (CollectionUtils.isEmpty(sidedNames) || sidedNames.size() == 1) {
            return null;
        }
        if (sidedNames.contains(SidedEnum.BIG.getName())) {
            return SidedEnum.SMALL.getName();
        } else if (sidedNames.contains(SidedEnum.SMALL.getName())) {
            return SidedEnum.BIG.getName();
        } else {
            return null;
        }
    }

    public String getNegativeSingleDoubleSidedName(List<String> sidedNames) {
        if (CollectionUtils.isEmpty(sidedNames) || sidedNames.size() == 1) {
            return null;
        }
        if (sidedNames.contains(SidedEnum.ODD.getName())) {
            return SidedEnum.EVEN.getName();
        } else if (sidedNames.contains(SidedEnum.EVEN.getName())) {
            return SidedEnum.ODD.getName();
        } else {
            return null;
        }
    }

}
