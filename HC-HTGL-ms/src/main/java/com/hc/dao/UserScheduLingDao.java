package com.hc.dao;

import com.hc.entity.UserScheduLing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author LiuZhiHao
 * @date 2020/7/31 15:13
 * 描述:
 **/
@Transactional
public interface UserScheduLingDao extends JpaRepository<UserScheduLing,Integer> {


    @Query(value = "DELETE FROM userscheduling WHERE  DATE_FORMAT(starttime,'%Y-%m-%d') = ?1 and hospitalcode = ?2", nativeQuery = true)
    @Modifying
    void deleteStHos(String starttime,String hospitalcode);

}
