package com.hc.my.common.core.domain;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 15:10
 * 描述: 点对点通知.
 **/
import com.hc.my.common.core.constant.enums.NotifyChannel;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class P2PNotify implements Serializable {

    private static final long                serialVersionUID = 3772440536643224403L;
    /** 推送渠道 */
    private              List<NotifyChannel> channels;
    /** 用户ID */
    private              String              userId;
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
