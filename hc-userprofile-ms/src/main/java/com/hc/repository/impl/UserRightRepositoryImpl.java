package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.infrastructure.dao.UserRightDao;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.UserRightPo;
import com.hc.repository.UserRightRepository;
import com.hc.vo.user.UserRightVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * 用户权限库实现
 * @author hc
 */
@Repository
public class UserRightRepositoryImpl extends ServiceImpl<UserRightDao, UserRightPo> implements UserRightRepository {

    @Autowired
    private UserRightDao userRightDao;

    /**
     * 根据分页对象查找用户权限信息
     * @param page 分页对象
     * @param userRightCommand 用户权限命令
     * @return
     */
    @Override
    public List<UserRightDto> findUserRightList(Page<UserRightVo> page, UserRightCommand userRightCommand) {
        String hospitalCode = userRightCommand.getHospitalCode();
        return userRightDao.findUserRightList(page,hospitalCode,userRightCommand.getUsername(),userRightCommand.getPhoneNum(),userRightCommand.getIsUse());
    }

    /**
     * 插入用户信息
     *
     * @param userRightCommand 用户
     */
    @Override
    public void insertUserRightInfo(UserRightCommand userRightCommand) {
        UserRightPo userRightPo = BeanConverter.convert(userRightCommand, UserRightPo.class);
        Integer integer = userRightDao.selectCount(Wrappers.lambdaQuery(new UserRightPo())
                .eq(UserRightPo::getUsername, userRightCommand.getUsername()));
        if(integer>0){
            throw new IedsException(LabSystemEnum.LOGIN_ACCOUNT_ALREADY_EXISTS);
        }
        if (StringUtils.isNotEmpty(userRightCommand.getPhoneNum())) {
            Integer num = userRightDao.selectCount(Wrappers.lambdaQuery(new UserRightPo())
                    .eq(UserRightPo::getHospitalCode,userRightCommand.getHospitalCode())
                    .eq(UserRightPo::getPhoneNum,userRightCommand.getPhoneNum()));
            if(num>0){
                throw new IedsException(LabSystemEnum.PHONE_NUM_EXISTS);
            }
        }
        userRightPo.setUserid(UUID.randomUUID().toString().replaceAll("-", ""));
        userRightDao.insert(userRightPo);
    }

    /**
     * 修改信息信息
     *
     * @param userRightCommand
     */
    @Override
    public void updateUserRightInfo(UserRightCommand userRightCommand) {
        UserRightPo convert = BeanConverter.convert(userRightCommand, UserRightPo.class);
        userRightDao.updateById(convert);
    }

    /**
     * 更具医院编码获取医院的所有人员信息
     *
     * @param hospitalCode 医院编码
     * @return
     */
    @Override
    public List<UserRightDto> selectHospitalInfoByCode(String hospitalCode) {
        List<UserRightPo> poList = userRightDao.selectList(Wrappers.lambdaQuery(new UserRightPo())
                .eq(UserRightPo::getHospitalCode, hospitalCode));
        return BeanConverter.convert(poList,UserRightDto.class);
    }

    /**
     * 删除用户信息
     *
     * @param userid
     */
    @Override
    public void deleteUserRightInfo(String userid) {
        userRightDao.delete(Wrappers.lambdaQuery(new UserRightPo()).eq(UserRightPo::getUserid,userid));
    }

    /**
     * 查询用户信息
     *
     * @param userid
     * @return
     */
    @Override
    public UserRightDto selectUserRightInfo(String userid) {
        UserRightPo userRightPo = userRightDao.selectById(userid);
        return BeanConverter.convert(userRightPo,UserRightDto.class);
    }

    /**
     * 查询用户信息
     *
     * @param userRightCommand
     * @return
     */
    @Override
    public UserRightDto selectUserRight(UserRightCommand userRightCommand) {
        UserRightPo userRightPo = userRightDao.selectOne(Wrappers.lambdaQuery(new UserRightPo())
                .eq(UserRightPo::getUsername, userRightCommand.getUsername()));
        String lang = userRightCommand.getLang();
        if(ObjectUtils.isEmpty(userRightPo)){
            if(lang.equals("zh")){
                throw new IedsException(LabSystemEnum.USERNAME_NOT_EXIST.getMessage());
            }else {
                throw new IedsException(LabSystemEnum.USERNAME_NOT_EXIST.name());
            }
        }
        if(!StringUtils.equals(userRightPo.getPwd(),userRightCommand.getPwd())){
            if(lang.equals("zh")){
                throw new IedsException(LabSystemEnum.USER_ACCOUNT_OR_PASSWORD_ERROR.getMessage());
            }else{
                throw new IedsException(LabSystemEnum.USER_ACCOUNT_OR_PASSWORD_ERROR.name());
            }
        }
        if(userRightPo.getIsUse() != 1L){
            if(lang.equals("zh")){
                throw new IedsException(LabSystemEnum.USER_NOT_ENABLED.getMessage());
            }else {
                throw new IedsException(LabSystemEnum.USER_NOT_ENABLED.name());
            }

        }
        return BeanConverter.convert(userRightPo,UserRightDto.class);
    }

    /**
     * 查询当前医院的所有人员
     *
     * @param hospitalCode 医院id
     * @return 当前医院的所有人员集合
     */
    @Override
    public List<UserRightDto> findALLUserRightInfoByHospitalCode(String hospitalCode) {
        List<UserRightPo> poList = userRightDao.selectList(Wrappers.lambdaQuery(new UserRightPo()).eq(UserRightPo::getHospitalCode, hospitalCode).eq(UserRightPo::getIsUse, DictEnum.TURN_ON.getCode()));
        return BeanConverter.convert(poList,UserRightDto.class);
    }

    /**
     * @param hospitalCode
     * @param phoneNum
     * @return
     */
    @Override
    public int selectUserRightByCodeAndPhone(String hospitalCode, String phoneNum) {
        return userRightDao.selectCount(Wrappers.lambdaQuery(new UserRightPo())
                .eq(UserRightPo::getHospitalCode,hospitalCode)
                .eq(UserRightPo::getPhoneNum,phoneNum));
    }

    /**
     * @param userName
     * @return
     */
    @Override
    public Boolean checkUsername(String userName) {
        Integer integer = userRightDao.checkUsername(userName);
        return integer > 0;
    }

    /**
     * 检验手机号是否存在
     *
     * @param userRightCommand
     * @return
     */
    @Override
    public Boolean checkPhoneNum(UserRightCommand userRightCommand) {
        String hospitalCode = StringUtils.isBlank(userRightCommand.getHospitalCode())?"":userRightCommand.getHospitalCode();
        String phoneNum = StringUtils.isBlank(userRightCommand.getPhoneNum())?"":userRightCommand.getPhoneNum();
        Integer integer = userRightDao.selectCount(Wrappers.lambdaQuery(new UserRightPo())
                .eq(UserRightPo::getHospitalCode, hospitalCode)
                .eq(UserRightPo::getPhoneNum, phoneNum));
        return integer>0;
    }

    @Override
    public String getUserName(String userId) {
        return userRightDao.getUserName(userId);
    }

}
