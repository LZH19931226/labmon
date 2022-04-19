package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.po.UserRightPo;

import java.util.List;

/**
 * 用户权限库接口
 * @author hc
 */
public interface UserRightRepository extends IService<UserRightPo> {
    /**
     * 根据分页对象查找用户权限信息
     * @param page 分页对象
     * @param userRightCommand 用户权限命令
     * @return
     */
    List<UserRightDto> findUserRightList(Page page, UserRightCommand userRightCommand);
}
