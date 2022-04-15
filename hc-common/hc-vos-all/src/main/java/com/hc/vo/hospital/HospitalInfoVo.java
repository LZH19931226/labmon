package com.hc.vo.hospital;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 医院信息视图对象
 * @author hc
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalInfoVo {

    /** 医院编号 */
    @JSONField(name = "hospitalCode")
    private String hospitalCode;

    /** 医院名称 */
    @JSONField(name = "hospitalName")
    private String hospitalName;

    /** 是否可用 */
    @JSONField(name = "isEnable")
    private String isEnable;

    /** 医院全称 */
    private String hospitalFullName;

    /** 全天报警 */
    private String alwaysAlarm;

    /** 开始时间 */
    private Date beginTime;

    /** 结束时间 */
    private Date endTime;

    /** 超时设置 */
    private String timeout;

    /** 更新时间 */
    private Date updateTime;

}
