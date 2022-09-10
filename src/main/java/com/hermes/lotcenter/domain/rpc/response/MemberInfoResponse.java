package com.hermes.lotcenter.domain.rpc.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@Data
@ApiModel("下注请求结果信息")
public class MemberInfoResponse {

    @ApiModelProperty("账号名称")
    private String account;

    @ApiModelProperty("账户金额，单位：元,保留两位小数")
    private Double money;

    @ApiModelProperty("系统时间")
    private String serverTime;//yyyy-MM-dd HH:mm:ss

    @ApiModelProperty("是否登录")
    private Boolean login;

    public boolean isSuccess() {
        return StringUtils.isNotBlank(serverTime);
    }

    public boolean nonSuccess() {
        return !isSuccess();
    }
}

//    {
//        "money": 7151.820000,
//            "serverTime": "2022-09-09 00:17:45",
//            "login": true,
//            "account": "kongkong888"
//    }
