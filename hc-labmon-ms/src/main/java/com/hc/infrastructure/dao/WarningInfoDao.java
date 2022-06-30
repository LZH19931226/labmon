package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WarningInfoDao extends BaseMapper<WarningRecordDto> {

    @Select("SELECT t1.*,t2.id,t2.info FROM warningrecord t1 LEFT JOIN warningrecordinfo t2 on t1.pkid =t2.warningrecordid " +
            "WHERE t1.hospitalcode = #{hospitalcode} AND t1.isPhone = '1' ORDER BY t1.inputdatetime DESC LIMIT 30")
    List<WarningRecordDto> getWarningRecord(String hospitalCode);
}
