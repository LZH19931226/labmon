package com.hc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.entity.Monitorinstrument;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by xxf on 2018/10/14.
 */
public interface MonitorinstrumentDao extends BaseMapper<Monitorinstrument> {

    @Select("select  a from Monitorinstrument a where a.hospitalcode=#{hospitalcode} and a.instrumenttypeid = 9 ")
    Monitorinstrument getMonitorinstrumentByCode(@Param("hospitalcode") String hospitalcode);
}
