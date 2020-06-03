package com.hc.controller;

import com.hc.scheduledfuturethread.MyRunnable1;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author LiuZhiHao
 * @date 2020/5/22 15:35
 * 描述:
 **/
@Slf4j
@RestController
@RequestMapping("/scheduledFuture")
public class ScheduledFutureController  {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final Map<String, ScheduledFuture<?>> notifiers = new ConcurrentHashMap<>(8);

    @GetMapping("/search")
    @ApiOperation("查看定时器运行状态")
    public Map<String, Boolean> searchCorn() {
        if (notifiers.isEmpty()) {
            return null;
        }
        Map<String, Boolean> map = new HashMap<>();
        notifiers.forEach((k, v) -> map.put(k, !v.isCancelled()));
        return map;
    }


    @GetMapping("/startCron")
    @ApiOperation("开始定时任务")
    public void startCron1(@ApiParam(value = "cron表达式", required = true, defaultValue = "0/5 * * * * ?") @RequestParam("cron") String cron) {
        ScheduledFuture<?> schedule = threadPoolTaskScheduler
                .schedule(new MyRunnable1(), triggerContext -> new CronTrigger(cron)
                        .nextExecutionTime(triggerContext));
        notifiers.put(MyRunnable1.class.getSimpleName(), schedule);

    }

    @GetMapping("/stopCron/{scheduledName}")
    @ApiOperation("关闭定时任务")
    public void stopCron1(@ApiParam(value = "定时器名称,可查看定时器运行状态获取", required = true) @PathVariable("scheduledName") String scheduledName) {
        ScheduledFuture<?> scheduledFuture = notifiers.get(scheduledName);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }


}
