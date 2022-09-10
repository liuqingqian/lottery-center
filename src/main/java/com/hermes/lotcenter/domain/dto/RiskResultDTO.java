package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/11.
 */
@Data
@ApiModel("风险结果信息")
public class RiskResultDTO {

    @ApiModelProperty("投注统计信息")
    private BetTaskStatsDTO betTaskStats;

    @ApiModelProperty("状态 @1:正常@2:异常")
    private int status;

    @ApiModelProperty("状态消息")
    private String message;
}
