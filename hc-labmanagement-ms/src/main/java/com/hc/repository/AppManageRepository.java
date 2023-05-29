package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.dto.AppVersionManageDto;
import com.hc.po.AppVersionManagePo;

import java.util.List;

/**
 * @author user
 */
public interface AppManageRepository extends IService<AppVersionManagePo> {
    /**
     * 分页查找app信息
     * @param appVersionManageDto
     * @return
     */
    List<AppVersionManageDto> listByPage(Page page,AppVersionManageDto appVersionManageDto);

    AppVersionManageDto getAppNewVersion();

    String getLatest();
}
