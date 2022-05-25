package com.hc.my.common.core.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class HospitalEquipmentTypeInfoDto implements Serializable {

    private String hospitalcode;

    private String equipmenttypeid;

    private String hospitalname;

    private String equipmenttypename;

    private String isvisible;

    /**
     * 全天报警
     *  1=全天报警
     *  0=不全天报警
     */
    private String alwayalarm ;

    /**
     * 报警时间段
     */
    List<MonitorEquipmentWarningTimeDto> warningTimeList;
}
