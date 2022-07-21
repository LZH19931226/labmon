package com.hc.service;

/**
 * 消息发送服务
 * @author hc
 */
public interface MessageSendService {

    /**
     * 发送信息
     * @param message  信息
     * @return
     */
    boolean send(String message);
}
