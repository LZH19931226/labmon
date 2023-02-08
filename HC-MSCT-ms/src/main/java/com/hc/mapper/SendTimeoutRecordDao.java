package com.hc.mapper;


import com.hc.clickhouse.mapper.RootMapper;
import com.hc.po.SendTimeoutRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SendTimeoutRecordDao extends RootMapper<SendTimeoutRecord> {

}
