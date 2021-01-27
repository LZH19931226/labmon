//package com.hc.dao;
//
//import com.hc.entity.WarningrecordSort;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
///**
// * @author LiuZhiHao
// * @date 2020/12/28 15:59
// * 描述:
// **/
//@Transactional
//public interface WarningrecordSortDao extends JpaRepository<WarningrecordSort,Integer> {
//
//
//
//
//    @Query("select  a from WarningrecordSort a where a.hospitalcode=:hospitalcode and a.isRead = '0' ")
//    List<WarningrecordSort> getWarningrecordSortByHospitalcodeAAndisRead (@Param("hospitalcode") String hospitalcode);
//}
