//package com.hc.task;
//
//import com.hc.mapper.laboratoryFrom.MonitorequipmentlastdataMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.time.LocalDate;
//
///**
// * @author LiuZhiHao
// * @date 2020/8/31 17:50
// * 描述:
// **/
//@EnableScheduling
//@Configuration
//public class MonitorequipmentlastdataTask {
//
//    @Autowired
//    private MonitorequipmentlastdataMapper monitorequipmentlastdataDao;
//
//
//
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void taskEndFissionWanted() {
//        LocalDate date = LocalDate.now();
//        date = date.minusMonths(1);
//        int year = date.getYear();
//        int monthValue= date.getMonthValue();
//        int dayOfMonth = date.getDayOfMonth();
//        if (dayOfMonth==1){
//            //获取上个月表名
//            String tableName = "monitorequipmentlastdata" + "_" + year + "0"+monthValue;
//            monitorequipmentlastdataDao.altetTableMonitorequipmentlastdata(tableName);
//            monitorequipmentlastdataDao.createTableMonitorequipmentlastdata(tableName);
//        }
//
//
//    }
//
//}
