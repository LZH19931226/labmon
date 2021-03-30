package com.hc.my.message.notify.redis;

import com.hc.my.message.model.Notification;
import com.hc.my.message.notify.Notifier;
import org.springframework.stereotype.Service;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:16
 * 描述: r
 **/
@Service("PUSH")
public class RedisSynchronous implements Notifier {


    @Override
    public String exec(Notification notification){
        //return  WebSocketServer.sendInfo(notification.getMessageBodys(),notification.getPublishKey());
        return null;
    }
}
