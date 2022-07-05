package com.hc.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.application.command.WorkTimeBlockCommand;
import com.hc.constants.HospitalEnumErrorCode;
import com.hc.constants.error.HospitalequimentEnumErrorCode;
import com.hc.dto.HospitalequimentDTO;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.HospitalequimentPo;
import com.hc.po.MonitorEquipmentPo;
import com.hc.po.MonitorEquipmentWarningTimePo;
import com.hc.repository.HospitalequimentRepository;
import com.hc.repository.MonitorEquipmentRepository;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import com.hc.vo.equimenttype.HospitalequimentVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalequimentServiceImpl implements HospitalequimentService {


    @Autowired
    private HospitalequimentRepository hospitalequimentRepository;

    @Autowired
    private MonitorequipmentwarningtimeRepository monitorequipmentwarningtimeRepository;

    @Autowired
    private MonitorEquipmentRepository monitorequipmentRepository;

    @Override
    public void addHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        String hospitalcode = hospitalEquimentTypeCommand.getHospitalcode();
        if(StringUtils.isBlank(hospitalcode)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_CODE_NOT_NULL.getCode());
        }
        String equipmenttypeid = hospitalEquimentTypeCommand.getEquipmenttypeid();
        if(StringUtils.isBlank(equipmenttypeid)){
            throw new IedsException(HospitalequimentEnumErrorCode.HOSPITAL_TYPE_ID_NOT_NULL.getCode());
        }
        WorkTimeBlockCommand[] workTimeBlock = hospitalEquimentTypeCommand.getWorkTimeBlock();
        HospitalequimentDTO hospitalequimentDTO = BeanConverter.convert(hospitalEquimentTypeCommand, HospitalequimentDTO.class);
        hospitalequimentRepository.saveHospitalEquiment(hospitalequimentDTO);
        List<MonitorequipmentwarningtimeDTO> monitorequipmentwarningtimeDTOS  = new ArrayList<>();
        if (null!=workTimeBlock && workTimeBlock.length>0){
            for (WorkTimeBlockCommand workTimeBlockCommand : workTimeBlock) {
                if (workTimeBlockCommand != null) {
                    MonitorequipmentwarningtimeDTO monitorequipmentwarningtimeDTO = buildMonitorequipmentwarningtimeDTO(workTimeBlockCommand.getBegintime(), workTimeBlockCommand.getEndtime(), hospitalcode, equipmenttypeid);
                    monitorequipmentwarningtimeDTOS.add(monitorequipmentwarningtimeDTO);
                }
            }
            if (CollectionUtils.isNotEmpty(monitorequipmentwarningtimeDTOS)){
                monitorequipmentwarningtimeRepository.saveBatch(BeanConverter.convert(monitorequipmentwarningtimeDTOS, MonitorEquipmentWarningTimePo.class));
            }
        }
    }

    @Override
    public void updateHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        String hospitalcode = hospitalEquimentTypeCommand.getHospitalcode();
        String equipmenttypeid = hospitalEquimentTypeCommand.getEquipmenttypeid();
        WorkTimeBlockCommand[] workTimeBlock = hospitalEquimentTypeCommand.getWorkTimeBlock();
        WorkTimeBlockCommand[] deleteWarningTimeBlock = hospitalEquimentTypeCommand.getDeleteWarningTimeBlock();
        HospitalequimentDTO hospitalequimentDTO = BeanConverter.convert(hospitalEquimentTypeCommand, HospitalequimentDTO.class);
        hospitalequimentRepository.updateHospitalEquiment(hospitalequimentDTO);
        //带时段id的更新,不带时段id的新增,处于移除时段里面的id删除
        if (null!=workTimeBlock && workTimeBlock.length>0){
            List<MonitorequipmentwarningtimeDTO> addMonitorequipmentwarningtimeDTO =  new ArrayList<>();
            List<MonitorequipmentwarningtimeDTO> updateMonitorequipmentwarningtimeDTO =  new ArrayList<>();
            for (WorkTimeBlockCommand workTimeBlockCommand : workTimeBlock) {
                if (workTimeBlockCommand != null) {
                    Integer timeblockid = workTimeBlockCommand.getTimeblockid();
                    MonitorequipmentwarningtimeDTO monitorequipmentwarningtimeDTO = buildMonitorequipmentwarningtimeDTO(
                            workTimeBlockCommand.getBegintime(), workTimeBlockCommand.getEndtime(), hospitalcode, equipmenttypeid);
                    if (null==timeblockid){
                        addMonitorequipmentwarningtimeDTO.add(monitorequipmentwarningtimeDTO);
                    }else {
                        monitorequipmentwarningtimeDTO.setTimeblockid(timeblockid);
                        updateMonitorequipmentwarningtimeDTO.add(monitorequipmentwarningtimeDTO);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(addMonitorequipmentwarningtimeDTO)){
                monitorequipmentwarningtimeRepository.saveBatch(BeanConverter.convert(addMonitorequipmentwarningtimeDTO, MonitorEquipmentWarningTimePo.class));
            }
            if (CollectionUtils.isNotEmpty(updateMonitorequipmentwarningtimeDTO)){
                monitorequipmentwarningtimeRepository.updateBatchById(BeanConverter.convert(updateMonitorequipmentwarningtimeDTO, MonitorEquipmentWarningTimePo.class));
            }
        }
        if (null!=deleteWarningTimeBlock && deleteWarningTimeBlock.length>0){
            List<Integer> deleteTimeBlockIds = Arrays.stream(deleteWarningTimeBlock).map(WorkTimeBlockCommand::getTimeblockid).collect(Collectors.toList());
            monitorequipmentwarningtimeRepository.removeByIds(deleteTimeBlockIds);
        }

    }

    @Override
    public List<HospitalequimentDTO> selectHospitalEquimentType(Page<HospitalequimentVo> page, HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        return  hospitalequimentRepository.selectHospitalEquimentType(page,hospitalEquimentTypeCommand);
    }

    @Override
    public void deleteHospitalEquimentType(String hospitalCode, String equipmenttypeid) {

        //判断医院底下是否还有设备,有设备则不允许删除设备类型
        List<MonitorEquipmentPo> equipment = monitorequipmentRepository.list(Wrappers.lambdaQuery(new MonitorEquipmentPo())
                .eq(MonitorEquipmentPo::getHospitalCode, hospitalCode)
                .eq(MonitorEquipmentPo::getEquipmentTypeId, equipmenttypeid)
                .last("limit 1"));
        if (CollectionUtils.isNotEmpty(equipment)){
            throw  new IedsException(HospitalequimentEnumErrorCode.DEVICES_EXIST_UNDER_THIS_DEVICE_TYPE.getCode());
        }
        hospitalequimentRepository.remove(Wrappers.lambdaQuery(new HospitalequimentPo())
        .eq(HospitalequimentPo::getHospitalcode,hospitalCode)
        .eq(HospitalequimentPo::getEquipmenttypeid,equipmenttypeid));
    }

    public MonitorequipmentwarningtimeDTO  buildMonitorequipmentwarningtimeDTO(Date beginTime,Date endTime,String hospitalcode,String equipmenttypeid){
        MonitorequipmentwarningtimeDTO monitorEquipmentWarningTime = new MonitorequipmentwarningtimeDTO();
        monitorEquipmentWarningTime.setBegintime(beginTime);
        monitorEquipmentWarningTime.setEndtime(endTime);
        monitorEquipmentWarningTime.setEquipmentcategory("TYPE");
        monitorEquipmentWarningTime.setHospitalcode(hospitalcode);
        monitorEquipmentWarningTime.setEquipmentid(equipmenttypeid);
        return monitorEquipmentWarningTime;
    }

    /**
     * 查询医院设备信息
     *
     * @param hospitalCode
     * @param equipmentTypeId
     * @return
     */
    @Override
    public HospitalequimentDTO selectHospitalEquimentInfoByCodeAndTypeId(String hospitalCode, String equipmentTypeId) {
        return hospitalequimentRepository.selectHospitalEquimentInfoByCodeAndTypeId(hospitalCode,equipmentTypeId);
    }

    /**
     * 查询医院的设备信息集合
     *
     * @param hospitalCode
     * @return
     */
    @Override
    public List<HospitalequimentDTO> findHospitalEquipmentTypeByCode(String hospitalCode) {
        return hospitalequimentRepository.findHospitalEquipmentTypeByCode(hospitalCode);
    }

    /**
     * 查询所有的医院设备信息
     * @return
     */
    @Override
    public List<HospitalequimentDTO> getAllHospitalEquipmentTypeInfo() {
        return hospitalequimentRepository.getAllHospitalEquipmentTypeInfo();
    }
}
