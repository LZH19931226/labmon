package com.hc.mapper;

/**
 * Created by 16956 on 2018-08-08.
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.entity.Instrumentparamconfig;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorinstrument;
import com.hc.model.TimeoutEquipment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 探头
 */
public interface MonitorInstrumentMapper extends BaseMapper<Monitorinstrument> {

    @Select(" SELECT\n" +
            "\tt3.*\n" +
            "FROM\n" +
            "\tmonitorequipment t1\n" +
            "LEFT JOIN monitorinstrument t2 ON t1.equipmentno = t2.equipmentno\n" +
            "left join instrumentparamconfig t3 on t2.instrumentno = t3.instrumentno\n" +
            "where t1.equipmentno = #{equipmentno}")
    List<Instrumentparamconfig> getInstrumentparamconfigByEquipmentno(@Param("equipmentno") String equipmentno);


    /**
     * 根据设备sn号查询医院信息
     */
    @Select("select * from monitorinstrument where sn = #{sn} limit 1 ")
    Monitorinstrument  selectHospitalCodeBySn(@Param("sn") String sn);


    @Select(" SELECT\n" +
            "\t t2.* \n" +
            "FROM\n" +
            "\tmonitorinstrument t1\n" +
            "\tLEFT JOIN monitorequipment t2 ON t1.equipmentno = t2.equipmentno \n" +
            "\twhere t1.sn = #{sn} limit 1  ")
    Monitorequipment isCliva(@Param("sn") String sn);



}
