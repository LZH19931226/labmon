package com.hc.model;

import com.hc.my.common.core.constant.enums.NotifyChannel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 13:55
 * 描述:
 **/
@Data
public class NotifyMessage implements Serializable {

    private static final long                serialVersionUID = 2269772248868796784L;
    /** 推送渠道 */
    private List<NotifyChannel> channels;
    /** 用户ID+手机号 */
    private Map<String, Object> principals;
    /** 消息业务号 */
    private              String              serviceNo;
    /** 消息标题 */
    private              String              messageTitle;
    /** 消息封面 */
    private              String              messageCover;
    /** 消息简介 */
    private              String              messageIntro;
    /** 消息内容 */
    private              String              messageBodys;
    /** 参数 */
    private              Map<String, String> params;
}
