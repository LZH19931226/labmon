package com.hc.appliction.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hc.dto.UserSchedulingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 用户排班命令
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserScheduleCommand {
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;


    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 创建者
     */
    private String createUser;


    /**
     * 医院名称
     */
    private String hospitalCode;

    /**
     * 人员排班集合
     */
    private List<UserSchedulingDto> userSchedulingDtoList;

    /**
     * 旧开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date oldStartTime;

    /**
     * 旧结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date oldEndTime;

    /**
     * 新开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date newStartTime;

    /**
     * 新结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date newEndTime;
}
