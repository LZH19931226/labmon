package com.hc.timer;

import com.alibaba.fastjson.JSON;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.device.SnDeviceRedisApi;
import com.hc.mapper.HospitalInfoMapper;
import com.hc.model.TimeoutEquipment;
import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.my.common.core.redis.command.EquipmentInfoCommand;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.redis.dto.WarningrecordRedisInfo;
import com.hc.my.common.core.redis.namespace.MswkServiceEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.service.MessagePushService;
import com.hc.utils.JsonUtil;
import com.hc.warning.WarningApi;
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

    @Autowired
    private WarningApi warningApi;

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;

    @Autowired
    private WarningrecordRepository warningrecordRepository;

    //没分钟的第30秒执行
    @Scheduled(cron = "0 0/30 * * * ?")
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
            if (CollectionUtils.isEmpty(codeList)) {
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
                if (CollectionUtils.isEmpty(equipmentNoList) || StringUtils.isEmpty(hospitalCode)) {
                    continue;
                }
                EquipmentInfoCommand equipmentInfoCommand = new EquipmentInfoCommand();
                equipmentInfoCommand.setHospitalCode(hospitalCode);
                equipmentInfoCommand.setEquipmentNoList(equipmentNoList);
                List<MonitorequipmentlastdataDto> lastDataResult =
                        snDeviceRedisApi.getTheCurrentValueOfTheDeviceInBatches(equipmentInfoCommand).getResult();
                if (CollectionUtils.isEmpty(lastDataResult)) {
                    continue;
                }
                int count = 0;
                TimeoutEquipment timeoutEquipment = null;
                //遍历当前医院设备类型下的所有设备
                for (MonitorequipmentlastdataDto monitorequipmentlastdataDto : lastDataResult) {
                    Date inputDateTime = monitorequipmentlastdataDto.getInputdatetime();
                    String equipmentNo = monitorequipmentlastdataDto.getEquipmentno();
                    if (MapUtils.isEmpty(timeoutEquipmentMap.get(hospitalCode)) || MapUtils.isEmpty(timeoutEquipmentMap.get(hospitalCode).get(equipmentTypeId)) || CollectionUtils.isEmpty(timeoutEquipmentMap.get(hospitalCode).get(equipmentTypeId).get(equipmentNo))) {
                        continue;
                    }
                    List<TimeoutEquipment> timeoutEquipmentList = timeoutEquipmentMap.get(hospitalCode).get(equipmentTypeId).get(equipmentNo);
                    //equipmentNo是唯一键只会有一个
                    TimeoutEquipment equipment = timeoutEquipmentList.get(0);
                    //-------------当clientvisib的值为0时表示未开启超时报警-------------
                    if (StringUtils.isNotEmpty(equipment.getClientvisible()) && !StringUtils.equals(DictEnum.TURN_ON.getCode(), equipment.getClientvisible())) {
                        continue;
                    }
                    Integer timeoutTime = equipment.getTimeouttime();
                    String differenceTime = compareTime(inputDateTime, timeoutTime);
                    if ("2".equals(differenceTime)) {
                        if (count == 0) {
                            timeoutEquipment = equipment;
                        }
                        count++;
                    }
                }
                if (!ObjectUtils.isEmpty(timeoutEquipment)) {
                    timeoutEquipment.setCount(count + "");
                    timeoutList.add(timeoutEquipment);
                }
            }
            if (CollectionUtils.isNotEmpty(timeoutList)) {
                String string = JSON.toJSONString(timeoutList);
                log.info("超时报警推送:{}", JsonUtil.toJson(string));
                messagePushService.pushMessage5(string);
            }
        }
    }

    /**
     * 判断如何处理这条记录
     * 当差值小于等于超时时长时不报警
     *
     * @param date        数据最后上传时间
     * @param timeoutTime 医院设置的超时时长
     * @return
     */
    private String compareTime(Date date, Integer timeoutTime) {
        //如果开启超时未设置超时时长则默认为60分钟
        if (ObjectUtils.isEmpty(timeoutTime)) {
            timeoutTime = 60;
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

    //每分钟执行一次
    @Scheduled(cron = "*/1 * * * *")
    public void Timing() {
        ApiResponse<Long> lastDataListSize = snDeviceRedisApi.getLastDataListSize(MswkServiceEnum.LAST_DATA.getCode());
        if (null == lastDataListSize) {
            return;
        }
        Long size = lastDataListSize.getResult();
        if (size == 0) {
            return;
        }
        List<MonitorequipmentlastdataDto> list = new ArrayList<>();
        for (long i = 0; i < size; i++) {
            MonitorequipmentlastdataDto monitorequipmentlastdataDto = snDeviceRedisApi.getLeftPopLastData(MswkServiceEnum.LAST_DATA.getCode()).getResult();
            if (null != monitorequipmentlastdataDto) {
                list.add(monitorequipmentlastdataDto);
            }
        }
        List<Monitorequipmentlastdata> convert = BeanConverter.convert(list, Monitorequipmentlastdata.class);
        convert.forEach(monitorequipmentlastdata -> {
            monitorequipmentlastdata.setId(DateUtils.getCurrentYYMM());
        });
        monitorequipmentlastdataRepository.batchInsert(convert);
    }

    @Scheduled(cron = "*/1 * * * *")
    public void waring() {
        ApiResponse<Long> warningRecordSize = warningApi.getWarningRecordSize(MswkServiceEnum.WARNING_RECORD.getCode());
        if (null == warningRecordSize) {
            return;
        }
        Long size = warningRecordSize.getResult();
        if (size ==null || size == 0) {
            return;
        }
        List<WarningrecordRedisInfo> list = new ArrayList<>();
        for (long i = 0; i < size; i++) {
            WarningrecordRedisInfo warningrecordRedisInfo = warningApi.getLeftPopWarningRecord(MswkServiceEnum.WARNING_RECORD.getCode()).getResult();
            if (null != warningrecordRedisInfo) {
                list.add(warningrecordRedisInfo);
            }
        }
        List<Warningrecord> convert = BeanConverter.convert(list, Warningrecord.class);
        convert.forEach(res -> {
            res.setId(DateUtils.getCurrentYYMM());
        });
        warningrecordRepository.batchInsert(convert);
    }
}
