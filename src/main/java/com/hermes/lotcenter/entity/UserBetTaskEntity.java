package com.hermes.lotcenter.entity;

/**
 * Created by liuqingqian on 2022/9/9.
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.hermes.lotcenter.domain.dto.SpittleStrategyDataDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value = "user_bet_task", autoResultMap = true)
public class UserBetTaskEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("code")
    @ApiModelProperty("彩种编码")
    private String code;

    @TableField("task_no")
    @ApiModelProperty("投注任务编号")
    private String taskNo;

    @TableField("account_id")
    @ApiModelProperty("账号ID")
    private Long accountId;

    @TableField("account_name")
    @ApiModelProperty("账号名称")
    private String accountName;

    @TableField("stop_loss_amount")
    @ApiModelProperty("止损金额 单位：元")
    private Double stopLossAmount;

    @TableField("stop_profit_amount")
    @ApiModelProperty("止盈金额 单位：元")
    private Double stopProfitAmount;

    @TableField("max_times")
    @ApiModelProperty("最大投注次数")
    private Integer maxTimes;

    @TableField("max_amount")
    @ApiModelProperty("最大投注（累计）金额 单位：元")
    private Double maxAmount;

    @TableField("strategy_type")
    @ApiModelProperty("投注策略类型 @1:顺投递减@2:逆投递减@3:倍投@4:顺龙@5:斩龙")
    private Integer strategyType;

    @TableField(value = "strategy_data", typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty("策略数据")
    private SpittleStrategyDataDTO strategyData;

    @TableField("session_id")
    @ApiModelProperty("会话ID")
    private String sessionId;

    @TableField("headers")
    @ApiModelProperty("请求头参数")
    private String headers;

    @TableField("trading_status")
    @ApiModelProperty("交易状态 @1:止损@2:止盈@3:已出场@4:未入场")
    private Integer tradingStatus;

    @TableField("total_loss_amount")
    @ApiModelProperty("总回撤金额 单位：元")
    private Integer totalLossAmount;

    @TableField("total_profit_amount")
    @ApiModelProperty("总盈利金额 单位：元")
    private Integer totalProfitAmount;

    @TableField("status")
    @ApiModelProperty("投注任务状态@1:未开始@2:进行中@3:已结束@4:已作废")
    private Integer status;

    @TableField("created_time")
    private String createdTime;//yyyy-MM-dd HH:mm:ss

    @TableField("updated_time")
    private String updatedTime;//yyyy-MM-dd HH:mm:ss
}
