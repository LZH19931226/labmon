package com.hc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.entity.Instrumentparamconfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 16956 on 2018-09-05.
 */

public interface InstrumentparamconfigDao extends BaseMapper<Instrumentparamconfig> {


    //此处有问题
    @Select("update Instrumentparamconfig a set a.warningtime=#{warningtime} where a.instrumentparamconfigno=#{instrumentparamconfigno}")
    Integer updateWarnTime(@Param("warningtime")Date warningtime, @Param("instrumentparamconfigno") String instrumentparamconfigno);

    @Select("select ins.highlimit from Monitorinstrument mon left join Instrumentparamconfig ins " +
            "            on mon.instrumentno=ins.instrumentno where  mon.instrumentno=#{instrumentno} and ins.instrumentconfigid='14'")
    BigDecimal getMt200mHighLimit(@Param("instrumentno") String instrumentno);
}
