package com.hc.service;

import com.hc.po.Userright;

import java.util.List;

public interface SendTimeoutRecordService {

    void saveTimeOutRecord(List<Userright> userrightByHospitalcodeAAndTimeout, String hospitalcode, String eqTypeName, String count);
}
