package com.hc.my.common.core.constant.enums;

import lombok.Getter;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 16:42
 * 描述: 用于内部消息通知
 **/
@Getter
public enum  PushType {
    /** 普通报警 */
    USUAL_ALARM,
    /** 超时报警  */
    TIMEOUT_ALARM;

    PushType() {

    }


}
