package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/5.
 * <p>
 * 统计一段时间，一个月或者几天内的模拟投注信息包括：
 * 回测顺投策略计算出最大回撤、最大盈利比、最快达成盈利目标；
 * 控制好回撤和目标盈利，按照盈亏比打法来操作；
 * 计算最快达成目标的投注次数和投注金额；
 * 盘中统计：投注次数、投注累计金额、当前回撤、当前盈利、最大回撤、最大盈利、回撤振幅（EMA均线理论）；
 */
@Data
@ApiModel("模拟投注记录信息")
public class AnalysisMockLotRecordDTO {
    //TODO:最大回撤：3000  目标盈利：10000 ，调整最大回撤与目标盈利，统计胜率；
}
