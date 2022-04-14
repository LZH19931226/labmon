package com.hc.service.impl;

import com.hc.appliction.command.UserCommand;
import com.hc.dto.UserBackDto;
import com.hc.my.common.core.constant.enums.UserEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.StringUtils;
import com.hc.po.UserBackPo;
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
    public UserBackDto userLogin(UserCommand userCommand) {
        String username = userCommand.getUsername();
        if(StringUtils.isEmpty(username)){
            throw new IedsException(UserEnumErrorCode.USERNAME_CAN_NOT_NULL.getMessage());
        }
        String pwd = userCommand.getPwd();
        if(StringUtils.isEmpty(pwd)){
            throw new IedsException(UserEnumErrorCode.PASSWORD_CAN_NOT_NULL.getMessage());
        }
        UserBackPo userBackPo =BeanConverter.convert(userCommand, UserBackPo.class);
        return userBackRepository.userLogin(userBackPo);
    }

    @Override
    public void updatePassword(UserCommand userCommand) {
        String userid = userCommand.getUserid();
        if(StringUtils.isEmpty(userid)){
            throw new IedsException(UserEnumErrorCode.USERID_NOT_NULL.getMessage());
        }
        UserBackPo userBackPo = BeanConverter.convert(userCommand,UserBackPo.class);
        userBackRepository.updatePassword(userBackPo);
    }
}
