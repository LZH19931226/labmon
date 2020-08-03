package com.hc.dao;

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


    @Query(nativeQuery = true,value = "select  * from userscheduling where hospitalcode = ?1  and  DATE_FORMAT(starttime,'%Y-%m-%d') = ?2")
    List<UserScheduLing> findUserScByHosSt(String hospitalcode,String starttime);

    @Query(nativeQuery = true,value = "SELECT *,ABS( NOW( ) - startTime ) AS diffTime  FROM userscheduling ORDER BY createtime desc limit 1")
    UserScheduLing selectRecentlyUs();
}
