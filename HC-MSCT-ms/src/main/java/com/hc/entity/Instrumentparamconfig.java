package com.hc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "instrumentparamconfig")
@Data
public class Instrumentparamconfig implements Serializable {
    /**
     * 监控参数编号
     */
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
    private Date pushtime;

    /**
     * 报警时间
     */
    private Date warningtime;

    private static final long serialVersionUID = 1L;


}