package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/5.
 */
@Data
@ApiModel("日周期热点时段信息")
public class DayCycleHotTimeDTO {

    @ApiModelProperty("周期组号")
    private Integer cycleGroup;

    @ApiModelProperty("周期时间单位：分钟")
    private Integer cycleTime;

    @ApiModelProperty("大小热度")
    private Integer sizeHot;

    @ApiModelProperty("单双热度")
    private Integer singleDoubleHot;
}
