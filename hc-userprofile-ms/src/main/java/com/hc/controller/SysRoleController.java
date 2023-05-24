package com.hc.controller;

import com.hc.appliction.SysRoleaApplication;
import com.hc.vo.user.dto.SysRoleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class SysRoleController {

    @Autowired
    private SysRoleaApplication sysRoleaApplication;

    @GetMapping("/list")
    @ApiOperation("获取所有角色信息")
    public List<SysRoleDTO> getSysRoleList(){
        return sysRoleaApplication.getSysRoleList();
    }

}
