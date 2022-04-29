package com.hc.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.my.common.core.constant.enums.UserRightEnumCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.repository.UserRightRepository;
import com.hc.service.UserRightService;
import com.hc.vo.user.UserRightVo;
import com.redis.util.RedisTemplateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户权限服务实现类
 * @author hc
 */
@Service
public class UserRightServiceImpl implements UserRightService {

    @Autowired
    private UserRightRepository userRightRepository;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

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
        String phoneNum = userRightCommand.getPhoneNum();
        String timeout = userRightCommand.getTimeout();
        String hospitalCode = userRightCommand.getHospitalCode();
        String userType = userRightCommand.getUserType();
        if (StringUtils.isBlank(username)) {
            throw new IedsException(UserRightEnumCode.USERNAME_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(nickname)) {
            throw new IedsException(UserRightEnumCode.NICKNAME_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(pwd)) {
            throw new IedsException(UserRightEnumCode.PWD_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(phoneNum)) {
            throw new IedsException(UserRightEnumCode.USER_PHONE_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(timeout)) {
            throw new IedsException(UserRightEnumCode.SUPERMARKET_CONTACT_CANNOT_BE_EMPTY.getMessage());
        }
        if (StringUtils.isBlank(hospitalCode)) {
            throw new IedsException(UserRightEnumCode.HOSPITAL_NAME_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(userType)) {
            throw new IedsException(UserRightEnumCode.USER_ROLE_NOT_NULL.getMessage());
        }
        userRightRepository.insertUserRightInfo(userRightCommand);
        //将当前医院所有人员同步进redis
        List<UserRightDto> list = userRightRepository.selectHospitalInfoByCode(userRightCommand.getHospitalCode());
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        objectObjectObjectHashOperations.put("hospital:phonenum",userRightCommand.getHospitalCode(), JSON.toJSON(list));

    }

    /**
     * 修改用户信息
     *
     * @param userRightCommand 用户权限命令
     */
    @Override
    public void updateUserRightInfo(UserRightCommand userRightCommand) {
        if(StringUtils.isBlank(userRightCommand.getUserid())){
            throw new IedsException(UserRightEnumCode.USE_ID_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(userRightCommand.getNickname())) {
            throw new IedsException(UserRightEnumCode.NICKNAME_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(userRightCommand.getPwd())) {
            throw new IedsException(UserRightEnumCode.PWD_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(userRightCommand.getHospitalCode())) {
            throw new IedsException(UserRightEnumCode.HOSPITAL_NAME_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(userRightCommand.getUserType())) {
            throw new IedsException(UserRightEnumCode.USER_ROLE_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(userRightCommand.getPhoneNum())) {
            throw new IedsException(UserRightEnumCode.USER_PHONE_NOT_NULL.getMessage());
        }
        if (StringUtils.isBlank(userRightCommand.getTimeout())) {
            throw new IedsException(UserRightEnumCode.SUPERMARKET_CONTACT_CANNOT_BE_EMPTY.getMessage());
        }
        userRightRepository.updateUserRightInfo(userRightCommand);
        //更新redis信息
        List<UserRightDto> list = userRightRepository.selectHospitalInfoByCode(userRightCommand.getHospitalCode());
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        objectObjectObjectHashOperations.put("hospital:phonenum",userRightCommand.getHospitalCode(), JSON.toJSON(list));

    }

    /**
     * 删除用户信息
     *
     * @param userRightCommand
     */
    @Override
    public void deleteUserRightInfo(UserRightCommand userRightCommand) {
        userRightRepository.deleteUserRightInfo(userRightCommand.getUserid());
        //更新redis用户信息
        List<UserRightDto> list = userRightRepository.selectHospitalInfoByCode(userRightCommand.getHospitalCode());
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        objectObjectObjectHashOperations.put("hospital:phonenum",userRightCommand.getHospitalCode(), JSON.toJSON(list));
        if (CollectionUtils.isNotEmpty(list)){
            objectObjectObjectHashOperations.put("hospital:phonenum",userRightCommand.getHospitalCode(),JSON.toJSON(list));
        }

    }
}
