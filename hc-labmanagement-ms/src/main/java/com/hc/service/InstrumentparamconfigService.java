package com.hc.service;


import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;

import java.util.List;

/**
 * 
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface InstrumentparamconfigService{


    /**
     * 插入探头配置信息
     * @param instrumentparamconfigDTO
     */
    void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO);

    /**
     * 仪器配置参数集合
     * @param instrumentNo
     * @return
     */
    List<InstrumentparamconfigDTO> slectInfo(String instrumentNo);

    List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo);
}

