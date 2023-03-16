package com.hc.vo.hospital;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hc.vo.equimenttype.HospitalequimentVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 医院信息视图对象
 * @author hc
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalInfoVo implements Serializable {

    /** 医院编号 */
    private String hospitalCode;

    /** 医院名称 */
    private String hospitalName;

    /** 是否可用 */
    private String isEnable;

    /** 医院简称 */
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

    /** 时间间隔 */
    private String timeInterval;

    /** 超时变红时长 */
    private String timeoutRedDuration;

    /**
     * 是否设置因子登录1为设置空为未设置
     */
    private String factor;

    /**
     * 是否开启声光报警是1否0
     */
    private String soundLightAlarm;

    private List<HospitalequimentVo> hospitalequimentVoList;

    private List<LabHosWarningTime> hosWarningTimes;

    public static class LabHosWarningTime{

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime beginTime;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime endTime;

        public LocalDateTime getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(LocalDateTime beginTime) {
            this.beginTime = beginTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }
    }
}
