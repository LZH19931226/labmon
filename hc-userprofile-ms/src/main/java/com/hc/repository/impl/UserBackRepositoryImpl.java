package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserCommand;
import com.hc.dto.UserBackDto;
import com.hc.infrastructure.dao.UserBackDao;
import com.hc.my.common.core.constant.enums.UserEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.UserBackPo;
import com.hc.repository.UserBackRepository;
import com.hc.vo.user.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author hc
 */
@Repository
public class UserBackRepositoryImpl  implements UserBackRepository {

    @Autowired
    private UserBackDao userBackDao;

    /**
     * 用户登录
     * @param userBackPo 用户信息
     * @return 用户信息
     */
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

    /**
     * 修改用户信息
     * @param userBackPo 数据传输对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserBackPo userBackPo) {

        String userid = userBackPo.getUserid();
        String pwd = userBackPo.getPwd();
        String username = userBackPo.getUsername();
        UserBackPo userBackPo1 = userBackDao.selectById(userid);
        if(userBackPo1 == null){
            throw new IedsException(UserEnumErrorCode.USER_NOT_EXISTS.getMessage());
        }
        userBackDao.updateById(new UserBackPo().setUserid(userid).setPwd(pwd).setUsername(username));
    }

    /**
     * 根据用户id查询user信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserBackDto selectUserBackByUserId(String userId) {
        UserBackPo userBackPo = userBackDao.selectById(userId);
        return BeanConverter.convert(userBackPo,UserBackDto.class);
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
        return userBackDao.selectPageAllInfo(page,userCommand.getUsername());
    }

    /**
     * @param username
     * @return
     */
    @Override
    public Integer selectOne(String username) {
        return userBackDao.selectCount(Wrappers.lambdaQuery(new UserBackPo()).eq(UserBackPo::getUsername,username));
    }

    /**
     * 删除用户信息
     *
     * @param userid
     */
    @Override
    public void deleteUserInfo(String[] userid) {
        List<String> longs = Arrays.asList(userid);
        userBackDao.deleteBatchIds(longs);
    }

    /**
     * 新增后台信息
     *
     * @param userCommand
     */
    @Override
    public void insertUserInfo(UserCommand userCommand) {
        String username = userCommand.getUsername();
        String pwd = userCommand.getPwd();
        userBackDao.insert(new UserBackPo().setUsername(username).setPwd(pwd));
    }

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return
     */
    @Override
    public UserBackDto selectUserBackByUsername(String username) {
        UserBackPo userBackPo = userBackDao.selectOne(Wrappers.lambdaQuery(new UserBackPo()).eq(UserBackPo::getUsername, username));
        return BeanConverter.convert(userBackPo,UserBackDto.class);
    }
}
