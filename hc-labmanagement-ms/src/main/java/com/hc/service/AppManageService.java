package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.AppVersionManageDto;

import java.util.List;

/**
 * @author hc
 */
public interface AppManageService {
    void save(AppVersionManageDto appVersionManageDto);

    /**
     * 分页查找app信息
     * @param appVersionManageDto
     * @return
     */
    List<AppVersionManageDto> list(Page page, AppVersionManageDto appVersionManageDto);

    void remove(Integer id);

    AppVersionManageDto getAppNewVersion();

    String getLatest();

}
