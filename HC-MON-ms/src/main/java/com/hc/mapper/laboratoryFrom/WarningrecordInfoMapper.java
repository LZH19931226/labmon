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
import java.util.Map;

/**
 * Created by 16956 on 2018-08-01.
 */
@Mapper
@Component
public interface WarningrecordInfoMapper {
    /**
     * 取前二十条报警
     */
    @Select("select * from warningrecord where hospitalcode = #{hospitalcode} and isPhone = '1' order by inputdatetime desc limit 30 ")
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
