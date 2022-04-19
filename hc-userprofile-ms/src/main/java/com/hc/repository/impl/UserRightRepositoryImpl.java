package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.infrastructure.dao.UserRightDao;
import com.hc.po.UserRightPo;
import com.hc.repository.UserRightRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户权限库实现
 * @author hc
 */
@Repository
public class UserRightRepositoryImpl extends ServiceImpl<UserRightDao, UserRightPo> implements UserRightRepository {

    @Autowired
    private UserRightDao userRightDao;


    @Override
    public List<UserRightDto> findUserRightList(Page page, UserRightCommand userRightCommand) {
        List<UserRightPo> userRightPos = userRightDao.selectList(Wrappers.lambdaQuery(new UserRightPo()).eq(UserRightPo::getNickname,null));
        if(CollectionUtils.isNotEmpty(userRightPos)){
            userRightPos.forEach(res->{
                res.setNickname(res.getUsername());
                userRightDao.updateById(res);
            });
        }
        return userRightDao.findUserRightList(page,userRightCommand.getHospitalName(),userRightCommand.getUsername(),userRightCommand.getPhoneNum(),userRightCommand.getIsUse());
    }
}
