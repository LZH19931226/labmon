package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorEquipmentWarningTimePo;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import com.hc.service.MonitorequipmentwarningtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorequipmentwarningtimeServiceImpl implements MonitorequipmentwarningtimeService {

    @Autowired
    private MonitorequipmentwarningtimeRepository monitorequipmentwarningtimeRepository;

    @Override
    public List<MonitorequipmentwarningtimeDTO> selectWarningtimeByHosCode(List<String> hospitalcodes) {
        List<MonitorEquipmentWarningTimePo> times = monitorequipmentwarningtimeRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentWarningTimePo())
                .eq(MonitorEquipmentWarningTimePo::getEquipmentCategory, "TYPE")
                .in(MonitorEquipmentWarningTimePo::getHospitalCode, hospitalcodes));
        return BeanConverter.convert(times,MonitorequipmentwarningtimeDTO.class);
    }

    /**
     * 差如报警时段
     *
     * @param monitorequipmentwarningtimeDTO
     */
    @Override
    public void insetWarningtimeList(MonitorequipmentwarningtimeDTO monitorequipmentwarningtimeDTO) {
        monitorequipmentwarningtimeRepository.insetWarningtimeList(monitorequipmentwarningtimeDTO);
    }
}