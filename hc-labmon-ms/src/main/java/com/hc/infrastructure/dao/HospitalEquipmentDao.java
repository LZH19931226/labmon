package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.HospitalEquipmentDto;
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


}
