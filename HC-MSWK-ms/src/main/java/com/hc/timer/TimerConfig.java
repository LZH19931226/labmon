package com.hc.timer;

import com.hc.config.RedisTemplateUtil;
import com.hc.dao.MonitorequipmentDao;
import com.hc.mapper.HospitalInfoMapper;
import com.hc.model.MapperModel.TimeoutEquipment;
import com.hc.service.MessagePushService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 15350 on 2019/10/8.
 */
@Component
public class TimerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerConfig.class);
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;
    @Autowired
    private MessagePushService messagePushService;
    @Autowired
    private MonitorequipmentDao monitorequipmentDao;
    @Scheduled(cron = "0 0/30 * * * ?")   // 从第0分钟开始 每三十分钟一次
    public void Time() {
        //  LocalDateTime localDateTime =LocalDateTime.now();
        //  System.out.println("当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        /**
         * 轮训查询： 半小时一次
         */
        // 查询所有需要超时报警的设备
        LOGGER.info("轮训启动："+TimeHelper.getCurrentTimes());
        List<TimeoutEquipment> timeoutEquipments = hospitalInfoMapper.getTimeoutEquipment();
        if (CollectionUtils.isEmpty(timeoutEquipments)) {
            return;
        }
        HashOperations<Object, Object, Object> redisTemple = redisTemplateUtil.opsForHash();
        LOGGER.info("设置超时设备:"+JsonUtil.toJson(timeoutEquipments));
        int i = 0;
        for (TimeoutEquipment timeoutEquipment :timeoutEquipments  ) {
            i++;
            String equipmentno = timeoutEquipment.getEquipmentno();

            if (!redisTemple.hasKey("timeOut","equipmentno:"+equipmentno)){
//                // 不存在数据：// 拨打电话报警
//                timeoutEquipment.setDisabletype("0");// 设备超时报警
//                String s = JsonUtil.toJson(timeoutEquipment);
//                messagePushService.pushMessage5(s);
                continue;

            }else {
                Integer timeouttime = timeoutEquipment.getTimeouttime();// 超时时间设置
                //忘记设置超时时间，则为空
                if (timeouttime == null){
                    continue;
                }
                // 存在数据，进行时间对比
                String timeOut = (String)redisTemple.get("timeOut", "equipmentno:" + equipmentno);
                // 时间对比
                int datePoors = TimeHelper.getDatePoors(timeOut);
                LOGGER.info("时间差："+datePoors);
                String time = time(datePoors, timeouttime);
                LOGGER.info("报警类型："+time);
                switch (time){
                    case "2":
                        LOGGER.info("进入超时报警："+JsonUtil.toJson(timeoutEquipment));
                        // 超时报警
                        timeoutEquipment.setDisabletype("2");
                        timeoutEquipment.setTimeouttime(datePoors);
                        String s = JsonUtil.toJson(timeoutEquipment);
                        messagePushService.pushMessage5(s);
                        break;
                    case "3":
                        //禁用设备报警
                        LOGGER.info("进入设备禁用报警："+JsonUtil.toJson(timeoutEquipment));
                        timeoutEquipment.setDisabletype("3");
                        String s1 = JsonUtil.toJson(timeoutEquipment);
                        messagePushService.pushMessage5(s1);
                        //设备禁用
                        monitorequipmentDao.updateMonitorequipmentAble(equipmentno);
                        //存入redis
                        redisTemple.put("disable","equipmentno:"+equipmentno,"1");
                        break;
                }

            }
        }
        LOGGER.info("循环次数："+i);

    }

    /**
     * 超时时间计算方式
     * @param i 当前时间与最后上传数据时间差
     * @param j 医院设备类型设置超时时间
     * @return
     */
    public String time(int i,int j){
        if(i < j){
            return "1";  // 不超时报警
        } else if ( i > j && i < j + 60){
            return "2"; // 超时报警
        }else if (i > j+60){
            return "3"; // 设备停用
        }
        return null;
    }

}
