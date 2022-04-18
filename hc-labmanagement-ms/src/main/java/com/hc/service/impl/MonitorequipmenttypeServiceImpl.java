package com.hc.service.impl;

import com.hc.dto.MonitorequipmenttypeDTO;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorequipmenttypePo;
import com.hc.repository.MonitorequipmenttypeRepository;
import com.hc.service.MonitorequipmenttypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorequipmenttypeServiceImpl implements MonitorequipmenttypeService {

    @Autowired
    private MonitorequipmenttypeRepository monitorequipmenttypeRepository;

    @Override
    public List<MonitorequipmenttypeDTO> getAllmonitorequipmentType() {
        List<MonitorequipmenttypePo> MonitorequipmenttypeList = monitorequipmenttypeRepository.list();
        return BeanConverter.convert(MonitorequipmenttypeList,MonitorequipmenttypeDTO.class);
    }
}