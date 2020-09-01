package com.hc.mapper.laboratoryFrom;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.springframework.stereotype.Component;

/**
 * @author LiuZhiHao
 * @date 2020/8/31 17:53
 * 描述:
 **/
@Mapper
@Component
public interface MonitorequipmentlastdataMapper {

    @Select(value = "alter table monitorequipmentlastdata rename  to #{tableName} ")
    void altetTableMonitorequipmentlastdata(@Param("tableName")String tableName);



    @Select(value = "create table monitorequipmentlastdata like  #{tableName}")
    void createTableMonitorequipmentlastdata(@Param("tableName")String tableName);
}
