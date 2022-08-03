package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.infrastructure.dao.InstrumentParamConfigDao;
import com.hc.repository.InstrumentParamConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Repository
public class InstrumentParamConfigRepositoryImpl  extends ServiceImpl<InstrumentParamConfigDao,InstrumentParamConfigDto> implements InstrumentParamConfigRepository {

    @Autowired
    private InstrumentParamConfigDao instrumentParamConfigDao;

    @Override
    public InstrumentParamConfigDto getProbeInfo(String instrumentParamConfigNo) {
        return instrumentParamConfigDao.getProbeInfo(instrumentParamConfigNo);
    }

    public Map<String,List<InstrumentParamConfigDto>> getInstrumentParamConfigByENo(String equipmentNo){
        List<InstrumentParamConfigDto> list = instrumentParamConfigDao.getInstrumentParamConfigByENo(equipmentNo);
        return list.stream().collect(Collectors.groupingBy(InstrumentParamConfigDto::getInstrumentconfigname));
    }

    /**
     * @param eNoList
     * @return
     */
    @Override
    public List<InstrumentParamConfigDto> getInstrumentParamConfigByENoList(List<String> eNoList) {
        return instrumentParamConfigDao.getInstrumentParamConfigByENoList(eNoList);
    }

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<InstrumentParamConfigDto> getInstrumentParamConfigByCode(String hospitalCode) {
        return instrumentParamConfigDao.getInstrumentParamConfigByCode(hospitalCode);
    }
}
