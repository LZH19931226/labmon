package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by xxf on 2018/9/12.
 */
@Getter
@Setter
@ToString
@ApiModel("报警信息推送模型")
public class MessageSendModel {

    private String userid;

    private String device_type;

    private String DeviceToken;

    private String equipmentTypeName;

    private String warningremark;

    private Date inputdatetime;

    private String equipmentTypeNo;

    private String equipmentNo;

    private String equipmentName;

    private String instrumentNo;
}
