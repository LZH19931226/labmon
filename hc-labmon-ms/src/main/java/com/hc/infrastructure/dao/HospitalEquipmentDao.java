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
            "t2.equipmenttypename,t2.equipmenttypename_us as  equipmenttypenameUs" +
            ",t1.* " +
            "FROM " +
            "hospitalequiment t1 " +
            "LEFT JOIN monitorequipmenttype t2 ON t1.equipmenttypeid = t2.equipmenttypeid " +
            "WHERE " +
            "t1.hospitalcode = #{hospitalCode}")
    List<HospitalEquipmentDto> hospitalEquipmentDao(@Param("hospitalCode") String hospitalCode);

    @Select("SELECT " +
            "t1.equipmentno," +
            "t2.equipmenttypeid," +
            "t2.equipmenttypename," +
            "t2.equipmenttypename_us " +
            "FROM " +
            "monitorequipment t1 " +
            "LEFT JOIN monitorequipmenttype t2 ON t1.equipmenttypeid = t2.equipmenttypeid " +
            "WHERE " +
            "t1.clientvisible = '1' " +
            "and  t1.hospitalcode = #{hospitalCode} ")
    List<eqTypeAlarmNumCountDto> findEquipmentByHosCode(@Param("hospitalCode") String hospitalCode);

    List<EquipmentTypeNumDto> getEquipmentTypeNum(@Param("param") EquipmentDataCommand equipmentDataCommand);

    @Select("SELECT " +
            "t2.equipmenttypename,t2.equipmenttypename_us as equipmenttypenameUs" +
            ",t1.*  " +
            "FROM " +
            "monitorequipmenttype t2 " +
            "LEFT JOIN hospitalequiment t1 ON t1.equipmenttypeid = t2.equipmenttypeid  " +
            "AND t1.hospitalcode = #{hospitalCode}")
    List<HospitalEquipmentDto> selectHospitalEquipmentInfoByPc(@Param("hospitalCode")String hospitalCode);
}
