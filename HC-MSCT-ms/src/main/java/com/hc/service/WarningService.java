package com.hc.service;

import com.hc.bean.WarningModel;
import com.hc.bean.WarningMqModel;
import com.hc.entity.Monitorinstrument;

import java.util.Date;

/**
 * Created by 16956 on 2018-08-09.
 */
public interface WarningService {

    WarningModel produceWarn(WarningMqModel warningMqModel, Monitorinstrument monitorinstrument, Date date, Integer instrumentconfigid, String unit);
}
