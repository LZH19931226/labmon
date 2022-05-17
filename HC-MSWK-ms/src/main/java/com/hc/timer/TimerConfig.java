package com.hc.timer;

import com.hc.po.Monitorequipmentlastdata;
import com.hc.mapper.HospitalInfoMapper;
import com.hc.model.TimeoutEquipment;
import com.hc.service.MessagePushService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by 15350 on 2019/10/8.
 */
@Component
@Slf4j
public class TimerConfig {

    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;
    @Autowired
    private MessagePushService messagePushService;
    //
    @Scheduled(cron = "0 0 * * * ?")
    public void Time() {
        // 查询所有需要超时报警的设备
        List<TimeoutEquipment> timeoutEquipments = hospitalInfoMapper.getTimeoutEquipment();
        if (CollectionUtils.isEmpty(timeoutEquipments)) {
            return;
        }
        //过滤禁用报警的设备
        //需要讨论下
        for (TimeoutEquipment timeoutEquipment : timeoutEquipments) {
//            String equipmentno = timeoutEquipment.getEquipmentno();
//            // 超时时间设置
//            Integer timeouttime = timeoutEquipment.getTimeouttime();
//            String hospitalcode = timeoutEquipment.getHospitalcode();
//            //忘记设置超时时间，则为空
//            if (timeouttime == null) {
//                continue;
//            }
//            String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, equipmentno);
//            if (StringUtils.isEmpty(lastdata)) {
//                continue;
//            }
//            Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
//            if (null==monitorequipmentlastdata){
//                continue;
//            }
//            Date timeOut = monitorequipmentlastdata.getInputdatetime();
//            // 时间对比
//            int datePoors = TimeHelper.getDatePoors(timeOut);
//            String time = time(datePoors, timeouttime);
//            if (StringUtils.isEmpty(time)) {
//                continue;
//            }
//            if ("2".equals(time)) {// 超时报警
//                timeoutEquipment.setDisabletype("2");
//                timeoutEquipment.setTimeouttime(datePoors);
//                String s = JsonUtil.toJson(timeoutEquipment);
//                log.info("超时报警推送:{}", JsonUtil.toJson(timeoutEquipment));
//                messagePushService.pushMessage5(s);
//            }

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
