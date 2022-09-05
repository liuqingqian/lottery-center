package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/6.
 * <p>
 * 交易状态枚举 @1:止损@2:止盈@3:出场
 */
public enum TradingStatusEnum {

    STOP_LOSS(1, "止损"),
    STOP_PROFIT(2, "止盈"),
    STOP(3, "出场");

    private Integer code;

    private String desc;

    TradingStatusEnum(Integer code, String desc) {
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

    public static TradingStatusEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        TradingStatusEnum[] values = values();
        for (TradingStatusEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
