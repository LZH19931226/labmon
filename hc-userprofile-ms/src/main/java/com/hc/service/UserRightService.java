package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.vo.user.UserRightVo;

import java.util.List;

/**
 * 用户权限服务接口
 * @author hc
 */
public interface UserRightService {

    /**
     * 分页信息查询用户权信息
     * @param page  分页对象
     * @param userRightCommand 用户权限命令
     * @return
     */
    List<UserRightDto> findUserRightList(Page<UserRightVo> page, UserRightCommand userRightCommand);

    /**
     * 添加用户信息
     * @param userRightCommand 用户权限命令
     */
    void insertUserRightInfo(UserRightCommand userRightCommand);

    /**
     * 修改用户信息
     * @param userRightCommand 用户权限命令
     */
    void updateUserRightInfo(UserRightCommand userRightCommand);

    /**
     * 删除用户信息
     * @param userRightCommand 用户权限信息
     */
    void deleteUserRightInfo(UserRightCommand userRightCommand);

    /**
     * 查询用户信息
     * @param userid
     * @return
     */
    UserRightDto selectUserRightInfo(String userid);
}
