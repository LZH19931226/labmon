package com.hc.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hc.model.LoginResponseModel;
import com.hc.model.UserModel;
import com.hc.service.UserInfoService;
import com.hc.utils.ApiResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by 16956 on 2018-07-31.
 */
@Api(value = "用户信息", description = "用户信息信息API")
@RestController
@RequestMapping(value = "/api/userInfo",produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/userLogin")
    @ApiOperation(value = "用户登录", notes = "用户登录", response = UserModel.class)
    public ApiResponse<LoginResponseModel> userLogin(@RequestBody @Valid UserModel userModel) {
        return userInfoService.userLoginByApp(userModel);
    }
    @PostMapping("/userLoginByAn")
    @ApiOperation(value = "用户登录", notes = "用户登录", response = UserModel.class)
    public ApiResponse<LoginResponseModel> userLoginBy(@RequestBody @Valid UserModel userModel) {
        return userInfoService.userLoginByAndrio(userModel);
    }
    @PostMapping("/userLoginByAns")
    @ApiOperation(value = "用户登录", notes = "用户登录", response = UserModel.class)
    public ApiResponse<LoginResponseModel> userLoginBys(@RequestBody @Valid UserModel userModel) {
        return userInfoService.userLoginByAndrios(userModel);
    }
    @GetMapping("/getCode")
    @ApiOperation(value = "获取短信验证码")
    public ApiResponse<String> getCode(@ApiParam(name = "phonenum", value = "用户手机号", required = true)
                                       @RequestParam(value = "phonenum", required = true) String phonenum) {
        return userInfoService.getCode(phonenum);
    }
    @PostMapping("/updatePhone")
    @ApiOperation(value = "修改手机号")
    public ApiResponse<String> updatePhone(@RequestBody @Valid UserModel userModel){
        return userInfoService.updatePhone(userModel);
    }
    @PostMapping("/updatePwd")
    @ApiOperation(value="修改密码")
    public ApiResponse<String> updatePwd(@RequestBody @Valid UserModel userModel){
        return userInfoService.updatePwd(userModel);
    }
    @PostMapping("/loginOut")
    @ApiOperation(value="退出登录")
    public ApiResponse<String> loginOut(@ApiParam(name = "token", value = "token", required = true)
                                            @RequestParam(value = "token", required = true) String token){
        return userInfoService.loginOut(token);
    }
}
