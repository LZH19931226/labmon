package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "hospitalofreginfo")
@Data
public class Hospitalofreginfo implements Serializable {
    /**
     * 医院编号
     */
    @TableId
    private String hospitalcode;

    /**
     * 医院名称
     */
    private String hospitalname;

    /**
     * 是否可用
     */
    private String isenable;

    /**
     * 医院全称
     */
    private String hospitalfullname;

    /**
     * 全天报警
     */
    private String alwayalarm;

    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date begintime;

    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endtime;

    private static final long serialVersionUID = 1L;

}