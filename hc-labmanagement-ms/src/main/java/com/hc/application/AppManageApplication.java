package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AppManageCommand;
import com.hc.dto.AppVersionManageDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.FileUtils;
import com.hc.service.AppManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author user
 */
@Component
public class AppManageApplication {

    @Value("${file.path}")
    private String path;

    @Autowired
    private AppManageService appManageService;

    /**
     * 上传并保存文件信息
     * @param appManageCommand
     */
    public void save(AppManageCommand appManageCommand,MultipartFile multipartFile) throws IOException {
        AppVersionManageDto appVersionManageDto = BeanConverter.convert(appManageCommand, AppVersionManageDto.class);
        String originalFilename = multipartFile.getOriginalFilename();
        appVersionManageDto.setApkUrl(path+"/"+originalFilename);
        appVersionManageDto.setAppName(originalFilename);
        FileUtils.upload(path,multipartFile);
        appManageService.save(appVersionManageDto);
    }

    /**
     * 分页查询app信息
     * @param appManageCommand
     * @return
     */
    public Page<AppVersionManageDto> list(AppManageCommand appManageCommand) {
        Page<AppVersionManageDto> page = new Page(appManageCommand.getPageCurrent(), appManageCommand.getPageSize());
        AppVersionManageDto appVersionManageDto = BeanConverter.convert(appManageCommand, AppVersionManageDto.class);
        List<AppVersionManageDto> list =  appManageService.list(page,appVersionManageDto);
        page.setRecords(list);
        return page;
    }

    /**
     * 下载文件
     * @param appName
     * @param httpServletResponse
     */
    public void dowload(String appName, HttpServletResponse httpServletResponse) {
        FileUtils.download(appName,path,httpServletResponse);
    }

    /**
     * 删除app信息
     * @param id
     * @param appName
     */
    public void remove(Integer id,String appName) {
        appManageService.remove(id);
        FileUtils.deleteFile(path,appName);
    }

    public AppVersionManageDto getAppNewVersion() {
        return appManageService.getAppNewVersion();
    }
}
