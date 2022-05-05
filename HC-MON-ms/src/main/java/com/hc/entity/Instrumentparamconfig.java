package com.hc.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "instrumentparamconfig")
@Entity
@Getter
@Setter
@ToString
public class Instrumentparamconfig implements Serializable {
    /**
     * 监控参数编号
     */
    @Id
    @Column(name = "instrumentparamconfigNO")
    private String instrumentparamconfigno;

    /**
     * 探头编号
     */
    private String instrumentno;

    /**
     * 监控参数类型编码
     */
    private Integer instrumentconfigid;

    /**
     * 探头名称
     */
    private String instrumentname;

    /**
     * 最低限值
     */
    private BigDecimal lowlimit;

    /**
     * 最高限值
     */
    private BigDecimal highlimit;

    /**
     * 探头类型编码
     */
    private Integer instrumenttypeid;

    /**
     * 是否启用电话/短信/App推送报警
     */
    private String warningphone;

    /**
     * 推送消息时间
     */
    private String pushtime;

    /**
     * 报警时间
     */
    private String warningtime;

    private String channel;

    private Integer alarmtime;

    private String calibration;

    /**
     * 饱和值
     */
    private BigDecimal saturation;

    @Transient
    private String userName;

    private static final long serialVersionUID = 1L;


}