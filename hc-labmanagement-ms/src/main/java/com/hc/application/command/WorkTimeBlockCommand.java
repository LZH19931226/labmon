package com.hc.application.command;

import com.alibaba.fastjson.annotation.JSONField;
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
    @JSONField(format = "HH:mm")
    private Date begintime;

    /**
     * 结束时间
     */
    @JSONField(format = "HH:mm")
    private Date endtime;
}
