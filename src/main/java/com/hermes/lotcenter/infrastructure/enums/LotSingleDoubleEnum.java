package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/1.
 * <p>
 * 单双枚举 @1:单@2:双@:3豹
 */
public enum LotSingleDoubleEnum {

    ODD(1, "单"),
    EVEN(2, "双"),
    DUAL(3, "豹");

    private Integer code;

    private String desc;

    LotSingleDoubleEnum(Integer code, String desc) {
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

    public boolean equalsDesc(String desc) {
        return this.getDesc().equals(desc);
    }

    public static LotSingleDoubleEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        LotSingleDoubleEnum[] values = values();
        for (LotSingleDoubleEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
