package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@Data
@ApiModel("唾沫策略数据信息")
public class SpittleStrategyDataDTO {

    @ApiModelProperty("策略内容")
    private List<Double> strategy;

    @ApiModelProperty("快三双面类型 @1:大小@2:单双@3:豹")
    private Integer sidedType;

}
