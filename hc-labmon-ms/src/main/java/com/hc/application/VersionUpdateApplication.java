package com.hc.application;

import com.hc.my.common.core.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class VersionUpdateApplication {

    @Value("${file.path}")
    private String path;

    /**
     * 文件下载
     */
    public void download(HttpServletResponse response) {
        //获取文件名称
        String filename = "b0cf4173-deaa-4289-a81e-2d9be81735c6.apk";
        FileUtils.download(filename,path,response);
    }

    /**
     * 文件上传
     * @param file 文件
     * @throws IOException
     */
    public void fileUpload(MultipartFile file) throws IOException {
        FileUtils.upload(path,file);
    }
}
