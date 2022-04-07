package com.hc.service;

import com.hc.entity.Monitorinstrument;
import com.hc.my.common.core.bean.ParamaterModel;

/**
 * Created by 16956 on 2018-08-21.
 */
public interface MTJudgeService {


    Monitorinstrument mtJudge(ParamaterModel paramaterModel);

    Monitorinstrument checkProbe(ParamaterModel model);
}
