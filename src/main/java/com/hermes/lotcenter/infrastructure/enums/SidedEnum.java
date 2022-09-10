package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/9.
 * <p>
 * 快三双面枚举：@1:大@2:小@3:单@4:双@4=5:豹
 */
public enum SidedEnum {
    BIG(1, "大"),
    SMALL(2, "小"),
    ODD(3, "单"),
    EVEN(4, "双"),
    DUAL(5, "豹");

    private Integer code;

    private String name;

    SidedEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Integer code) {
        return this.getCode().equals(code);
    }

    public static SidedEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        SidedEnum[] values = values();
        for (SidedEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static SidedEnum fromName(String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        SidedEnum[] values = values();
        for (SidedEnum item : values) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
