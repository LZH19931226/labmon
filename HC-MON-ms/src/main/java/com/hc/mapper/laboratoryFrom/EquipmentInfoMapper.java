package com.hc.mapper.laboratoryFrom;

import com.hc.entity.*;
import com.hc.model.*;
import com.hc.model.ExcleInfoModel.*;
import com.hc.model.ResponseModel.EquipmentConfigInfoModel;
import com.hc.model.ResponseModel.InstrumentParamConfigInfo;
import com.hc.model.SingleTimeExcle.SingleTimeEquipmentModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by 16956 on 2018-08-01.
 */
@Mapper
@Component
public interface EquipmentInfoMapper {

    @Select(" select  a.* from Monitorequipment a  where a.hospitalcode ='2cc0db66222042389cca37ba4ac5f281'")
    List<Monitorequipment> getEquipmentInfoByHospitalcode();


    @Select("SELECT\n" +
            "\tt4.*\n" +
            "FROM\n" +
            "\thospitalofreginfo t1\n" +
            "LEFT JOIN monitorequipment t2 on t1.hospitalcode = t2.hospitalcode\n" +
            "left join monitorinstrument t3 on t2.equipmentno = t3.equipmentno\n" +
            "left join instrumentparamconfig t4 on t3.instrumentno = t4.instrumentno\n" +
            "where t1.hospitalcode = #{hospitalcode} and t2.equipmenttypeid = #{equipmenttypeid}")
    List<Instrumentparamconfig> getEquipmentTypeAllConfig(@Param("hospitalcode") String hospitalcode,@Param("equipmenttypeid") String equipmenttypeid);

    /**
     * 根据设备类型编号和医院编号查询设备信息
     */
    @Select("SELECT\n" +
            "\tt1.* \n" +

            "FROM\n" +
            "\tmonitorequipment t1 " +
            "\tWHERE\n" +
            "\tt1.hospitalcode = #{hospitalcode} \n" +
            "\tAND t1.equipmenttypeid = #{equipmenttypeid} \n" +
            "\tAND t1.clientvisible = '1' \n" +
            "ORDER BY\n" +
            "\tt1.equipmentname  ")
    List<Monitorequipment> getEquipmentByType(@Param("hospitalcode") String hospitalcode,@Param("equipmenttypeid") String equipmenttypeid);
    /**
     * 根据设备编号查询设备信息
     */
    @Select("select * from monitorequipment where equipmentno = #{equipmentno}")
    Monitorequipment getEquipmentByNo(@Param("equipmentno") String equipmentno);

    @Select("select sn from monitorinstrument where equipmentno = #{equipmentno} limit 1")
    String getSn(@Param("equipmentno") String equipmentno);


    List<Monitorinstrument> getSns(@Param("equipmentnos") List<String> equipmentnos);



    /**
     * 查询设备总数
     */
    @Select("select count(equipmentno) totalcount from monitorequipment where equipmenttypeid=#{equipmenttypeid}")
    int getCount(@Param("equipmenttypeid") String equipmenttypeid);

    /**
     * 分页显示   根据设备类型编号和医院编号查询设备信息   -- app使用
     */
    List<Monitorequipment> getEquipmentByTypePageInfo(RowBounds rowBounds, PageUserModel pageUserModel);


    /**
     * 获取设备当前值
     */
    @Select("SELECT a.equipmentname,b.*" +
            "        FROM monitorequipment a " +
            "        left join  monitorequipmentlastdata b " +
            "        on a.equipmentno = b.equipmentno " +
            "        where a.equipmentno = #{equipmentno} and a.clientvisible='1' order by inputdatetime desc  limit 1")
    EquipmentCurrentDateModel getEquipmentCurrentData(Monitorequipment monitorequipment);

    /**
     *根据设备编号和door类型查询开关量最低值
     */
    @Select("SELECT " +
            " c.lowlimit " +
            "FROM " +
            " monitorequipment a " +
            "LEFT JOIN monitorinstrument b ON a.equipmentno = b.equipmentno " +
            "left join instrumentparamconfig c on b.instrumentno = c.instrumentno " +
            "where a.equipmentno = #{equipmentno} and ifnull(b.instrumentno,'xxx') !='xxx'  and c.instrumentconfigid = 11 limit 1")
    String getLowlimit(@Param("equipmentno") String equipmentno);


    List<WarningCurveDatamModel> getLowlimitByEqNos(@Param("equipmentnos") List<String> equipmentnos);


    @Select("select ups,inputdatetime from MonitorUPSRecord where equipmentno = #{equipmentno} order by inputdatetime desc limit 1 ")
    Monitorupsrecord selectUps(@Param("equipmentno") String equipmentno);

    /**
     * 获取设备探头sn号，可能有多个，用list
     */
    @Select("select * from monitorinstrument where equipmentno = #{equipmentno} ")
    List<Monitorinstrument> getEquipmentSn(Monitorequipment monitorequipment);

    /**
     * 获取市电当前值
     */
    @Select("select * from monitorequipmentlastdata " +
            "where hospitalcode = #{hospitalcode} and  inputdatetime = (select max(inputdatetime) from monitorequipmentlastdata )")
    EquipmentCurrentDateModel getUps(@Param("hospitalcode") String hospitalcode);

    /**
     * APP取环境 、其他设备  web 取环境 当天或者其他时间曲线值
     */
    List<Monitorequipmentlastdata> getCurveInfo(CurvelReqeustModel curvelReqeustModel);

    /**
     * web端查询 当前设备月份所有数据
     * @param curvelReqeustModel
     * @return
     */
    List<Monitorequipmentlastdata1> getSearchInfo(CurvelReqeustModel curvelReqeustModel);

    /**
     * 查询环境数据    excle 导出
     */
    EnvironmentListModel getHJExcleInfo(Params curvelReqeustModel);

    Map<String,Object> map();

    List<EnvironmentListModel> getHJExcleInfoById(Params curvelReqeustModel);

    /**
     * 查询培养箱数据  excle导出
     */
    IncubatorListModel getPyxExcleInfo(Params curvelReqeustModel);
    List<IncubatorListModel> getPyxExcleInfoById(Params curvelReqeustModel);
    /**
     * 查询其他数据 excle导出
     */
    OtherListModel getOtherExcleInfo(Params curvelReqeustModel);
    List<OtherListModel> getOtherExcleInfoById(Params curvelReqeustModel);

    /**
     * 当前医院所有设备当前时间点当前值查询(月查询)
     */
    List<SingleTimeEquipmentModel> getSingleEquipmentInfoByHospitalCode(Params params);


    /**
     * 当前医院当前设备当前时间点当前值查询(月查询)
     */
    List<SingleTimeEquipmentModel> getSingleEquipmentInfoByEquipmentNo(Params params);


    /**
     * 天查询
     */
    List<SingleTimeEquipmentModel> getSingleEquipmentInfoByHospitalCodeByDay(Params params);
    /**
     * 展示设备所有配置信息（显示多少，不显示多少）
     */
    List<EquipmentConfigInfoModel> showEquipmentConfigInfo(String hospitalcode);

    /**
     * 根据设备编号查询设备类型编号
     */
    @Select("select equipmenttypeid from monitorequipment where equipmentno = #{equipmentno}")
    String getTypeIdByNo(@Param("equipmentno") String equipmentno);


    List<Monitorequipmentlastdata> getMonitorequipmentlastdataByType(@Param("monitortlastdataTypeModel") MonitortlastdataTypeModel monitortlastdataTypeModel);


}
