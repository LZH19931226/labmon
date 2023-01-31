package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserCommand;
import com.hc.dto.UserBackDto;
import com.hc.po.UserBackPo;
import com.hc.vo.user.UserInfoVo;

import java.util.List;

/**
 *
 * @author hc
 */
public interface UserBackRepository{

    /**
     * 用户登录
     * @param userBackPo 数据传输对象
     * @return UserBackDto
     */
    UserBackDto userLogin(UserBackPo userBackPo,String lang);

    /**
     * 修改用户信息
     * @param userBackPo 数据传输对象
     */
    void updateUserInfo(UserBackPo userBackPo);

    /**
     * 根据用户id查询user信息
     * @param userId
     * @return
     */
    UserBackDto selectUserBackByUserId(String userId);

    /**
     * 分页获取后台人员信息
     * @param page
     * @param userCommand
     * @return
     */
    List<UserBackDto> findUserAllInfo(Page<UserInfoVo> page, UserCommand userCommand);

    /**
     *  查询用户是否存在
     * @param username
     * @return
     */
    Integer selectOne(String username);

    /**
     * 删除用户信息
     * @param userid
     */
    void deleteUserInfo(String[] userid);

    /**
     * 新增后台信息
     * @param userCommand
     */
    void insertUserInfo(UserCommand userCommand);

    /**
     * 查询用户信息
     * @param username 用户名
     * @return
     */
    UserBackDto selectUserBackByUsername(String username);
}
