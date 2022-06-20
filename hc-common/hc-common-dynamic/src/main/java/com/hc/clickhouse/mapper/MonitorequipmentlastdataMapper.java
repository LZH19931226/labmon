package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MonitorequipmentlastdataMapper extends BaseMapper<Monitorequipmentlastdata> {

    @Select("select * from monitorequipmentlastdata where formatDateTime(inputdatetime,'%Y-%m-%d') = #{date} and equipmentno = #{equipmentNo}")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(@Param("date") String date,
                                                                   @Param("equipmentNo") String equipmentNo);

    @Select("SELECT * FROM lab_mon.monitorequipmentlastdata WHERE formatDateTime(inputdatetime ,'%Y-%m-%d %H:%M') BETWEEN  #{startTime}  AND  #{endTime} AND equipmentno  = #{equipmentNo}")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastData(@Param("startTime") String startTime,
                                                                   @Param("endTime") String endTime,
                                                                   @Param("equipmentNo") String equipmentNo);
}
