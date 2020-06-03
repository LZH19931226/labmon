package com.hc.dao;

import com.hc.entity.Warningrecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-09.
 */
@Transactional
public interface WarningrecordDao extends JpaRepository<Warningrecord,String> {

    @Modifying
    @Query("update Warningrecord a set a.isPhone = '1' where a.pkid =:pkid")
    Integer updatePhone(@Param("pkid") String pkid);

}
