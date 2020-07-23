package com.hc.timer;

import com.hc.config.RedisTemplateUtil;
import com.hc.dao.MonitorequipmentDao;
import com.hc.entity.Hospitalofreginfo;
import com.hc.mapper.HospitalInfoMapper;
import com.hc.model.MapperModel.TimeoutEquipment;
import com.hc.my.common.core.util.DateUtils;
import com.hc.service.MessagePushService;
import com.hc.service.ThirdPartyService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Scheduled(cron = "0 0 * * * ?")
    public void Time() {
        // 查询所有需要超时报警的设备
        List<TimeoutEquipment> timeoutEquipments = hospitalInfoMapper.getTimeoutEquipment();
        if (CollectionUtils.isEmpty(timeoutEquipments)) {
            return;
        }
        HashOperations<Object, Object, Object> redisTemple = redisTemplateUtil.opsForHash();
        LOGGER.info("设置超时设备:" + JsonUtil.toJson(timeoutEquipments));

        //过滤禁用报警的设备

        for (TimeoutEquipment timeoutEquipment : timeoutEquipments) {
            String equipmentno = timeoutEquipment.getEquipmentno();
            if (redisTemple.hasKey("timeOut", "equipmentno:" + equipmentno)) {
                // 超时时间设置
                Integer timeouttime = timeoutEquipment.getTimeouttime();
                String hospitalcode = timeoutEquipment.getHospitalcode();
                //忘记设置超时时间，则为空
                if (timeouttime == null) {
                    continue;
                }
                // 存在数据，进行时间对比
                String timeOut = (String) redisTemple.get("timeOut", "equipmentno:" + equipmentno);
                // 时间对比
                int datePoors = TimeHelper.getDatePoors(timeOut);
                String time = time(datePoors, timeouttime);
                if (StringUtils.isEmpty(time)) {
                    return;
                }
                //对报警区间进行判断
                Hospitalofreginfo hospitalofreginfo;
                BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("hospital:info");
                String o = (String) objectObjectObjectBoundHashOperations.get(hospitalcode);
                if (StringUtils.isNotEmpty(o)) {
                    hospitalofreginfo = JsonUtil.toBean(o, Hospitalofreginfo.class);
                } else {
                    LOGGER.info("不存在当前医院信息，医院编号：" + hospitalcode);
                    return;
                }
                String alwayalarm = hospitalofreginfo.getAlwayalarm();
                //报警区间
                if (!StringUtils.equals(alwayalarm, "1")) {
                    Date starttime = hospitalofreginfo.getBegintime();
                    Date endtime = hospitalofreginfo.getEndtime();
                    boolean b = DateUtils.isEffectiveDate(new Date(), starttime, endtime);
                    if (b) {
                        return;
                    }

                }
                switch (time) {
                    case "2":
                        // 超时报警
                        timeoutEquipment.setDisabletype("2");
                        timeoutEquipment.setTimeouttime(datePoors);
                        String s = JsonUtil.toJson(timeoutEquipment);
                        LOGGER.info("超时报警推送:{}", JsonUtil.toJson(timeoutEquipment));
                        messagePushService.pushMessage5(s);
                        break;
//                    case "3":
//                        //超时继续报警,但不禁用设备
//                        if (StringUtils.equalsAnyIgnoreCase(hospitalcode,"H24")){
//                            timeoutEquipment.setTimeouttime(datePoors);
//                            thirdPartyService.disableAlarm(timeoutEquipment);
//                            break;
//                        }
//                        //禁用设备报警
//                        timeoutEquipment.setDisabletype("3");
//                        String s1 = JsonUtil.toJson(timeoutEquipment);
//                        LOGGER.info("禁用报警推送:{}",JsonUtil.toJson(timeoutEquipment));
//                        messagePushService.pushMessage5(s1);
//                        //设备禁用
//                        monitorequipmentDao.updateMonitorequipmentAble(equipmentno);
//                        //存入redis
//                        redisTemple.put("disable", "equipmentno:" + equipmentno, "1");
//                        break;
                    default:
                        break;
                }

            }
        }

    }

    /**
     * 超时时间计算方式
     *
     * @param i 当前时间与最后上传数据时间差
     * @param j 医院设备类型设置超时时间
     * @return
     */
    private String time(int i, int j) {
        // 不超时报警
        if (i < j) {
            return "1";
            // 超时报警
        } else if (i > j && i < j + 60) {
            return "2";
            // 设备停用
        } else if (i > j + 60) {
            return "2";
        }
        return null;
    }

}
