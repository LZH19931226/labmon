package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.po.WarningrecordPo;
import com.hc.vo.waring.WarningrecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface WarningrecordDao extends BaseMapper<WarningrecordPo> {
    @Select("SELECT\n" +
            "\tdistinct(e.inputdatetime) inputdatetime,\n" +
            "\ta.equipmenttypeid equipmenttypeid,\n" +
            "\ta.equipmentno equipmentno,\n" +
            "\ta.equipmentname equipmentname,\n" +
            "\tb.instrumentno instrumentno,\n" +
            "\tc.instrumentconfigid instrumentconfigid,\n" +
            "\tc.instrumentparamconfigNO instrumentparamconfigNO,\n" +
            "\td.instrumentconfigname instrumentconfigname,\n" +
            "\ta.hospitalcode hospitalcode,\n" +
            "\te.warningremark warningremark,\n" +
            "\te.pkid pkid\n" +
            "FROM\n" +
            "\tmonitorequipment a\n" +
            "LEFT JOIN monitorinstrument b ON a.equipmentno = b.equipmentno\n" +
            "LEFT JOIN instrumentparamconfig c ON b.instrumentno = c.instrumentno\n" +
            "LEFT JOIN instrumentconfig d ON c.instrumentconfigid = d.instrumentconfigid\n" +
            "RIGHT JOIN warningrecord e ON c.instrumentparamconfigNO = e.instrumentparamconfigNO\n" +
            "INNER JOIN (\n" +
            "\tSELECT\n" +
            "\t\tinstrumentparamconfigNO,\n" +
            "\t\tmax(inputdatetime) inputdatetime\n" +
            "\tFROM\n" +
            "\t\twarningrecord\n" +
            "\tGROUP BY\n" +
            "\t\tinstrumentparamconfigNO\n" +
            "\tORDER BY\n" +
            "\t\tinputdatetime DESC\n" +
            ") f ON (\n" +
            "\tc.instrumentparamconfigNO = f.instrumentparamconfigNO\n" +
            "\tAND e.inputdatetime = f.inputdatetime\n" +
            "\n" +
            ")\n" +
            "\n" +
            "WHERE\n" +
            "\ta.hospitalcode = #{hospitalcode}\n" +
            "AND ifnull(a.clientvisible, '0') = '1'\n" +
            "and ifnull(c.warningphone,'0') = '1'\n" +
            " -- and e.isPhone = '1'\n" +
            "ORDER BY\n" +
            "\tinputdatetime DESC\n")
    List<WarningrecordPo> getNewWarnRecord(Page<WarningrecordVo> page,@Param("hospitalcode") String hospitalcode);

    @Select("SELECT\n" +
            "instrumentparamconfigNO,\n" +
            "COUNT( * ) AS count1\n" +
            "FROM\n" +
            "warningrecord\n" +
            "WHERE\n" +
            "DATE_FORMAT( inputdatetime, '%Y-%m-%d %H:%i:%s') BETWEEN #{currentDateTimeBeforeOneMonth} AND #{nowDate}\n" +
            "AND instrumentparamconfigNO IN\n" +
            "<foreach collection=\"collect\" item=\"id\" open=\"(\" close=\")\" separator=\",\" index=\"index\">\n" +
            "#{id}\n" +
            "</foreach>\n" +
            "GROUP BY\n" +
            "instrumentparamconfigNO")
    List<WarningrecordPo> getWarNingRecordMonthCount(List<String> collect,@Param("currentDateTimeBeforeOneMonth") String currentDateTimeBeforeOneMonth,@Param("nowDate") String nowDate);

    @Select("SELECT\n" +
            "t2.instrumentparamconfigNO,\n" +
            "COUNT( * ) AS count1\n" +
            "FROM\n" +
            "warningrecordinfo t1, warningrecord t2\n" +
            "WHERE\n" +
            "t1.warningrecordid=t2.pkid and\n" +
            "DATE_FORMAT( t1.createtime, '%Y-%m-%d %H:%i:%s') BETWEEN  #{currentDateTimeBeforeOneMonth} AND #{nowDate}\n" +
            "AND t2.instrumentparamconfigNO IN\n" +
            "<foreach collection=\"collect\" item=\"id\" open=\"(\" close=\")\" separator=\",\" index=\"index\">\n" +
            "#{id}\n" +
            "</foreach>\n" +
            "GROUP BY\n" +
            "t2.instrumentparamconfigNO")
    List<WarningrecordPo> getWarNingRecordInfoMonthCount(List<String> collect,@Param("currentDateTimeBeforeOneMonth") String currentDateTimeBeforeOneMonth,@Param("nowDate") String nowDate);

    @Select("SELECT\n" +
            "\tinstrumentparamconfigNO,\n" +
            "\tinputdatetime,\n" +
            "\twarningremark,\n" +
            "\tifnull( msgflag, '0' ) msgflag,\n" +
            "\tt2.id,\n" +
            "\tt2.info\n" +
            "FROM\n" +
            "\twarningrecord  LEFT JOIN warningrecordinfo t2 on pkid =t2.warningrecordid\n" +
            "WHERE\n" +
            "\tinstrumentparamconfigNO = #{instrumentparamconfigNO} \n" +
            "ORDER BY\n" +
            "\tinputdatetime DESC")
    List<WarningrecordPo> getInstrumentTypeHistoryWarnAll(@Param("instrumentparamconfigNO") String instrumentparamconfigNO, Page page);

    @Select("SELECT\n" +
            "\tinstrumentparamconfigNO,\n" +
            "\tinputdatetime,\n" +
            "\twarningremark,\n" +
            "\tifnull( msgflag, '0' ) msgflag,\n" +
            "\tt2.id,\n" +
            "\tt2.info\n" +
            "FROM\n" +
            "\twarningrecord  LEFT JOIN warningrecordinfo t2 on pkid =t2.warningrecordid\n" +
            "WHERE\n" +
            "\tinstrumentparamconfigNO = #{instrumentparamconfigNO} \n" +
            "\tAND isPhone = '1' \n" +
            "ORDER BY\n" +
            "\tinputdatetime DESC")
    List<WarningrecordPo> getInstrumentTypeHistoryWarn(@Param("instrumentparamconfigNO") String instrumentparamconfigNO, Page page);

    @Update("update Warningrecord  set msgflag = '1' where instrumentparamconfigNO = #{instrumentparamconfigNO} ")
    void updateMsgFlag(@Param("instrumentparamconfigNO") String instrumentparamconfigNO);

    @Update("update Warningrecord  warningrecord set warningrecord.pushstate =#{s}  where warningrecord.instrumentparamconfigNO = #{instrumentparamconfigNO}}")
    int updatePushState(@Param("instrumentparamconfigNO") String instrumentparamconfigNO,@Param("s") String s);
}
