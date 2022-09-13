package com.hermes.lotcenter.domain.task;

import com.hermes.lotcenter.domain.DoBetTaskDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by liuqingqian on 2022/9/12.
 */
@Slf4j
@Component
public class DoBetTaskTwo {

    @Autowired
    private DoBetTaskDomain doBetTaskDomain;

    @Scheduled(cron = "4 * * * * ?")
    public void doBet() {
        String taskNo = "T20220913H000001";
        doBetTaskDomain.doBet(taskNo);
    }
}
