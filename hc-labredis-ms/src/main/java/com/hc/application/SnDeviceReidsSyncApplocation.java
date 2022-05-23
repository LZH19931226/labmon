package com.hc.application;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.hc.application.config.RedisUtils;
import com.hc.my.common.core.redis.command.EquipmentInfoCommand;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import com.hc.my.common.core.redis.namespace.MswkServiceEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class SnDeviceReidsSyncApplocation {

    @Autowired
    private RedisUtils redisUtils;

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
            List<MonitorequipmentlastdataDto> removeList =
                    monitorEquipmentLastDataDtoList.stream().filter(res -> res.getCmdId().equals(monitorequipmentlastdataDto.getCmdId())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(removeList)){
                monitorEquipmentLastDataDtoList.removeAll(removeList);
            }
            monitorEquipmentLastDataDtoList.add(monitorequipmentlastdataDto);
            redisUtils.hset(MswkServiceEnum.L.getCode()+monitorequipmentlastdataDto.getHospitalcode(),
                    monitorequipmentlastdataDto.getEquipmentno(),JSONUtil.toJsonStr(monitorEquipmentLastDataDtoList));
        }else {
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
        boolean flag = redisUtils.hHasKey(MswkServiceEnum.L.getCode() +hospitalCode,equipmentNo);
        if(!flag){
            return null;
        }
        String string = redisUtils.hget(MswkServiceEnum.L.getCode() + hospitalCode, equipmentNo).toString();
        JSONArray objects = JSON.parseArray(string);
       return objects.toJavaList(MonitorequipmentlastdataDto.class);

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
        List<String> equipmentNoList = equipmentInfoCommand.getEquipmentNoList();
        if(equipmentNoList == null || equipmentNoList.size() == 0){
            return null;
        }
        String hospitalCode = equipmentInfoCommand.getHospitalCode();
        List<MonitorequipmentlastdataDto>  monitorequipmentlastdataDtos = new ArrayList<>();
        for (String res: equipmentNoList) {
            List<MonitorequipmentlastdataDto> currentInfo = getCurrentInfo(hospitalCode, res);
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(currentInfo)){
                MonitorequipmentlastdataDto monitorequipmentlastdataDto= buildCurrentData(currentInfo);
                monitorequipmentlastdataDtos.add(monitorequipmentlastdataDto);
            }
        }
        return monitorequipmentlastdataDtos;
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
}
