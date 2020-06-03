package com.hc.model.ResponseModel;

import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-05.
 */
@Getter
@Setter
@ToString
@ApiModel("设备展示数量信息模型")
@Component
public class EquipmentConfigInfoModel {

    private String hospitalcode;

    private String equipmenttypeid;

    private String equipmenttypename;

    private String shows;

    private String hide;

}
