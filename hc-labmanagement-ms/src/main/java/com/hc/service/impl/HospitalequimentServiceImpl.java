package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.application.command.WorkTimeBlockCommand;
import com.hc.constants.error.HospitalequimentEnumErrorCode;
import com.hc.dto.HospitalequimentDTO;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.my.common.core.constant.enums.HospitalEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.HospitalequimentPo;
import com.hc.po.MonitorEquipmentPo;
import com.hc.po.MonitorEquipmentWarningTimePo;
import com.hc.repository.HospitalequimentRepository;
import com.hc.repository.MonitorEquipmentRepository;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import com.hc.service.HospitalequimentService;
import org.apache.commons.collections4.CollectionUtils;
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

//        //更新缓存
//        HospitalEquipmentTypeInfoModel hospitalEquipment = hospitalEquipmentMapper
//                .selectEquipmentTypeByHospitalcodeAndEquipmenttypeid(equipmentTypeInfoModel.getHospitalcode(),
//                        equipmentTypeInfoModel.getEquipmenttypeid());
//        if(hospitalEquipment != null){
//            MonitorEquipmentWarningTime monitorEquipmentWarningTime = new MonitorEquipmentWarningTime();
//            monitorEquipmentWarningTime.setEquipmentid(hospitalEquipment.getEquipmenttypeid());
//            monitorEquipmentWarningTime.setEquipmentcategory("TYPE");
//            monitorEquipmentWarningTime.setHospitalcode(hospitalEquipment.getHospitalcode());
//            Example<MonitorEquipmentWarningTime> timeExample = Example.of(monitorEquipmentWarningTime);
//            List<MonitorEquipmentWarningTime> warningTimeDaoAll = monitorEquipmentWarningTimeDao.findAll(timeExample);
//            hospitalEquipment.setWarningTimeList(warningTimeDaoAll);
//            hospitalEquipment.setAlwayalarm(equipmentTypeInfoModel.getAlwayalarm());
//            //同步探头名称到缓存
//            String key = hospitalEquipment.getEquipmenttypeid()+"@"+ hospitalEquipment.getHospitalcode();
//            Object o = redisTemplateUtil.boundHashOps("hospital:equipmenttype")
//                    .get(key);
//            //存在
//            String o1 = (String) o;
//            HospitalEquipmentTypeInfoModel monitorinstrumentObj = JsonUtil.toBean(o1, HospitalEquipmentTypeInfoModel.class);
//            if (monitorinstrumentObj != null){
//                redisTemplateUtil.boundHashOps("hospital:equipmenttype").put(key,JsonUtil.toJson(hospitalEquipment));
//            }
//        }

    }

    @Override
    public List<HospitalequimentDTO> selectHospitalEquimentType(Page page, HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
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
}