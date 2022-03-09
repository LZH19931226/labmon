package com.hc.model.RequestModel;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@ApiModel("工作时间段")
public class WorkTimeBlockModel {
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
