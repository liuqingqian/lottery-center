package com.hermes.lotcenter.domain;

import com.beicai.common.DateTimeUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Component
public class MockTask {

    @Scheduled(cron = "4 * * * * ?")
    public void task() {
        System.out.println("触发时间 = " + DateTimeUtil.nowStr());
    }
}
