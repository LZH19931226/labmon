package com.hc.service;


import com.hc.entity.IosFileEntity;
import com.hc.model.ApkCheck;
import com.hc.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ApkFileEntityService {

     ApiResponse<String> uploadFile(MultipartFile file,String info,String forceUpdate,String code,String versionname);



     ApiResponse<ApkCheck>  checkApk( String versionId);


     ApiResponse<String> downloadFile(HttpServletResponse response, String filename);


     ApiResponse<IosFileEntity>  checkIos(String versionId);


     ApiResponse<String> uploadIosVersion(String info,String code,String versionname,String filename);
}
