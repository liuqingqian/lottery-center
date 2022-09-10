package com.hermes.lotcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@Data
@TableName("user_bet_record")
public class UserBetRecordEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("code")
    @ApiModelProperty("彩种编码")
    private String code;

    @TableField("period_number")
    @ApiModelProperty("期数")
    private String periodNumber;

    @TableField("record_id")
    @ApiModelProperty("记录ID=系统的ID")
    private Long recordId;

    @TableField("order_id")
    @ApiModelProperty("订单ID")
    private String orderId;

    @TableField("task_no")
    @ApiModelProperty("投注任务编号")
    private String taskNo;

    @TableField("account_id")
    @ApiModelProperty("账号ID")
    private Long accountId;

    @TableField("account_name")
    @ApiModelProperty("账号名称")
    private String accountName;

    @TableField("buy_ip")
    @ApiModelProperty("投注IP")
    private String buyIp;

    @TableField("buy_money")
    @ApiModelProperty("投注金额")
    private Double buyMoney;

    @TableField("buy_zhu_shu")
    @ApiModelProperty("投注注数")
    private Integer buyZhuShu;

    @TableField("hao_ma")
    @ApiModelProperty("投注号码")
    private String haoMa;

    @TableField("odds")
    @ApiModelProperty("赔率")
    private Double odds;

    @TableField("lot_name")
    @ApiModelProperty("彩种名称")
    private String lotName;

    @TableField("lot_type")
    @ApiModelProperty("彩种类型@10:快三")
    private Integer lotType;

    @TableField("lottery_hao_ma")
    @ApiModelProperty("开奖号码")
    private String lotteryHaoMa;

    @TableField("play_code")
    @ApiModelProperty("玩法代码")
    private String playCode;

    @TableField("play_name")
    @ApiModelProperty("玩法名称")
    private String playName;

    @TableField("win_money")
    @ApiModelProperty("赢得金额")
    private Double winMoney;

    @TableField("win_zhu_shu")
    @ApiModelProperty("赢得注数")
    private Integer winZhuShu;

    @TableField("station_id")
    @ApiModelProperty("站点ID")
    private Integer stationId;

    @TableField("sign_key")
    @ApiModelProperty("签名密钥")
    private String signKey;

    @TableField("status")
    @ApiModelProperty("投注记录状态@1:未开奖@2:已中奖@3:未中奖")
    private Integer status;

    @TableField("open_time")
    @ApiModelProperty("开奖时间")
    private String openTime;//yyyy-MM-dd HH:mm:ss

    @TableField("created_time")
    private String createdTime;//yyyy-MM-dd HH:mm:ss

    @TableField("updated_time")
    private String updatedTime;//yyyy-MM-dd HH:mm:ss
}
