package com.hc.mapper.laboratoryFrom;

import com.hc.po.Operationlog;
import com.hc.po.Operationlogdetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 15350 on 2020/5/20.
 */
@Component
public interface OperationlogdetaiMapper {



    @Select(" SELECT\n" +
            "\tt2.hospitalname\n" +
            "FROM\n" +
            "\tuserright t1\n" +
            "LEFT JOIN hospitalofreginfo t2 ON t1.hospitalcode = t2.hospitalcode\n" +
            "where t1.userid = #{userid}")
    String getHospitalName(@Param("userid") String userid);
}
