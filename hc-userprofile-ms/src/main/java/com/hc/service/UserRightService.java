package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.SysNationalDto;
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

    /**
     * 用户登录
     * @param userRightCommand
     * @return
     */
    UserRightDto selectUserRight(UserRightCommand userRightCommand);

    /**
     * 查询当前医院的所有人员
     * @param hospitalCode 医院id
     * @return 当前医院的所有人员集合
     */
    List<UserRightDto> findALLUserRightInfoByHospitalCode(String hospitalCode);

    /**
     * 验证用户名是否存在
     * @param userName
     * @return
     */
    Boolean checkUsername(String userName);

    /**
     * app修改用户信息
     * @param userRightCommand
     */
    void appUpdateUser(UserRightCommand userRightCommand);

    /**
     * 验证手机号是否存在
     * @param userRightCommand
     * @return
     */
    Boolean checkPhoneNum(UserRightCommand userRightCommand);

    String getUserName(String userId);

    List<SysNationalDto> getNational();

}
