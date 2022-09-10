package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/9.
 * <p>
 * 投注记录状态枚举 @1:未开奖@2:已中奖@3:未中奖
 */
public enum UserBetRecordStatusEnum {
    NOT_LOT(1, "未开奖"),
    WINNING_LOT(2, "已中奖"),
    LOSING_LOT(3, "未中奖");

    private Integer code;

    private String desc;

    UserBetRecordStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean equals(Integer code) {
        return this.getCode().equals(code);
    }

    public static UserBetRecordStatusEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        UserBetRecordStatusEnum[] values = values();
        for (UserBetRecordStatusEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
