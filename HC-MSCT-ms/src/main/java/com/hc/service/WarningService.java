package com.hc.service;

import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.model.WarningModel;

import java.util.List;


/**
 * Created by 16956 on 2018-08-09.
 */
public interface WarningService {

    WarningModel produceWarn( WarningAlarmDo warningAlarmDo);

}
