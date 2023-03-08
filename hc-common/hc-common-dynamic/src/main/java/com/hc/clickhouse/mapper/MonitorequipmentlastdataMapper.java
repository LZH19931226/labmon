package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.clickhouse.param.CurveParam;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MonitorequipmentlastdataMapper extends RootMapper<Monitorequipmentlastdata> {



    List<MonitorequipmentlastdataDto> getWarningCurveData(@Param("equipmentNo") String equipmentNo,
                                                          @Param("startTime") String startTime,
                                                          @Param("endTime") String endTime,
                                                          @Param("instrumentConfigName") String instrumentConfigName,
                                                          @Param("ym")String ym);

    List<Monitorequipmentlastdata> getEquipmentData(Page page,@Param("param") EquipmentDataParam dataParam);


    List<Monitorequipmentlastdata> getEquipmentPacketData(Page page, @Param("param")EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getPacketLossColumnar(@Param("param")EquipmentDataParam dataParam);


    List<Monitorequipmentlastdata> getLastDataByTime(@Param("param") EquipmentDataParam dataParam);

    List<Monitorequipmentlastdata> getMonitorEquuipmentLastList(@Param("param")CurveParam curveParam);

    List<Monitorequipmentlastdata> getMT310DcLastDataByTime(@Param("param")EquipmentDataParam dataParam);
}
