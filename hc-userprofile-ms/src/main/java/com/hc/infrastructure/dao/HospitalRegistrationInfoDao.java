package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.po.HospitalEquipmentPo;
import com.hc.po.HospitalRegistrationInfoPo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author user
 */
public interface HospitalRegistrationInfoDao extends BaseMapper<HospitalRegistrationInfoPo> {

    /**
     * 查询医院信息列表
     * @param page  分页对象
     * @param hospitalName 医院编码
     * @param isEnable 是否启用
     * @return 医院信息传输对象集合
     */
    @Select("<script>"+
            "SELECT " +
            "hospitalcode hospitalCode," +
            "hospitalname hospitalName," +
            "isenable  isEnable," +
            "hospitalfullname hospitalFullName," +
            "alwayalarm AS alwaysAlarm," +
            "begintime beginTime," +
            "endtime endTime," +
            "timeout,update_time AS updateTime," +
            "timeInterval," +
            "update_by AS updateBy " +
            "FROM hospitalofreginfo where 1=1 " +
            "<if test = 'hospitalName != null  and hospitalName != \"\"'> " +
            "and hospitalname like concat('%', #{hospitalName},'%')" +
            "</if> " +
            "<if  test = 'isEnable != null and isEnable != \"\" '> " +
            "and isenable = #{isEnable}"+
            "</if>" +
            "</script>")
    List<HospitalRegistrationInfoDto> selectListByHospital(Page page,
                                                           @Param(value = "hospitalName") String hospitalName,
                                                           @Param(value = "isEnable") String isEnable);


    /**
     * 获取有无重复的医院名称
     * @param hospitalName 医院名称
     * @param hospitalCode 医院编码
     * @return 医院设备对象
     */
    @Select("select * from hospitalofreginfo where hospitalname = #{hospitalName}  and hospitalcode !=#{hospitalCode}")
    HospitalEquipmentPo selectHospitalName(@Param("hospitalName") String hospitalName, @Param("hospitalCode") String hospitalCode);
}
