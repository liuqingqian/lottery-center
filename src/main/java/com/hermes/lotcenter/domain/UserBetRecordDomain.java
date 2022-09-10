package com.hermes.lotcenter.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hermes.lotcenter.domain.dto.BetTaskStatsDTO;
import com.hermes.lotcenter.entity.UserBetRecordEntity;
import com.hermes.lotcenter.repository.UserBetRecordMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@Component
public class UserBetRecordDomain extends ServiceImpl<UserBetRecordMapper, UserBetRecordEntity> {

    public int insert(UserBetRecordEntity userBetRecordEntity) {
        if (Objects.isNull(userBetRecordEntity)) {
            return 0;
        }
        return getBaseMapper().insert(userBetRecordEntity);
    }

    public int updateBetRecord(String taskNo, String code, String periodNumber, UserBetRecordEntity updateBetRecordEntity) {
        if (StringUtils.isBlank(taskNo) || StringUtils.isBlank(code) || StringUtils.isBlank(periodNumber) || Objects.isNull(updateBetRecordEntity)) {
            return 0;
        }
        LambdaUpdateWrapper<UserBetRecordEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserBetRecordEntity::getTaskNo, taskNo);
        updateWrapper.eq(UserBetRecordEntity::getCode, code);
        updateWrapper.eq(UserBetRecordEntity::getPeriodNumber, periodNumber);

        //条件字段不更新
        updateBetRecordEntity.setTaskNo(null);
        updateBetRecordEntity.setCode(null);
        updateBetRecordEntity.setPeriodNumber(null);
        return baseMapper.update(updateBetRecordEntity, updateWrapper);
    }

    public UserBetRecordEntity query(String taskNo, String code, String periodNumber) {
        if (StringUtils.isBlank(taskNo) || StringUtils.isBlank(code) || StringUtils.isBlank(periodNumber)) {
            return null;
        }
        LambdaQueryWrapper<UserBetRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserBetRecordEntity::getTaskNo, taskNo);
        queryWrapper.eq(UserBetRecordEntity::getCode, code);
        queryWrapper.eq(UserBetRecordEntity::getPeriodNumber, periodNumber);
        queryWrapper.last("LIMIT 1");
        return baseMapper.selectOne(queryWrapper);
    }

    public List<UserBetRecordEntity> queryByTaskList(String taskNo, String code) {
        if (StringUtils.isBlank(taskNo) || StringUtils.isBlank(code)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<UserBetRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserBetRecordEntity::getTaskNo, taskNo);
        queryWrapper.eq(UserBetRecordEntity::getCode, code);

        return baseMapper.selectList(queryWrapper);
    }

    public List<UserBetRecordEntity> queryByTaskStatusList(String taskNo, String code, Integer status) {
        if (StringUtils.isBlank(taskNo) || StringUtils.isBlank(code)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<UserBetRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserBetRecordEntity::getTaskNo, taskNo);
        queryWrapper.eq(UserBetRecordEntity::getCode, code);
        if (Objects.nonNull(status)) {
            queryWrapper.eq(UserBetRecordEntity::getStatus, status);
        }

        return baseMapper.selectList(queryWrapper);
    }

    public List<UserBetRecordEntity> queryByPeriodList(String code, String periodNumber) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(periodNumber)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<UserBetRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserBetRecordEntity::getCode, code);
        queryWrapper.eq(UserBetRecordEntity::getPeriodNumber, periodNumber);

        return baseMapper.selectList(queryWrapper);
    }

    public BetTaskStatsDTO queryBetTaskStats(String taskNo, String code) {
        if (StringUtils.isBlank(taskNo) || StringUtils.isBlank(code)) {
            return null;
        }
        QueryWrapper<UserBetRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("COUNT(period_number) AS betTimes",
                "SUM(buy_money) AS totalBuyMoney",
                "SUM(win_money) AS totalWinMoney")
                .eq("task_no", taskNo)
                .eq("code", code);
        Map<String, Object> map = this.getMap(queryWrapper);
        Long betTimes = (Long) map.get("betTimes");
        BigDecimal totalBuyMoney = (BigDecimal) map.get("totalBuyMoney");
        BigDecimal totalWinMoney = (BigDecimal) map.get("totalWinMoney");
        BetTaskStatsDTO betTaskStatsDTO = new BetTaskStatsDTO();
        betTaskStatsDTO.setTaskNo(taskNo);
        betTaskStatsDTO.setCode(code);
        betTaskStatsDTO.setBetTimes(betTimes.intValue());
        betTaskStatsDTO.setTotalBuyMoney(Objects.isNull(totalBuyMoney) ? 0 : totalBuyMoney.doubleValue());
        betTaskStatsDTO.setTotalWinMoney(Objects.isNull(totalWinMoney) ? 0 : totalWinMoney.doubleValue());

        return betTaskStatsDTO;
    }
}
