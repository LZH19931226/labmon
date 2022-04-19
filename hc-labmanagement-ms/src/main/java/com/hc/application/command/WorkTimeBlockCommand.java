package com.hc.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class WorkTimeBlockCommand {

    private Integer timeblockid;
    /**
     * 开始时间
     */
    private Date begintime;

    /**
     * 结束时间
     */
    private Date endtime;
}
