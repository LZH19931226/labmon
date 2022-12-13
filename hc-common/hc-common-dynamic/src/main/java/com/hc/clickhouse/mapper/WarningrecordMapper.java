package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Warningrecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WarningrecordMapper extends BaseMapper<Warningrecord> {



    @Select("select * from lab_mon.warningrecord where equipmentno = #{equipmentNo}")
    List<Warningrecord> getWarningRecordInfo(String equipmentNo);

    @Select("select * from lab_mon.warningrecord where hospitalcode = #{hospitalCode} and formatDateTime(inputdatetime ,'%Y-%m-%d %H:%M:%S') BETWEEN #{startTime} and #{endTime}")
    List<Warningrecord> getWarningInfo(@Param("hospitalCode") String hospitalCode,
                                       @Param("startTime") String startTime,
                                       @Param("endTime") String endTime);

    @Select("select * from lab_mon.warningrecord where equipmentno = #{equipmentNo} and formatDateTime(inputdatetime ,'%Y-%m-%d %H:%M:%S') BETWEEN #{startTime} and #{endTime} order by inputdatetime desc")
    List<Warningrecord> getWarningRecordDetailInfo(@Param("equipmentNo") String equipmentNo,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime);

    @Select("select " +
            "hospitalcode, " +
            "equipmentno, " +
            "COUNT(instrumentparamconfigno) num " +
            "FROM " +
            "lab_mon.warningrecord " +
            "WHERE toYYYYMM ( inputdatetime ) IN ( #{time} ) GROUP BY hospitalcode ,equipmentno  order BY  hospitalcode ")
    List<Warningrecord> getWarningInfoByTime(@Param("time") String time);

    List<Warningrecord> getSummaryOfAlarms(@Param("param") EquipmentDataParam convert);
}
