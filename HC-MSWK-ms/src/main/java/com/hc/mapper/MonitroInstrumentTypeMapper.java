package com.hc.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by 16956 on 2018-08-08.
 */
@Mapper
@Component
public interface MonitroInstrumentTypeMapper {
    /**
     * 根据设备监控类型查询监控类型编号
     * @param instrumenttypename
     * @return
     */
    @Select("select instrumenttypeid  from monitorinstrumenttype where instrumenttypename = #{instrumenttypename}")
    Integer selectInfoByTypeName(@Param("instrumenttypename") String instrumenttypename);

}
