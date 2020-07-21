package com.hc.mapper.laboratoryFrom;


import com.hc.model.InstrumentMonitorInfoModel;
import com.hc.model.ResponseModel.MessageSendModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Mapper
@Component
public interface InstrumentMonitorInfoMapper {





    @Select("select " +
            "d.instrumentno," +
            "e.equipmentname, " +
            "b.instrumenttypeid," +
            "b.instrumenttypename," +
            "c.instrumentconfigid," +
            "c.instrumentconfigname," +
            "a.lowlimit," +
            "a.highlimit," +
            "b.alarmtime, " +
            "a.instrumentparamconfigNO, " +
            "a.pushtime, "+
            "a.warningtime,"+
            " a.calibration,"+
            "ifnull(a.warningphone,'0') warningphone  " +
            "from InstrumentParamConfig a left join  monitorinstrumenttype b on a.instrumenttypeid = b.instrumenttypeid " +
            "                         left join  instrumentconfig c on a.instrumentconfigid = c.instrumentconfigid " +
            "                         left join  MonitorInstrument d on a.instrumentno = d.instrumentno   " +
            "                         left join MonitorEquipment e on d.equipmentno = e.equipmentno  " +
            "                         where a.instrumentparamconfigNO = #{instrumentparamconfigNO}"
    )
    InstrumentMonitorInfoModel selectInstrumentOneInfo(@Param("instrumentparamconfigNO") String instrumentparamconfigNO);





    @Select("SELECT " +
            "a.userid, " +
            "a.devicetype device_type, " +
            "a.devicetoken DeviceToken, " +
            "c.equipmenttypename equipmentTypeName," +
            "c.equipmenttypeid equipmentTypeNo,"+
            "d.warningremark," +
            "d.inputdatetime," +
            "e.equipmentno equipmentNo,"+
            "d.instrumentparamconfigNO instrumentNo," +
            "e.equipmentname equipmentName "+
            "FROM " +
            " userright a " +
            "LEFT JOIN hospitalequiment b on a.hospitalcode = b.hospitalcode " +
            "LEFT JOIN monitorequipmenttype c on b.equipmenttypeid = c.equipmenttypeid " +
            "inner join monitorequipment e on (b.hospitalcode = e.hospitalcode and b.equipmenttypeid = e.equipmenttypeid) " +
            "left join monitorinstrument f on e.equipmentno = f.equipmentno " +
            "left join instrumentparamconfig g on f.instrumentno = g.instrumentno " +
            "left join warningrecord d on d.instrumentparamconfigNO = g.instrumentparamconfigNO " +
            "where d.pkid = #{pkid}")
    List<MessageSendModel> selectSendInfoByPkid(@Param("pkid") String pkid);





}
