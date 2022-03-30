package com.hc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value = "instrumentmonitor")
@Data
public class Instrumentmonitor implements Serializable {
    /**
     * 监控参数类型编码
     */
    private Integer instrumentconfigid;

    /**
     * 探头类型编码
     */
    private Integer instrumenttypeid;


    /**
     * 最低限值
     */
    private BigDecimal lowlimit;

    /**
     * 最高限值
     */
    private BigDecimal highlimit;


    /**
     * 饱和值
     */
    private BigDecimal saturation;


    private static final long serialVersionUID = 1L;



}