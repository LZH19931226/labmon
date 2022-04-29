package com.hc.service.impl;

import com.hc.dto.InstrumentconfigDTO;
import com.hc.repository.InstrumentconfigRepository;
import com.hc.service.InstrumentconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentconfigServiceImpl implements InstrumentconfigService {

    @Autowired
    private InstrumentconfigRepository instrumentconfigRepository;
    /**
     * 查询探头配置信息
     *
     * @param instrumentconfigid
     * @return
     */
    @Override
    public InstrumentconfigDTO selectInfoByConfigid(Integer instrumentconfigid) {
        return instrumentconfigRepository.selectInfoByConfigid(instrumentconfigid);
    }

    /**
     * 查询所有的探头配置信息
     *
     * @return
     */
    @Override
    public List<InstrumentconfigDTO> selectAllInfo() {
        return instrumentconfigRepository.selectAllInfo();
    }
}