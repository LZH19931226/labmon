package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.UserRightDto;
import com.hc.po.UserRightPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hc
 */
@Mapper
public interface UserRightDao extends BaseMapper<UserRightPo>  {

    /**
     * 查询用户信息
     * @param page 分页对象
     * @param hospitalName 医院名称
     * @param username 用户名
     * @param phoneNum 手机号
     * @param isUse 是否启用
     * @return
     */
    @Select("<script>"+
            "SELECT " +
            "u.userid userid " +
            ",u.username username " +
            ",u.pwd pwd " +
            ",u.nickname nickname " +
            ",h.hospitalname hospitalName " +
            ",h.hospitalcode hospitalCode " +
            ",u.phonenum  phoneNum " +
            ",u.isuse isUse " +
            ",u.usertype userType " +
            ",u.devicetype deviceType " +
            ",u.timeout timeout " +
            ",u.timeoutwarning timeoutWarning " +
            "FROM userright u " +
            "LEFT JOIN hospitalofreginfo h ON u.hospitalcode = h.hospitalcode "+
            "where 1=1" +
            "<if test = 'hospitalName !=null and hospitalName != \"\"'>" +
            "and h.hospitalName = #{hospitalName}" +
            "</if>" +
            "<if test = 'username != null and username != \"\"'>" +
            "and u.username like concat('%',#{username},'%') "+
            "</if>" +
            "<if test = 'phoneNum != null and phoneNum !=\"\"'> " +
            "and u.phonenum like concat('%',#{phoneNum},'%') " +
            "</if> " +
            "<if test = 'isUse != null'> " +
            "and u.isuse  = #{isUse} " +
            "</if>" +
            "</script>")
    List<UserRightDto> findUserRightList(Page page, @Param("hospitalName") String hospitalName, @Param("username") String username, @Param("phoneNum") String phoneNum, @Param("isUse") Long isUse);

}
