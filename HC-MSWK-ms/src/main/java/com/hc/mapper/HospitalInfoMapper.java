package com.hc.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.entity.Hospitalofreginfo;
import com.hc.model.TimeoutEquipment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * Created by 16956 on 2018-08-10.
 */
public interface HospitalInfoMapper extends BaseMapper<Hospitalofreginfo> {



    @Select(" select equipmenttypeid from monitorequipment where equipmentno = #{equipmentno}")
    String getEquipmentTypeId(@Param("equipmentno") String equipmentno);

    /**
     * 获取超时报警设置设备
     * @return
     */
    @Select("SELECT \n" +
            "t3.hospitalname,\n" +
            "t3.hospitalcode,\n" +
            "t2.equipmentno,\n" +
            "t2.equipmentname,\n" +
            "t1.timeouttime "+
            "FROM\n" +
            "\thospitalofreginfo t3\n" +
            "\tLEFT JOIN hospitalequiment t1 ON t3.hospitalcode = t1.hospitalcode\n" +
            "\tLEFT JOIN monitorequipment t2 ON t1.hospitalcode = t2.hospitalcode \n" +
            "\tAND t1.equipmenttypeid = t2.equipmenttypeid \n" +
            "\twhere t1.timeout = '1' and t2.clientvisible = '1' ")
    List<TimeoutEquipment> getTimeoutEquipment();

}
