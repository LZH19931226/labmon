package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.UserInfoApplication;
import com.hc.appliction.command.UserCommand;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.vo.user.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author hc
 */
@Api(value = "后台用户信息")
@RestController
@RequestMapping(value = "/userBackInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserInfoController {

    @Autowired
    private UserInfoApplication userInfoApplication;

    @PostMapping("/findAllUserInfo")
    @ApiOperation(value = "分页获取后台用户信息")
    public Page<UserInfoVo> findUserAllInfo(@RequestBody UserCommand userCommand){
       return   userInfoApplication.findUserAllInfo(userCommand);
    }

    @PostMapping("/userLogin")
    @ApiOperation(value = "后台管理登录")
    public UserInfoVo userLogin(@ApiParam(name = "UserCommand", value = "用户登录对象", required = true)
            @RequestBody UserCommand userCommand) {
       return userInfoApplication.userLogin(userCommand);
    }

    @PutMapping("/updateUserInfo")
    @ApiOperation(value = "修改后台管理信息")
    public void updateUserInfo( @RequestBody UserCommand userCommand) {
        userInfoApplication.UserInfo(userCommand);
    }

    @DeleteMapping("/{userid}")
    @ApiOperation("删除后台用户信息")
    public void remove(@PathVariable(value = "userid") String[] userid){
        userInfoApplication.deleteUserInfo(userid);
    }

    @GetMapping("/findUserInfo")
    @ApiOperation("获取用户信息")
    public UserBackModel findUserInfo(@RequestParam(value = "userid")String userid){
        return userInfoApplication.findUserInfo(userid);
    }

    @PostMapping("/addUserInfo")
    @ApiOperation("新增后台用户信息")
    public void addUserInfo(@RequestBody UserCommand userCommand){
       userInfoApplication.insertUserInfo(userCommand);
    }

}
