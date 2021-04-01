package com.hc.my.common.core.util;

import com.hc.my.common.core.constant.enums.NotifyChannel;
import com.hc.my.common.core.domain.P2PNotify;

import java.util.Collections;

/**
 * @author LiuZhiHao
 * @date 2021/3/31 17:33
 * 描述:
 **/
public class RedisSyncBeanConversion {

    /**
     * <p>
     * 改变 : 将相要转换的对象变成异步推送对象
     * </p>
     * @return redis同步对象
     */
    public  static  P2PNotify  redisSyncBean(String key,String hashKey,String body,String type){
        P2PNotify notification = new P2PNotify();
        notification.setMessageTitle(key);
        notification.setMessageIntro(hashKey);
        notification.setMessageBodys(body);
        notification.setMessageCover(type);
        notification.setChannels(Collections.singletonList(NotifyChannel.PUSH));
        notification.setUserId("admin");
        notification.setServiceNo("1");
        return notification;
    }

}
