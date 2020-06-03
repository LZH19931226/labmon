package com.hc.model.ResponseModel;

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
@ApiModel("探头类型信息展示")
public class InstrumentParamConfigInfo {

    private String equipmentname;

    private String instrumentconfigname;

    private String instrumentparamconfigNO;

    private String warningphone;

    private String sn;

}
