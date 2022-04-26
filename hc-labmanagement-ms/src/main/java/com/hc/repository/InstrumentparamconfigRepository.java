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

    /**
     * 删除探头参数信息
     * @param instrumentno
     */
    void deleteInfoByEno(String instrumentno);


    List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo);

    /**
     * 更新探头配置信息
     * @param instrumentparamconfigDTO
     */
    void updateInfo(InstrumentparamconfigDTO instrumentparamconfigDTO);
}