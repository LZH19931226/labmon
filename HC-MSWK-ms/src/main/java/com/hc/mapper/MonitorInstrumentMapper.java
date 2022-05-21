package com.hc.mapper;

/**
 * Created by 16956 on 2018-08-08.
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.po.Instrumentparamconfig;
import com.hc.po.Monitorequipment;
import com.hc.po.Monitorinstrument;
import com.hc.model.MonitorinstrumentModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 探头
 */

@Mapper
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



    /**
     * 根据设备sn号设备信息
     */
    @Select("SELECT t2.*,t1.clientvisible FROM monitorequipment t1,monitorinstrument t2 WHERE t1.equipmentno =t2.equipmentno  AND t2.sn = #{sn}")
    MonitorinstrumentModel selectMonitorinstrumentInfoBySn(@Param("sn") String sn);


}
