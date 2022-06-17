package com.hc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.po.Userright;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 15350 on 2019/10/9.
 */
public interface UserrightDao extends BaseMapper<Userright> {

    @Select("select  * from userright  where hospitalcode=#{hospitalcode} and timeout = '1' ")
    List<Userright> getUserrightByHospitalcodeAAndTimeout (@Param("hospitalcode") String hospitalcode);
}
