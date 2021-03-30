package com.hc.my.message.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.my.common.core.annotation.Monitor;
import com.hc.my.common.core.constant.enums.NotifyChannel;
import com.hc.my.common.core.current.ThreadPool;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.struct.Maps;
import com.hc.my.message.entity.MessengerPublishTask;
import com.hc.my.message.entity.MessengerServiceDefine;
import com.hc.my.message.mapper.MessengerPublishTaskMapper;
import com.hc.my.message.mapper.MessengerServiceDefineMapper;
import com.hc.my.message.model.Notification;
import com.hc.my.message.notify.Notifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

;
/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:02
 * 描述:
 **/
@Slf4j
@Service
@Monitor
public class NotifyScheduledExecutor implements NotifyExecutor,
        InitializingBean {
    private final ExecutorService executors = ThreadPool.create(100);
    private final ScheduledExecutorService scheduled = ThreadPool.scheduled(1);
    @Resource
    private MessengerPublishTaskMapper publishTaskDao;
    @Resource
    private MessengerServiceDefineMapper serviceDefineDao;
    @Resource
    private Notifier notifier;


    @Override
    public Future<String> submit(String batchNo) {
        LambdaQueryWrapper<MessengerPublishTask> wrapper = Wrappers
                .lambdaQuery(new MessengerPublishTask())
                .eq(MessengerPublishTask::getBatchNo, batchNo)
                .and(r -> r.le(MessengerPublishTask::getFailureTimes, 3));
        List<MessengerPublishTask> ts = publishTaskDao.selectList(wrapper);
        String serviceNo = ts.stream().findFirst().map(MessengerPublishTask::getServiceNo).orElse("");
        MessengerServiceDefine define = getServiceDefine(serviceNo);
        return executors.submit(() -> {
            try {
                ts.parallelStream().forEach(task -> notify(define, task));
                return batchNo;
            } catch (Throwable e) {
                log.error("Notify {} failed", e, batchNo);
                throw new IedsException("发送消息失败！", e);
            }
        });
    }

    private MessengerServiceDefine getServiceDefine(String serviceNo) {
        List<MessengerServiceDefine>     defines = serviceDefineDao.selectByMap(new Maps("service_no", serviceNo));
        Optional<MessengerServiceDefine> define  = Optional.ofNullable(defines).flatMap(fs -> fs.stream().findFirst());
        return define.orElseThrow(() -> new IedsException("服务号{}不存在!", serviceNo));
    }

    private void notify(MessengerServiceDefine define, MessengerPublishTask task) {
        Notification notification = new Notification();
        if (NotifyChannel.SMS.toString().equals(task.getPublishType())) {
            JSONObject body = JSON.parseObject(task.getMessageBodys());
            notification.setParam(body.getObject("args", new TypeReference<HashMap<String, String>>() {}));
            notification.setMessageBodys(body.getString("tpl"));
            notification.setPublishKey("86" + task.getPublishKey());
            //用户id作为推送核心
        } else if (StringUtils.equalsAny(task.getPublishType(),NotifyChannel.PUSH.getCode(),NotifyChannel.TEST.getCode())) {
            notification.setParam(Collections.emptyMap());
            notification.setMessageBodys(task.getMessageBodys());
            notification.setPublishKey(task.getPublishKey());
        }
        notification.setBatchNo(task.getBatchNo());
        notification.setBusinessNo(define.getBusinessNo());
        notification.setConsumerId(task.getConsumerId());
        notification.setMessageBodys(task.getMessageBodys());
        notification.setMessageCover(task.getMessageCover());
        notification.setMessageIntro(task.getMessageIntro());
        notification.setMessageTitle(task.getMessageTitle());
        notification.setPublishType(task.getPublishType());
        notification.setServiceNo(task.getServiceNo());
        notification.setSupplierId(task.getSupplierId());
        notification.setTaskNo(task.getTaskNo());

        MessengerPublishTask ts = new MessengerPublishTask();
        ts.setId(task.getId());
        ts.setUpdateTime(LocalDateTime.now());
        try {
            String bizNo = notifier.exec(notification);
            ts.setRemark(bizNo);
            ts.setStatus(2L);
            publishTaskDao.updateById(ts);
        } catch (Throwable e) {
            ts.setRemark(e.getMessage());
            ts.setStatus(4L);
            ts.setFailureTimes(task.getFailureTimes() + 1);
            publishTaskDao.updateById(ts);
            throw new IedsException("发送消息失败！", e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        scheduled.scheduleAtFixedRate(() -> {
            try {
                LambdaQueryWrapper<MessengerPublishTask> wrapper = Wrappers
                        .lambdaQuery(new MessengerPublishTask())
                        .eq(MessengerPublishTask::getStatus, 4)
                        .and(r -> r.le(MessengerPublishTask::getFailureTimes, 3));
                List<MessengerPublishTask> tasks = publishTaskDao.selectList(wrapper);
                if (CollectionUtils.isEmpty(tasks)) {
                    return;
                }
                String serviceNo = tasks.stream().findFirst().map(MessengerPublishTask::getServiceNo).orElse("");
                MessengerServiceDefine define = getServiceDefine(serviceNo);
                tasks.parallelStream().forEach(task -> notify(define, task));
            } catch (Throwable e) {
                log.error("Notify retry failed", e);
            }
        }, 120, 60, TimeUnit.SECONDS);
    }
}
