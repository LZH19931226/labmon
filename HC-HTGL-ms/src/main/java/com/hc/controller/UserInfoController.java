package com.hc.controller;

import com.hc.entity.Userback;
import com.hc.service.BugInXXFService;
import com.hc.service.UserInfoService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by 16956 on 2018-08-05.
 */
@Api(value = "后台用户信息", description = "后台用户信息Api")
@RestController
@RequestMapping(value = "/api/userBackInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private BugInXXFService bugInXXFService;
    @PostMapping("/userLogin")
    @ApiOperation(value = "后台管理员登录",response = Userback.class)
    public ApiResponse<String> userLogin(@RequestBody @Valid Userback userback){
        return userInfoService.userLogin(userback);
    }
    @PostMapping("/updatePwd")
    @ApiOperation(value = "更改用户密码",response = Userback.class)
    public ApiResponse<String> updatePwd(@RequestBody @Valid Userback userback){
        return userInfoService.updatePassword(userback);
    }
//    @GetMapping("/update")
//    public ApiResponse<String> update(){
//       return  bugInXXFService.update1();
//    }
}

