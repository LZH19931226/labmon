package com.hc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.entity.Monitorequipment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * Created by 15350 on 2020/5/25.
 */
public interface MonitorequipmentDao extends BaseMapper<Monitorequipment> {


    @Select("select  a  from Monitorequipment a  where a.hospitalcode = '2cc0db66222042389cca37ba4ac5f281' ")
    List<Monitorequipment> getMonitorequipmentByHospitalcode();

    @Select("select  equipmenttypeid  from Monitorequipment a  where a.equipmentno=#{equipmentno} ")
    String getByEquipmentno(@Param("equipmentno") String equipmentno);
}
