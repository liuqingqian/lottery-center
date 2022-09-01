package com.hermes.lotcenter.domain;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hermes.lotcenter.domain.dto.LotRecordDTO;
import com.hermes.lotcenter.entity.LotteryRecordEntity;
import com.hermes.lotcenter.infrastructure.enums.LotSingleDoubleEnum;
import com.hermes.lotcenter.infrastructure.enums.LotSizeEnum;
import com.hermes.lotcenter.repository.LotteryRecordMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/1.
 */
@Component
public class LotteryRecordDomain extends ServiceImpl<LotteryRecordMapper, LotteryRecordEntity> {


    public boolean batchInsert(List<LotteryRecordEntity> lotteryRecordEntities) {
        if (CollectionUtils.isEmpty(lotteryRecordEntities)) {
            return false;
        }
        return saveBatch(lotteryRecordEntities);
    }

    public LotteryRecordEntity toLotteryRecordEntity(LotRecordDTO lotRecordDTO) {
        if (Objects.isNull(lotRecordDTO) ||
                StringUtils.isBlank(lotRecordDTO.getCode()) ||
                StringUtils.isBlank(lotRecordDTO.getPeriodNumber())) {
            return null;
        }

        LotteryRecordEntity recordEntity = new LotteryRecordEntity();
        BeanUtils.copyProperties(lotRecordDTO, recordEntity);
        //处理特殊字段：
        //大小@1:小@2:大
        if (LotSizeEnum.SMALL.equalsDesc(lotRecordDTO.getSizeTxt())) {
            recordEntity.setSize(LotSizeEnum.SMALL.getCode());
        } else if (LotSizeEnum.BIG.equalsDesc(lotRecordDTO.getSizeTxt())) {
            recordEntity.setSize(LotSizeEnum.BIG.getCode());
        } else {
            recordEntity.setSize(null);
        }
        //单双@1:单@2:双
        if (LotSingleDoubleEnum.ODD.equalsDesc(lotRecordDTO.getSingleDoubleTxt())) {
            recordEntity.setSingleDouble(LotSingleDoubleEnum.ODD.getCode());
        } else if (LotSingleDoubleEnum.EVEN.equalsDesc(lotRecordDTO.getSingleDoubleTxt())) {
            recordEntity.setSingleDouble(LotSingleDoubleEnum.EVEN.getCode());
        } else {
            recordEntity.setSingleDouble(null);
        }
        //豹@0:否@1:是
        recordEntity.setEquals(lotRecordDTO.getIsEquals() ? 1 : 0);
        return recordEntity;
    }

}
