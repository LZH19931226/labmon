package com.hc.command.labmanagement.model.hospital;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "医院请求信息体")
public class HospitalCommand {

    /** 医院编号 */
    @ApiModelProperty(value = "医院id")
    private String hospitalCode;

    /** 医院名称 */
    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    /** 是否可用 */
    @ApiModelProperty(value = "是否可用 0不可用 1可用")
    private String isEnable;

    /** 医院全称 */
    @ApiModelProperty(value = "医院全称")
    private String hospitalFullName;

    /** 全天报警 */
    @ApiModelProperty(value = "全天报警")
    private String alwaysAlarm;

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /** 超时设置 */
    @ApiModelProperty(value = "超时设置")
    private String timeout;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    /** 分页大小 */
    @ApiModelProperty(value = "分页大小")
    private Long pageSize;

    /** 当前页 */
    @ApiModelProperty(value = "当前页")
    private Long pageCurrent;

    /** 排序方式 */
    private String orderBy;

    /** 时间间隔 */
    private String timeInterval;

    /** 超时变红时长 */
    private String timeoutRedDuration;

    /** 是否开启声光报警是1否0 */
    private String soundLightAlarm;

    /** 是否设置因子登录1为设置空为未设置 */
    private String factor;

    /** 医院 */
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
