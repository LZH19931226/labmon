package com.hc.notify;

import com.hc.my.common.core.annotation.Monitor;
import com.hc.model.Notification;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:06
 * 描述:
 **/
@Monitor
public interface Notifier {

    String exec(Notification notification) throws Throwable;
}

