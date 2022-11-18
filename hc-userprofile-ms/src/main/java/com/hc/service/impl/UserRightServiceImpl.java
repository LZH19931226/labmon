package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.repository.UserRightRepository;
import com.hc.service.UserRightService;
import com.hc.vo.user.UserRightVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 用户权限服务实现类
 * @author hc
 */
@Service
public class UserRightServiceImpl implements UserRightService {

    @Autowired
    private UserRightRepository userRightRepository;



    /**
     * 分页查询用户信息
     * @param page  分页对象
     * @param userRightCommand 用户权限命令
     * @return 用户信息集合
     */
    @Override
    public List<UserRightDto> findUserRightList(Page<UserRightVo> page, UserRightCommand userRightCommand) {
        return    userRightRepository.findUserRightList(page,userRightCommand);
    }

    /**
     * 添加用户信息
     * @param userRightCommand 用户权限命令
     */
    @Override
    public void insertUserRightInfo(UserRightCommand userRightCommand) {
        String username = userRightCommand.getUsername();
        String nickname = userRightCommand.getNickname();
        String pwd = userRightCommand.getPwd();
        String timeout = userRightCommand.getTimeout();
        String hospitalCode = userRightCommand.getHospitalCode();
        String userType = userRightCommand.getUserType();
        if (StringUtils.isBlank(username)) {
            throw new IedsException(LabSystemEnum.USERNAME_NOT_NULL);
        }
        if (StringUtils.isBlank(nickname)) {
            throw new IedsException(LabSystemEnum.NICKNAME_NOT_NULL);
        }
        if (StringUtils.isBlank(pwd)) {
            throw new IedsException(LabSystemEnum.PASSWORD_CAN_NOT_NULL);
        }
        if (StringUtils.isBlank(timeout)) {
            throw new IedsException(LabSystemEnum.SUPERMARKET_CONTACT_CANNOT_BE_EMPTY);
        }
        if (StringUtils.isBlank(hospitalCode)) {
            throw new IedsException(LabSystemEnum.HOSPITAL_CODE_NOT_NULL);
        }
        if (StringUtils.isBlank(userType)) {
            throw new IedsException(LabSystemEnum.USER_ROLE_NOT_NULL);
        }

        userRightRepository.insertUserRightInfo(userRightCommand);
    }

    /**
     * 修改用户信息
     *
     * @param userRightCommand 用户参数对象
     */
    @Override
    public void updateUserRightInfo(UserRightCommand userRightCommand) {
        if(StringUtils.isBlank(userRightCommand.getUserid())){
            throw new IedsException(LabSystemEnum.USERID_NOT_NULL);
        }
        if (StringUtils.isBlank(userRightCommand.getNickname())) {
            throw new IedsException(LabSystemEnum.NICKNAME_NOT_NULL);
        }
        if (StringUtils.isBlank(userRightCommand.getPwd())) {
            throw new IedsException(LabSystemEnum.PASSWORD_CAN_NOT_NULL);
        }
        if (StringUtils.isBlank(userRightCommand.getHospitalCode())) {
            throw new IedsException(LabSystemEnum.HOSPITAL_NAME_NOT_NULL);
        }
        if (StringUtils.isBlank(userRightCommand.getUserType())) {
            throw new IedsException(LabSystemEnum.USER_ROLE_NOT_NULL);
        }
        if (StringUtils.isBlank(userRightCommand.getTimeout())) {
            throw new IedsException(LabSystemEnum.SUPERMARKET_CONTACT_CANNOT_BE_EMPTY);
        }
        UserRightDto userRightDto = userRightRepository.selectUserRightInfo(userRightCommand.getUserid());
        if (ObjectUtils.isEmpty(userRightDto)) {
            throw new IedsException(LabSystemEnum.THIS_INFORMATION_NO_LONGER_EXISTS);
        }
        //判断phonenum有没有修改，如修改了在用修改的手机号和医院查记录数量大于0是手机号重复抛出异常
        if(!userRightDto.getPhoneNum().equals(userRightCommand.getPhoneNum())){
            int i = userRightRepository.selectUserRightByCodeAndPhone(userRightDto.getHospitalCode(), userRightCommand.getPhoneNum());
            if (i>0) {
                throw new IedsException(LabSystemEnum.HOSPITALS_CANNOT_HAVE_THE_SAME_MOBILE_NUMBER);
            }
        }
        userRightRepository.updateUserRightInfo(userRightCommand);
    }

    /**
     * 删除用户信息
     *
     * @param userRightCommand 用户参数对象
     */
    @Override
    public void deleteUserRightInfo(UserRightCommand userRightCommand) {
        userRightRepository.deleteUserRightInfo(userRightCommand.getUserid());
    }

    /**
     * 查询用户信息
     *
     * @param userid 用户id
     * @return 用户信息
     */
    @Override
    public UserRightDto selectUserRightInfo(String userid) {
        return userRightRepository.selectUserRightInfo(userid);
    }

    /**
     * 查询用户信息
     *
     * @param userRightCommand 用户参数对象
     * @return 用户信息
     */
    @Override
    public UserRightDto selectUserRight(UserRightCommand userRightCommand) {

        return userRightRepository.selectUserRight(userRightCommand);
    }

    /**
     * 查询当前医院的所有人员
     *
     * @param hospitalCode 医院id
     * @return 当前医院的所有人员集合
     */
    @Override
    public List<UserRightDto> findALLUserRightInfoByHospitalCode(String hospitalCode) {
        return userRightRepository.findALLUserRightInfoByHospitalCode(hospitalCode);
    }

    /**
     * 验证用户名是否存在
     * @param userName
     * @return
     */
    @Override
    public Boolean checkUsername(String userName) {
        return userRightRepository.checkUsername(userName);
    }

    /**
     * app修改用户信息
     *
     * @param userRightCommand
     */
    @Override
    public void appUpdateUser(UserRightCommand userRightCommand) {
        userRightRepository.updateUserRightInfo(userRightCommand);
    }

    /**
     * 验证手机号是否存在
     *
     * @param userRightCommand
     * @return
     */
    @Override
    public Boolean checkPhoneNum(UserRightCommand userRightCommand) {
        return userRightRepository.checkPhoneNum(userRightCommand);
    }
}
