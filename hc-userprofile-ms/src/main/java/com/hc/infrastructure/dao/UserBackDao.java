package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.UserBackDto;
import com.hc.po.UserBackPo;
import com.hc.vo.user.UserInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hc
 */
@Mapper
public interface UserBackDao extends BaseMapper<UserBackPo> {

    @Select("<script> " +
            "select " +
            "userid" +
            ",username" +
            ",pwd " +
            "from userback " +
            "where 1 = 1" +
            "<if test = 'username != null and username != \"\" '> " +
            "and username like concat('%',#{username},'%') " +
            "</if>" +
            "</script>")
    List<UserBackDto> selectPageAllInfo(Page<UserInfoVo> page, @Param(value = "username") String username);
}
