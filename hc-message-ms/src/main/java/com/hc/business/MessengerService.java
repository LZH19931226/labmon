package com.hc.business;

import com.hc.model.NotifyMessage;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 13:58
 * 描述:
 **/


public interface MessengerService {

    /**
     * 发送通知.
     *
     * @param notify 通知发送参数
     * @return 唯一消息ID
     */
    String send(NotifyMessage notify);



}
