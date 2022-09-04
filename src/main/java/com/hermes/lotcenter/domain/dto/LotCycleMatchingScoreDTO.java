package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/4.
 */
@Data
@ApiModel("开奖周期连号分值信息")
public class LotCycleMatchingScoreDTO {

    @ApiModelProperty("日期")
    private String date;//yyyy-MM-dd

    @ApiModelProperty("周期组号")
    private Integer cycleGroup;

    @ApiModelProperty("周期时间单位：分钟")
    private Integer cycleTime;

    @ApiModelProperty("大小匹配度")
    private Integer sizeMatchingScore;

    @ApiModelProperty("单双匹配度")
    private Integer singleDoubleMatchingScore;

}
