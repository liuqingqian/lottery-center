package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
    //TODO:止损回撤：3000  目标盈利：10000 ，调整最大回撤与目标盈利，统计胜率；
    @ApiModelProperty("彩种编码")
    private String code;

    @ApiModelProperty("日期")
    private String date;//yyyy-MM-dd

    @ApiModelProperty("投注序号")
    private Integer lotNo;

    @ApiModelProperty("止损金额")
    private Integer stopLossAmount;

    @ApiModelProperty("止盈金额")
    private Integer stopProfitAmount;

    @ApiModelProperty("投注次数")
    private Integer times;

    @ApiModelProperty("成功次数")
    private Integer winTimes;

    @ApiModelProperty("失败次数")
    private Integer lossTimes;

    @ApiModelProperty("总投注（累计）金额 单位：元")
    private Integer totalAmount;

    @ApiModelProperty("总回撤金额")
    private Integer totalDrawDownAmount;

    @ApiModelProperty("总盈利金额")
    private Integer totalProfitAmount;

    @ApiModelProperty("场内最大回撤")
    private Integer maxDrawDown;

    @ApiModelProperty("场内最大盈利")
    private Integer maxProfit;

    @ApiModelProperty("交易状态 @1:止损@2:止盈@3:已出场@4:未入场")
    private Integer tradingStatus;

    @ApiModelProperty("本次投注记录（完整）")
    private List<MockLotRecordDTO> mockLotRecords;

    @ApiModelProperty("全局最大回撤")
    private Integer globalMaxDrawDown;

    @ApiModelProperty("全局最大盈利")
    private Integer globalMaxProfit;
}
