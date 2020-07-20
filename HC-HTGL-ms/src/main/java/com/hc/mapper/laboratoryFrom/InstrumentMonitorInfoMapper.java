package com.hc.mapper.laboratoryFrom;

import com.hc.entity.Monitorinstrument;
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
            "a.highlimit " +

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
            "a.calibration,"+
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
            "a.calibration,"+
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
            "a.alarmtime, " +
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

    /**
     * 查询探头通道信息
     */
    @Select("select * from monitorinstrument where ifnull(channel,'0') !='0'")
    List<Monitorinstrument> selectInstruChannel();

    @Select(" select * from monitorinstrument where sn = #{sn} limit 1")
    Monitorinstrument getMonitor(@Param("sn") String sn);

}
