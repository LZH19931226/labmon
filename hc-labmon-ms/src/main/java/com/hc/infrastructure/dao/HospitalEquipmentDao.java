package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.dto.EquipmentTypeNumDto;
import com.hc.dto.HospitalEquipmentDto;
import com.hc.dto.eqTypeAlarmNumCountDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HospitalEquipmentDao extends BaseMapper<HospitalEquipmentDto> {
    @Select("SELECT " +
            "t2.equipmenttypename, " +
            "t1.* " +
            "FROM " +
            "hospitalequiment t1 " +
            "LEFT JOIN monitorequipmenttype t2 ON t1.equipmenttypeid = t2.equipmenttypeid " +
            "WHERE " +
            "t1.hospitalcode = #{hospitalCode}")
    List<HospitalEquipmentDto> hospitalEquipmentDao(@Param("hospitalCode") String hospitalCode);

    @Select("SELECT het.equipmenttypeid,het.equipmenttypename,hor.hospitalname,he.orderno FROM hospitalequiment he LEFT JOIN monitorequipmenttype het " +
            "ON het.equipmenttypeid = he.equipmenttypeid  LEFT JOIN hospitalofreginfo hor ON hor.hospitalcode = he.hospitalcode  WHERE he.hospitalcode = #{hospitalCode}")
    List<HospitalEquipmentDto> findHospitalEquipmentTypeByCode(String hospitalCode);

    @Select("SELECT " +
            "t1.equipmentno," +
            "t2.equipmenttypeid," +
            "t2.equipmenttypename," +
            "t2.equipmenttypename_us " +
            "FROM " +
            "monitorequipment t1" +
            "LEFT JOIN monitorequipmenttype t2 ON t1.equipmenttypeid = t2.equipmenttypeid " +
            "WHERE " +
            "t1.hospitalcode = #{hospitalCode} ")
    List<eqTypeAlarmNumCountDto> findEquipmentByHosCode(@Param("hospitalCode") String hospitalCode);

    List<EquipmentTypeNumDto> getEquipmentTypeNum(@Param("param") EquipmentDataCommand equipmentDataCommand);
}
