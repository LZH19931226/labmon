package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.po.Warningrecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WarningrecordMapper extends RootMapper<Warningrecord> {



    @Select("select * from lab_mon.warningrecord where equipmentno = #{equipmentNo}")
    List<Warningrecord> getWarningRecordInfo(String equipmentNo);

    List<Warningrecord> getWarningInfoList(@Param("hospitalCode") String hospitalCode,
                                           @Param("ymd") String ymd);

    List<Warningrecord> getWarningEquuipmentInfos(@Param("hospitalCode") String hospitalCode,
                                       @Param("startTime") String startTime,
                                       @Param("endTime") String endTime);


    List<Warningrecord> getWarningRecordDetailInfo(Page page,
            @Param("equipmentNo") String equipmentNo,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime);


    List<Warningrecord> getWarningInfoByTime(@Param("time") String time);

    List<Warningrecord> getSummaryOfAlarms(@Param("param") EquipmentDataParam convert);

    List<Warningrecord> getWarningEquuipmentCounts(@Param("hospitalCode") String hospitalCode,@Param("count") Integer count);

    List<Warningrecord> getAlarmDeviceNum(@Param("param") EquipmentDataParam convert);
}
