package com.hc.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xxf on 2018/9/17.
 */
@Getter
@Setter
@ToString
@ApiModel("修改devicetoken模型")
public class UpdateDeviceTokenModel {
    @NonNull
    private String userid;
    @NonNull
    private String devicetoken;
}
