package com.hc.mapper.laboratoryFrom;

import com.hc.entity.Instrumentmonitor;
import com.hc.entity.Instrumentparamconfig;
import com.hc.entity.Monitorinstrument;
import com.hc.entity.Monitorinstrumenttype;
import com.hc.model.EquipmentInfoModel;
import com.hc.model.InstrumentInfoModel;
import com.hc.model.ShowModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by 16956 on 2018-08-07.
 */
@Mapper
@Component
public interface MonitorInstrumentMapper {
    /**
     * 根据探头配置表主键id获取探头名称
     * @param instrumentparamconfigNO
     * @return
     */
    @Select("SELECT\n" +
            "\tt2.instrumentconfigname\n" +
            "FROM\n" +
            "\tinstrumentparamconfig t1\n" +
            "LEFT JOIN instrumentconfig t2 ON t1.instrumentconfigid = t2.instrumentconfigid\n" +
            "WHERE\n" +
            "\tt1.instrumentparamconfigNO = #{instrumentparamconfigNO}")
    String getInstrumentName(@Param("instrumentparamconfigNO") String instrumentparamconfigNO);
    /**
     根据设备编号获取设备信息
     * @param equipmentno
     * @return
     */
    @Select(" select * from monitorequipment where equipmentno = #{equipmentno}")
    EquipmentInfoModel getEquipmentInfoByEquipmentno(@Param("equipmentno") String equipmentno);


    @Select("SELECT\n" +
            "\tt1.*,\n" +
            "  t2.sn\n" +
            "FROM\n" +
            "\tinstrumentparamconfig t1\n" +
            "LEFT JOIN monitorinstrument t2 ON t1.instrumentno = t2.instrumentno \n" +
            "where t1.instrumentparamconfigNO = #{instrumentparamconfigNO} limit 1")
    InstrumentInfoModel getInstrumentInfoByNo(@Param("instrumentparamconfigNO") String instrumentparamconfigNO);





    @Select("SELECT\n" +
            "\tt1.*,\n" +
            "\tt2.sn,\n" +
            "\tt3.instrumentconfigname\n" +
            "FROM\n" +
            "\tinstrumentparamconfig t1\n" +
            "\tLEFT JOIN monitorinstrument t2 ON t1.instrumentno = t2.instrumentno \n" +
            "\tLEFT JOIN instrumentconfig t3 on t1.instrumentconfigid=t3.instrumentconfigid\n" +
            "WHERE\n" +
            "\tt1.instrumentparamconfigNO = #{instrumentparamconfigNO} limit 1 ;\n" +
            "\t")
    InstrumentInfoModel getInstrumentInfoByNoNew(@Param("instrumentparamconfigNO") String instrumentparamconfigNO);



    /**
     * 根据探头编号获取设备名称和医院名称
     */
    @Select("SELECT\n" +
            "\tt3.equipmentname,\n" +
            "  t4.hospitalname\n" +
            "FROM\n" +
            "\tinstrumentparamconfig t1\n" +
            "LEFT JOIN monitorinstrument t2 ON t1.instrumentno = t2.instrumentno\n" +
            "left join monitorequipment t3 on t2.equipmentno = t3.equipmentno\n" +
            "left join hospitalofreginfo t4 on t3.hospitalcode= t4.hospitalcode\n" +
            "where t1.instrumentparamconfigNO = #{instrumentparamconfigNO}")
    ShowModel getHospitalNameEquipmentNameByNo(@Param("instrumentparamconfigNO") String instrumentparamconfigNO);

//    /**
//        根据设备编号获取设备信息
//     * @param equipmentno
//     * @return
//     */
//    @Select(" select * from monitorequipment where equipmentno = #{equipmentno}")
//    EquipmentInfoModel getEquipmentInfoByEquipmentno(@Param("equipmentno") String equipmentno);

    /**
     * 根据设备编号获取医院名称
     * @param equipmentno
     * @return
     */
    @Select("SELECT\n" +
            "\tt2.hospitalname\n" +
            "FROM\n" +
            "\tmonitorequipment t1\n" +
            "LEFT JOIN hospitalofreginfo t2 ON t1.hospitalcode = t2.hospitalcode\n" +
            "where t1.equipmentno = #{equipmentno}")
    String getHospitalNameByEquipmentno(@Param("equipmentno") String equipmentno);

    /**
     * 判断当前探头sn号是否重复
     */
    @Select("select count(*) from monitorinstrument where sn = #{sn} ")
    Integer isExist(@Param("sn") String sn);

    /**
     * 根据sn查询设备信息
     */
    @Select("select * from monitorinstrument where instrumentno = #{instrumentno}")
    Monitorinstrument selectMonInfoBySn(@Param("instrumentno") String instrumentno);

    @Select("select * from monitorinstrument order by equipmentno ")
    List<Monitorinstrument> selectInstrumentInfo();

    /**
     * 查询当前设备是否存在探头
     * @param equipmentno
     * @return
     */
    @Select("select count(*) from monitorinstrument where equipmentno = #{equipmentno}")
    Integer isIntrument(@Param("equipmentno") String equipmentno);

    /**
     * 查询当前未绑定设备探头类型
     */
    @Select("select " +
            "distinct(a.instrumenttypeid)," +
            "b.instrumenttypename  " +
            "from monitorinstrument a left join monitorinstrumenttype b on a.instrumenttypeid = b.instrumenttypeid " +
            "where ifnull(equipmentno,'cj') = 'cj' and a.hospitalcode = #{hospitalcode} order by b.instrumenttypename")
    List<Monitorinstrumenttype> showInstrumenttpid(@Param("hospitalcode") String hospitalcode);

    @Select("select " +
            "* " +
            "from " +
            "monitorinstrument where ifnull(equipmentno,'CJCJ') = 'CJCJ'  " +
            "and hospitalcode = #{hospitalcode} " +
            "and instrumenttypeid = #{instrumenttypeid} and ifnull(channel,'0') = '0'")
    List<Monitorinstrument> showInstrument(@Param("hospitalcode") String hospitalcode, @Param("instrumenttypeid") Integer instrumenttypeid, @Param("channer") String channel);
    @Select("select " +
            "* " +
            "from " +
            "monitorinstrument where ifnull(equipmentno,'CJCJ') = 'CJCJ'  " +
            "and hospitalcode = #{hospitalcode} " +
            "and instrumenttypeid = #{instrumenttypeid} and ifnull(channel,'0') = #{channel}")
    List<Monitorinstrument> showInstruments(@Param("hospitalcode") String hospitalcode, @Param("instrumenttypeid") Integer instrumenttypeid, @Param("channel") String channel);

    @Select("select count(*) from monitorinstrument where sn = #{sn} and instrumentno !=#{instrumentno}")
    Integer isSn(@Param("sn") String sn, @Param("instrumentno") String instrumentno);

    @Select("select * from monitorinstrument where channel = '1' or channel = '2'")
    List<Monitorinstrument> showMonitorInstrumentChannel();

    /**
     * 获取当前设备下所有监控探头
     */
    @Select("SELECT\n" +
            "\tt3.*\n" +
            "FROM\n" +
            "\tmonitorequipment t1\n" +
            "LEFT JOIN monitorinstrument t2 on t1.equipmentno = t2.equipmentno\n" +
            "left join instrumentparamconfig t3 on t2.instrumentno = t2.instrumentno\n" +
            "where t1.equipmentno = #{equipmentno}")
    List<Instrumentparamconfig> getAllInstrumentByEquipmentno(@Param("equipmentno") String equipmentno);

    /**
     * 获取当前设备下所有探头类型信息:无线探头一般只有一个，有线暂时存在多个
     */
    @Select("SELECT\n" +
            "\tt2.*\n" +
            "FROM\n" +
            "\tmonitorequipment t1\n" +
            "LEFT JOIN monitorinstrument t2 on t1.equipmentno = t2.equipmentno\n" +
            "where t1.equipmentno = #{equipmentno}")
    List<Monitorinstrument> getAllMonitorInstrumentByEquipmentno(@Param("equipmentno") String equipmentno);

    @Select("select * from instrumentmonitor where instrumenttypeid = #{instrumenttypeid}")
    List<Instrumentmonitor> getInstrumentmonitorByTypeid(@Param("instrumenttypeid") Integer instrumenttypeid);

    @Select("select * from xxx ")
    List<Map<String,Object>> xxx();

    @Select("select * from monitorinstrument where sn = #{sn} limit 1")
    Monitorinstrument getMonitorInstrument(@Param("sn") String sn);

}

