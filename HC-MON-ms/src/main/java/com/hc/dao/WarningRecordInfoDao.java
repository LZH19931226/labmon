package com.hc.mapper;

import com.hc.entity.WarningRecordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * @author LiuZhiHao
 * @date 2020/6/8 15:41
 * 描述:
 **/
@Transactional
public interface WarningRecordInfoDao extends JpaRepository<WarningRecordInfo,Integer> {

    @Modifying
    @Query("update WarningRecordInfo wa set wa.info =:info,wa.updateuser=:updateuser  where wa.id=:id ")
    int updateWarningRecordInfo(@Param("info") String info, @Param("updateuser") String updateuser,@Param("id") int id);
}
