package com.hc.application.response;

import lombok.Data;

@Data
public class AlarmNoticeResult {

    /**
     * 数据记录时间
     */
    private String dataLoggingTime;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 通知的手机号
     */
    private String phoneNum;

    /**
     * 通知类型
     */
    private String publishType;

    /**
     * 通知状态
     */
    private String state;

    /**
     * 失败原因
     */
    private String fReason;

    /**
     * 邮件内容
     */
    private String mailContent;
}
