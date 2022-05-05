package com.hc.infrastructure.dao;

import com.hc.po.Userright;
import com.hc.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-07-31.
 */
@Transactional
public interface UserrightDao extends JpaRepository<Userright,String>{
    /**
     * 修改手机号
     * @param
     * @return
     */
    @Modifying
    @Query("update Userright userrigth set userrigth.phonenum =:phonenum where userrigth.userid=:userid ")
    int updatePhonenum(@Param("phonenum") String phonenum,@Param("userid") String userid);

    /**
     * 修改密码
     * @param
     * @return
     */
    @Modifying
    @Query("update Userright userright set userright.pwd=:pwd where userright.userid=:userid")
    int updatePwd(@Param("pwd") String pwd,@Param("userid") String userid);

    @Modifying
    @Query("update Userright a set a.devicetoken =null ,a.devicetype=null where a.userid=:userid")
    int updateDevice(@Param("userid") String userid);

    @Modifying
    @Query("update Userright a set a.devicetoken=:devicetoken where a.userid=:userid")
    int updateDeviceTokenById(@Param("devicetoken") String devicetoken,@Param("userid") String userid);
}
