package com.hermes.lotcenter.domain.rpc.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Data
@ApiModel("指定期号开奖记录请求结果信息")
public class LotteryLastResponse {

    @ApiModelProperty("最近")
    private Last last;

    @ApiModelProperty("是否成")
    private Boolean success;

    @ApiModelProperty("消息")
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public boolean nonSuccess() {
        return !isSuccess();
    }

    @Data
    @ApiModel("最近")
    public static class Last {

        private Long id;

        private Integer actionNo;

        @ApiModelProperty("彩种代码")
        private String lotCode;

        @ApiModelProperty("期号")
        private String qiHao;

        @ApiModelProperty("号码")
        private String haoMa;

        @ApiModelProperty("省略")
        private String ommit;

        @ApiModelProperty("开奖状态")
        private Integer openStatus;

        private Integer stationId;
        private Long openTime;
        private Long startTime;
    }
}


//{
//        "last": {
//        "actionNo": 1315,
//        "endTime": 1662558840000,
//        "haoMa": "2,2,5",
//        "id": 86210217,
//        "lotCode": "FFK3",
//        "ommit": "5,0,10,9,6,3973#35,0,7,9,5,3#7908,35,7,12,0,1",
//        "openStatus": 3,
//        "openTime": 1662558843271,
//        "qiHao": "202209071315",
//        "startTime": 1662558780000,
//        "stationId": 111
//        },
//        "success": true
//}
