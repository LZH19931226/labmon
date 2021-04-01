package com.hc.my.message.notify.redis;

import com.hc.my.message.config.RedisTemplateUtil;
import com.hc.my.message.model.Notification;
import com.hc.my.message.notify.Notifier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:16
 * 描述: r
 **/
@Service("PUSH")
public class RedisSynchronous implements Notifier {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public String exec(Notification notification){
        //规定redis缓存的对象json
        String messageBodys = notification.getMessageBodys();
        //规定redis缓存的key
        String messageTitle = notification.getMessageTitle();
        //如果是hash类型的redis 此处存储hashkey
        String messageIntro = notification.getMessageIntro();
        //1:put , 2:delete
        String messageCover = notification.getMessageCover();
        if (StringUtils.equals(messageCover,"1")){
            if (StringUtils.isNotEmpty(messageIntro)){
                HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
                objectObjectObjectHashOperations.put(messageTitle,messageIntro,messageBodys);
            }else {
                ValueOperations<Object, Object> objectObjectValueOperations = redisTemplateUtil.opsForValue();
                objectObjectValueOperations.set(messageTitle,messageBodys);
            }
        }
        if (StringUtils.equals(messageCover,"2")){
               redisTemplateUtil.delete(messageTitle);
        }
        return null;
    }
}
