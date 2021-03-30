package com.hc.my.common.core.constant.enums;

import lombok.Getter;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 16:42
 * 描述: 用于消息推送
 **/
@Getter
public enum  PushType {
    /** WEBSOCKET */
    WEBSOCKET("WEBSOCKET"),
    /** RABBITMQ */
    RABBITMQ("RABBITMQ"),
    /** KAFKA */
    KAFKA("KAFKA");
    private String code;

    PushType(String code){
        this.code =code;
    };


}
