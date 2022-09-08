package com.hermes.lotcenter.domain.rpc.criteria;

/**
 * Created by liuqingqian on 2022/9/8.
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("下注请求参数")
public class DoBetCriteria {

    @ApiModelProperty("彩种代码")
    private String lotCode;

    @ApiModelProperty("期号")
    private String qiHao;

    @ApiModelProperty("群组代码")
    private String groupCode;//daxiaoshaibao

    @ApiModelProperty("玩法类别代码")
    private String playCode;//daxiaoshaibao

    @ApiModelProperty("彩种类型@10:快三")
    private Integer lotType;

    @ApiModelProperty("站点ID")
    private Integer stId;//stationId:111

    @ApiModelProperty("投注数据")
    private String data;//BetDataDTO JSONArrayString


}
