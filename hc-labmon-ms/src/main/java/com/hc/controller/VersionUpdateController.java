package com.hc.controller;

import com.hc.application.VersionUpdateApplication;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/update")
@Api("app版本更新控制类")
public class VersionUpdateController {

    @Autowired
    private VersionUpdateApplication versionUpdateApplication;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public void fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        versionUpdateApplication.fileUpload(file);
    }

    @GetMapping("/download")
    @ApiOperation("文件下载")
    public void downFile(HttpServletResponse response) {
        versionUpdateApplication.download(response);
    }
}
