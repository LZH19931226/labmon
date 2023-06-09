package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorEquipmentPo;
import com.hc.repository.MonitorEquipmentRepository;
import com.hc.service.MonitorEquipmentService;
import com.hc.vo.equimenttype.MonitorEquipmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 监控设备服务实现类
 * @author HC
 */
@Service
public class MonitorEquipmentServiceImpl implements MonitorEquipmentService {

    @Autowired
    private MonitorEquipmentRepository monitorEquipmentRepository;

    /**
     * 分页查询监控设备信息
     *
     * @param page                    分页对象
     * @param monitorEquipmentCommand 监控设备参数
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentInfoList(Page<MonitorEquipmentVo> page, MonitorEquipmentCommand monitorEquipmentCommand) {

        return monitorEquipmentRepository.getEquipmentInfoList(page,monitorEquipmentCommand);
    }

    /**
     * 插入监控设备信息
     *
     * @param monitorEquipmentDto
     */
    @Override
    public void insertMonitorEquipment(MonitorEquipmentDto monitorEquipmentDto) {
        monitorEquipmentRepository.insertMonitorEquipment(monitorEquipmentDto);
    }

    /**
     * 更新监控设备信息
     *
     * @param monitorEquipmentDto
     */
    @Override
    public void updateMonitorEquipment(MonitorEquipmentDto monitorEquipmentDto) {
        monitorEquipmentRepository.updateMonitorEquipment(monitorEquipmentDto);
    }

    /**
     * @param monitorEquipmentDto
     * @return
     */
    @Override
    public Integer selectCount(MonitorEquipmentDto monitorEquipmentDto) {
        return monitorEquipmentRepository.selectCount(monitorEquipmentDto);
    }

    /**
     * @param equipmentNo
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> selectMonitorEquipmentInfoByNo(String equipmentNo) {

        return monitorEquipmentRepository.selectMonitorEquipmentInfoByNo(equipmentNo);
    }

    /**
     * 删除设备信息
     *
     * @param equipmentNo
     */
    @Override
    public void deleteMonitorEquipmentInfo(String equipmentNo) {
        monitorEquipmentRepository.deleteMonitorEquipmentInfo(equipmentNo);
    }

    @Override
    public List<MonitorEquipmentDto> getAllMonitorEquipmentInfo() {
        return monitorEquipmentRepository.getAllMonitorEquipmentInfo();
    }

    /**
     * 获取设备信息
     * @param hospitalCode
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentNoList(String hospitalCode,String equipmentTypeId) {
        return monitorEquipmentRepository.getEquipmentNoList(hospitalCode,equipmentTypeId);
    }

    @Override
    public MonitorEquipmentDto selectMonitorEquipmentInfoByEno(String equipmentNo) {
        return monitorEquipmentRepository.selectMonitorEquipmentInfoByEno(equipmentNo);
    }

    @Override
    public List<MonitorEquipmentDto> getMonitorEquipmentInfoByHCode(String hospitalCode) {
        return monitorEquipmentRepository.getMonitorEquipmentInfoByHCode(hospitalCode);
    }

    /**
     * @param sn
     * @return
     */
    @Override
    public Boolean checkSn(String sn) {
        return monitorEquipmentRepository.checkSn(sn);
    }

    @Override
    public void updateEquipmentWarningSwitch(MonitorEquipmentDto monitorEquipmentDto) {
        MonitorEquipmentPo convert = BeanConverter.convert(monitorEquipmentDto, MonitorEquipmentPo.class);
        monitorEquipmentRepository.updateById(convert);
    }

    @Override
    public void updateEquipmentWarningSwitchByHospitalCodeAndEquipmentTypeId(String hospitalCode, String equipmentTypeId,String warningPhone) {
        monitorEquipmentRepository.updateEquipmentWarningSwitchByHospitalCodeAndEquipmentTypeId(hospitalCode,equipmentTypeId,warningPhone);
    }

    @Override
    public List<MonitorEquipmentDto> getMonitorEquipmentList(Page<MonitorEquipmentVo> page, MonitorEquipmentCommand monitorEquipmentCommand) {
        return monitorEquipmentRepository.getMonitorEquipmentList(page,monitorEquipmentCommand);
    }

    @Override
    public List<String> getHosEqTypeEqInfo(String hospitalCode, String equipmentTypeId) {
        return monitorEquipmentRepository.getHosEqTypeEqInfo(hospitalCode,equipmentTypeId);
    }


    @Override
    public List<String> getSns(String equipmentNo) {
        return monitorEquipmentRepository.getSns(equipmentNo);
    }

    @Override
    public MonitorEquipmentDto getMonitorEquipmentById(String equipmentNo) {
        MonitorEquipmentPo one = monitorEquipmentRepository.getOne(Wrappers.lambdaQuery(new MonitorEquipmentPo()).eq(MonitorEquipmentPo::getEquipmentNo, equipmentNo));
        return BeanConverter.convert(one,MonitorEquipmentDto.class);
    }
}
