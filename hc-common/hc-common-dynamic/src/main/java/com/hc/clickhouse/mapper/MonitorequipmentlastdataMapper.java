package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Select("select * from monitorequipmentlastdata where toYYYYMM(inputdatetime) in (#{ym}) and  formatDateTime(inputdatetime,'%Y-%m-%d') = #{date} and equipmentno = #{equipmentNo}  ORDER BY inputdatetime ASC ")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(@Param("date") String date,
                                                                   @Param("equipmentNo") String equipmentNo,
                                                                   @Param("ym")String ym);
    @Select("SELECT DISTINCT (*) FROM lab_mon.monitorequipmentlastdata" +
            " where " +
            " inputdatetime in ( " +
            " SELECT" +
            " MAX( inputdatetime ) AS time " +
            " FROM" +
            " lab_mon.monitorequipmentlastdata " +
            " WHERE" +
            " toYYYYMM ( inputdatetime ) IN ( #{month} )" +
            " AND formatDateTime ( inputdatetime, '%H:%M' ) BETWEEN #{startTime} AND #{endTime} " +
            " AND hospitalcode = #{hospitalCode} " +
            " GROUP BY" +
            " formatDateTime ( inputdatetime, '%d' )," +
            " equipmentno " +
            " )" +
            " ORDER BY inputdatetime")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByPeriod(@Param("hospitalCode") String hospitalCode,
                                                                           @Param("startTime") String startTime,
                                                                           @Param("endTime") String endTime ,
                                                                           @Param("month") String month);

    @Select(" SELECT " +
            " * " +
            " FROM" +
            " lab_mon.monitorequipmentlastdata" +
            " WHERE" +
            " hospitalcode = #{hospitalCode} " +
            " AND  inputdatetime in " +
            " (" +
            " SELECT MAX(inputdatetime)  FROM lab_mon.monitorequipmentlastdata where hospitalcode = #{hospitalCode} " +
            " AND  formatDateTime(inputdatetime ,'%Y-%m-%d') = #{date}" +
            " and formatDateTime(inputdatetime ,'%H:%M:%S') BETWEEN #{startTime} and #{endTime}" +
            " GROUP BY equipmentno " +
            " ) ORDER BY inputdatetime ASC")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByDate(@Param("hospitalCode") String hospitalCode,
                                                                         @Param("startTime") String startTime,
                                                                         @Param("endTime") String endTime ,
                                                                         @Param("date") String date);

    @Insert("<script> " +
            "INSERT INTO lab_mon.monitorequipmentlastdata " +
            "(id, " +
            "sn, " +
            "cmdid, " +
            "hospitalcode, " +
            "equipmentno, " +
            "inputdatetime, " +
            "currenttemperature, " +
            "currentcarbondioxide, " +
            "currento2, " +
            "currentairflow, " +
            "currentdoorstate, " +
            "currenthumidity, " +
            "currentvoc, " +
            "currentformaldehyde, " +
            "currentpm25, " +
            "currentpm10, " +
            "currentups, " +
            "currentqc, " +
            "currentairflow1, " +
            "currenttemperature1, " +
            "currenttemperature2, " +
            "currenttemperature3, " +
            "currenttemperature4, " +
            "currenttemperature5, " +
            "currenttemperature6, " +
            "currenttemperature7, " +
            "currenttemperature8, " +
            "currenttemperature9, " +
            "currenttemperature10, " +
            "currentlefttemperature, " +
            "currentrigthtemperature, " +
            "currenttemperaturediff, " +
            "currentpm5, " +
            "currentpm05, " +
            "currentleftcovertemperature, " +
            "currentleftendtemperature, " +
            "currentleftairflow, " +
            "currentrightcovertemperature, " +
            "currentrightendtemperature, " +
            "currentrightairflow, " +
            "currentqcl, " +
            "currentn2, " +
            "leftCompartmentHumidity, " +
            "rightCompartmentHumidity, " +
            "model, " +
            "probe1model, " +
            "probe1data, " +
            "probe2model, " +
            "probe2data, " +
            "probe3model, " +
            "probe3data, " +
            "voltage, " +
            "power, " +
            "qccurrent)" +
            "VALUES " +
            "<foreach collection = 'converts' item = 'convert'  separator=','> " +
            "('0'" +
            ",#{convert.sn}" +
            ",#{convert.cmdid}" +
            ",#{convert.hospitalcode}" +
            ",#{convert.equipmentno}" +
            ",#{convert.inputdatetime}" +
            ",#{convert.currenttemperature}" +
            ",#{convert.currentcarbondioxide}" +
            ",#{convert.currento2}" +
            ",#{convert.currentairflow}" +
            ",#{convert.currentdoorstate}" +
            ",#{convert.currenthumidity}" +
            ",#{convert.currentvoc}" +
            ",#{convert.currentformaldehyde}" +
            ",#{convert.currentpm25}" +
            ",#{convert.currentpm10}" +
            ",#{convert.currentups}" +
            ",#{convert.currentqc}" +
            ",#{convert.currentairflow1}" +
            ",#{convert.currenttemperature1}" +
            ",#{convert.currenttemperature2}" +
            ",#{convert.currenttemperature3}" +
            ",#{convert.currenttemperature4}" +
            ",#{convert.currenttemperature5}" +
            ",#{convert.currenttemperature6}" +
            ",#{convert.currenttemperature7}" +
            ",#{convert.currenttemperature8}" +
            ",#{convert.currenttemperature9}" +
            ",#{convert.currenttemperature10}" +
            ",#{convert.currentlefttemperature}" +
            ",#{convert.currentrigthtemperature}" +
            ",#{convert.currenttemperaturediff}" +
            ",#{convert.currentpm5}" +
            ",#{convert.currentpm05}" +
            ",#{convert.currentleftcovertemperature}" +
            ",#{convert.currentleftendtemperature}" +
            ",#{convert.currentleftairflow}" +
            ",#{convert.currentrightcovertemperature}" +
            ",#{convert.currentrightendtemperature}" +
            ",#{convert.currentrightairflow}" +
            ",#{convert.currentqcl}" +
            ",#{convert.currentn2}" +
            ",#{convert.leftCompartmentHumidity}" +
            ",#{convert.rightCompartmentHumidity}" +
            ",#{convert.model}" +
            ",#{convert.probe1model}" +
            ",#{convert.probe1data}" +
            ",#{convert.probe2model}" +
            ",#{convert.probe2data}" +
            ",#{convert.probe3model}" +
            ",#{convert.probe3data}" +
            ",#{convert.voltage}" +
            ",#{convert.power}" +
            ",#{convert.qccurrent})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("converts") List<Monitorequipmentlastdata> converts);

    @Select(" SELECT" +
            " * " +
            " FROM " +
            " lab_mon.monitorequipmentlastdata " +
            " WHERE " +
            " toYYYYMM(inputdatetime) in (#{ym})" +
            " and formatDateTime ( inputdatetime, '%H:%M' ) BETWEEN #{startTime} AND #{endTime} " +
            " AND equipmentno = #{equipmentNo} " +
            "AND formatDateTime ( inputdatetime, '%Y-%m' ) = #{month}")
    List<Monitorequipmentlastdata> getLastDataByEnoAndMonth(@Param("equipmentNo") String equipmentNo,
                                                            @Param("startTime") String startTime,
                                                            @Param("endTime") String endTime,
                                                            @Param("month") String month,
                                                            @Param("ym") String ym);


    @Select("SELECT  inputdatetime,${instrumentConfigName}  FROM lab_mon.monitorequipmentlastdata WHERE toYYYYMM(inputdatetime) in (#{ym}) and equipmentno  = #{equipmentNo}  AND  formatDateTime(inputdatetime,'%Y-%m-%d %H:%M:%S') BETWEEN #{startTime} AND #{endTime} ORDER BY inputdatetime ASC" )
    List<MonitorequipmentlastdataDto> getWarningCurveData(@Param("equipmentNo") String equipmentNo,
                                                          @Param("startTime") String startTime,
                                                          @Param("endTime") String endTime,
                                                          @Param("instrumentConfigName") String instrumentConfigName,
                                                          @Param("ym")String ym);

    @Select("SELECT * FROM lab_mon.monitorequipmentlastdata WHERE  toYYYYMM(inputdatetime) in (${ym}) and formatDateTime(inputdatetime ,'%Y-%m-%d %H:%M')BETWEEN  #{startTime}  AND #{endTime} AND equipmentno  = #{equipmentNo} ORDER BY inputdatetime ASC")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo1(@Param("startTime") String startTime,
                                                                    @Param("endTime")String endTime,
                                                                    @Param("equipmentNo") String equipmentNo,
                                                                    @Param("ym") String ym);

    List<Monitorequipmentlastdata> getEquipmentData(Page page,@Param("param") EquipmentDataParam dataParam);
}
