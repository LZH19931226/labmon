package com.hc.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-07-31.
 */
@Getter
@Setter
@ToString
@ApiModel("医院设备类型")
public class MonitorequipmenttypeModel {

    /**
     * 设备类型编码
     */

    private String equipmenttypeid;

    /**
     * 设备类型名称
     */
    private String equipmenttypename;
}
