package com.hermes.lotcenter.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hermes.lotcenter.entity.LotteryRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by liuqingqian on 2022/8/31.
 */
@Mapper
@Repository
public interface LotteryRecordMapper extends BaseMapper<LotteryRecordEntity> {
}
