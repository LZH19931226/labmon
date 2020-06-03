package com.hc.model.ResponseModel;

import com.hc.entity.Monitorequipmentlastdata;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-10-11.
 */
@Getter
@Setter
@ToString
public class AlarmData {

    private Monitorequipmentlastdata monitorequipmentlastdata;

    private String equipmentname;

    private String type;

    private String hospitalname;
}
