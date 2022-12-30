package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.clickhouse.param.CurveParam;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MonitorequipmentlastdataMapper extends RootMapper<Monitorequipmentlastdata> {


    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(@Param("date") String date,
                                                                   @Param("equipmentNo") String equipmentNo,
                                                                   @Param("ym")String ym);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByPeriod(@Param("hospitalCode") String hospitalCode,
                                                                           @Param("startTime") String startTime,
                                                                           @Param("endTime") String endTime ,
                                                                           @Param("month") String month);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByDate(@Param("hospitalCode") String hospitalCode,
                                                                         @Param("startTime") String startTime,
                                                                         @Param("endTime") String endTime ,
                                                                         @Param("date") String date);

    List<Monitorequipmentlastdata> getLastDataByEnoAndMonth(@Param("equipmentNo") String equipmentNo,
                                                            @Param("startTime") String startTime,
                                                            @Param("endTime") String endTime,
                                                            @Param("month") String month,
                                                            @Param("ym") String ym);


    List<MonitorequipmentlastdataDto> getWarningCurveData(@Param("equipmentNo") String equipmentNo,
                                                          @Param("startTime") String startTime,
                                                          @Param("endTime") String endTime,
                                                          @Param("instrumentConfigName") String instrumentConfigName,
                                                          @Param("ym")String ym);

    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo1(@Param("startTime") String startTime,
                                                                    @Param("endTime")String endTime,
                                                                    @Param("equipmentNo") String equipmentNo,
                                                                    @Param("ym") String ym);

    List<Monitorequipmentlastdata> getEquipmentData(Page page,@Param("param") EquipmentDataParam dataParam);


    List<Monitorequipmentlastdata> getEquipmentPacketData(Page page, @Param("param")EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getPacketLossColumnar(@Param("param")EquipmentDataParam dataParam);


    List<Monitorequipmentlastdata> getLastDataByTime(@Param("param") EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getMonitorEquuipmentLastList(@Param("param")CurveParam curveParam);

}
