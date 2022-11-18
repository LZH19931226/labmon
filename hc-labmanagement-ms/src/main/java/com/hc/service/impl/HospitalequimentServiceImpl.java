package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.application.command.WorkTimeBlockCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.HospitalequimentPo;
import com.hc.po.MonitorEquipmentPo;
import com.hc.po.MonitorEquipmentWarningTimePo;
import com.hc.repository.HospitalequimentRepository;
import com.hc.repository.MonitorEquipmentRepository;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import com.hc.service.HospitalequimentService;
import com.hc.vo.equimenttype.HospitalequimentVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

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
            throw new IedsException(LabSystemEnum.HOSPITAL_CODE_NOT_NULL);
        }
        String equipmenttypeid = hospitalEquimentTypeCommand.getEquipmenttypeid();
        if(StringUtils.isBlank(equipmenttypeid)){
            throw new IedsException(LabSystemEnum.HOSPITAL_TYPE_ID_NOT_NULL);
        }
        HospitalequimentDTO hospitalequimentDTO1 = hospitalequimentRepository.selectHospitalEquimentInfoByCodeAndTypeId(hospitalcode, equipmenttypeid);
        if (!ObjectUtils.isEmpty(hospitalequimentDTO1)) {
            throw new IedsException(LabSystemEnum.THE_SAME_DEVICE_TYPE_EXISTS);
        }
        WorkTimeBlockCommand[] workTimeBlock = hospitalEquimentTypeCommand.getWorkTimeBlock();
        HospitalequimentDTO hospitalequimentDTO = BeanConverter.convert(hospitalEquimentTypeCommand, HospitalequimentDTO.class);
        hospitalequimentRepository.saveHospitalEquiment(hospitalequimentDTO);
        List<MonitorequipmentwarningtimeDTO> monitorequipmentwarningtimeDTOS  = new ArrayList<>();
        if (null!=workTimeBlock && workTimeBlock.length>0){
            //校验时间
            checkWorkTime(Arrays.asList(workTimeBlock));
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

    /**
     * 检测时间
     * @param singletonList
     */
    private void checkWorkTime(List<WorkTimeBlockCommand> singletonList) {
        List<Date> list = new ArrayList<>();
        for (WorkTimeBlockCommand workTimeBlockCommand : singletonList) {
            Date startTime = workTimeBlockCommand.getBegintime();
            Date endTime = workTimeBlockCommand.getEndtime();
            if(endTime.compareTo(startTime)<=0){
                throw new IedsException(LabSystemEnum.START_TIME_AND_END_TIME_ARE_ABNORMAL);
            }
            list.add(buildTime(startTime));
            list.add(buildTime(endTime));
        }
        if(CollectionUtils.isNotEmpty(list)){
            int size = list.size();
            switch (size){
                case 4:
                    Boolean aBoolean = checkTimesHasOverlap(list.get(0), list.get(1), list.get(2), list.get(3));
                    if(aBoolean){
                        throw new IedsException(LabSystemEnum.THERE_IS_AN_OVERLAP_BETWEEN_THE_TWO_TIME_PERIODS);
                    }
                    break;
                case 6:
                    //有三段时间需要比三次
                    Boolean one = checkTimesHasOverlap(list.get(0), list.get(1), list.get(2), list.get(3));
                    Boolean two = checkTimesHasOverlap(list.get(0), list.get(1), list.get(4), list.get(5));
                    Boolean three = checkTimesHasOverlap(list.get(2), list.get(3), list.get(4), list.get(5));
                    if(one || two || three){
                        throw new IedsException(LabSystemEnum.THERE_IS_AN_OVERLAP_OF_THE_THREE_TIME_PERIODS);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 统一时间
     * @param date
     * @return
     */
    public Date buildTime(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR,1970);
        cal.set(Calendar.MONTH,1);
        cal.set(Calendar.DATE,1);
        return cal.getTime();
    }


    /**
     * 判断两个时间范围是否有交集
     * 1. 比较时间段的结束时间在参考时间段的开始时间之前
     * 2. 比较时间段的开始时间在参考时间段的结束时间之后
     * 取反得到所有的交集
     */
    public static Boolean checkTimesHasOverlap(Date dynaStartTime, Date dynaEndTime, Date fixedStartTime, Date fixedEndTime) {
        return !(dynaEndTime.getTime() < fixedStartTime.getTime() || dynaStartTime.getTime() > fixedEndTime.getTime());
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
            //校验时间
            checkWorkTime(Arrays.asList(workTimeBlock));
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
            List<Integer> list = new ArrayList<>();
            for (WorkTimeBlockCommand workTimeBlockCommand : deleteWarningTimeBlock) {
                if(workTimeBlockCommand!=null){
                    list.add(workTimeBlockCommand.getTimeblockid());
                }
            }
            if(CollectionUtils.isNotEmpty(list)){
                monitorequipmentwarningtimeRepository.removeByIds(list);
            }
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
            throw  new IedsException(LabSystemEnum.DEVICES_EXIST_UNDER_THIS_DEVICE_TYPE);
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
