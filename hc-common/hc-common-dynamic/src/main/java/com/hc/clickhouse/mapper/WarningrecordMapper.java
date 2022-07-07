package com.hc.clickhouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.clickhouse.po.Warningrecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface WarningrecordMapper extends BaseMapper<Warningrecord> {


    @Update("alter table lab_mon.warningrecord update isphone = #{isPhone} WHERE pkid = #{pkid} ")
    void updateIsPhoneInfo(@Param("pkid") String pkid,
                           @Param("isPhone") String isPhone);
}
