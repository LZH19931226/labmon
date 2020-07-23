package com.hc.my.common.core.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LiuZhiHao
 * @date 2020/7/20 17:18
 * 描述:
 **/
@Data
public class InstrumentMonitorInfoModel {
    //"探头类型编码
    private Integer instrumenttypeid;
    //监控参数类型编码
    private Integer instrumentconfigid;
  //"探头类型名称
    private String instrumenttypename;
    //监控参数类型名称
    private String instrumentconfigname;
    private BigDecimal lowlimit;
    private BigDecimal highlimit;
    //智能报警次数 连续几次才推送报警，条件之一
    private Integer alarmtime;
    //探头监控类型编号
    private String instrumentparamconfigNO;
    //设备名称
    private String equipmentname;
    //APP推送时间
    private Date pushtime;
    //是否启用短信、电话、APP报警推送
    private String warningphone;
    //探头编号
    private String instrumentno;
    //报警推送时间
    private Date warningtime;
    private String calibration;
    //饱和值
    private BigDecimal saturation;
    private String equipmentno;
}
