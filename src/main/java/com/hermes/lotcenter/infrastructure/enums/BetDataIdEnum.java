package com.hermes.lotcenter.infrastructure.enums;

import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/8.
 * <p>
 * 彩种投注骰数据类型枚举：
 * id取值：1=1、2=2、3=3、4=5、5=6、6=7、大=4、小=8、单=9、双=10（name=id）
 */
public enum BetDataIdEnum {
    ONE(1, "1"),
    TWO(2, "2"),
    THREE(3, "3"),
    FOUR(5, "4"),
    FIVE(6, "5"),
    SIX(7, "6"),
    BIG(4, "大"),
    SMALL(8, "小"),
    ODD(9, "单"),
    EVEN(10, "双");

    private Integer id;

    private String name;

    BetDataIdEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Integer id) {
        return this.getId().equals(id);
    }

    public boolean equalsName(String name) {
        return this.getName().equals(name);
    }

    public static BetDataIdEnum fromId(Integer id) {
        if (Objects.isNull(id)) {
            return null;
        }
        BetDataIdEnum[] values = values();
        for (BetDataIdEnum item : values) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static BetDataIdEnum fromName(String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        BetDataIdEnum[] values = values();
        for (BetDataIdEnum item : values) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
