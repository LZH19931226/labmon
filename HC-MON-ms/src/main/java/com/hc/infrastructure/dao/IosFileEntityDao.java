package com.hc.infrastructure.dao;


import com.hc.po.IosFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IosFileEntityDao extends JpaRepository<IosFileEntity,String> {

    @Query(value = "SELECT * FROM IosFileEntity a ORDER BY a.date desc LIMIT 1",nativeQuery = true)
    IosFileEntity getlastIosFileEntity();
}
