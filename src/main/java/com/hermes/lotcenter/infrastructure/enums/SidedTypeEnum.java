package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/9.
 * <p>
 * 快三双面类型枚举：@1:大小@2:单双@3:豹
 */
public enum SidedTypeEnum {

    SIZE(1, "大小"),
    SINGLE_DOUBLE(2, "单双"),
    DUAL(3, "豹");

    private Integer code;

    private String name;

    SidedTypeEnum(Integer code, String name) {
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

    public static SidedTypeEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        SidedTypeEnum[] values = values();
        for (SidedTypeEnum item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
