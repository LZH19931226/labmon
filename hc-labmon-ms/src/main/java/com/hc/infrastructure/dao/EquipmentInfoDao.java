package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.MonitorinstrumentDto;
import com.hc.vo.labmon.model.MonitorEquipmentLastDataModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface EquipmentInfoDao extends BaseMapper<MonitorEquipmentDto> {

    @Select(" select" +
            " t1.*," +
            " t2.sn " +
            "from monitorequipment t1" +
            "LEFT JOIN monitorinstrument t2 ON t1.equipmentno = t2.equipmentno" +
            " WHERE t1.hospitalcode = #{hospitalCode}" +
            " AND t1.equipmenttypeid = #{equipmentTypeId}" +
            " AND t1.clientvisible = '1'")
    List<MonitorEquipmentDto> getEquipmentInfoByCodeAndTypeId(@Param("hospitalCode") String hospitalCode,@Param("equipmentTypeId") String equipmentTypeId);

    List<MonitorinstrumentDto> getSns(@Param("equipmentNoList") List<String> equipmentNoList);

    @Select("SELECT  c.lowlimit  FROM monitorequipment a LEFT JOIN monitorinstrument b ON a.equipmentno = b.equipmentno left join instrumentparamconfig c on b.instrumentno = c.instrumentno where a.equipmentno = #{equipmentNo} and ifnull(b.instrumentno,'xxx') !='xxx'  and c.instrumentconfigid = 11 limit 1")
    String getLowlimit(@Param("equipmentNo") String equipmentNo);

    List<MonitorinstrumentDto> getLowLimitList(@Param("equipmentNoList") List<String> equipmentNoList);


    List<MonitorEquipmentLastDataModel> getCurveInfo(@Param("date") String date,
                                                     @Param("equipmentNo") String equipmentNo,
                                                     @Param("tableName") String tableName);

    @Select("select * from monitorequipment where equipmentno = #{equipmentNo}")
    MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo);

}
