package com.hc.mapper;

import com.hc.entity.Warningrecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-04.
 */
@Transactional
public interface WarningRecordDao extends JpaRepository<Warningrecord,Integer> {

    /**
     *  修改当前报警记录是否已读
     * @param pushstate
     * @param instrumentparamconfigNO
     * @return
     */
    @Modifying
    @Query("update Warningrecord  warningrecord set warningrecord.pushstate =:pushstate  where warningrecord.instrumentparamconfigNO =:instrumentparamconfigNO")
    Integer updatePushState(@Param("pushstate") String pushstate,@Param("instrumentparamconfigNO") String instrumentparamconfigNO);

    @Modifying
    @Query("update Warningrecord a set msgflag = '1' where a.instrumentparamconfigNO =:instrumentparamconfigNO")
    Integer updateMsgFlag(@Param("instrumentparamconfigNO") String instrumentparamconfigNO);

    @Modifying
    @Query("delete from Warningrecord  a where a.instrumentparamconfigNO =:instrumentparamconfigNO")
    Integer deleteByInstrumentparamconfigNO(@Param("instrumentparamconfigNO") String instrumentparamconfigNO);

}
