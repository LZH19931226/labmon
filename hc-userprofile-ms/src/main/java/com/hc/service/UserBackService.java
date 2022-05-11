package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserCommand;
import com.hc.dto.UserBackDto;
import com.hc.vo.user.UserInfoVo;

import java.util.List;

/**
 * @author hc
 */
public interface UserBackService {

    /**
     * 用户登录
     *
     * @param userBackDto 用户数据传输对象
     * @return 用户对象
     */
    UserBackDto userLogin(UserCommand userBackDto);

    /**
     * 修改密码
     * @param userBackDto 用户数据传输对象
     */
    void updateUserInfo(UserCommand userBackDto);

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
     * 删除用户信息
     * @param userid
     */
    void deleteUserInfo(String[] userid);

    /**
     * 新增用户信息
     * @param userCommand
     */
    void insertUserInfo(UserCommand userCommand);
}
