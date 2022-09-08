package com.hermes.lotcenter.domain;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.hermes.lotcenter.domain.dto.BetDataDTO;
import com.hermes.lotcenter.domain.rpc.criteria.DoBetCriteria;
import com.hermes.lotcenter.infrastructure.enums.BetDataIdEnum;
import com.hermes.lotcenter.infrastructure.utils.JSONTextUtil;
import org.springframework.stereotype.Component;

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

    public DoBetCriteria buildCriteria(String lotCode, String qiHao, Integer money, BetDataIdEnum dataIdEnum) {
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

    public Map<String, ?> buildFromCriteria(String lotCode, String qiHao, Integer money, BetDataIdEnum dataIdEnum) {
        DoBetCriteria doBetCriteria = buildCriteria(lotCode, qiHao, money, dataIdEnum);
        return BeanUtil.beanToMap(doBetCriteria);
    }

    public BetDataDTO buildBetData(Integer money, BetDataIdEnum dataIdEnum) {
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
}
