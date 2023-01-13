package com.hc.application.command;

import lombok.Data;

@Data
public class AlarmNoticeCommand {
    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 医院id
     */
    private String hospitalCode;

    /**
     * 通知状态(0为成功，1为失败)
     */
    private String noticeState;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 当前分页
     */
    private Integer pageCurrent;


    private String usrId;
}
