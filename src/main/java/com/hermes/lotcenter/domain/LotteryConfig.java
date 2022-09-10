package com.hermes.lotcenter.domain;

import org.springframework.stereotype.Component;

/**
 * Created by liuqingqian on 2022/8/29.
 */
@Component
public class LotteryConfig {


    public static final String HOST = "ft5812.com";

    public static final String ORIGIN = "http://" + HOST;

    public static final String SESSION = "5bb0c377-cea8-40e1-83db-964e09ddb57d";

    /**
     * 开奖结果URL
     */
    public static final String RESULT_85_URL = ORIGIN + "/lotteryV2/resultList.do";

    /**
     * 彩种文件主路径
     */
    public static final String BASE_PATH = "/Users/liuqingqian/work/hermes/lottery";


    public static class Referer {
        public static final String LOTTERY_INDEX = ORIGIN + "/lotteryV2/index.do";
    }


}
