package com.hermes.lotcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/8/31.
 */
@Data
@TableName("lottery_record")
public class LotteryRecordEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("code")
    @ApiModelProperty("彩种编码")
    private String code;

    @TableField("period_number")
    @ApiModelProperty("期数")
    private String periodNumber;

    @TableField("dice1")
    @ApiModelProperty("骰1")
    private Integer dice1;

    @TableField("dice2")
    @ApiModelProperty("骰2")
    private Integer dice2;

    @TableField("dice3")
    @ApiModelProperty("骰3")
    private Integer dice3;

    @TableField("equals")
    @ApiModelProperty("豹@0:否@1:是")
    private Integer equals;

    @TableField("sum")
    @ApiModelProperty("和-彩球号码")
    private Integer sum;

    @TableField("size")
    @ApiModelProperty("大小@1:小@2:大@:3豹")
    private Integer size;

    @TableField("single_double")
    @ApiModelProperty("单双@1:单@2:双@:3豹")
    private Integer singleDouble;

    @TableField("lot_status")
    @ApiModelProperty("状态 @1:已开奖@2:未开奖")
    private Integer lotStatus;

    @TableField("lot_time")
    @ApiModelProperty("开奖时间")
    private String lotTime;//yyyy-MM-dd HH:mm:ss

    @TableField("created_time")
    private String createdTime;//yyyy-MM-dd HH:mm:ss

    @TableField("updated_time")
    private String updatedTime;//yyyy-MM-dd HH:mm:ss
}
