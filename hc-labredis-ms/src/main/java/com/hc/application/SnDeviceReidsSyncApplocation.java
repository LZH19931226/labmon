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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        redisUtils.hset(LabManageMentServiceEnum.DEVICEINFO.getCode(),snDeviceDto.getSn(), JSONUtil.toJsonStr(snDeviceDto));
    }

    /**
     * 获取设备redis缓存信息
     * @param sn
     * @return
     */
    public SnDeviceDto getSnDeviceDto(String sn) {
        Object snInfo = redisUtils.hget(LabManageMentServiceEnum.DEVICEINFO.getCode(),sn);
        if (null==snInfo){
            return null;
        }
        return JSONUtil.toBean((String) snInfo,SnDeviceDto.class);
    }

    /**
     * 删除设备redis缓存信息
     * @param sn
     */
    public void deleteSnDeviceDto(String sn) {
        redisUtils.hdel(LabManageMentServiceEnum.DEVICEINFO.getCode(),sn);
    }

    /**
     * 新增和更新当前信息
     * @param monitorequipmentlastdataDto
     */
    public void updateSnCurrentInfo(MonitorequipmentlastdataDto monitorequipmentlastdataDto) {
        //获取获取设备当前值
        List<MonitorequipmentlastdataDto>   monitorEquipmentLastDataDtoList =
                getCurrentInfo(monitorequipmentlastdataDto.getHospitalcode(), monitorequipmentlastdataDto.getEquipmentno());
        if(CollectionUtils.isNotEmpty(monitorEquipmentLastDataDtoList)){
            String cmdId = monitorequipmentlastdataDto.getCmdId();
            String sn = monitorequipmentlastdataDto.getSn();
            List<MonitorequipmentlastdataDto> removeList = new ArrayList<>();
            for (MonitorequipmentlastdataDto dto : monitorEquipmentLastDataDtoList) {
                boolean empty = StringUtils.isEmpty(dto.getSn());
                if(empty){
                    if(cmdId.equals(dto.getCmdId())){
                        removeList.add(dto);
                    }
                }else {
                    if(cmdId.equals(dto.getCmdId()) && sn.equals(dto.getSn())){
                        removeList.add(dto);
                    }
                }
            }
//            List<MonitorequipmentlastdataDto> removeList = monitorEquipmentLastDataDtoList.stream()
//                            .filter(res -> res.getCmdId().equals(cmdId))
//                            .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(removeList)){
                monitorEquipmentLastDataDtoList.removeAll(removeList);
            }
            monitorEquipmentLastDataDtoList.add(monitorequipmentlastdataDto);
            redisUtils.hset(MswkServiceEnum.L.getCode()+monitorequipmentlastdataDto.getHospitalcode(),
                    monitorequipmentlastdataDto.getEquipmentno(),JSONUtil.toJsonStr(monitorEquipmentLastDataDtoList));
        }
        else {
            List<MonitorequipmentlastdataDto> monitorList = new ArrayList<>();
            monitorList.add(monitorequipmentlastdataDto);
            redisUtils.hset(MswkServiceEnum.L.getCode()+monitorequipmentlastdataDto.getHospitalcode(),
                    monitorequipmentlastdataDto.getEquipmentno(),JSONUtil.toJsonStr(monitorList));
        }
    }

    /**
     * 获取当前设备的值
     * @param hospitalCode 医院id
     * @param equipmentNo 设备id
     * @return
     */
    public List<MonitorequipmentlastdataDto> getCurrentInfo(String hospitalCode,String equipmentNo){
        Object lastData = redisUtils.hget(MswkServiceEnum.L.getCode() + hospitalCode, equipmentNo);
        if(ObjectUtils.isEmpty(lastData)){
            return null;
        }
        cn.hutool.json.JSONArray objects = JSONUtil.parseArray(lastData);
       return  objects.toList(MonitorequipmentlastdataDto.class);

    }

    /**
     * 清除redis信息
     * @param hospitalCode 医院id
     * @param equipmentNo 设备id
     */
    public void remove(String hospitalCode, String equipmentNo) {
        redisUtils.hdel(MswkServiceEnum.L.getCode() +hospitalCode,equipmentNo);
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
        List<Object> collect = objects.stream().filter(res -> ObjectUtils.isEmpty(res)).collect(Collectors.toList());
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
            redisUtils.hset(LabManageMentServiceEnum.DEVICEINFO.getCode(),sn,JSONUtil.toJsonStr(snDeviceDto));
        }
    }
}
