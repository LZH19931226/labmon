package com.hc.dao;

import com.hc.entity.WarningrecordSort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * @author LiuZhiHao
 * @date 2020/12/28 15:59
 * 描述:
 **/
@Transactional
public interface WarningrecordSortDao extends JpaRepository<WarningrecordSort,Integer> {
}
