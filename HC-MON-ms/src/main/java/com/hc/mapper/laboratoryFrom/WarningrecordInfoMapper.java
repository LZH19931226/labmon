package com.hc.mapper.laboratoryFrom;

import com.hc.entity.Warningrecord;
import com.hc.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 16956 on 2018-08-01.
 */
@Mapper
@Component
public interface WarningrecordInfoMapper {
    /**
     * 取前二十条报警
     */
    @Select("SELECT t1.*,t2.id,t2.info FROM warningrecord t1 LEFT JOIN warningrecordinfo t2 on t1.pkid =t2.warningrecordid\n" +
            "WHERE t1.hospitalcode = #{hospitalcode} AND t1.isPhone = '1' ORDER BY t1.inputdatetime DESC LIMIT 30")
    List<Warningrecord> getWarningRecord(@Param("hospitalcode") String hospitalcode);

    /**
     * 获取每个设备探头类型最新一条报警信息
     */
    List<NewWarningRecord> getNewWarnRecord(RowBounds rowBounds, PageUserModel pageUserModel);

    /**
     * 获取当前探头监控类型历史报警（一个探头有多个监控参数：co2  o2  甲醛  这些几把玩意）
     */
    @Select("select instrumentparamconfigNO,inputdatetime,warningremark,ifnull(msgflag,'0') msgflag from warningrecord where instrumentparamconfigNO = #{instrumentparamconfigNO}" +
            " and isPhone = '1' order by inputdatetime desc")
    List<Warningrecord> getInstrumentTypeHistoryWarn(String instrumentparamconfigNO,RowBounds rowBounds);

    @Select(" SELECT " +
            "a.inputdatetime, " +
            "b.hospitalname, " +
            "a.warningvalue data " +
            "FROM " +
            " warningrecord a " +
            "LEFT JOIN hospitalofreginfo b ON a.hospitalcode = b.hospitalcode " +
            "ORDER BY " +
            " inputdatetime DESC " +
            "LIMIT 100 ")
    List<ShowData> showData();


    @Select("SELECT t3.equipmentno,t4.instrumentconfigname,t1.instrumentparamconfigNO FROM warningrecord t1 LEFT JOIN instrumentparamconfig t2 ON t1.instrumentparamconfigNO = t2.instrumentparamconfigNO LEFT JOIN instrumentconfig t4 ON t2.instrumentconfigid = t4.instrumentconfigid LEFT JOIN monitorinstrument t3 ON t2.instrumentno = t3.instrumentno \n" +
            "WHERE t1.pkid = #{pkid}")
    WarningCurveDatamModel getWarningCurveData(@Param("pkid") String pkid);

    /**
     * 获取探头一个月的报警数量
     */
    List<NewWarningRecord> getWarNingRecordMonthCount(@Param("monitortlastdataTypeModel") MonitortlastdataTypeModel monitortlastdataTypeModel);

    /**
     * 获取探头一个月的报警备注信息数量
     */
    List<NewWarningRecord> getWarNingRecordInfoMonthCount(@Param("monitortlastdataTypeModel") MonitortlastdataTypeModel monitortlastdataTypeModel);


}
