package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.UserBackDto;
import com.hc.infrastructure.dao.UserBackDao;
import com.hc.my.common.core.constant.enums.UserEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.UserBackPo;
import com.hc.repository.UserBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hc
 */
@Repository
public class UserBackRepositoryImpl implements UserBackRepository {

    @Autowired
    private UserBackDao userBackDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserBackDto userLogin(UserBackPo userBackPo) {

        String username = userBackPo.getUsername();
        String pwd = userBackPo.getPwd();

        UserBackPo userPo = userBackDao
                .selectOne(Wrappers.lambdaQuery(new UserBackPo())
                        .eq(UserBackPo::getUsername, username));

        if(userPo == null){
           throw  new IedsException(UserEnumErrorCode.USER_NOT_EXISTS.getMessage());
        }
        if(!userPo.getPwd().equals(pwd)){
            throw  new IedsException(UserEnumErrorCode.USER_ACCOUNT_OR_PASSWORD_ERROR.getMessage());
        }

        return BeanConverter.convert(userPo, UserBackDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UserBackPo userBackPo) {

        String userid = userBackPo.getUserid();
        String pwd = userBackPo.getPwd();

        UserBackPo userBackPo1 = userBackDao.selectById(userid);
        if(userBackPo1 == null){
            throw new IedsException(UserEnumErrorCode.USER_NOT_EXISTS.getMessage());
        }
        userBackDao.updateById(new UserBackPo().setUserid(userid).setPwd(pwd));
    }
}
