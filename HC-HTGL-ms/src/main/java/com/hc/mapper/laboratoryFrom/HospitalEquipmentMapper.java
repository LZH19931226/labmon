package com.hc.mapper.laboratoryFrom;

import com.hc.po.Monitorequipmenttype;
import com.hc.model.MapperModel.MonitorEquipmentWarningTimeModel;
import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.RequestModel.EquipmentTypeInfoModel;
import com.hc.model.ResponseModel.HospitalEquipmentTypeInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Mapper
@Component
public interface HospitalEquipmentMapper {
    /**
     * 查询当前医院是否存在当前设备类型  HospitalEquiment
     */
    @Select("select count(*) from  hospitalequiment where hospitalcode = #{hospitalcode} and equipmenttypeid = #{equipmenttypeid}")
    Integer isEquipmenttype(@Param("hospitalcode") String hospitalcode,@Param("equipmenttypeid") String equipmenttypeid);
    /**
     * 查询当前设备医院设备类型是否存在设备
     */
    @Select("select count(*) from monitorequipment where hospitalcode=#{hospitalcode} and equipmenttypeid = #{equipmenttypeid}")
    Integer selectEquipmentIs(EquipmentTypeInfoModel equipmentTypeInfoModel);

    /**
     * 删除设备类型
     * @param equipmentTypeInfoModel
     * @return
     */
    @Select("delete from hospitalequiment where hospitalcode=#{hospitalcode} and equipmenttypeid = #{equipmenttypeid} ")
    Integer deleteEquipmenttype(EquipmentTypeInfoModel equipmentTypeInfoModel);

    /**
     * 查询所有设备类型信息
     */
    @Select("select * from monitorequipmenttype")
    List<Monitorequipmenttype> selectAllEquipmetTypeInfo();

    /**
     * 根据医院编号查询当前医院所有设备类型信息
     */
    @Select("select a.equipmenttypeid,b.equipmenttypename from hospitalequiment a left join monitorequipmenttype b on a.equipmenttypeid = b.equipmenttypeid where a.hospitalcode =#{hospitalcode}")
    List<Monitorequipmenttype> selectEquipmentTypeByCodes(@Param("hospitalcode") String hospitalcode);
    /**
     * 分页模糊查询所有医院设备类型信息
     */
    List<HospitalEquipmentTypeInfoModel> selectAllEquipmentPage(RowBounds rowBounds, PageUserModel pageUserModel);

    @Select(" select * from hospitalequiment where hospitalcode = #{hospitalcode} and equipmenttypeid = #{equipmenttypeid}  ")
    EquipmentTypeInfoModel getInfo(@Param("hospitalcode") String hospitalcode,@Param("equipmenttypeid") String equipmenttypeid);

    @Select("select * from ( select " +
            "a.equipmenttypeid, " +
            "b.equipmenttypename, " +
            "a.hospitalcode, " +
            "c.hospitalname, " +
            "a.isvisible, " +
            "a.timeout, " +
            "a.timeouttime, " +
            "a.alwayalarm " +
            " from hospitalequiment a left join monitorequipmenttype b on a.equipmenttypeid = b.equipmenttypeid " +
            " left join HospitalOfRegInfo c on a.hospitalcode = c.hospitalcode " +
            " order by a.hospitalcode desc , a.equipmenttypeid ) K")
    List<HospitalEquipmentTypeInfoModel> selectAllEquipmentType();

    @Select("select * from ( select " +
            " a.equipmenttypeid, " +
            " b.equipmenttypename, " +
            " a.hospitalcode, " +
            " c.hospitalname, " +
            " a.isvisible, " +
            " a.timeout, " +
            " a.timeouttime, " +
            " a.alwayalarm " +
            " from hospitalequiment a left join monitorequipmenttype b on a.equipmenttypeid = b.equipmenttypeid" +
            " left join HospitalOfRegInfo c on a.hospitalcode = c.hospitalcode " +
            " order by a.hospitalcode desc , a.equipmenttypeid ) K where K.hospitalcode = #{hospitalcode} and K.equipmenttypeid = #{equipmenttypeid}")
    HospitalEquipmentTypeInfoModel selectEquipmentTypeByHospitalcodeAndEquipmenttypeid(@Param("hospitalcode") String hospitalcode , @Param("equipmenttypeid")String equipmenttypeid);



    @Select("SELECT " +
            " t1.alwayalarm, " +
            " t2.* " +
            " FROM " +
            " hospitalequiment t1, " +
            " monitorequipmentwarningtime t2  " +
            " WHERE " +
            " t1.hospitalcode = t2.hospitalcode " +
            " AND t2.hospitalcode = #{hospitalcode} " +
            " AND t2.equipmentid = #{equipmenttypeid} " +
            " AND t2.equipmentcategory = 'TYPE' " +
            " AND t1.equipmenttypeid = #{equipmenttypeid}")
    MonitorEquipmentWarningTimeModel getMonitorEquipmentWarningTimeModel(@Param("hospitalcode") String hospitalcode , @Param("equipmenttypeid")String equipmenttypeid);





}
