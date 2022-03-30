package com.hc.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.entity.Instrumentmonitor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * Created by 16956 on 2018-08-06.
 */
public interface InstrumentMonitorInfoMapper extends BaseMapper<Instrumentmonitor> {





    @Select("SELECT " +
            " sn " +
            "FROM " +
            " monitorinstrument a " +
            "LEFT JOIN monitorequipment b ON a.equipmentno = b.equipmentno " +
            "WHERE " +
            " a.hospitalcode = ( " +
            " SELECT DISTINCT " +
            " (hospitalcode) " +
            " FROM " +
            " monitorinstrument " +
            " WHERE " +
            " sn = #{sn} " +
            " ) and b.equipmenttypeid = 6 limit 1")
    String getMT600SN(@Param("sn") String sn);

}
