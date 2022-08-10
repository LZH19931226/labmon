package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.clickhouse.po.Warningrecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WarningrecordMapper extends BaseMapper<Warningrecord> {



    @Select("select * from lab_mon.warningrecord where equipmentno = #{equipmentNo}")
    List<Warningrecord> getWarningRecordInfo(String equipmentNo);

    @Select("select * from lab_mon.warningrecord where hospitalcode = #{hospitalCode} and formatDateTime(inputdatetime ,'%Y-%m-%d %H:%M:%S') BETWEEN #{startTime} and #{endTime}")
    List<Warningrecord> getWarningInfo(@Param("hospitalCode") String hospitalCode,@Param("startTime") String startTime,@Param("endTime") String endTime);
}
