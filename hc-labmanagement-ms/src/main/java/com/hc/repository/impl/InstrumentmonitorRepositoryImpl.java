package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.InstrumentmonitorDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hc.repository.InstrumentmonitorRepository;
import com.hc.infrastructure.dao.InstrumentmonitorDao;
import com.hc.po.InstrumentmonitorPo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
public class InstrumentmonitorRepositoryImpl extends ServiceImpl<InstrumentmonitorDao,InstrumentmonitorPo> implements InstrumentmonitorRepository  {

    @Autowired
    private InstrumentmonitorDao instrumentmonitorDao;


    @Override
    public List<MonitorinstrumenttypeDTO> selectMonitorEquipmentType(String instrumenttypeid) {
       List<InstrumentmonitorDTO> instrumentmonitorDTOS  = instrumentmonitorDao.selectMonitorEquipmentType(instrumenttypeid);
       if (CollectionUtils.isNotEmpty(instrumentmonitorDTOS)){
           List<MonitorinstrumenttypeDTO> dtoList  = new ArrayList<>();
           Map<Integer, List<InstrumentmonitorDTO>> collect = instrumentmonitorDTOS.stream().collect(Collectors.groupingBy(InstrumentmonitorDTO::getInstrumenttypeid));
           collect.forEach((k,v)->{
               MonitorinstrumenttypeDTO monitorinstrumenttypeDTO = new MonitorinstrumenttypeDTO();
               monitorinstrumenttypeDTO.setInstrumenttypeid(k);
               monitorinstrumenttypeDTO.setInstrumenttypename(v.get(0).getInstrumenttypename());
               monitorinstrumenttypeDTO.setInstrumentmonitorDTOS(v);
               dtoList.add(monitorinstrumenttypeDTO);
            });
           return  dtoList;
       }
       return null;
    }
}