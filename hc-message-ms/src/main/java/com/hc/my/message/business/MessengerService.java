package com.hc.my.message.business;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 13:58
 * 描述:
 **/

import com.hc.my.message.model.NotifyMessage;

public interface MessengerService {

    /**
     * 发送通知.
     *
     * @param notify 通知发送参数
     * @return 唯一消息ID
     */
    String send(NotifyMessage notify);



}
