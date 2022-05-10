package com.hc.appliction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserRightCommand;
import com.hc.command.labmanagement.user.UserRightInfoCommand;
import com.hc.command.labmanagement.user.UserRightLogCommand;
import com.hc.dto.UserBackDto;
import com.hc.dto.UserRightDto;
import com.hc.labmanagent.OperationlogApi;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.UserBackService;
import com.hc.service.UserRightService;
import com.hc.vo.user.UserRightVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户权限申请
 * @author hc
 */
@Component
public class UserRightApplication {

    @Autowired
    private UserRightService userRightService;

    @Autowired
    private OperationlogApi operationlogApi;

    @Autowired
    private  UserBackService userBackService;
    /**
     * 根据分页信息查询用户权限信息
     * @param userRightCommand  用户权限命令
     * @param pageSize 分页大小
     * @param pageCurrent 当前分页
     * @return
     */
    public Page<UserRightVo> findUserRightList(UserRightCommand userRightCommand, Long pageSize, Long pageCurrent) {
        Page<UserRightVo> page = new Page<>(pageCurrent,pageSize);
        List<UserRightDto> userRightList = userRightService.findUserRightList(page, userRightCommand);
        List<UserRightVo> list = new ArrayList<>();
        if(userRightList!=null && userRightList.size()!=0){
            userRightList.forEach(res ->{
                UserRightVo result = UserRightVo.builder()
                        .hospitalCode(res.getHospitalCode())
                        .userid(res.getUserid())
                        .username(res.getUsername())
                        .nickname(res.getNickname())
                        .pwd(res.getPwd())
                        .hospitalName(res.getHospitalName())
                        .phoneNum(res.getPhoneNum())
                        .isUse(res.getIsUse())
                        .userType(res.getUserType())
                        .deviceType(res.getDeviceType())
                        .timeout(res.getTimeout()==null?"":res.getTimeout())
                        .timeoutWarning(res.getTimeoutWarning()==null?"":res.getTimeoutWarning())
                        .build();
                list.add(result);
            });
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 新增用户信息
     * @param userRightCommand
     */
    public void insertUserRightInfo(UserRightCommand userRightCommand) {
        userRightService.insertUserRightInfo(userRightCommand);
        UserRightInfoCommand userRightInfoCommand = build(Context.getUserId(),
                 new UserRightLogCommand(),
                 userRightCommand,
                 OperationLogEunm.USER_INFO.getCode(),
                 OperationLogEunmDerailEnum.ADD.getCode());
        operationlogApi.addUserRightLog(userRightInfoCommand);
    }

    private UserRightInfoCommand build(String userId, UserRightLogCommand oldInfo, UserRightCommand newInfo, String type, String operationType) {
        UserRightInfoCommand userRightInfoCommand = new UserRightInfoCommand();
        UserBackDto userBackDto = userBackService.selectUserBackByUserId(userId);
        if(!ObjectUtils.isEmpty(userBackDto)){
            userRightInfoCommand.setUsername(userBackDto.getUsername());
        }
        userRightInfoCommand.setHospitalName(newInfo.getHospitalName());
        userRightInfoCommand.setType(type);
        userRightInfoCommand.setOperationType(operationType);
        UserRightLogCommand convert = BeanConverter.convert(newInfo, UserRightLogCommand.class);
        userRightInfoCommand.setNewUserRight(convert);
        userRightInfoCommand.setOldUserRight(oldInfo);
        return userRightInfoCommand;
    }

    /**
     * 修改用户信息
     * @param userRightCommand
     */
    public void updateUserRightInfo(UserRightCommand userRightCommand) {
        UserRightDto userRightDto = userRightService.selectUserRightInfo(userRightCommand.getUserid());
        userRightService.updateUserRightInfo(userRightCommand);
        UserRightLogCommand convert = BeanConverter.convert(userRightDto, UserRightLogCommand.class);
        UserRightInfoCommand build = build(Context.getUserId(),
                convert,
                userRightCommand,
                OperationLogEunm.USER_INFO.getCode(),
                OperationLogEunmDerailEnum.EDIT.getCode());
        operationlogApi.addUserRightLog(build);
    }

    /**
     * 删除用户信息
     * @param userRightCommand
     */
    public void deleteUserRightInfo(UserRightCommand userRightCommand) {
        userRightService.deleteUserRightInfo(userRightCommand);
        UserRightDto userRightDto = userRightService.selectUserRightInfo(userRightCommand.getUserid());
        UserRightLogCommand command = BeanConverter.convert(userRightDto, UserRightLogCommand.class);
        UserRightInfoCommand build =
                build(Context.getUserId(), command, new UserRightCommand(), OperationLogEunm.USER_INFO.getCode(), OperationLogEunmDerailEnum.REMOVE.getCode());
        operationlogApi.addUserRightLog(build);
    }
}
