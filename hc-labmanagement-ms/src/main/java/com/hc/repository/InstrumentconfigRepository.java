package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.InstrumentConfigDTO;
import com.hc.po.InstrumentconfigPo;

import java.util.List;


public interface InstrumentconfigRepository extends IService <InstrumentconfigPo>{

    /**
     * 查询探头配置信息
     * @param instrumentconfigid
     * @return
     */
    InstrumentConfigDTO selectInfoByConfigid(Integer instrumentconfigid);

    /**
     * 查询所有的探头配置信息
     * @return
     */
    List<InstrumentConfigDTO> selectAllInfo();

    /**
     * 分页查询监控参数类型信息
     * @param page
     * @param instrumentConfigName
     * @return
     */
    List<InstrumentConfigDTO> listByPage(Page<InstrumentConfigDTO> page, String instrumentConfigName);
}
