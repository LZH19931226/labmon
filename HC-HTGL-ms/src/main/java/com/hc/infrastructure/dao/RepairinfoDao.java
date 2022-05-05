package com.hc.infrastructure.dao;

import com.hc.po.Repairinfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 15350 on 2020/5/27.
 */
public interface RepairinfoDao extends JpaRepository<Repairinfo,String> {
}
