package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/11.
 */
@Data
@ApiModel("投注统计数据信息")
public class BetTaskStatsDTO {
    @ApiModelProperty("彩种编码")
    private String code;

    @ApiModelProperty("投注任务编号")
    private String taskNo;

    @ApiModelProperty("总投注次数")
    private Integer betTimes;

    @ApiModelProperty("总投注金额")
    private Double totalBuyMoney;

    @ApiModelProperty("总赢得金额")
    private Double totalWinMoney;
}
