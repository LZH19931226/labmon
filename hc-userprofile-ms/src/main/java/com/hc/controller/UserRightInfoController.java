package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.UserRightApplication;
import com.hc.appliction.command.UserRightCommand;
import com.hc.my.common.core.jwt.JwtIgnore;
import com.hc.my.common.core.redis.dto.UserRightRedisDto;
import com.hc.vo.country.SysNationalVo;
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

    @GetMapping("/checkUsername")
    @ApiOperation("验证用户名是否已存在")
    public Boolean search(@RequestParam("userName") String userName){
        return userRightApplication.checkUsername(userName);
    }

    @DeleteMapping("/deleteUserRightInfo")
    @ApiOperation("删除用户信息")
    public void remove(@RequestBody UserRightCommand userRightCommand){
        userRightApplication.deleteUserRightInfo(userRightCommand);
    }

    @PostMapping("/userLogin")
    @ApiOperation("用户账号登录")
    @JwtIgnore
    public UserRightVo userRightLogin(@RequestBody UserRightCommand userRightCommand){
        return userRightApplication.login(userRightCommand);
    }

    @PostMapping("/userRightLoginByPhone")
    @ApiOperation("手机号登录")
    public UserRightVo userRightLoginByPhone(@RequestBody UserRightCommand userRightCommand){
        return userRightApplication.userRightLoginByPhone(userRightCommand);
    }

    @GetMapping("/getPhoneCode")
    @ApiOperation("获取手机验证码")
    public void  getPhoneCode(@RequestParam("phoneNum")String phoneNum,String code){
        userRightApplication.getPhoneCode(phoneNum,code);
    }

    /**
     * 查询当前医院所有的人员信息
     * @param hospitalCode 医院id
     * @return
     */
    @JwtIgnore
    @GetMapping("/getALLHospitalUserRightInfo")
    @ApiOperation("通过医院code获取医院用户信息")
    public List<UserRightRedisDto> findALLUserRightInfoByHC(@RequestParam("hospitalCode")String hospitalCode){
        return userRightApplication.findALLUserRightInfo(hospitalCode);
    }

    @PostMapping("/getPhoneCode")
    @ApiOperation("获取手机登录验证码")
    public void selectPhoneCode(@RequestParam("phoneNum") String phoneNum,@RequestParam("code") String code){
         userRightApplication.getPhoneCode(phoneNum,code);
    }


    @PostMapping("/appUpdateUser")
    @ApiOperation("app修改用户信息")
    public void appUpdateUser(@RequestBody UserRightCommand userRightCommand){
        userRightApplication.appUpdateUser(userRightCommand);
    }

    @PostMapping("/checkPhoneNum")
    @ApiOperation("检验手机号是否重复")
    public Boolean checkPhoneNum(@RequestBody UserRightCommand userRightCommand){
        return  userRightApplication.checkPhoneNum(userRightCommand);
    }

    @JwtIgnore
    @GetMapping("/getUserName")
    @ApiOperation("获取用户信息")
    public String getUserName(@RequestParam("userId") String userId){
        return  userRightApplication.getUserName(userId);
    }

    @GetMapping("/getNational")
    @ApiOperation("获取国家信息")
    public List<SysNationalVo> getNational(){
        return userRightApplication.getNational();
    }

}
