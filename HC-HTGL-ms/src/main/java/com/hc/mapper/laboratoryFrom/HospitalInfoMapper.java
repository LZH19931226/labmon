package com.hc.mapper.laboratoryFrom;

import com.hc.po.Hospitalequiment;
import com.hc.po.Hospitalofreginfo;
import com.hc.model.AbnormalDataModel;
import com.hc.model.ResponseModel.AlarmEquipmentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by 16956 on 2018-08-05.
 */
@Mapper
@Component
public interface HospitalInfoMapper {


    @Select("SELECT\n" +
            "\tt4.hospitalname,\n" +
            "  t3.equipmentname,\n" +
            "  t2.sn,\n" +
            "  t5.instrumenttypename mtName,\n" +
            "  t1.firsttime \n" +
            "FROM\n" +
            "\tinstrumentparamconfig t1\n" +
            "LEFT JOIN monitorinstrument t2 on t1.instrumentno = t2.instrumentno\n" +
            "left join monitorequipment t3 on t2.equipmentno = t3.equipmentno\n" +
            "left join monitorinstrumenttype t5 on t2.instrumenttypeid = t5.instrumenttypeid\n" +
            "left join hospitalofreginfo t4 on t3.hospitalcode = t4.hospitalcode\n" +
            "where t1.instrumentconfigid = '2' and ifnull(t1.firsttime,'2019-01-01') != '2019-01-01'\n" +
            "order by firsttime desc")
    List<AbnormalDataModel> getFirstTimeO2();

    @Select("SELECT\n" +
            "\tt1.inputdatetime,\n" +
            "  t1.warningremark abnormaldetails,\n" +
            "  t5.hospitalname,\n" +
            "  t4.equipmentname,\n" +
            "  t3.sn,\n" +
            "   '报警异常' abnormaltype ,"+
            "  t7.instrumenttypename mtName\n" +
            "  \n" +
            "FROM\n" +
            "\twarningrecord t1\n" +
            "LEFT JOIN instrumentparamconfig t2 ON t1.instrumentparamconfigNO = t2.instrumentparamconfigNO\n" +
            "left join monitorinstrument t3 on t2.instrumentno = t3.instrumentno\n" +
            "left join monitorinstrumenttype t7 on t3.instrumenttypeid = t7.instrumenttypeid\n" +
            "left join monitorequipment t4 on t3.equipmentno = t4.equipmentno\n" +
            "left join hospitalofreginfo t5 on t4.hospitalcode = t5.hospitalcode\n" +
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
            ") t6 ON \n" +
            "\tt1.instrumentparamconfigNO = t6.instrumentparamconfigNO\n" +
            "\tAND t1.inputdatetime = t6.inputdatetime\n" +
            "\n" +
            "where ifnull(t5.hospitalname,'tsl') !='tsl'\n" +
            " \tand  str_to_date(t1.inputdatetime,'%Y-%m-%d') = #{operationDate}\n" +
            "ORDER BY\n" +
            "t5.hospitalname,t4.equipmentname,\tt1.inputdatetime DESC")
    List<AbnormalDataModel> getWarningInfo(@Param("operationDate") String operationDate);

    /**
     * 查询当前医院简称和全称是否存在
     */
    @Select("select * from HospitalOfRegInfo where hospitalname = #{hospitalname}  and hospitalcode !=#{hospitalcode}")
    Hospitalofreginfo valideName(Hospitalofreginfo hospitalofreginfo);

    /**
     *根据医院编码查询医院信息
     */
    @Select("select * from HospitalEquiment where hospitalcode = #{hospitalcode}")
    List<Hospitalequiment> selectHospitalInfoByCode(Hospitalofreginfo hospitalofreginfo);

    /**
     * 分页模糊查询
     */
    List<Hospitalofreginfo> selectHospitalInfoPage(RowBounds rowBounds,Map<String,Object> fuzzy);

    /**
     * 展示所有医院信息
     */
    @Select("select * from HospitalOfRegInfo  order by convert(hospitalname using gbk) ")
    List<Hospitalofreginfo> selectHospitalInfo();
    @Select("      SELECT " +
            " a.hospitalcode, " +
            " a.hospitalname, " +
            "  b.equipmenttypeid, " +
            "  b.equipmentno, " +
            "  b.equipmentname " +
            "FROM " +
            " hospitalofreginfo a " +
            "LEFT JOIN monitorequipment b ON a.hospitalcode = b.hospitalcode " +
            "where ifnull(b.clientvisible,0) = 1 and a.hospitalcode = #{hospitalcode} order by b.equipmentname")
    List<AlarmEquipmentInfo> getAllAlarmEquipmentInfo(@Param("hospitalcode") String hospitalcode);

}

