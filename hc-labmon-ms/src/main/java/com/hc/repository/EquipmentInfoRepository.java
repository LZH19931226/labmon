package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.MonitorinstrumentDto;
import com.hc.vo.labmon.model.MonitorEquipmentLastDataModel;

import java.util.List;

public interface EquipmentInfoRepository extends IService<MonitorEquipmentDto> {

    /**
     * 查询所有设备当前值信息
     * @param hospitalCode 医院id
     * @param equipmentTypeId 设备类型id
     * @return 监控设备集合
     */
    List<MonitorEquipmentDto> getEquipmentInfoByCodeAndTypeId(String hospitalCode, String equipmentTypeId);

    List<MonitorinstrumentDto> getSns(List<String> equipmentNoList);

    String getLowlimit(String equipmentNo);

    List<MonitorinstrumentDto> getLowLimitList(List<String> equipmentNoList);

    /**
     * 获取曲线表信息
     * @param date 医院id
     * @param equipmentNo 设备id
     * @param tableName 查询的表名称
     * @return
     */
    List<MonitorEquipmentLastDataModel> getCurveInfo(String date, String equipmentNo, String tableName);

    /**
     * 查询设备信息
     * @param equipmentNo
     * @return
     */
    MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo);

    List<MonitorEquipmentDto> getEquipmentInfoByHospitalCode(String hospitalCode);

    /**
     * 分页获取设备编号
     * @param page
     * @return
     */
    List<MonitorEquipmentDto> getEquipmentInfoByPage(Page page, ProbeCommand probeCommand);
}
