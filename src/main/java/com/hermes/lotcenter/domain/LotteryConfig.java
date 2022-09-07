package com.hermes.lotcenter.domain;

import org.springframework.stereotype.Component;

/**
 * Created by liuqingqian on 2022/8/29.
 */
@Component
public class LotteryConfig {


    public static final String HOST = "ft5812.com";

    public static final String ORIGIN = "http://" + HOST;

    /**
     * 开奖结果URL
     */
    public static final String RESULT_85_URL = "http://8585055.cc/lotteryV2/resultList.do";

    /**
     * 彩种文件主路径
     */
    public static final String BASE_PATH = "/Users/liuqingqian/work/hermes/lottery";


    public static class Referer {
        public static final String LOTTERY_OPT = ORIGIN + "/lotteryV2/index.do";
    }


}
