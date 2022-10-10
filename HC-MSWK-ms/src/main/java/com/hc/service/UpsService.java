package com.hc.service;

import com.hc.my.common.core.redis.dto.ParamaterModel;
import org.springframework.stereotype.Service;

public interface UpsService {

    void sendInfo(ParamaterModel model, String equipmentno, String hospitalcode);
}
