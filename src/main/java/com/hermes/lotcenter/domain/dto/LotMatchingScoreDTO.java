package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/4.
 */
@Data
@ApiModel("开奖记录连号分值信息")
public class LotMatchingScoreDTO {

    @ApiModelProperty("彩种编码")
    private String code;

    @ApiModelProperty("期数")
    private String periodNumber;

    @ApiModelProperty("大小匹配度")
    private Integer sizeMatchingScore;

    @ApiModelProperty("单双匹配度")
    private Integer singleDoubleMatchingScore;

    @ApiModelProperty("开奖时间")
    private String lotTime;//yyyy-MM-dd HH:mm:ss
}
