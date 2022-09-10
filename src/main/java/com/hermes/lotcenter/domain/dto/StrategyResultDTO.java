package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@Data
@ApiModel("策略结果信息")
public class StrategyResultDTO {

    @ApiModelProperty("彩种编码")
    private String code;

    @ApiModelProperty("投注期号")
    private String qiHao;

    @ApiModelProperty("投注号码")
    private String haoMa;

    @ApiModelProperty("投注金额")
    private Double money;

    @ApiModelProperty("开奖时间")
    private String activeTime;//yyyy-MM-dd HH:mm:ss

    @ApiModelProperty("开盘时长，单位：秒")
    private Integer ago;

    @ApiModelProperty("状态 @1:正常@2:异常@3:跳过")
    private int status;

    @ApiModelProperty("状态消息")
    private String message;
}
