package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.repository.InstrumentParamConfigRepository;
import com.hc.service.InstrumentParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InstrumentParamConfigServiceImpl  implements InstrumentParamConfigService {

    @Autowired
    private InstrumentParamConfigRepository instrumentParamConfigRepository;

    @Override
    public Map<String, List<InstrumentParamConfigDto>> getInstrumentParamConfigByENo(String equipmentNo) {
        return instrumentParamConfigRepository.getInstrumentParamConfigByENo(equipmentNo);
    }

    /**
     * @param eNoList
     * @return
     */
    @Override
    public List<InstrumentParamConfigDto> getInstrumentParamConfigByENoList(List<String> eNoList) {
        return instrumentParamConfigRepository.getInstrumentParamConfigByENoList(eNoList);
    }

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<InstrumentParamConfigDto> getInstrumentParamConfigByCode(String hospitalCode) {
        return instrumentParamConfigRepository.getInstrumentParamConfigByCode(hospitalCode);
    }

    /**
     * @param configParamNo
     * @return
     */
    @Override
    public List<InstrumentParamConfigDto> batchGetProbeInfo(List<String> configParamNo) {
        return instrumentParamConfigRepository.batchGetProbeInfo(configParamNo);
    }

    /**
     * @return
     */
    @Override
    public List<InstrumentParamConfigDto> getAll() {
        return instrumentParamConfigRepository.list();
    }

    /**
     * @param hospitalCode
     * @param equipmentTypeId
     * @return
     */
    @Override
    public List<InstrumentParamConfigDto> getInstrumentParamConfigByCodeAndTypeId(String hospitalCode, String equipmentTypeId) {
        return instrumentParamConfigRepository.getInstrumentParamConfigByCodeAndTypeId(hospitalCode,equipmentTypeId);
    }

    @Override
    public List<InstrumentParamConfigDto> getInstrumentParamConfigByINo(List<String> iNos) {
        return instrumentParamConfigRepository.list(Wrappers.lambdaQuery(new InstrumentParamConfigDto()).in(InstrumentParamConfigDto::getInstrumentno,iNos));
    }
}
