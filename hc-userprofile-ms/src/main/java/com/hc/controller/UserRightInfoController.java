package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.UserRightApplication;
import com.hc.appliction.command.UserRightCommand;
import com.hc.vo.User.UserRightVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户权限信息控制器
 * @author hc
 */
@RestController
@RequestMapping("/userRight")
@Api(value = "用户权限信息")
public class UserRightInfoController {

    @Autowired
    private UserRightApplication userRightApplication;

    @PostMapping("/findUserRightInfoList")
    @ApiOperation("分页获取人员信息")
    public Page<UserRightVo> list(@RequestBody UserRightCommand userRightCommand){
        return userRightApplication.findUserRightList(userRightCommand,userRightCommand.getPageSize(),userRightCommand.getPageCurrent());
    }

    @PostMapping("/addUserRightInfo")
    public void add(@RequestBody UserRightCommand userRightCommand){
        userRightApplication.insertUserRightInfo(userRightCommand);
    }
}
