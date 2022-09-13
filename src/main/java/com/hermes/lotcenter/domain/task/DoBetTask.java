package com.hermes.lotcenter.domain.task;

import com.hermes.lotcenter.domain.DoBetTaskDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Component
public class DoBetTask {

    @Autowired
    private DoBetTaskDomain doBetTaskDomain;


    @Scheduled(cron = "7 * * * * ?")
    public void doBet() {

        String taskNo = "T202209130000001";
        doBetTaskDomain.doBet(taskNo);
    }
}