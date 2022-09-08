package com.hermes.lotcenter.domain.rpc.criteria;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Data
@ApiModel("开奖记录请求参数")
public class LotteryLastCriteria {

    @ApiModelProperty("彩种代码")
    private String lotCode;

    @ApiModelProperty("期号")
    private String qiHao;

}
