package com.hc.infrastructure.dao;

import com.hc.entity.UserScheduLing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/7/31 15:13
 * 描述:
 **/
@Transactional
public interface UserScheduLingDao extends JpaRepository<UserScheduLing,Integer> {


    @Query(value = "DELETE FROM userscheduling WHERE hospitalcode=?1 and starttime BETWEEN ?2 and ?3 ", nativeQuery = true)
    @Modifying
    void deleteStHos(String hospitalcode,String starttime,String endtime);

    List<UserScheduLing> queryByHospitalcode(String hospitalcode);
}
