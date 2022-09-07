package com.hermes.lotcenter.domain.rpc.criteria;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Data
@ApiModel("开奖操作信息请求参数")
public class LotteryOptCriteria {
    @ApiModelProperty("彩种代码")
    private String lotCode;
}
