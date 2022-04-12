package com.hc.controller;

import com.hc.appliction.UserInfoApplication;
import com.hc.vo.User.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author hc
 */
@Api(value = "后台用户信息")
@RestController
@RequestMapping(value = "/api/userBackInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserInfoController {

    @Autowired
    private UserInfoApplication userInfoApplication;

    @PostMapping("/userLogin")
    @ApiOperation(value = "后台管理员登录")
    public void userLogin(@ApiParam(name = "UserInfoVo", value = "用户登录对象", required = true)
            @RequestBody @Valid UserInfoVo userInfoVo) {
        userInfoApplication.userLogin(userInfoVo);
    }

    @PostMapping("/updatePwd")
    @ApiOperation(value = "更改用户密码")
    public void updatePwd( @RequestBody @Valid UserInfoVo userInfoVo) {
        userInfoApplication.updatePassword(userInfoVo);
    }

}
