package com.hc.controller;

import com.hc.po.UserScheduLing;
import com.hc.po.Userback;
import com.hc.model.RequestModel.UserScheduLingPostModel;
import com.hc.service.UserInfoService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by 16956 on 2018-08-05.
 */
@Api(value = "后台用户信息", description = "后台用户信息Api")
@RestController
@RequestMapping(value = "/api/userBackInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/userLogin")
    @ApiOperation(value = "后台管理员登录", response = Userback.class)
    public ApiResponse<String> userLogin(@RequestBody @Valid Userback userback) {
        return userInfoService.userLogin(userback);
    }

    @PostMapping("/updatePwd")
    @ApiOperation(value = "更改用户密码", response = Userback.class)
    public ApiResponse<String> updatePwd(@RequestBody @Valid Userback userback) {
        return userInfoService.updatePassword(userback);
    }


    @PostMapping("/addusersc")
    @ApiOperation("保存排班")
    public ApiResponse<String> addusersc(@RequestBody UserScheduLingPostModel userScheduLingPostModel) {
        return userInfoService.addusersc(userScheduLingPostModel);
    }


    @GetMapping("/searchScByHosMon")
    @ApiOperation("获取该医院某时间排班")
    public ApiResponse<List<UserScheduLing>> searchScByHosMon(@ApiParam(name = "hosId", value = "医院id", required = true)
    @RequestParam(value = "hosId") String hosId,
    @ApiParam(name = "startmonth", value = "年月", required = true)
    @RequestParam(value = "startmonth") String startmonth,
    @ApiParam(name = "endmonth", value = "年月", required = true)
    @RequestParam(value = "endmonth") String endmonth) {
        return userInfoService.searchScByHosMonSection(hosId, startmonth, endmonth);
    }


}

