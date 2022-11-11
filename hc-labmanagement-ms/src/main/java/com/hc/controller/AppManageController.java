package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.AppManageApplication;
import com.hc.application.command.AppManageCommand;
import com.hc.dto.AppVersionManageDto;
import com.hc.my.common.core.jwt.JwtIgnore;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author user
 */
@RestController
@RequestMapping("/appManage")
public class AppManageController {


    @Autowired
    public AppManageApplication appManageApplication;

    @JwtIgnore
    @PostMapping("/save")
    @ApiOperation(value = "保存上传源文件")
    public void save(AppManageCommand appManageCommand,@RequestParam("file") MultipartFile file) throws IOException {
        appManageApplication.save(appManageCommand,file);
    }

    @JwtIgnore
    @PostMapping("/list")
    @ApiOperation(value = "分页查找app信息")
    public Page<AppVersionManageDto> list(@RequestBody AppManageCommand appManageCommand){
        return appManageApplication.list(appManageCommand);
    }

    @JwtIgnore
    @GetMapping("/download")
    @ApiOperation(value = "文件下载")
    public void download(@RequestParam("appName")String appName, HttpServletResponse httpServletResponse){
        appManageApplication.dowload(appName,httpServletResponse);
    }

    @JwtIgnore
    @DeleteMapping("/remove")
    @ApiOperation(value = "删除app信息")
    public void remove(@RequestParam("id")Integer id,@RequestParam("appName")String appName){
        appManageApplication.remove(id,appName);
    }

    @JwtIgnore
    @GetMapping("/getAppNewVersion")
    @ApiOperation(value = "获取安卓最新版本信息")
    public AppVersionManageDto getAppNewVersion(){
        return appManageApplication.getAppNewVersion();
    }
}
