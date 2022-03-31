package com.hc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.entity.Userright;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 15350 on 2019/10/9.
 */
public interface UserrightDao extends BaseMapper<Userright> {

    @Select("select  a from Userright a where a.hospitalcode=#{hospitalcode} and a.timeout = '1' ")
    List<Userright> getUserrightByHospitalcodeAAndTimeout (@Param("hospitalcode") String hospitalcode);
}
