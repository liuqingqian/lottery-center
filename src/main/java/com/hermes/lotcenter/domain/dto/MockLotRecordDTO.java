package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/2.
 */
@Data
@ApiModel("模拟投注记录信息")
public class MockLotRecordDTO {

    @ApiModelProperty("彩种编码")
    private String code;

    @ApiModelProperty("期数")
    private String periodNumber;

    @ApiModelProperty("投注大小")
    private String lotSizeTxt;

    @ApiModelProperty("大小")
    private String sizeTxt;

    @ApiModelProperty("投注单双")
    private String lotSingleDoubleTxt;

    @ApiModelProperty("单双")
    private String singleDoubleTxt;

    @ApiModelProperty("投注金额 单位：元")
    private Integer amount;

    @ApiModelProperty("账户金额 单位：元")
    private Integer accountAmount;

    @ApiModelProperty("模拟投注状态 @1:未开奖@2:未中奖@3:已中奖")
    private int lotStatus;
}
