package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.UserSchedulingDto;
import com.hc.po.UserSchedulingPo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author hc
 */
@Mapper
public interface UserSchedulingDao extends BaseMapper<UserSchedulingPo> {
    /**
     * 删除人员排班信息
     * @param hospitalCode 医院编码
     * @param startTime 开始时间
     */
    @Delete("DELETE FROM userscheduling WHERE hospitalcode=#{hospitalCode} and starttime >= #{startTime} and starttime < #{endTime}")
    void deleteInfo(@Param("hospitalCode") String hospitalCode,@Param("startTime") Date startTime,@Param("endTime")Date endTime);

    /**
     * 获取当月的排班信息
     * @param hospitalCode 医院编码
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return
     */
    @Select("<script>" +
            "select " +
            "usid usid " +
            ",createtime createTime " +
            ",createuser createUser " +
            ",endtime endTime" +
            ",hospitalcode hospitalCode " +
            ",reminders reminders " +
            ",starttime startTime" +
            ",userid userid" +
            ",username username " +
            ",userphone userPhone " +
            "from userscheduling where hospitalcode = #{hospitalCode} "+
            "<if test = 'startMonth.length() lt 8 and endMonth.length() lt 8'> "+
            "and  DATE_FORMAT(starttime,'%Y-%m') &gt;= #{startMonth}" +
            "and  DATE_FORMAT(endtime,'%Y-%m') &lt;= #{endMonth}" +
            "</if> " +
            "<if test = 'startMonth.length() gt 8 and endMonth.length() gt 8'> " +
            "and  DATE_FORMAT(starttime,'%Y-%m-%d') = #{startMonth} " +
            "and  str_to_date(#{endMonth},'%Y-%m-%d') &lt;= endtime " +
            "</if> " +
            "</script>")
    List<UserSchedulingDto> searchScByHosMon(@Param("hospitalCode") String hospitalCode,@Param("startMonth") String startMonth,@Param("endMonth") String endMonth);

    /**
     *更具医院编码查询用户排班信息
     * @param hospitalCode
     * @return
     */
    @Select("SELECT " +
            "username username, " +
            "reminders reminders, " +
            "userphone userPhone, " +
            "MIN( starttime ) starttime, " +
            "MAX( endtime ) endtime  " +
            "FROM " +
            "userscheduling " +
            "WHERE " +
            "hospitalcode = #{hospitalCode} " +
            "GROUP BY " +
            " username," +
            " reminders, " +
            " userphone")
    List<UserSchedulingDto> selectScheduleWeekByCode(String hospitalCode);
}
