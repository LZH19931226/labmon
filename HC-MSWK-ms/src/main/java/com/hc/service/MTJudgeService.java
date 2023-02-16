package com.hc.service;

import com.hc.po.Monitorinstrument;
import com.hc.my.common.core.redis.dto.ParamaterModel;

import java.util.List;

/**
 * Created by 16956 on 2018-08-21.
 */
public interface MTJudgeService {

    List<Monitorinstrument> checkProbe(ParamaterModel model);
}
