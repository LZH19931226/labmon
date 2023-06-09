package com.hc.vo.waring;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class WarningrecordVo implements Serializable {
    /**
     * pkid
     */
    private String pkid;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 监控参数类型编码
     */
    private String instrumentparamconfigNO;

    /**
     * 报警值
     */
    private String warningvalue;

    /**
     * 报警时间
     */
    private String inputdatetime;

    /**
     * 报警信息
     */
    private String warningremark;

    /**
     * 推送状态
     */
    private String pushstate;

    /**
     * 消息是否已读
     */
    private String msgflag;



    /**
     * 报警备注信息主键
     */
    private Integer id;

    /**
     * 报警备注信息
     */
    private String info;

    private static final long serialVersionUID = 1L;


    }
