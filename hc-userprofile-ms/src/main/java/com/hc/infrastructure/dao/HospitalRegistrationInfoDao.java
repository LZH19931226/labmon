package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.po.HospitalRegistrationInfoPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author user
 */
@Mapper
public interface HospitalRegistrationInfoDao extends BaseMapper<HospitalRegistrationInfoPo> {

    @Select("<script>"+
            "SELECT " +
            "hospitalcode," +
            "hospitalname," +
            "isenable," +
            "hospitalfullname," +
            "alwayalarm AS alwaysAlarm," +
            "begintime" +
            ",endtime," +
            "timeout,update_time AS updateTime," +
            "update_by AS updateBy " +
            "FROM hospitalofreginfo where 1=1 " +
            "<if test = 'hospitalFullName != null'> " +
            "and hospitalfullname like concat('%', #{hospitalFullName}, '%')" +
            "</if>" +
            "<if  test = 'isEnable != null'> and isenable = #{isEnable}"+
            "</if>"+
            "</script>")
    List<HospitalRegistrationInfoDto> selectListByHospital(Page page, @Param(value = "hospitalFullName") String hospitalFullName, @Param(value = "isEnable") String isEnable);
}
