package com.hc.mapper.laboratoryFrom;

import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.RequestModel.InstrumentInfoModel;
import com.hc.model.ResponseModel.AllInstrumentInfoModel;
import com.hc.my.common.core.bean.InstrumentMonitorInfoModel;
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
public interface InstrumentMonitorInfoMapper {

    /**
     * 报警探头类型查询
     */
    @Select("select " +
            "b.instrumenttypeid," +
            "b.instrumenttypename," +
            "c.instrumentconfigid," +
            "c.instrumentconfigname," +
            "a.lowlimit," +
            "a.highlimit,a.saturation " +

            "from instrumentmonitor a left join  monitorinstrumenttype b on a.instrumenttypeid = b.instrumenttypeid " +
            "                         left join  instrumentconfig c on a.instrumentconfigid = c.instrumentconfigid where a.instrumenttypeid = #{instrumenttypeid}")
    List<InstrumentMonitorInfoModel> selectInfoByInsTypeId(@Param("instrumenttypeid") Integer instrumenttypeid);

    /**
     * 通用版本
     */
    List<InstrumentMonitorInfoModel> selectInfo(InstrumentInfoModel instrumentInfoModel);

    /**
     * 查询所有探头详细信息
     */
    List<AllInstrumentInfoModel> selectInstrumentPage(RowBounds rowBounds, PageUserModel pageUserModel);

    @Select("select " +
            "d.instrumentno," +
            "e.equipmentname,e.hospitalcode, " +
            "b.instrumenttypeid," +
            "b.instrumenttypename," +
            "c.instrumentconfigid," +
            "c.instrumentconfigname," +
            "a.lowlimit," +
            "a.highlimit," +
            "a.alarmtime, " +
            "a.instrumentparamconfigNO, " +
            "a.pushtime, "+
            "a.warningtime,"+
            "a.calibration,"+
            "e.alwayalarm,"+
            "e.equipmentno,"+
            "ifnull(a.warningphone,'0') warningphone  " +
            "from InstrumentParamConfig a left join  monitorinstrumenttype b on a.instrumenttypeid = b.instrumenttypeid " +
            "                         left join  instrumentconfig c on a.instrumentconfigid = c.instrumentconfigid " +
            "                         left join  MonitorInstrument d on a.instrumentno = d.instrumentno   " +
            "                         left join MonitorEquipment e on d.equipmentno = e.equipmentno  "
           )
    List<InstrumentMonitorInfoModel> selectInstrumentInfo();

    @Select("select " +
            "d.instrumentno," +
            "e.equipmentname, " +
            "b.instrumenttypeid," +
            "b.instrumenttypename," +
            "c.instrumentconfigid," +
            "c.instrumentconfigname," +
            "a.lowlimit," +
            "a.highlimit," +
            "a.alarmtime, " +
            "a.instrumentparamconfigNO, " +
            "a.pushtime, "+
            "a.warningtime,"+
            "a.calibration,a.saturation,"+
            "ifnull(a.warningphone,'0') warningphone  " +
            "from InstrumentParamConfig a left join  monitorinstrumenttype b on a.instrumenttypeid = b.instrumenttypeid " +
            "                         left join  instrumentconfig c on a.instrumentconfigid = c.instrumentconfigid " +
            "                         left join  MonitorInstrument d on a.instrumentno = d.instrumentno   " +
            "                         left join MonitorEquipment e on d.equipmentno = e.equipmentno  " +
            "                         where a.instrumentparamconfigNO = #{instrumentparamconfigNO}"
    )
    InstrumentMonitorInfoModel selectInstrumentOneInfo(@Param("instrumentparamconfigNO") String instrumentparamconfigNO);

    @Select("select " +
            "d.instrumentno," +
            "e.equipmentname, " +
            "b.instrumenttypeid," +
            "b.instrumenttypename," +
            "c.instrumentconfigid," +
            "c.instrumentconfigname," +
            "a.lowlimit," +
            "a.highlimit," +
            "a.alarmtime, a.saturation, " +
            "a.instrumentparamconfigNO, " +
            "a.pushtime, "+
            "a.warningtime,"+
            "a.calibration,"+
            "ifnull(a.warningphone,'0') warningphone  " +
            "from InstrumentParamConfig a left join  monitorinstrumenttype b on a.instrumenttypeid = b.instrumenttypeid " +
            "                         left join  instrumentconfig c on a.instrumentconfigid = c.instrumentconfigid " +
            "                         left join  MonitorInstrument d on a.instrumentno = d.instrumentno   " +
            "                         left join MonitorEquipment e on d.equipmentno = e.equipmentno  " +
            "                         where d.instrumentno = #{instrumentno}"
    )
    List<InstrumentMonitorInfoModel> selectInstrumentManyInfo(@Param("instrumentno") String instrumentno);

    @Select("select count(*) from InstrumentParamConfig where instrumentno = #{instrumentno}")
    Integer getCount(@Param("instrumentno") String instrumentno);


    @Select("SELECT\n" +
            "  t3.hospitalname," +
            "\tt4.equipmenttypename,\n" +
            "\tt1.equipmentname,\n" +
            "\tt2.sn\n" +
            "FROM\n" +
            "\tmonitorequipment t1\n" +
            "\tLEFT JOIN monitorinstrument t2 ON t1.equipmentno = t2.equipmentno \n" +
            "\tLEFT JOIN hospitalofreginfo t3 on t1.hospitalcode =t3.hospitalcode\n" +
            "\tLEFT JOIN monitorequipmenttype t4 on t1.equipmenttypeid=t4.equipmenttypeid\n" +
            "WHERE\n" +
            " \tSUBSTRING(t2.sn,1,4)  <= #{sn}\n" +
            "\t")
    List<AllInstrumentInfoModel> searchEqByTwoYear(@Param("sn") String sn);

}
