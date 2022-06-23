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
}
