package com.hc.clickhouse.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "warningrecord")
@Data
public class Warningrecord implements Serializable {
    /**
     *
     */

    private long id;

    /**
     * uuid
     */
    private String pkid;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 监控参数类型编码
     *
     */
    private String instrumentparamconfigno;

    /**
     * 报警值
     */
    private String warningvalue;

    /**
     * 报警时间
     */
    private Date inputdatetime;

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

    private String isphone;

    private String equipmentno;

}
