package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.AppVersionManageDto;
import com.hc.infrastructure.dao.AppVersionManageDao;
import com.hc.po.AppVersionManagePo;
import com.hc.repository.AppManageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author user
 */
@Repository
public class AppManageRepositoryImpl extends ServiceImpl<AppVersionManageDao, AppVersionManagePo> implements AppManageRepository {

    @Autowired
    private AppVersionManageDao appVersionManageDao;

    /**
     * 分页查找app信息
     *
     * @param appVersionManageDto
     * @return
     */
    @Override
    public List<AppVersionManageDto> listByPage(Page page,AppVersionManageDto appVersionManageDto) {
        return appVersionManageDao.listByPage(page,appVersionManageDto);
    }
}
