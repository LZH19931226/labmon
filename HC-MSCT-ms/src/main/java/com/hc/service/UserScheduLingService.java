package com.hc.service;

import com.hc.po.UserScheduLing;

import java.util.List;

public interface UserScheduLingService {
    List<UserScheduLing> getHospitalScheduleInfo(String hospitalcode);
}
