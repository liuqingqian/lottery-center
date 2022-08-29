package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/8/29.
 */
@Data
@ApiModel("开奖记录信息")
public class LotRecordDTO {

    @ApiModelProperty("彩种编码")
    private String code;

    @ApiModelProperty("期数")
    private String periodNumber;

    @ApiModelProperty("骰1")
    private Integer dice1;

    @ApiModelProperty("骰2")
    private Integer dice2;

    @ApiModelProperty("骰3")
    private Integer dice3;

    @ApiModelProperty("豹")
    private Boolean isEquals;

    @ApiModelProperty("和-彩球号码")
    private Integer sum;

    @ApiModelProperty("大小")
    private String sizeTxt;

    @ApiModelProperty("单双")
    private String singleDoubleTxt;

    @ApiModelProperty("状态 @1:已开奖@2:未开奖")
    private int lotStatus;

    @ApiModelProperty("开奖时间")
    private String lotTime;//yyyy-MM-dd HH:mm:ss
}

