package com.hermes.lotcenter.application.web.criteria;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Data
@ApiModel("开奖操作信息请求参数")
public class LotteryRecordCriteria {

    @NotNull(message = "彩种代码不能为空")
    @ApiModelProperty("彩种代码")
    private String code;

    @ApiModelProperty("日期")
    private String date;//yyyy-MM-dd

    @ApiModelProperty("周期时间单位：分钟")
    private Integer cycleTime = 60;
}
