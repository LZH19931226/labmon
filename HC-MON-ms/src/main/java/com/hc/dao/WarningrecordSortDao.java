package com.hc.dao;

import com.hc.entity.WarningrecordSort;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author LiuZhiHao
 * @date 2020/12/28 15:59
 * 描述:
 **/
@Transactional
public interface WarningrecordSortDao extends JpaRepository<WarningrecordSort,Integer> {


    @Modifying
    @Query("update WarningrecordSort  ws set ws.isRead ='1' where ws.id <=:warningRecordSortId")
    void zfbwarningRuleSend(String warningRecordSortId);


}
