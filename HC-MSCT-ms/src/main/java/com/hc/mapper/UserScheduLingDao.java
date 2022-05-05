package com.hc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.po.UserScheduLing;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/7/31 15:13
 * 描述:
 **/
public interface UserScheduLingDao extends BaseMapper<UserScheduLing> {


    @Select(value = "SELECT * FROM userscheduling WHERE hospitalcode = #{hospitalcode}} AND( DATE_FORMAT( starttime, '%Y-%m-%d' ) = #{starttime}} or DATE_FORMAT( starttime, '%Y-%m-%d' ) = #{endtime}} ) " +
            "ORDER BY starttime ")
    List<UserScheduLing> findUserScByHosSt(@Param("hospitalcode") String hospitalcode,@Param("starttime") String starttime,@Param("endtime") String endtime);

}
