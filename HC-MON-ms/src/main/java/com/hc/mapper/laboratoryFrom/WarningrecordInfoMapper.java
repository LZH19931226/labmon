package com.hc.mapper.laboratoryFrom;

import com.hc.entity.Warningrecord;
import com.hc.model.NewWarningRecord;
import com.hc.model.PageUserModel;
import com.hc.model.ShowData;
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



}
