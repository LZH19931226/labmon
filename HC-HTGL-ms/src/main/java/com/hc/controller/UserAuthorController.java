package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.model.RequestModel.UserauthorInfoModel;
import com.hc.model.ResponseModel.UserAuthInfoModel;
import com.hc.service.UserAuthoSetService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by 16956 on 2018-08-06.
 */
@Api(value = "用户权限管理", description = "用户权限管理Api")
@RestController
@RequestMapping(value = "/api/userAuthorInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserAuthorController {

    @Autowired
    private UserAuthoSetService userAuthoSetService;

    @GetMapping("/selectUserAuthorInfoPage")
    @ApiOperation(value = "分页查询用户权限信息")
    public ApiResponse<Page<UserAuthInfoModel>> selectUserAuthorInfoPage(@ApiParam(name = "pagesize", value = "每页显示条目数", required = true)
                                                        @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                                         @ApiParam(name = "pagenum", value = "当前页码数", required = true)
                                                        @RequestParam(value = "pagenum", required = true) Integer pagenum){
        return userAuthoSetService.showUserAuth(pagesize,pagenum);
    }
    @PostMapping("/updateUserAuthor")
    @ApiOperation(value="更新用户权限",response = UserauthorInfoModel.class)
    public ApiResponse<String> updateUserAuthor(@RequestBody @Valid UserauthorInfoModel userauthorInfoModel){
        return userAuthoSetService.updateAuthor(userauthorInfoModel);
    }
}
