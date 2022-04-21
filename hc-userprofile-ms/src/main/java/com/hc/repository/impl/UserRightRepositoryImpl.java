package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.infrastructure.dao.UserRightDao;
import com.hc.my.common.core.constant.enums.UserEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.UserRightPo;
import com.hc.repository.UserRightRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public List<UserRightDto> findUserRightList(Page page, UserRightCommand userRightCommand) {
        List<UserRightPo> poList = userRightDao.selectList(null);
        if(CollectionUtils.isNotEmpty(poList)){
            List<UserRightPo> collect = poList.stream().filter(res -> res.getNickname() == null || res.getNickname() == "").collect(Collectors.toList());
            collect.forEach(res->{
                res.setNickname(res.getUsername());
                userRightDao.updateById(res);
            });
        }

        return userRightDao.findUserRightList(page,userRightCommand.getHospitalName(),userRightCommand.getUsername(),userRightCommand.getPhoneNum(),userRightCommand.getIsUse());
    }

    /**
     * 插入用户权限信息
     *
     * @param userRightCommand 用户
     */
    @Override
    public void insertUserRightInfo(UserRightCommand userRightCommand) {
        UserRightPo userRightPo = BeanConverter.convert(userRightCommand, UserRightPo.class);
        Integer integer = userRightDao.selectCount(Wrappers.lambdaQuery(new UserRightPo())
                .eq(UserRightPo::getUsername, userRightCommand.getUsername()));
        if(integer>0){
            throw new IedsException(UserEnumErrorCode.USERNAME_ALREADY_EXISTS.getMessage());
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


}
