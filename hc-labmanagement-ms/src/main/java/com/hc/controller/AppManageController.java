package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.AppManageApplication;
import com.hc.application.command.AppManageCommand;
import com.hc.dto.AppVersionManageDto;
import io.swagger.annotations.ApiOperation;
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

    @PostMapping("/save")
    @ApiOperation(value = "保存上传源文件")
    public void save(AppManageCommand appManageCommand,@RequestParam("file") MultipartFile file) throws IOException {
        appManageApplication.save(appManageCommand,file);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查找app信息")
    public Page<AppVersionManageDto> list(@RequestBody AppManageCommand appManageCommand){
        return appManageApplication.list(appManageCommand);
    }

    @GetMapping("/download")
    @ApiOperation(value = "文件下载")
    public void download(@RequestParam("appName")String appName, HttpServletResponse httpServletResponse){
        appManageApplication.dowload(appName,httpServletResponse);
    }

    @DeleteMapping("/remove")
    @ApiOperation(value = "删除app信息")
    public void remove(@RequestParam("id")Integer id,@RequestParam("appName")String appName){
        appManageApplication.remove(id,appName);
    }

    @GetMapping("/getAppNewVersion")
    @ApiOperation(value = "获取安卓最新版本信息")
    public AppVersionManageDto getAppNewVersion(){
        return appManageApplication.getAppNewVersion();
    }
}
