package com.hc.timer;

import com.alibaba.fastjson.JSON;
import com.hc.device.SnDeviceRedisApi;
import com.hc.mapper.HospitalInfoMapper;
import com.hc.model.TimeoutEquipment;
import com.hc.my.common.core.redis.command.EquipmentInfoCommand;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.service.MessagePushService;
import com.hc.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 15350 on 2019/10/8.
 */
@Component
@Slf4j
@EnableScheduling
public class TimerConfig {

    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;
    @Autowired
    private MessagePushService messagePushService;
    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;

    //
    @Scheduled(cron = "30 * * * * ?")
    public void Time() {
        // 查询所有需要超时报警的设备
        List<TimeoutEquipment> timeoutEquipments = hospitalInfoMapper.getTimeoutEquipment();
        if (CollectionUtils.isEmpty(timeoutEquipments)) {
            return;
        }
        Map<String, List<TimeoutEquipment>> map =
                timeoutEquipments.stream().collect(Collectors.groupingBy(TimeoutEquipment::getHospitalcode));
        Map<String, Map<String, Map<String, List<TimeoutEquipment>>>> timeoutEquipmentMap =
                       timeoutEquipments.stream().collect(Collectors.groupingBy(TimeoutEquipment::getHospitalcode, Collectors.groupingBy(TimeoutEquipment::getEquipmenttypeid, Collectors.groupingBy(TimeoutEquipment::getEquipmentno))));
        //先以医院分组
        for (String hospitalCode : map.keySet()) {
           List<TimeoutEquipment> codeList = map.get(hospitalCode);
            if(CollectionUtils.isEmpty(codeList)){
                continue;
            }
            List<TimeoutEquipment> timeoutList = new ArrayList<>();
            //再以医院设备类型分组
            Map<String, List<TimeoutEquipment>> equipmentTypeIdMap = codeList.stream().collect(Collectors.groupingBy(TimeoutEquipment::getEquipmenttypeid));
            for (String equipmentTypeId : equipmentTypeIdMap.keySet()) {
                if (!equipmentTypeIdMap.containsKey(equipmentTypeId)) {
                    continue;
                }
                List<TimeoutEquipment> equipmentList = equipmentTypeIdMap.get(equipmentTypeId);
                List<String> equipmentNoList = equipmentList.stream().map(TimeoutEquipment::getEquipmentno).collect(Collectors.toList());
                EquipmentInfoCommand equipmentInfoCommand = new EquipmentInfoCommand();
                equipmentInfoCommand.setHospitalCode(hospitalCode);
                equipmentInfoCommand.setEquipmentNoList(equipmentNoList);
                List<MonitorequipmentlastdataDto> lastDataResult =
                        snDeviceRedisApi.getTheCurrentValueOfTheDeviceInBatches(equipmentInfoCommand).getResult();
                if(CollectionUtils.isEmpty(lastDataResult)){
                   continue;
                }
                int count = 0;
                //遍历当前医院设备类型下的所有设备
                for (MonitorequipmentlastdataDto monitorequipmentlastdataDto : lastDataResult) {
                    Date inputDateTime = monitorequipmentlastdataDto.getInputdatetime();
                    String equipmentNo = monitorequipmentlastdataDto.getEquipmentno();
                    if (MapUtils.isEmpty(timeoutEquipmentMap.get(hospitalCode)) || MapUtils.isEmpty(timeoutEquipmentMap.get(hospitalCode).get(equipmentTypeId))|| CollectionUtils.isEmpty(timeoutEquipmentMap.get(hospitalCode).get(equipmentTypeId).get(equipmentNo)) ) {
                        continue;
                    }
                    List<TimeoutEquipment> timeoutEquipmentList = timeoutEquipmentMap.get(hospitalCode).get(equipmentTypeId).get(equipmentNo);
                    //equipmentNo是唯一键只会有一个
                    TimeoutEquipment timeoutEquipment = timeoutEquipmentList.get(0);
                    //-------------当clientvisib的值为0时表示未开启超时报警-------------
                    if (StringUtils.isNotEmpty(timeoutEquipment.getClientvisible()) && StringUtils.equals("0",timeoutEquipment.getClientvisible())) {
                        continue;
                    }
                    Integer timeoutTime = timeoutEquipment.getTimeouttime();
                    String differenceTime = compareTime(inputDateTime, timeoutTime);
                    if ("2".equals(differenceTime)) {
                        count++;
                        timeoutList.add(timeoutEquipment);
                    }
                }
//                if(count>0 && !CollectionUtils.isEmpty(timeoutList)){
//                    TimeoutMessage timeoutMessage = new TimeoutMessage();
//                    timeoutMessage.setInteger(count);
//                    timeoutMessage.setHospitalCode(hospitalCode);
//                    timeoutMessage.setEquipmentTypeId(equipmentTypeId);
//                    timeoutMessage.setTimeoutEquipmentList(timeoutList);
//                    String string = JSON.toJSONString(timeoutMessage);
//                    log.info("超时报警推送:{}", JsonUtil.toJson(string));
//                    messagePushService.pushMessage5(string);
//                }
            }
            if(CollectionUtils.isNotEmpty(timeoutList)){
                String string = JSON.toJSONString(timeoutList);
                log.info("超时报警推送:{}", JsonUtil.toJson(string));
                messagePushService.pushMessage5(string);
            }
        }
    }

    /**
     * 判断如何处理这条记录
     *  当差值小于等于超时时长时不报警
     *  @param date 数据最后上传时间
     * @param timeoutTime 医院设置的超时时长
     * @return
     */
    private String compareTime(Date date,Integer timeoutTime) {
        //如果开启超时未设置超时时长则默认为60分钟
        if(ObjectUtils.isEmpty(timeoutTime)){
            timeoutTime=60;
        }
        Date currentTime = new Date();
        long difference = currentTime.getTime() - date.getTime();
        long minute = difference / 1000 / 60;
        // 不超时报警
        if (minute <= timeoutTime) {
            return "1";
        }
        return "2";
    }
    //过滤禁用报警的设备
        //需要讨论下
//        for (TimeoutEquipment timeoutEquipment : timeoutEquipments) {
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

//        }


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
