package com.hermes.lotcenter;

import org.apache.commons.lang3.RandomUtils;

/**
 * Created by liuqingqian on 2022/9/11.
 */
public class TestRandomUtils {
    public static void main(String[] args) {
        int startInclusive = 5;
        int endExclusive = 25;


        for (int i = 0; i <100 ; i++) {
            int nextInt = RandomUtils.nextInt(startInclusive, endExclusive);
            System.out.println("nextInt = " + nextInt);
        }
    }
}
