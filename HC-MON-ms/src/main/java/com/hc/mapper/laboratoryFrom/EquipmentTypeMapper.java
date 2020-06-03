package com.hc.mapper.laboratoryFrom;

import com.hc.model.MonitorequipmenttypeModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 16956 on 2018-07-31.
 */
@Mapper
@Component
public interface EquipmentTypeMapper {

    /**
     * 查询当前医院所有设备类型
     */
    @Select("select b.equipmenttypeid,b.equipmenttypename from " +
            "hospitalequiment a left join monitorequipmenttype b on a.equipmenttypeid = b.equipmenttypeid " +
            "where a.hospitalcode = #{hospitalcode} and a.isvisible = '1' order by a.orderno ")
    List<MonitorequipmenttypeModel> getAllEquipmentType(@Param("hospitalcode") String hospitalcode);

    /**
     * 根据登录人员信息查询当前人员账户所能查看医院设备类型
     */
    @Select("select b.equipmenttypeid,b.equipmenttypename from" +
            " userequipmentright c left join  hospitalequiment a on c.equipmentitem = a.equipmenttypeid" +
            " join monitorequipmenttype b on a.equipmenttypeid = b.equipmenttypeid " +
            "where a.hospitalcode = #{hospitalcode} and a.isvisible = '1' and userid = #{userid} order by a.orderno")
    List<MonitorequipmenttypeModel> getAllEquipmentTypeByUserid(@Param("hospitalcode") String hospitalcode,@Param("userid") String userid);
}
