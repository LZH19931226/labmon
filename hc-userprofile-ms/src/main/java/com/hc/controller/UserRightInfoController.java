package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.UserRightApplication;
import com.hc.appliction.command.UserRightCommand;
import com.hc.vo.user.UserRightVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiOperation("新增人员信息")
    public void add(@RequestBody UserRightCommand userRightCommand){
        userRightApplication.insertUserRightInfo(userRightCommand);
    }

    @PutMapping("/updateUserRightInfo")
    @ApiOperation("修改用户信息")
    public void edit(@RequestBody UserRightCommand userRightCommand){
        userRightApplication.updateUserRightInfo(userRightCommand);
    }

    @DeleteMapping("/deleteUserRightInfo")
    @ApiOperation("删除用户信息")
    public void remove(@RequestBody UserRightCommand userRightCommand){
        userRightApplication.deleteUserRightInfo(userRightCommand);
    }

    @PostMapping("/userLogin")
    @ApiOperation("用户登录")
    public UserRightVo UserRightLogin(@RequestBody UserRightCommand userRightCommand ){
        return userRightApplication.Login(userRightCommand);
    }

    /**
     * 查询当前医院所有的人员信息
     * @param hospitalCode 医院id
     * @return
     */
    @GetMapping("/getALLHospitalUserRightInfo")
    public List<UserRightVo> findALLUserRightInfoByHC(@RequestParam("hospitalCode")String hospitalCode){
        return userRightApplication.findALLUserRightInfo(hospitalCode);
    }
}
