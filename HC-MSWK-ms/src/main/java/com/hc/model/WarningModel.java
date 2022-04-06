package com.hc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-09.
 */
@Getter
@Setter
@ToString
public class WarningModel {
    private String pkid;
    private String hospitalcode;
    private String unit;
    private String value;
    private String equipmentname;

}
