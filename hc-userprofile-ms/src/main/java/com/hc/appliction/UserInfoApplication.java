package com.hc.appliction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserCommand;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.dto.UserBackDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.UserBackService;
import com.hc.vo.user.UserInfoVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息申请
 * @author hc
 */
@Component
public class UserInfoApplication {

    @Autowired
    private UserBackService userBackService;

    /**
     * 用户登录
     *
     * @param userCommand 用户信息
     */
    public UserInfoVo userLogin(UserCommand userCommand) {
        UserBackDto userBackDto = userBackService.userLogin(userCommand);
        return UserInfoVo.builder()
                .username(userBackDto.getUsername())
                .userid(userBackDto.getUserid())
                .build();
    }

    /**
     * 修改用户信息
     *
     * @param userCommand 用户信息
     */
    public void UserInfo(UserCommand userCommand) {
        userBackService.updateUserInfo(userCommand);
    }

    /**
     * 获取用户信息
     *
     * @param userid 用户id
     * @return 用户信息
     */
    public UserBackModel findUserInfo(String userid) {
        UserBackDto userBackDto = userBackService.selectUserBackByUserId(userid);
        return BeanConverter.convert(userBackDto, UserBackModel.class);
    }

    /**
     * 分页获取后台用户信息
     * @param userCommand 用户参数对象
     * @return 分页对象
     */
    public Page<UserInfoVo> findUserAllInfo(UserCommand userCommand) {
        Page<UserInfoVo> page = new Page<>(userCommand.getPageCurrent(),userCommand.getPageSize());
        List<UserBackDto> list = userBackService.findUserAllInfo(page,userCommand);
        List<UserInfoVo> voList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(res->{
                UserInfoVo build = UserInfoVo.builder()
                        .userid(res.getUserid())
                        .username(res.getUsername())
                        .pwd(res.getPwd())
                        .build();
                voList.add(build);
            });
        }
        page.setRecords(voList);
        return page;
    }

    /**
     * 删除用户信息
     * @param userid 用户id
     */
    public void deleteUserInfo(String[] userid) {
        userBackService.deleteUserInfo(userid);
    }

    /**
     * 新增后台用户信息
     * @param userCommand 用户参数对象
     */
    public void insertUserInfo(UserCommand userCommand) {
        userBackService.insertUserInfo(userCommand);
    }
}
