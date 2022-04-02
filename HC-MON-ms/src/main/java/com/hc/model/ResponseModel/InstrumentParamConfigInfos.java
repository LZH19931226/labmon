package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by 16956 on 2018-08-05.
 */
@Getter
@Setter
@ToString
@ApiModel("探头类型信息展示")
public class InstrumentParamConfigInfos {

    private String equipmentno;

    private String equipmentname;

    private String clientvisible;

    private List<InstrumentParamConfigInfo> instrumentParamConfigInfoList;


}
