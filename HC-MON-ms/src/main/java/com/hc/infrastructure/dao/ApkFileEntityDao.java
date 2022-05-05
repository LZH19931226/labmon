package com.hc.infrastructure.dao;

import com.hc.po.ApkFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApkFileEntityDao  extends JpaRepository<ApkFileEntity,String>{

    @Query(value = "SELECT * FROM ApkFileEntity a ORDER BY a.date desc LIMIT 1",nativeQuery = true)
    ApkFileEntity getlastApkFileEntity();
}
