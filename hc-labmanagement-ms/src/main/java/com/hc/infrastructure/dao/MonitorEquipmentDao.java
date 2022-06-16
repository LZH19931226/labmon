package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.po.MonitorEquipmentPo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 监控设备
 * @author hc
 */
public interface MonitorEquipmentDao extends BaseMapper<MonitorEquipmentPo> {
    /**
     * 分页查询设信息
     * @param page 分页对象
     * @param hospitalCode 医院编码
     * @param equipmentTypeId 设备类型id
     * @param equipmentName 设备名称
     * @param clientVisible 是否启用
     * @return
     */
    List<MonitorEquipmentDto> getEquipmentInfoList(Page page, @Param("hospitalCode") String hospitalCode,
                                                   @Param("equipmentTypeId") String equipmentTypeId,
                                                   @Param("equipmentName") String equipmentName,
                                                   @Param("clientVisible") Long clientVisible);

    /**
     * 获取所有的设备信息
     * @return
     */
    List<MonitorEquipmentDto> getAllMonitorEquipmentInfo();

    /**
     * 获取设备信息
     * @param hospitalCode
     * @return
     */
    @Select("select * FROM monitorequipment WHERE hospitalcode = #{hospitalCode} AND clientvisible = '1' AND equipmenttypeid = '6'")
    List<MonitorEquipmentDto> getEquipmentNoList(String hospitalCode);

}
