package com.hc.appliction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.service.UserRightService;
import com.hc.vo.user.UserRightVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    }

    /**
     * 修改用户信息
     * @param userRightCommand
     */
    public void updateUserRightInfo(UserRightCommand userRightCommand) {
        userRightService.updateUserRightInfo(userRightCommand);
    }

    /**
     * 删除用户信息
     * @param userRightCommand
     */
    public void deleteUserRightInfo(UserRightCommand userRightCommand) {
        userRightService.deleteUserRightInfo(userRightCommand);
    }
}
