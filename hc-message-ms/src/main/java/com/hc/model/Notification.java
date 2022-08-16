package com.hc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 13:55
 * 描述:
 **/
@Data
public class Notification implements Serializable {

    /** 批次号 */
    private String batchNo;

    /** 任务编号 */
    private String taskNo;

    /** 订阅者用户ID */
    private String consumerId;

    /** 发布者用户ID */
    private String supplierId;

    /** 消息服务编号 */
    private String serviceNo;

    /** 业务编号 */
    private String businessNo;

    /** 消息标题 */
    private String messageTitle;

    /** 消息封面 */
    private String messageCover;

    /** 消息简介 */
    private String messageIntro;

    /** 消息内容 */
    private String messageBodys;

    /** 发布关键字/userId/mobile/mail */
    private String publishKey;

    /** 发布类型/notify/sms/mail/mipush */
    private String publishType;

    /** 扩展参数 */
    private Map<String, String> param;
}
