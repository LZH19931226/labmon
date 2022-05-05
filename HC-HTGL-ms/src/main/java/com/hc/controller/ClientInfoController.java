package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.po.Userright;
import com.hc.model.ResponseModel.ClientInfoModel;
import com.hc.service.ClientInfoService;
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
@Api(value = "客户信息", description = "客户信息Api")
@RestController
@RequestMapping(value = "/api/clientInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ClientInfoController {
    @Autowired
    private ClientInfoService clientInfoService;

    @PostMapping("/addUser")
    @ApiOperation(value = "添加用户信息")
    public ApiResponse<Userright> addUser(@RequestBody @Valid Userright userright) {
        return clientInfoService.addUser(userright);

    }

    @PostMapping("/updateUser")
    @ApiOperation(value = "修改用户信息")
    public ApiResponse<Userright> updateUser(@RequestBody @Valid Userright userright) {
        return clientInfoService.updateUser(userright);

    }

    @PostMapping("/deleteUser")
    @ApiOperation(value = "删除用户信息")
    public ApiResponse<String> deleteUser(@RequestBody @Valid Userright userright) {
        return clientInfoService.deleteUser(userright);

    }

    @GetMapping("/selectUserInfoPage")
    @ApiOperation(value = "分页模糊查询用户信息")
    public ApiResponse<Page<ClientInfoModel>> selectUserInfoPage(@ApiParam(name = "fuzzy", value = "模糊查询参数", required = false)
                                                                 @RequestParam(value = "fuzzy", required = false) String fuzzy,
                                                                 @ApiParam(name = "pagesize", value = "每页显示条目数", required = true)
                                                                 @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                                 @ApiParam(name = "pagenum", value = "当前页码数", required = true)
                                                                 @RequestParam(value = "pagenum", required = true) Integer pagenum,
                                                                 @ApiParam(name = "hospitalcode", value = "模糊查询参数", required = false)
                                                                 @RequestParam(value = "hospitalcode", required = false) String hospitalcode,
                                                                 @RequestParam(value = "setterWarningUsername", required = false) String setterWarningUsername) {
        return clientInfoService.selectUserInfoPage(hospitalcode,fuzzy,pagesize,pagenum,setterWarningUsername);
    }
}
