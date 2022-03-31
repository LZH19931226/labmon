package com.hc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.entity.Warningrecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * Created by 16956 on 2018-08-09.
 */
public interface WarningrecordDao extends BaseMapper<Warningrecord> {

    @Select("update Warningrecord a set a.isPhone = '1' where a.pkid =#{pkid}")
    Integer updatePhone(@Param("pkid") String pkid);

}
