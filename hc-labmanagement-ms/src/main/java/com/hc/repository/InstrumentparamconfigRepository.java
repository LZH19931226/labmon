package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.po.InstrumentparamconfigPo;

import java.util.List;


public interface InstrumentparamconfigRepository extends IService <InstrumentparamconfigPo>{

    /**
     * 插入探头配置信息
     * @param instrumentparamconfigDTO
     */
    void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO);

    /**
     * 获取仪器信息集合
     * @param instrumentNo
     * @return
     */
    List<InstrumentparamconfigDTO> slectinfo(String instrumentNo);

    List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo);
}