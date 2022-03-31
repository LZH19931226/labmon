package com.hc.service.serviceimpl;

import com.hc.mapper.ApkFileEntityDao;
import com.hc.mapper.IosFileEntityDao;
import com.hc.entity.ApkFileEntity;
import com.hc.entity.IosFileEntity;
import com.hc.model.ApkCheck;
import com.hc.service.ApkFileEntityService;
import com.hc.utils.ApiResponse;
import com.hc.utils.RuntimeCheck;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

@Service
public class ApkFileEntityServiceImpl implements ApkFileEntityService {

    @Autowired
    private ApkFileEntityDao apkFileEntityDao;

    @Autowired
    private IosFileEntityDao iosFileEntityDao;

    @Value("${Android.baseUrl}")
    private String baseUrl;

    @Value("${Android.ipport}")
    private String ipport;


    @Override
    public ApiResponse<String> uploadFile(MultipartFile file, String info, String forceUpdate, String code, String versionname) {

        ApiResponse<String> apiResponse = new ApiResponse<>();

        String filename = file.getOriginalFilename();


        File basefilepath = new File(baseUrl + "\\" + filename);

        File fileurl = new File(baseUrl);


        //判断文件是否已经存在
        if (!fileurl.exists()) {
            fileurl.mkdir();
        }

        try {
            file.transferTo(basefilepath);
        } catch (IOException e) {
            apiResponse.setMessage("上传失败" + e);
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        ApkFileEntity entity = new ApkFileEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCode(code);
        entity.setFilename(filename);
        entity.setInfo(info);
        entity.setUrl(ipport + filename);
        entity.setDate(new Date());
        entity.setSize(String.valueOf(file.getSize()));
        entity.setVersionname(versionname);
        if (StringUtils.isEmpty(forceUpdate)) {
            entity.setForceUpdate("0");
        }
        entity.setForceUpdate(forceUpdate);
        apkFileEntityDao.save(entity);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<ApkCheck> checkApk(String versionId) {
        ApiResponse<ApkCheck> apiResponse = new ApiResponse<>();
        //获取数据库最新一条的apk信息
        ApkFileEntity entity = apkFileEntityDao.getlastApkFileEntity();
        if (ObjectUtils.isEmpty(entity)) {
            apiResponse.setMessage("未上传最新版本");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }

        ApkCheck apkCheck = new ApkCheck();
        //比对当前版本号和最新版本号
        if (StringUtils.equals(versionId, entity.getCode())) {
            apkCheck.setUpdate("0");
            apiResponse.setResult(apkCheck);
            return apiResponse;
        } else {
            apkCheck.setUpdate("1");
            apkCheck.setFilename(entity.getFilename());
            apkCheck.setForceUpdate(entity.getForceUpdate());
            apkCheck.setNewversion(entity.getCode());
            apkCheck.setUrl(entity.getUrl());
            apkCheck.setUpdateInfo(entity.getInfo());
            apkCheck.setVersionname(entity.getVersionname());
            apkCheck.setSize(entity.getSize());
        }
        apiResponse.setResult(apkCheck);

        return apiResponse;
    }
    @Override
    public ApiResponse<String> downloadFile(HttpServletResponse response, String filename) {


        File file = new File(baseUrl + "\\" + filename);
        if (file.exists()) {
            // 初始化文件流，提供客户端下载

            if (file.exists()) {
                OutputStream outs = null;//获取文件输出IO流
                InputStream ins = null;
                BufferedInputStream bins = null;
                try {
                    outs = response.getOutputStream();
                    ins = new FileInputStream(file);//构造一个读取文件的IO流对象
                    bins = new BufferedInputStream(ins);//放到缓冲流里面
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedOutputStream bouts = new BufferedOutputStream(outs);
                //response.setContentType("application/x-download");//设置response内容的类型 普通下载类型
                response.setContentType("application/vnd.android.package-archive");//设置response内容的类型 下载安卓应用apk
                response.setContentLength((int) file.length());//设置文件大小
                try {
                    response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));//设置头部信息
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                //开始向网络传输文件流
                try {
                    while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
                        bouts.write(buffer, 0, bytesRead);
                    }
                    bouts.flush();//这里一定要调用flush()方法
                    ins.close();
                    bins.close();
                    outs.close();
                    bouts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            } else {
                return ApiResponse.fail("该文件不存在");
            }

            return ApiResponse.success();
        }

    @Override
    public ApiResponse<IosFileEntity> checkIos(String versionId) {

        ApiResponse<IosFileEntity>  fileEntityApiResponse= new ApiResponse<>();

        IosFileEntity iosFileEntity = iosFileEntityDao.getlastIosFileEntity();
        if (StringUtils.equals(versionId,iosFileEntity.getCode())){
            fileEntityApiResponse.setCode(ApiResponse.SUCCESS);
            fileEntityApiResponse.setMessage("当前版本与最新版本一致");
            return  fileEntityApiResponse;
        }else {
            fileEntityApiResponse.setCode(ApiResponse.FAILED);
            fileEntityApiResponse.setResult(iosFileEntity);
            return  fileEntityApiResponse;
        }

    }

    @Override
    public ApiResponse<String> uploadIosVersion(String info,  String code, String versionname, String filename) {

        IosFileEntity iosFileEntity = new IosFileEntity();

        iosFileEntity.setId(UUID.randomUUID().toString());
        iosFileEntity.setDate(new Date());
        RuntimeCheck.ifBlank(info,"更新信息不能为空");
        RuntimeCheck.ifBlank(code,"版本号不能为空");
        RuntimeCheck.ifBlank(filename,"文件名称不能为空");
        RuntimeCheck.ifBlank(versionname,"版本名称不能为空");

        iosFileEntity.setCode(code);
        iosFileEntity.setFilename(filename);
        iosFileEntity.setInfo(info);
        iosFileEntity.setVersionname(versionname);

        iosFileEntityDao.save(iosFileEntity);
        return ApiResponse.success();
    }


}
