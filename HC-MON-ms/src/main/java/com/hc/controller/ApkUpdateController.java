package com.hc.controller;


import com.hc.po.IosFileEntity;
import com.hc.model.ApkCheck;
import com.hc.service.ApkFileEntityService;
import com.hc.utils.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
@Api(value = "apk更新接口", description = "apk更新,检查,下载")
@RestController
@RequestMapping("/apk/")
public class ApkUpdateController {
     @Autowired
     private ApkFileEntityService apkFileEntityService;

     @ApiOperation("通过上传的apk版本号告知是否要更新")
     @GetMapping("/checkApk")
     public ApiResponse<ApkCheck>  checkApk(String versionId){

         ApiResponse<ApkCheck> apiResponse = apkFileEntityService.checkApk(versionId);

         return  apiResponse;
     }

     @ApiOperation("上传文件接口")
     @PostMapping("/uploadFile")
         public ApiResponse<String> uploadFile(@RequestParam("fileName") MultipartFile file, @RequestParam("info")String info, @RequestParam("forceUpdate")String forceUpdate, @RequestParam("code")String code, @RequestParam("versionname")String versionname){

         ApiResponse<String> apiResponse = apkFileEntityService.uploadFile(file,info,forceUpdate,code,versionname);

         return  apiResponse;
     }


    @ApiOperation("根据文件名称下载apk接口")
    @GetMapping("/downloadFile/{filename:.+}")
    public ApiResponse<String> downloadFile(HttpServletResponse res,@PathVariable(value = "filename")String filename){

        ApiResponse<String> apiResponse = apkFileEntityService.downloadFile(res, filename);
        return  apiResponse;
    }



    @ApiOperation("通过上传的ios当前版本,如果不是最新则返回最新版本信息")
    @GetMapping("/checkIos")
    public ApiResponse<IosFileEntity>  checkIos(String versionId){

        ApiResponse<IosFileEntity> fileEntityApiResponse = apkFileEntityService.checkIos(versionId);
        return  fileEntityApiResponse;
    }


    @ApiOperation("上传ios版本信息,默认更新时间最新的为最新版本")
    @GetMapping("/uploadIosVersion")
    public ApiResponse<String>  uploadIosVersion(String info, String code, String versionname, String filename){

        ApiResponse<String> apiResponse = apkFileEntityService.uploadIosVersion(info,  code, versionname, filename);

        return  apiResponse;
    }



}
