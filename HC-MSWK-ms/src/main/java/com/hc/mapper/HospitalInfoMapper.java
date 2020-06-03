package com.hc.mapper;

import java.util.List;

import com.hc.model.MapperModel.TimeoutEquipment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.hc.entity.Hospitalofreginfo;

/**
 * Created by 16956 on 2018-08-10.
 */
@Mapper
@Component
public interface HospitalInfoMapper {

    @Select("select * from hospitalofreginfo where hospitalcode = #{hospitalcode}")
    Hospitalofreginfo selectHospitalInfo(@Param("hospitalcode") String hospitalcode );

    @Select("select phonenum from userright where hospitalcode = #{hospitalcode} and isuse = '1'")
    List<String> selectUserByCode(@Param("hospitalcode") String hospitalcode);

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
