package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.MonitorinstrumentDto;
import com.hc.repository.EquipmentInfoRepository;
import com.hc.service.EquipmentInfoService;
import com.hc.vo.labmon.model.MonitorEquipmentLastDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentInfoServiceImpl implements EquipmentInfoService {

    @Autowired
    private EquipmentInfoRepository equipmentInfoRepository;

    /**
     * 查询所有设备当前值信息
     *
     * @param hospitalCode    医院id
     * @param equipmentTypeId 设备类型id
     * @return 监控设备集合
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentInfoByCodeAndTypeId(String hospitalCode, String equipmentTypeId) {
        return equipmentInfoRepository.getEquipmentInfoByCodeAndTypeId(hospitalCode,equipmentTypeId);
    }

    @Override
    public List<MonitorinstrumentDto> getSns(List<String> equipmentNoList) {
        return equipmentInfoRepository.getSns(equipmentNoList);
    }

    @Override
    public String getLowlimit(String equipmentNo) {
        return equipmentInfoRepository.getLowlimit(equipmentNo);
    }

    @Override
    public List<MonitorinstrumentDto> getLowLimitList(List<String> equipmentNoList) {
        return equipmentInfoRepository.getLowLimitList(equipmentNoList);
    }

    /**
     * 获取曲线表信息
     *
     * @param date 医院id
     * @param equipmentNo  设备id
     * @param tableName    查询的表名称
     * @return
     */
    @Override
    public List<MonitorEquipmentLastDataModel> getCurveInfo(String date, String equipmentNo, String tableName) {
        return equipmentInfoRepository.getCurveInfo(date,equipmentNo,tableName);
    }

    /**
     * 查询设备信息
     *
     * @param equipmentNo
     * @return
     */
    @Override
    public MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo) {
        return equipmentInfoRepository.getEquipmentInfoByNo(equipmentNo);
    }

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentInfoByHospitalCode(String hospitalCode) {
        return equipmentInfoRepository.getEquipmentInfoByHospitalCode(hospitalCode);
    }

    /**
     * @param result
     */
    @Override
    public void update(List<MonitorEquipmentDto> result) {
        equipmentInfoRepository.updateBatchById(result);
    }

    /**
     * 分页获取设备id
     * @param page
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentInfoByPage(Page page, ProbeCommand probeCommand) {
        return equipmentInfoRepository.getEquipmentInfoByPage(page,probeCommand);
    }

    /**
     * @param equipmentNoList
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> batchGetEquipmentInfo(List<String> equipmentNoList) {
        return equipmentInfoRepository.batchGetEquipmentInfo(equipmentNoList);
    }

    /**
     * @param monitorEquipmentDto
     */
    @Override
    public void updateEquipmentWarningSwitch(MonitorEquipmentDto monitorEquipmentDto) {
        equipmentInfoRepository.updateById(monitorEquipmentDto);
    }

    /**
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getAll() {
        return equipmentInfoRepository.getAll();
    }

    /**
     * @param list
     */
    @Override
    public void bulkUpdate(List<MonitorEquipmentDto> list) {
        equipmentInfoRepository.updateBatchById(list);
    }

    /**
     * 查询所有的设备配置ID
     *
     * @param equipmentNo
     * @return
     */
    @Override
    public List<Integer> selectInstrumentConfigId(String equipmentNo) {
        return equipmentInfoRepository.selectInstrumentConfigId(equipmentNo);
    }

    @Override
    public List<InstrumentParamConfigDto> selectProbeByHosCodeAndEqTypeId(String hospitalCode, String equipmentTypeId) {
        return equipmentInfoRepository.selectProbeByHosCodeAndEqTypeId(hospitalCode,equipmentTypeId);
    }
}
