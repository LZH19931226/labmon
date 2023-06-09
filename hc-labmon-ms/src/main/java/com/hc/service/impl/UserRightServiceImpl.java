package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.dto.UserRightDto;
import com.hc.repository.UserRightRepository;
import com.hc.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRightServiceImpl implements UserRightService {
    @Autowired
    private UserRightRepository userRightRepository;

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<UserRightDto> getImplementerInformation(String hospitalCode) {
        return userRightRepository.list(Wrappers.lambdaQuery(new UserRightDto()).eq(UserRightDto::getHospitalCode, hospitalCode)
                .eq(UserRightDto::getIsUse, "1").eq(UserRightDto::getRole,"1"));
    }

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<UserRightDto> getallByHospitalCode(String hospitalCode) {
        return userRightRepository.list(Wrappers.lambdaQuery(new UserRightDto()).eq(UserRightDto::getHospitalCode, hospitalCode));
    }

    @Override
    public List<UserRightDto> getUserRightInfo(AlarmNoticeCommand alarmNoticeCommand) {
        return userRightRepository.list(Wrappers.lambdaQuery(new UserRightDto())
                .eq(UserRightDto::getHospitalCode, alarmNoticeCommand.getHospitalCode())
                .eq(UserRightDto::getPhoneNum,alarmNoticeCommand.getPhoneNum()));
    }

    @Override
    public UserRightDto getUserRightInfoByUserId(String userId) {
        return userRightRepository.getOne(Wrappers.lambdaQuery(new UserRightDto()).eq(UserRightDto::getUserid,userId));
    }

    @Override
    public List<UserRightDto> getAll() {
        return userRightRepository.list();
    }
}
