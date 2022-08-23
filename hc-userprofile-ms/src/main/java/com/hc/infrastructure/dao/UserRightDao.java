package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.UserRightDto;
import com.hc.po.UserRightPo;
import com.hc.vo.user.UserRightVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hc
 */
public interface UserRightDao extends BaseMapper<UserRightPo>  {
    /**
     * 分页查询医院人员信息
     * @param page 分页对象
     * @param hospitalCode 医院编码
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
            ",u.reminders reminders " +
            ",u.role role " +
            " FROM userright u " +
            "LEFT JOIN hospitalofreginfo h ON u.hospitalcode = h.hospitalcode "+
            "where 1=1" +
            "<if test = 'hospitalCode !=null and hospitalCode != \"\"'>" +
            "and h.hospitalcode = #{hospitalCode}" +
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
    List<UserRightDto> findUserRightList(Page<UserRightVo> page,
                                         @Param("hospitalCode") String hospitalCode,
                                         @Param("username") String username,
                                         @Param("phoneNum") String phoneNum,
                                         @Param("isUse") Long isUse);

    @Select("select count(0) from userright where username = #{userName}" )
    Integer checkUsername(String userName);

//    /**
//     * 查询用户信息
//     * @param page 分页对象
//     * @param hospitalCode 医院名称
//     * @param username 用户名
//     * @param phoneNum 手机号
//     * @param isUse 是否启用
//     * @return
//     */
//    @Select("<script>"+
//            "SELECT " +
//            "u.userid userid " +
//            ",u.username username " +
//            ",u.pwd pwd " +
//            ",u.nickname nickname " +
//            ",h.hospitalname hospitalName " +
//            ",h.hospitalcode hospitalCode " +
//            ",u.phonenum  phoneNum " +
//            ",u.isuse isUse " +
//            ",u.usertype userType " +
//            ",u.devicetype deviceType " +
//            ",u.timeout timeout " +
//            ",u.timeoutwarning timeoutWarning " +
//            ",u.reminders reminders " +
//            " FROM userright u " +
//            "LEFT JOIN hospitalofreginfo h ON u.hospitalcode = h.hospitalcode "+
//            "where 1=1" +
//            "<if test = 'hospitalCode !=null and hospitalCode != \"\"'>" +
//            "and h.hospitalcode = #{hospitalCode}" +
//            "</if>" +
//            "<if test = 'username != null and username != \"\"'>" +
//            "and u.username like concat('%',#{username},'%') "+
//            "</if>" +
//            "<if test = 'phoneNum != null and phoneNum !=\"\"'> " +
//            "and u.phonenum like concat('%',#{phoneNum},'%') " +
//            "</if> " +
//            "<if test = 'isUse != null'> " +
//            "and u.isuse  = #{isUse} " +
//            "</if>" +
//            "</script>")
//    List<UserRightDto> findUserRightList(Page page,
//                                         @Param("hospitalCode") String hospitalCode,
//                                         @Param("username") String username,
//                                         @Param("phoneNum") String phoneNum,
//                                         @Param("isUse") Long isUse);

}
