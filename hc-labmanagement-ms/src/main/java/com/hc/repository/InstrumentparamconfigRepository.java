package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.po.InstrumentparamconfigPo;


public interface InstrumentparamconfigRepository extends IService <InstrumentparamconfigPo>{

    /**
     * 插入探头配置信息
     * @param instrumentparamconfigDTO
     */
    void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO);
}