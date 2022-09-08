package com.hermes.lotcenter.domain.rpc.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Data
@ApiModel("下注请求结果信息")
public class DoBetResponse {

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
}
