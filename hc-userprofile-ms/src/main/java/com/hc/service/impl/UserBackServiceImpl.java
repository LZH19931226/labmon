package com.hc.service.impl;

import com.hc.dto.UserBackDto;
import com.hc.repository.UserBackRepository;
import com.hc.service.UserBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hc
 */
@Service
public class UserBackServiceImpl implements UserBackService {

    @Autowired
    private UserBackRepository userBackRepository;

    @Override
    public UserBackDto userLogin(UserBackDto userBackDto) {
        return userBackRepository.userLogin(userBackDto);
    }

    @Override
    public void updatePassword(UserBackDto userBackDto) {
        userBackRepository.updatePassword(userBackDto);
    }
}
