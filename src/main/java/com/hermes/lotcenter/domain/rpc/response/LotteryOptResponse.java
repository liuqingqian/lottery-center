package com.hermes.lotcenter.domain.rpc.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Data
@ApiModel("指定彩种当下开奖操作请求结果信息")
public class LotteryOptResponse {

    @ApiModelProperty("当前")
    private Current current;

    @ApiModelProperty("最近")
    private Last last;

    @ApiModelProperty("历史十期")
    private List<History> history;

    @ApiModelProperty("是否成")
    private Boolean success;

    @Data
    @ApiModel("当前")
    public static class Current {
        @ApiModelProperty("期号")
        private String qiHao;

        @ApiModelProperty("开奖时间")
        private String activeTime;//yyyy-MM-dd HH:mm:ss

        @ApiModelProperty("系统时间")
        private String serverTime;//yyyy-MM-dd HH:mm:ss

        private Integer ago;

        @ApiModelProperty("状态")
        private Integer state;
    }

    @Data
    @ApiModel("最近")
    public static class Last {
        @ApiModelProperty("期号")
        private String qiHao;

        @ApiModelProperty("号码")
        private String haoMa;
    }

    @Data
    @ApiModel("历史十期")
    public static class History {
        @ApiModelProperty("期号")
        private String qiHao;

        @ApiModelProperty("号码")
        private String haoMa;
    }


    @Data
    @ApiModel("省略")
    public static class Ommit {

    }


//    {
//        "current": {
//        "qiHao": "202209080090",
//                "activeTime": "2022-09-08 01:29:00",
//                "ago": 10,
//                "serverTime": "2022-09-08 01:28:10",
//                "state": 1
//    },
//        "last": {
//        "qiHao": "202209080089",
//                "haoMa": "1,4,6"
//    },
//        "success": true,
//            "history": [
//        {
//            "qiHao": "202209080089",
//                "haoMa": "1,4,6"
//        },
//        {
//            "qiHao": "202209080088",
//                "haoMa": "3,5,5"
//        },
//        {
//            "qiHao": "202209080087",
//                "haoMa": "1,2,4"
//        },
//        {
//            "qiHao": "202209080086",
//                "haoMa": "1,2,2"
//        },
//        {
//            "qiHao": "202209080085",
//                "haoMa": "1,2,4"
//        },
//        {
//            "qiHao": "202209080084",
//                "haoMa": "1,2,2"
//        },
//        {
//            "qiHao": "202209080083",
//                "haoMa": "4,4,5"
//        },
//        {
//            "qiHao": "202209080082",
//                "haoMa": "1,2,5"
//        },
//        {
//            "qiHao": "202209080081",
//                "haoMa": "2,2,6"
//        },
//        {
//            "qiHao": "202209080080",
//                "haoMa": "1,1,6"
//        }
//    ],
//        "ommitList": [
//        {
//            "addFirst": false,
//                "le": "FFK3",
//                "name": "总和",
//                "names": "单",
//                "rebake": [
//            {
//                "name": "单",
//                    "bate": 1003
//            },
//            {
//                "name": "双",
//                    "bate": 1003
//            }
//            ],
//            "sortNum": 7
//        },
//        {
//            "addFirst": false,
//                "le": "FFK3",
//                "name": "总和",
//                "names": "大",
//                "rebake": [
//            {
//                "name": "大",
//                    "bate": 1003
//            },
//            {
//                "name": "小",
//                    "bate": 1003
//            }
//            ],
//            "sortNum": 2
//        }
//    ]
//    }
}
