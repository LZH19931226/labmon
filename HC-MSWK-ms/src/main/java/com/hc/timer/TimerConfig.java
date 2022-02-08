package com.hc.timer;

import com.hc.config.RedisTemplateUtil;
import com.hc.dao.MonitorequipmentDao;
import com.hc.entity.Hospitalofreginfo;
import com.hc.entity.Monitorequipmentlastdata;
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
    //
    @Scheduled(cron = "0 0 * * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void Time() {
        // 查询所有需要超时报警的设备
        List<TimeoutEquipment> timeoutEquipments = hospitalInfoMapper.getTimeoutEquipment();
        if (CollectionUtils.isEmpty(timeoutEquipments)) {
            return;
        }
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        LOGGER.info("设置超时设备:" + JsonUtil.toJson(timeoutEquipments));
        //过滤禁用报警的设备
        for (TimeoutEquipment timeoutEquipment : timeoutEquipments) {
            String equipmentno = timeoutEquipment.getEquipmentno();
            // 超时时间设置
            Integer timeouttime = timeoutEquipment.getTimeouttime();
            String hospitalcode = timeoutEquipment.getHospitalcode();
            //忘记设置超时时间，则为空
            if (timeouttime == null) {
                continue;
            }
            //对报警区间进行判断
            BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("hospital:info");
            String o = (String) objectObjectObjectBoundHashOperations.get(hospitalcode);
            if (StringUtils.isEmpty(o)) {
                continue;
            }
            Hospitalofreginfo  hospitalofreginfo = JsonUtil.toBean(o, Hospitalofreginfo.class);
            if (null==hospitalofreginfo){
                continue;
            }
            String alwayalarm = hospitalofreginfo.getAlwayalarm();
            //报警区间
            if (!StringUtils.equals(alwayalarm, "1")) {
                Date starttime = hospitalofreginfo.getBegintime();
                Date endtime = hospitalofreginfo.getEndtime();
                boolean b = DateUtils.isEffectiveDate(new Date(), starttime, endtime);
                if (b) {
                    continue;
                }
            }
            String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, equipmentno);
            if (StringUtils.isEmpty(lastdata)) {
                continue;
            }
            Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
            if (null==monitorequipmentlastdata){
                continue;
            }
            Date timeOut = monitorequipmentlastdata.getInputdatetime();
            // 时间对比
            int datePoors = TimeHelper.getDatePoors(timeOut);
            String time = time(datePoors, timeouttime);
            if (StringUtils.isEmpty(time)) {
                continue;
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
                default:
                    break;
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
