package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * Created by 16956 on 2018-08-06.
 */
@Getter
@Setter
@ToString
@ApiModel("展示所有医院设备类型信息")
@Component
public class HospitalEquipmentTypeInfoModel {
    private String hospitalcode;

    private String equipmenttypeid;

    private String hospitalname;

    private String equipmenttypename;

    private String isvisible;
}
