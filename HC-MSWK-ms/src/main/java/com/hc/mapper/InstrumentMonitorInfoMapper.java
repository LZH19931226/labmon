package com.hc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import com.hc.entity.Instrumentparamconfig;
import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.ResponseModel.AllInstrumentInfoModel;
import com.hc.model.ResponseModel.InstrumentMonitorInfoModel;

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
            "ifnull(a.warningphone,'0') warningphone  " +
            "from InstrumentParamConfig a left join  monitorinstrumenttype b on a.instrumenttypeid = b.instrumenttypeid " +
            "                         left join  instrumentconfig c on a.instrumentconfigid = c.instrumentconfigid " +
            "                         left join  MonitorInstrument d on a.instrumentno = d.instrumentno   " +
            "                         left join MonitorEquipment e on d.equipmentno = e.equipmentno  " +
            "where a.instrumentno = #{instrumentno} and c.instrumentconfigid = #{instrumentconfigid}")
    InstrumentMonitorInfoModel selectInfoByInsTypeId(@Param("instrumentno") String instrumentno,@Param("instrumentconfigid") Integer instrumentconfigid);

    /**
     * 通用版本
     */
    List<InstrumentMonitorInfoModel> selectInfo(Integer instrumenttypeid);

    /**
     * 查询所有探头详细信息
     */
    List<AllInstrumentInfoModel> selectInstrumentPage(RowBounds rowBounds, PageUserModel pageUserModel);

    /**
     *
     */
    @Select("select * from  InstrumentParamConfig  where instrumentno = #{instrumentno}  ")
    List<Instrumentparamconfig> selectConfigNo(@Param("instrumentno") String instrumentno);

    @Select("SELECT " +
            " sn " +
            "FROM " +
            " monitorinstrument a " +
            "LEFT JOIN monitorequipment b ON a.equipmentno = b.equipmentno " +
            "WHERE " +
            " a.hospitalcode = ( " +
            " SELECT DISTINCT " +
            " (hospitalcode) " +
            " FROM " +
            " monitorinstrument " +
            " WHERE " +
            " sn = #{sn} " +
            " ) and b.equipmenttypeid = 6 limit 1")
    String getMT600SN(@Param("sn") String sn);

}
