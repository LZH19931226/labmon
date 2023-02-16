package com.hc.application;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hc.application.config.RedisUtils;
import com.hc.labmanagent.MonitorEquipmentApi;
import com.hc.my.common.core.redis.command.EquipmentInfoCommand;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import com.hc.my.common.core.redis.namespace.MswkServiceEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class SnDeviceReidsSyncApplocation {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MonitorEquipmentApi monitorEquipmentApi;
    /**
     * 添加或更新设备缓存信息
     * @param snDeviceDto
     */
    public void updateSnDeviceDtoSync(SnDeviceDto snDeviceDto) {
        String sn = snDeviceDto.getSn();
        String equipmentNo = snDeviceDto.getEquipmentNo();
        List<SnDeviceDto> snDevices = getSnDeviceDto(sn);
        if(snDevices==null){
            List<SnDeviceDto> list = new ArrayList<>();
            list.add(snDeviceDto);
            redisUtils.hset(LabManageMentServiceEnum.DEVICEINFO.getCode(),snDeviceDto.getSn(), JSONUtil.toJsonStr(list));
        } else{
            List<SnDeviceDto> removeList = new ArrayList<>();
            for (SnDeviceDto snDevice : snDevices) {
                String eno = snDevice.getEquipmentNo();
                if(eno.equals(equipmentNo)){
                    removeList.add(snDevice);
                }
            }
            if(!CollectionUtils.isEmpty(removeList)){
                snDevices.removeAll(removeList);
            }
            snDevices.add(snDeviceDto);
            redisUtils.hset(LabManageMentServiceEnum.DEVICEINFO.getCode(),snDeviceDto.getSn(), JSONUtil.toJsonStr(snDevices));
        }

    }

    /**
     * 获取设备redis缓存信息
     * @param sn
     * @return
     */
    public List<SnDeviceDto> getSnDeviceDto(String sn) {
        Object snInfo = redisUtils.hget(LabManageMentServiceEnum.DEVICEINFO.getCode(),sn);
        if(snInfo==null){
            return null;
        }
        return JSON.parseArray((String) snInfo, SnDeviceDto.class);
    }

    /**
     * 获取设备redis缓存信息
     * @param sn
     * @param equipmentNo
     * @return
     */
    public SnDeviceDto getSnDeviceDto(String sn, String equipmentNo) {
        List<SnDeviceDto> snDevices = getSnDeviceDto(sn);
        if(CollectionUtils.isEmpty(snDevices)){
            return null;
        }
        for (SnDeviceDto snDevice : snDevices) {
            if(equipmentNo.equals(snDevice.getEquipmentNo())){
                return snDevice;
            }
        }
        return null;
    }

    /**
     * 删除设备redis缓存信息
     * @param sn
     */
    public void deleteSnDeviceDto(String sn,String equipmentNo) {
        List<SnDeviceDto> snDevices = getSnDeviceDto(sn);
        if(CollectionUtils.isNotEmpty(snDevices)){
            List<SnDeviceDto> removeList = snDevices.stream().filter(res -> !StringUtils.equals(equipmentNo,res.getEquipmentNo())).collect(Collectors.toList());
            redisUtils.hset(LabManageMentServiceEnum.DEVICEINFO.getCode(),sn, JSONUtil.toJsonStr(removeList));
        }
    }

    /**
     * 新增和更新当前信息
     * @param monitorequipmentlastdataDto
     */
    public void updateSnCurrentInfo(MonitorequipmentlastdataDto monitorequipmentlastdataDto) {
        //将lastData数据存入redis
        redisUtils.lSet(MswkServiceEnum.LAST_DATA.getCode(), JSON.toJSONString(monitorequipmentlastdataDto));
    }

    /**
     * 批量获取设备当前值
     */
    public  List<MonitorequipmentlastdataDto> getTheCurrentValue(EquipmentInfoCommand equipmentInfoCommand) {
        String hospitalCode = equipmentInfoCommand.getHospitalCode();
        List<String> equipmentNoList = equipmentInfoCommand.getEquipmentNoList();
        if(equipmentNoList == null || equipmentNoList.size() == 0){
            return null;
        }
        List<Object> objects = redisUtils.multiGet(MswkServiceEnum.L.getCode()+hospitalCode, equipmentNoList);
        List<Object> collect = objects.stream().filter(ObjectUtils::isEmpty).collect(Collectors.toList());
        objects.removeAll(collect);
        if(CollectionUtils.isEmpty(objects)){
            return null;
        }
        return parseList(objects);
    }

    public List<MonitorequipmentlastdataDto>  parseList(List<Object> objectList){
        if(CollectionUtils.isEmpty(objectList)){
            return null;
        }
        List<MonitorequipmentlastdataDto> list = new ArrayList<>();
        for (Object object : objectList) {
            List<MonitorequipmentlastdataDto> list1 = JSON.parseArray((String) object, MonitorequipmentlastdataDto.class);
            MonitorequipmentlastdataDto monitorequipmentlastdataDto = buildCurrentData(list1);
            list.add(monitorequipmentlastdataDto);
        }
        return list;
    }

    /**
     * 构建监控设备最新的数据信息
     * @param currentDataInfo
     * @return
     */
    public MonitorequipmentlastdataDto buildCurrentData(List<MonitorequipmentlastdataDto> currentDataInfo){
        if(org.apache.commons.collections.CollectionUtils.isEmpty(currentDataInfo)){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        for (MonitorequipmentlastdataDto monitorequipmentlastdataDto : currentDataInfo) {
            Map<String,Object> map1 = JSON.parseObject(JSON.toJSONString(monitorequipmentlastdataDto),new TypeReference<Map<String,Object>>(){});
            map.putAll(map1);
        }
        return JSON.parseObject(JSON.toJSONString(map), new TypeReference<MonitorequipmentlastdataDto>(){});
    }

    /**
     *  同步监控设备信息
     */
    public void MonitorEquipmentInfoCache() {
        List<SnDeviceDto> snDeviceDtoList = monitorEquipmentApi.getAllMonitorEquipmentInfo().getResult();
        if (CollectionUtils.isEmpty(snDeviceDtoList)) {
            return;
        }
        //清除所有的sn设备信息
        redisUtils.hDel(LabManageMentServiceEnum.DEVICEINFO.getCode());
        for (SnDeviceDto snDeviceDto : snDeviceDtoList) {
            String sn = snDeviceDto.getSn();
            if(StringUtils.isEmpty(sn)){
                continue;
            }
            updateSnDeviceDtoSync(snDeviceDto);
        }
    }

    public Long getLastDataListSize(String listCode) {
           return  redisUtils.lGetListSize(listCode);
    }

    public MonitorequipmentlastdataDto getLeftPopLastData(String listCode) {
        Object object = redisUtils.lLeftPop(listCode);
        if (null==object){
           return  null;
        }
       return JSON.parseObject((String)object, new TypeReference<MonitorequipmentlastdataDto>(){});
    }


}
