package com.hc.mapper;

import com.hc.entity.ApkFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApkFileEntityDao  extends JpaRepository<ApkFileEntity,String>{

    @Query(value = "SELECT * FROM ApkFileEntity a ORDER BY a.date desc LIMIT 1",nativeQuery = true)
    ApkFileEntity getlastApkFileEntity();
}
