package com.hc.service.impl;

import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.application.command.WorkTimeBlockCommand;
import com.hc.constants.error.HospitalequimentEnumErrorCode;
import com.hc.dto.HospitalequimentDTO;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.HospitalequimentPo;
import com.hc.po.MonitorequipmentwarningtimePo;
import com.hc.repository.HospitalequimentRepository;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import com.hc.service.HospitalequimentService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalequimentServiceImpl implements HospitalequimentService {


    @Autowired
    private HospitalequimentRepository hospitalequimentRepository;

    @Autowired
    private MonitorequipmentwarningtimeRepository monitorequipmentwarningtimeRepository;

    @Override
    public void addHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        String hospitalcode = hospitalEquimentTypeCommand.getHospitalcode();
        String equipmenttypeid = hospitalEquimentTypeCommand.getEquipmenttypeid();
        //判断该医院该设备类型是不是被绑定过
        HospitalequimentDTO hospitalequiment =hospitalequimentRepository.selectHospitalEquiment(hospitalcode,equipmenttypeid);
        if (null!=hospitalequiment){
            throw   new IedsException(HospitalequimentEnumErrorCode.THE_SAME_DEVICE_TYPE_EXISTS.getCode());
        }
        WorkTimeBlockCommand[] workTimeBlock = hospitalEquimentTypeCommand.getWorkTimeBlock();
        HospitalequimentDTO hospitalequimentDTO = BeanConverter.convert(hospitalEquimentTypeCommand, HospitalequimentDTO.class);
        hospitalequimentRepository.saveHospitalEquiment(hospitalequimentDTO);
        List<MonitorequipmentwarningtimeDTO> monitorequipmentwarningtimeDTOS  = new ArrayList<>();
        if (null!=workTimeBlock && workTimeBlock.length>0){
            for (WorkTimeBlockCommand workTimeBlockCommand : workTimeBlock) {
                if (workTimeBlockCommand != null) {
                    MonitorequipmentwarningtimeDTO monitorEquipmentWarningTime = new MonitorequipmentwarningtimeDTO();
                    monitorEquipmentWarningTime.setBegintime(workTimeBlockCommand.getBegintime());
                    monitorEquipmentWarningTime.setEndtime(workTimeBlockCommand.getEndtime());
                    monitorEquipmentWarningTime.setEquipmentcategory("TYPE");
                    monitorEquipmentWarningTime.setHospitalcode(hospitalcode);
                    monitorEquipmentWarningTime.setEquipmentid(equipmenttypeid);
                    monitorequipmentwarningtimeDTOS.add(monitorEquipmentWarningTime);
                }
            }
            if (CollectionUtils.isNotEmpty(monitorequipmentwarningtimeDTOS)){
                monitorequipmentwarningtimeRepository.saveBatch(BeanConverter.convert(monitorequipmentwarningtimeDTOS, MonitorequipmentwarningtimePo.class));
            }
        }


    }

    @Override
    public void updateHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {

    }
}