package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.vo.User.UserRightVo;

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
     * @param userRightCommand
     */
    void insertUserRightInfo(UserRightCommand userRightCommand);

}
