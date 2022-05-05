package com.hc.mapper.laboratoryMain;

import com.hc.po.Monitorinstrument;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.hc.po.Instrumentparamconfig;

import java.util.List;

/**
 * Created by 16956 on 2018-08-05.
 */
@Mapper
@Component
public interface InstrumentparamConfigSetMapper {


    @Select(" select * from monitorinstrument where SUBSTR(sn,5,2) = '04'")
    List<Monitorinstrument> getAllMt100();

    @Select(" select * from instrumentparamconfig where instrumentno = #{instrumentno} and instrumentconfigid = 7 limit 1 ")
    Monitorinstrument getQcInstrument(@Param("instrumentno") String instrumentno);

    @Select(" select equipmentname from monitorequipment   where equipmentno = #{equipmentno} ")
    String getEquipmentName(@Param("equipmentno") String equipmentno);

    @Select("update instrumentparamconfig  set pushtime=#{pushtime}, " +
            "warningphone=#{warningphone} where instrumentparamconfigNO =#{instrumentparamconfigno}")
    void updateWarningState(Instrumentparamconfig instrumentparamconfig);

    @Select(" update monitorequipment set clientvisible = #{clientvisible} where equipmentno = #{equipmentno\n" +
            "}")
    void updateEquipmentClientvisible(@Param("clientvisible") int i,@Param("equipmentno") String equipmentno);

    @Select("update instrumentparamconfig set " +
            "warningphone=#{warningphone} where instrumentparamconfigNO =#{instrumentparamconfigno}")
    void updateWarnPhone(Instrumentparamconfig instrumentparamconfig);

    @Select("select * from monitorinstrument ")
    List<Monitorinstrument>  test1();

    @Select("select * from instrumentparamconfig where ifnull(channel,'0') != '0'")
    List<Instrumentparamconfig> test2();

    @Select("update instrumentparamconfig set instrumentname = #{instrumentname} where instrumentno = #{instrumentno} ")
    void test3(@Param("instrumentname") String instrumentname,@Param("instrumentno") String instrumentno);

    @Select("select sn from instrumentparamconfig where instrumentno = #{instrumentno} limit 1")
    String testSN(@Param("instrumentno") String instrumentno);

    @Select("update monitorinstrument set channel = #{channel}  where instrumentno = #{instrumentno}")
    void testInstrumentno(@Param("channel") String channel,@Param("instrumentno") String instrumentno);

}
