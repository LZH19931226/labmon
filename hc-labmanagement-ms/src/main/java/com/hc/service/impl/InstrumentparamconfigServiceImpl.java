package com.hc.service.impl;

import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.repository.InstrumentparamconfigRepository;
import com.hc.service.InstrumentparamconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentparamconfigServiceImpl implements InstrumentparamconfigService {

    @Autowired
    private InstrumentparamconfigRepository instrumentparamconfigRepository;
    /**
     * 插入探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        instrumentparamconfigRepository.insertInstrumentmonitor(instrumentparamconfigDTO);
    }

    /**
     * 仪器配置参数集合
     *
     * @param instrumentNo
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> slectInfo(String instrumentNo) {
        return instrumentparamconfigRepository.slectinfo(instrumentNo);
    }

    /**
     * 删除探头参数信息
     *
     * @param instrumentno
     */
    @Override
    public void deleteInfoByEno(String instrumentno) {
        instrumentparamconfigRepository.deleteInfoByEno(instrumentno);
    }

    @Override
    public List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo) {
        return instrumentparamconfigRepository.selectInstrumentparamconfigByEqNo(equipmentNo);
    }

    /**
     * 更新探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void updateInfo(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        instrumentparamconfigRepository.updateInfo(instrumentparamconfigDTO);
    }
}