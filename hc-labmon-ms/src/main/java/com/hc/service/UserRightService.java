package com.hc.service;

import com.hc.application.command.AlarmNoticeCommand;
import com.hc.dto.UserRightDto;

import java.util.List;

public interface UserRightService {

    List<UserRightDto> getImplementerInformation(String hospitalCode);

    List<UserRightDto> getallByHospitalCode(String hospitalCode);

    List<UserRightDto> getUserRightInfo(AlarmNoticeCommand alarmNoticeCommand);

    UserRightDto getUserRightInfoByUserId(String userId);
}
