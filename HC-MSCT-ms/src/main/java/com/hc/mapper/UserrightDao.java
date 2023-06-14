package com.hc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.po.Userright;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 15350 on 2019/10/9.
 */
@Mapper
public interface UserrightDao extends BaseMapper<Userright> {

    @Select("SELECT t1.*,t2.`code` FROM userright t1 ,sys_national t2 WHERE t1.national_id = t2.national_id  AND t1.hospitalcode = #{hospitalcode} and t1.timeout = '1' ")
    List<Userright> getUserrightByHospitalcodeAAndTimeout (@Param("hospitalcode") String hospitalcode);


    @Select("SELECT t1.*,t2.`code` FROM userright t1 ,sys_national t2 WHERE t1.national_id = t2.national_id  AND t1.hospitalcode = #{hospitalcode}")
    List<Userright> findALLUserRightInfoByHC(@Param("hospitalcode") String hospitalcode);
}
