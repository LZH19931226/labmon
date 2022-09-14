package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.InstrumentParamConfigDto;
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

    @Select("select t1.*,t2.instrumenttypeid,t2.sn from monitorequipment t1 left join monitorinstrument t2 on t1.equipmentno = t2.equipmentno where t1.equipmentno = #{equipmentNo} ")
    MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo);

    @Select("<script>" +
            "select " +
            "t1.*," +
            "t2.sn," +
            "t2.instrumenttypeid " +
            "from " +
            "monitorequipment t1 " +
            "left join monitorinstrument t2 on t1.equipmentno = t2.equipmentno " +
            "where t1.clientvisible = '1' and t1.hospitalcode = #{probeCommand.hospitalCode} and t1.equipmenttypeid = #{probeCommand.equipmentTypeId}" +
            "<if test = 'probeCommand.warningSwitch != null and probeCommand.warningSwitch != \"\"'>" +
            "<if test = 'probeCommand.warningSwitch == 0'> " +
            " and (t1.warning_switch = #{probeCommand.warningSwitch}  or  t1.warning_switch IS NULL) " +
            "</if>" +
            "<if test = 'probeCommand.warningSwitch == 1'>" +
            "and t1.warning_switch = #{probeCommand.warningSwitch}" +
            "</if>" +
            "</if>" +
            "<if test = 'probeCommand.equipmentName != null and probeCommand.equipmentName != \"\"'>" +
            " and (t1.equipmentname like concat('%',#{probeCommand.equipmentName},'%')" +
            " or t2.sn like concat('%',#{probeCommand.equipmentName},'%'))"+
            "</if> " +
            " ORDER BY equipmentname " +
            "</script>")
    List<MonitorEquipmentDto> getEquipmentInfoByPage(Page page,@Param("probeCommand")ProbeCommand probeCommand);

    List<MonitorEquipmentDto> batchGetEquipmentInfo(@Param("equipmentNoList") List<String> equipmentNoList);

    List<MonitorEquipmentDto> getAll();

    List<Integer> selectInstrumentConfigIdByENo(String equipmentNo);

    @Select("SELECT\n" +
            "\tt3.instrumentparamconfigNO,\n" +
            "\tt3.warningphone\n" +
            "FROM\n" +
            "\tmonitorequipment t1\n" +
            "\tLEFT JOIN monitorinstrument t2 ON t1.equipmentno = t2.equipmentno\n" +
            "\tLEFT JOIN instrumentparamconfig t3 on t2.instrumentno = t3.instrumentno\n" +
            "\tWHERE t1.hospitalcode = #{hospitalCode} AND t1.equipmenttypeid = #{equipmentTypeId}")
    List<InstrumentParamConfigDto> selectProbeByHosCodeAndEqTypeId(@Param("hospitalCode") String hospitalCode,@Param("equipmentTypeId") String equipmentTypeId);
}
