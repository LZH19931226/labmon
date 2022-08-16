package com.hc.notify;

import com.alibaba.fastjson.JSON;
import com.hc.my.common.core.spring.AbstractComponentCollector;
import com.hc.my.common.core.util.UniqueHash;
import com.hc.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:06
 * 描述:
 **/
@Slf4j
@Service("notifier")
public class CompositorNotifier extends AbstractComponentCollector<Notifier> implements Notifier {

    private final Map<String, List<Notifier>> notifiers = new ConcurrentHashMap<>();

    @Override
    public String exec(Notification notification) throws Throwable {
        List<Notifier> ns = notifiers.get(notification.getPublishType());
        if (CollectionUtils.isEmpty(ns)) {
            log.warn("No notify found for {}-{}", notifiers.size(), JSON.toJSONString(notification));
            return UniqueHash.Id();
        }
        Throwable cause = null;
        for (Notifier notifier : ns) {
            try {
                return notifier.exec(notification);
            } catch (Throwable e) {
                log.error("", e);
                cause = e;
            }
        }
        assert cause != null;
        throw cause;
    }

    @Override
    protected void collect(String name, Notifier component) {
        notifiers.computeIfAbsent(name, key -> new ArrayList<>()).add(component);
    }
}
