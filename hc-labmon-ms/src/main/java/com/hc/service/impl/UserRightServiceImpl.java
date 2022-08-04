package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
}
