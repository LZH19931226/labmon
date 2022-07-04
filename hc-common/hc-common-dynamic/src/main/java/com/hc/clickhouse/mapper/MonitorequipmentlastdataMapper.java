package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MonitorequipmentlastdataMapper extends BaseMapper<Monitorequipmentlastdata> {

    @Select("select * from monitorequipmentlastdata where formatDateTime(inputdatetime,'%Y-%m-%d') = #{date} and equipmentno = #{equipmentNo}  ORDER BY inputdatetime ASC ")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfo(@Param("date") String date,
                                                                   @Param("equipmentNo") String equipmentNo);

    @Select("SELECT * FROM lab_mon.monitorequipmentlastdata WHERE formatDateTime(inputdatetime ,'%Y-%m-%d %H:%M') BETWEEN  #{startTime}  AND  #{endTime} AND equipmentno  = #{equipmentNo}")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastData(@Param("startTime") String startTime,
                                                                   @Param("endTime") String endTime,
                                                                   @Param("equipmentNo") String equipmentNo);
    @Select("SELECT \n" +
            "DISTINCT (*) \n" +
            "FROM \n" +
            "lab_mon.monitorequipmentlastdata t1\n" +
            "inner JOIN\n" +
            "(\t\n" +
            "SELECT \n" +
            "\tMAX(inputdatetime) as time\n" +
            "FROM    \n" +
            "    lab_mon.monitorequipmentlastdata\n" +
            "WHERE \n" +
            "\tformatDateTime(inputdatetime ,'%H:%M')  BETWEEN #{startTime} and #{endTime}\n" +
            "AND \n" +
            "\thospitalcode  = #{hospitalCode}\n" +
            "AND \n" +
            "\tformatDateTime(inputdatetime ,'%Y-%m') = #{month}\n" +
            "GROUP BY \n" +
            "\tformatDateTime(inputdatetime ,'%d'),equipmentno \n" +
            ") t2 ON  t1.inputdatetime  = t2.time\n" +
            "ORDER BY inputdatetime ASC ")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByPeriod(@Param("hospitalCode") String hospitalCode,
                                                                           @Param("startTime") String startTime,
                                                                           @Param("endTime") String endTime ,
                                                                           @Param("month") String month);

    @Select("SELECT\n" +
            "*\n" +
            "FROM\n" +
            "lab_mon.monitorequipmentlastdata\n" +
            "WHERE\n" +
            "hospitalcode = #{hospitalCode} \n" +
            "AND  inputdatetime in \n" +
            "(\n" +
            "SELECT MAX(inputdatetime)  FROM lab_mon.monitorequipmentlastdata where hospitalcode = #{hospitalCode} \n" +
            "AND  formatDateTime(inputdatetime ,'%Y-%m-%d') = #{date}\n" +
            "and formatDateTime(inputdatetime ,'%H:%M:%S') BETWEEN #{startTime} and #{endTime}\n" +
            "GROUP BY equipmentno \n" +
            ")")
    List<Monitorequipmentlastdata> getMonitorEquipmentLastDataInfoByDate(@Param("hospitalCode") String hospitalCode,
                                                                         @Param("startTime") String startTime,
                                                                         @Param("endTime") String endTime ,
                                                                         @Param("date") String date);

    @Insert("<script> " +
            "INSERT INTO lab_mon.monitorequipmentlastdata " +
            "(id, sn, cmdid, hospitalcode, equipmentno, inputdatetime, currenttemperature, currentcarbondioxide, currento2, currentairflow, currentdoorstate, currenthumidity, currentvoc, currentformaldehyde, currentpm25, currentpm10, currentups, currentqc, currentairflow1, " +
            "currenttemperature1, currenttemperature2, currenttemperature3, currenttemperature4, currenttemperature5, currenttemperature6, currenttemperature7, currenttemperature8, currenttemperature9, currenttemperature10, currentlefttemperature, currentrigthtemperature, " +
            "currenttemperaturediff, currentpm5, currentpm05, currentleftcovertemperature, currentleftendtemperature, currentleftairflow, currentrightcovertemperature, currentrightendtemperature, currentrightairflow, currentqcl, currentn2, leftCompartmentHumidity, rightCompartmentHumidity, model, probe1model, probe1data, probe2model, probe2data, probe3model, probe3data, voltage, power, qccurrent)" +
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

    @Select("SELECT \n" +
            "DISTINCT (*) \n" +
            "FROM \n" +
            "lab_mon.monitorequipmentlastdata t1\n" +
            "inner JOIN\n" +
            "(\t\n" +
            "SELECT \n" +
            "\tMAX(inputdatetime) as time\n" +
            "FROM    \n" +
            "    lab_mon.monitorequipmentlastdata\n" +
            "WHERE \n" +
            "\tformatDateTime(inputdatetime ,'%H:%M')  BETWEEN  #{startTime} and  #{endTime} \n" +
            "AND \n" +
            "\tequipmentno  = #{equipmentNo}\n" +
            "AND \n" +
            "\tformatDateTime(inputdatetime ,'%Y-%m') = #{month}\n" +
            "GROUP BY \n" +
            "\tformatDateTime(inputdatetime ,'%Y-%m-%d')\n" +
            ") t2 ON  t1.inputdatetime  = t2.time\n" +
            "WHERE \n" +
            "t1.equipmentno  = #{equipmentNo}\n" +
            "ORDER BY inputdatetime ASC ")
    List<Monitorequipmentlastdata> getLastDataByEnoAndMonth(@Param("equipmentNo") String equipmentNo,
                                                            @Param("startTime") String startTime,
                                                            @Param("endTime") String endTime,
                                                            @Param("month") String month);
}
