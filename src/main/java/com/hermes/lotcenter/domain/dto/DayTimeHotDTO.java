package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by liuqingqian on 2022/9/4.
 */
@Data
@ApiModel("日热点时段信息")
public class DayTimeHotDTO {

    @ApiModelProperty("彩种编码")
    private String code;

    @ApiModelProperty("日期")
    private String date;//yyyy-MM-dd

    @ApiModelProperty("大小匹配度")
    private Integer sizeMatchingScore;

    @ApiModelProperty("单双匹配度")
    private Integer singleDoubleMatchingScore;

    private List<LotCycleMatchingScoreDTO> matchingScoreList;
}
