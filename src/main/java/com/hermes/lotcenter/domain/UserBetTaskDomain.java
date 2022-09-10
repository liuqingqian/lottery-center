package com.hermes.lotcenter.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hermes.lotcenter.domain.dto.SpittleStrategyDataDTO;
import com.hermes.lotcenter.entity.UserBetTaskEntity;
import com.hermes.lotcenter.infrastructure.enums.BetTaskStatusEnum;
import com.hermes.lotcenter.repository.UserBetTaskMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@Component
public class UserBetTaskDomain extends ServiceImpl<UserBetTaskMapper, UserBetTaskEntity> {

    public int insert(UserBetTaskEntity userBetTaskEntity) {
        if (Objects.isNull(userBetTaskEntity)) {
            return 0;
        }
        return getBaseMapper().insert(userBetTaskEntity);
    }

    public int updateTaskStatus(String taskNo, Integer status) {
        BetTaskStatusEnum betTaskStatusEnum = BetTaskStatusEnum.fromCode(status);
        if (StringUtils.isBlank(taskNo) || Objects.isNull(betTaskStatusEnum)) {
            return 0;
        }
        LambdaUpdateWrapper<UserBetTaskEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserBetTaskEntity::getTaskNo, taskNo);

        UserBetTaskEntity updateBetTask = new UserBetTaskEntity();
        updateBetTask.setStatus(betTaskStatusEnum.getCode());

        return baseMapper.update(updateBetTask, updateWrapper);
    }

    public int updateTaskSession(String taskNo, String sessionId) {
        if (StringUtils.isBlank(taskNo) || StringUtils.isBlank(sessionId)) {
            return 0;
        }
        LambdaUpdateWrapper<UserBetTaskEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserBetTaskEntity::getTaskNo, taskNo);

        UserBetTaskEntity updateBetTask = new UserBetTaskEntity();
        updateBetTask.setSessionId(sessionId);

        return baseMapper.update(updateBetTask, updateWrapper);
    }

    public int updateTaskStrategy(String taskNo, Integer strategyType, SpittleStrategyDataDTO spittleStrategyDataDTO) {
        if (StringUtils.isBlank(taskNo) || (Objects.isNull(strategyType) && Objects.isNull(spittleStrategyDataDTO))) {
            return 0;
        }
        LambdaUpdateWrapper<UserBetTaskEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserBetTaskEntity::getTaskNo, taskNo);

        UserBetTaskEntity updateBetTask = new UserBetTaskEntity();
        if (Objects.nonNull(strategyType)) {
            updateBetTask.setStrategyType(strategyType);
        }
        if (Objects.nonNull(spittleStrategyDataDTO)) {
            updateBetTask.setStrategyData(spittleStrategyDataDTO);
        }
        return baseMapper.update(updateBetTask, updateWrapper);
    }

    public UserBetTaskEntity query(String taskNo) {
        if (StringUtils.isBlank(taskNo)) {
            return null;
        }
        LambdaQueryWrapper<UserBetTaskEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserBetTaskEntity::getTaskNo, taskNo);
        queryWrapper.last("LIMIT 1");
        return baseMapper.selectOne(queryWrapper);
    }
}
