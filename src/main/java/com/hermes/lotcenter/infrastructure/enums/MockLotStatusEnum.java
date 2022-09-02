package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/1.
 * <p>
 * 模拟投注状态枚举 @1:未开奖@2:未中奖@3:已中奖
 */
public enum MockLotStatusEnum {

    NOT_LOT(1, "未开奖"),
    LOSING_LOT(2, "未中奖"),
    WINNING_LOT(3, "已中奖");

    private Integer code;

    private String desc;

    MockLotStatusEnum(Integer code, String desc) {
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

    public static MockLotStatusEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        MockLotStatusEnum[] values = values();
        for (MockLotStatusEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
