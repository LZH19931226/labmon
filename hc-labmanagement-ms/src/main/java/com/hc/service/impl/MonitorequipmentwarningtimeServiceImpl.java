package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorequipmentwarningtimePo;
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
        List<MonitorequipmentwarningtimePo> times = monitorequipmentwarningtimeRepository.list(Wrappers.lambdaQuery(new MonitorequipmentwarningtimePo())
                .eq(MonitorequipmentwarningtimePo::getEquipmentcategory, "TYPE")
                .in(MonitorequipmentwarningtimePo::getHospitalcode, hospitalcodes));
        return BeanConverter.convert(times,MonitorequipmentwarningtimeDTO.class);
    }
}