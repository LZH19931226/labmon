package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.po.UserRightPo;
import com.hc.vo.user.UserRightVo;

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
    List<UserRightDto> findUserRightList(Page<UserRightVo> page, UserRightCommand userRightCommand);

    /**
     * 插入用户信息
     * @param userRightCommand 用户
     */
    void insertUserRightInfo(UserRightCommand userRightCommand);

    /**
     * 修改信息信息
     * @param userRightCommand
     */
    void updateUserRightInfo(UserRightCommand userRightCommand);

    /**
     * 更具医院编码获取医院的所有人员信息
     * @param hospitalCode 医院编码
     * @return
     */
    List<UserRightDto> selectHospitalInfoByCode(String hospitalCode);

    /**
     * 删除用户信息
     * @param userid
     */
    void deleteUserRightInfo(String userid);

    /**
     * 查询用户信息
     * @param userid
     * @return
     */
    UserRightDto selectUserRightInfo(String userid);

    /**
     * 查询用户信息
     * @param userRightCommand
     * @return
     */
    UserRightDto selectUserRight(UserRightCommand userRightCommand);

    /**
     * 查询当前医院的所有人员
     *
     * @param hospitalCode 医院id
     * @return 当前医院的所有人员集合
     */
    List<UserRightDto> findALLUserRightInfoByHospitalCode(String hospitalCode);

    int selectUserRightByCodeAndPhone(String hospitalCode, String phoneNum);

    /**
     * 验证用户名是否存在
     * @param userName
     * @return
     */
    Boolean checkUsername(String userName);

    /**
     * 检验手机号是否存在
     * @param userRightCommand
     * @return
     */
    Boolean checkPhoneNum(UserRightCommand userRightCommand);

    String getUserName(String userId);
}
