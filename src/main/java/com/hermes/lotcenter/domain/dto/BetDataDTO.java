package com.hermes.lotcenter.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Data
@ApiModel("投注数据信息")
public class BetDataDTO {
    @ApiModelProperty("投注ID")
    private String id;

    @ApiModelProperty("投注金额，单位：元")
    private String money;

    @ApiModelProperty("投注名称")
    private String name;

    @ApiModelProperty("赔率ID")
    private String oddsId;//60219

    @ApiModelProperty("赔率")
    private String rate;

    @ApiModelProperty("最大投注")
    private String max;//500000

    @ApiModelProperty("最小投注")
    private String min;//1

    @ApiModelProperty("娱乐代码")
    private String playCode;

}
//{
//        "money": "1",
//        "id": "10",
//        "name": "双",
//        "oddsId": "60219",
//        "rate": "1.98",
//        "max": "500000",
//        "min": "1",
//        "playCode": null
// }
