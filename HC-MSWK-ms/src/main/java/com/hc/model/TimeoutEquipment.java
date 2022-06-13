package com.hc.model;

import lombok.Data;

/**
 * Created by 15350 on 2019/10/8.
 */
@Data
public class TimeoutEquipment {

    private String hospitalcode;

    private String hospitalname;

    private String equipmentno;

    private String equipmentname;

    private Integer timeouttime;

    private String disabletype;

    private String equipmenttypeid;
}
