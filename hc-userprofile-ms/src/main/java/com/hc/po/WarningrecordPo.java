package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("warningrecord")
public class WarningrecordPo {


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
    @TableField(exist = false)
    private Integer id;

    /**
     * 报警备注信息
     */
    @TableField(exist = false)
    private String info;

    private Integer count1;
}
