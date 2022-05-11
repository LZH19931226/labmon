package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserCommand;
import com.hc.dto.UserBackDto;
import com.hc.my.common.core.constant.enums.UserEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.UserBackPo;
import com.hc.repository.UserBackRepository;
import com.hc.service.UserBackService;
import com.hc.vo.user.UserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author hc
 */
@Service
public class UserBackServiceImpl  implements UserBackService {

    @Autowired
    private UserBackRepository userBackRepository;

    @Override
    public UserBackDto userLogin(UserCommand userCommand) {
        String username = userCommand.getUsername();
        if(StringUtils.isBlank(username)){
            throw new IedsException(UserEnumErrorCode.USERNAME_CAN_NOT_NULL.getMessage());
        }
        String pwd = userCommand.getPwd();
        if(StringUtils.isBlank(pwd)){
            throw new IedsException(UserEnumErrorCode.PASSWORD_CAN_NOT_NULL.getMessage());
        }
        UserBackPo userBackPo =BeanConverter.convert(userCommand, UserBackPo.class);
        return userBackRepository.userLogin(userBackPo);
    }

    /**
     * 修改用户信息
     * @param userCommand
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserCommand userCommand) {
        String userid = userCommand.getUserid();
        if(StringUtils.isBlank(userid)){
            throw new IedsException(UserEnumErrorCode.USERID_NOT_NULL.getMessage());
        }
        String username = userCommand.getUsername();
        Integer integer = userBackRepository.selectOne(username);
        if(integer>1){
            throw new IedsException(UserEnumErrorCode.USERNAME_ALREADY_EXISTS.getMessage());
        }
        UserBackPo userBackPo = BeanConverter.convert(userCommand,UserBackPo.class);
        userBackRepository.updateUserInfo(userBackPo);
    }

    /**
     * 根据用户id查询user信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserBackDto selectUserBackByUserId(String userId) {
        return userBackRepository.selectUserBackByUserId(userId);
    }

    /**
     * 分页获取后台人员信息
     *
     * @param page
     * @param userCommand
     * @return
     */
    @Override
    public List<UserBackDto> findUserAllInfo(Page<UserInfoVo> page, UserCommand userCommand) {
        return userBackRepository.findUserAllInfo(page,userCommand);
    }

    /**
     * 删除用户信息
     *
     * @param userid
     */
    @Override
    public void deleteUserInfo(String[] userid) {
        userBackRepository.deleteUserInfo(userid);
    }

    /**
     * 新增用户信息
     *
     * @param userCommand
     */
    @Override
    public void insertUserInfo(UserCommand userCommand) {
        String username = userCommand.getUsername();
        UserBackDto userBackDto = userBackRepository.selectUserBackByUsername(username);
        if(!ObjectUtils.isEmpty(userBackDto)){
            throw new IedsException(UserEnumErrorCode.USERNAME_ALREADY_EXISTS.getMessage());
        }
        userBackRepository.insertUserInfo(userCommand);
    }
}
