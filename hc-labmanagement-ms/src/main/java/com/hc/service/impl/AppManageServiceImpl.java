package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.AppVersionManageDto;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.AppVersionManagePo;
import com.hc.repository.AppManageRepository;
import com.hc.service.AppManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author user
 */
@Service
public class AppManageServiceImpl implements AppManageService {

    @Autowired
    private AppManageRepository appManageRepository;

    /**
     * @param appVersionManageDto
     */
    @Override
    public void save(AppVersionManageDto appVersionManageDto) {
        String versionCode = appVersionManageDto.getVersionCode();
        int count = appManageRepository.count(Wrappers.lambdaQuery(new AppVersionManagePo()).eq(AppVersionManagePo::getVersionCode, versionCode));
        if(count>0){
            throw new IedsException(LabSystemEnum.VERSION_NUMBER_ALREADY_EXISTS);
        }
        appVersionManageDto.setCreateTime(new Date());
        appVersionManageDto.setUpdateTime(new Date());
        AppVersionManagePo appVersionManagePo = BeanConverter.convert(appVersionManageDto, AppVersionManagePo.class);
        appManageRepository.save(appVersionManagePo);
    }

    /**
     * 分页查找app信息
     *
     * @param appVersionManageDto
     * @return
     */
    @Override
    public List<AppVersionManageDto> list(Page page,AppVersionManageDto appVersionManageDto) {
        return appManageRepository.listByPage(page,appVersionManageDto);
    }

    /**
     * @param id
     */
    @Override
    public void remove(Integer id) {
        appManageRepository.removeById(id);
    }

    @Override
    public AppVersionManageDto getAppNewVersion() {
        return appManageRepository.getAppNewVersion();
    }

    @Override
    public AppVersionManageDto getLatest() {
        return appManageRepository.getLatest();
    }
}
