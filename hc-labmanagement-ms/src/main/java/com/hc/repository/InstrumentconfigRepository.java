package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.InstrumentconfigDTO;
import com.hc.po.InstrumentconfigPo;


public interface InstrumentconfigRepository extends IService <InstrumentconfigPo>{

    /**
     * 查询探头配置信息
     * @param instrumentconfigid
     * @return
     */
    InstrumentconfigDTO selectInfoByConfigid(Integer instrumentconfigid);
}