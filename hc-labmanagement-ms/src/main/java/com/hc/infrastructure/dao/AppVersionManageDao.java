package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.AppVersionManageDto;
import com.hc.po.AppVersionManagePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 */
@Mapper
public interface AppVersionManageDao extends BaseMapper<AppVersionManagePo> {

    /**
     *
     * @param page
     * @param appVersionManageDto
     * @return
     */
    List<AppVersionManageDto> listByPage(Page page,@Param("appVersionManageDto") AppVersionManageDto appVersionManageDto);

    String getLatest();
}
