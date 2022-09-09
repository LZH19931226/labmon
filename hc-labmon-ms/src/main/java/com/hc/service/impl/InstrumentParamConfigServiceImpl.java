package com.hc.service.impl;

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
     * @param instrumentParamConfigNo
     * @param warningPhone
     */
    @Override
    public void updateProbeAlarmState(String instrumentParamConfigNo, String warningPhone) {
        InstrumentParamConfigDto instrumentParamConfigDto = new InstrumentParamConfigDto();
        instrumentParamConfigDto.setInstrumentparamconfigno(instrumentParamConfigNo);
        instrumentParamConfigDto.setWarningphone(warningPhone);
        instrumentParamConfigRepository.updateById(instrumentParamConfigDto);
    }

    /**
     * @param equipmentNo
     * @return
     */
    @Override
    public List<InstrumentParamConfigDto> getInstrumentParamConfigInfo(String equipmentNo) {
        return instrumentParamConfigRepository.getInstrumentParamConfigInfo(equipmentNo);
    }

    /**
     * @param warningPhone
     * @param equipmentNo
     */
    @Override
    public void batchUpdateProbeAlarmState(String warningPhone, String equipmentNo) {
        instrumentParamConfigRepository.batchUpdateProbeAlarmState(warningPhone,equipmentNo);
    }

    /**
     * @return
     */
    @Override
    public List<InstrumentParamConfigDto> getAll() {
        return instrumentParamConfigRepository.list();
    }
}
