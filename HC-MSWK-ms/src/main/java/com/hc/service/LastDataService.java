package com.hc.service;


import com.hc.clickhouse.po.Monitorequipmentlastdata;

import java.util.Date;

/**
 * Created by 16956 on 2018-09-04.
 */
public interface LastDataService {

    void saveLastData(Monitorequipmentlastdata monitorequipmentlastdata, String equipmentno, String hospitalcode,String cmdId,String sn);

    void saveHaverLastData(String messageContent);
}
