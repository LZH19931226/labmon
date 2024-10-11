package com.hc.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.hc.clickhouse.po.Harvester;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.HarvesterRepository;
import com.hc.device.SnDeviceRedisApi;
import com.hc.hospital.HospitalRedisApi;
import com.hc.my.common.core.redis.dto.HarvesterDto;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.service.LastDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by 16956 on 2018-09-04.
 */
@Service
@Slf4j
public class LastDataServiceImpl implements LastDataService {


    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;
    @Autowired
    private HarvesterRepository harvesterRepository;
    @Autowired
    private HospitalRedisApi hospitalRedisApi;

    @Override
    public void saveLastData(Monitorequipmentlastdata monitorequipmentlastdata, String equipmentno, String hospitalcode,String cmdId,String sn) {
        monitorequipmentlastdata.setSn(sn);
        monitorequipmentlastdata.setCmdid(cmdId);
        monitorequipmentlastdata.setEquipmentno(equipmentno);
        HospitalInfoDto hospitalInfoDto = hospitalRedisApi.findHospitalRedisInfo(hospitalcode).getResult();
        //存储时间转换
        monitorequipmentlastdata.setInputdatetime(DateUtils.designatedAreaDate(DateUtils.getChinaTime(),hospitalInfoDto.getZone()));
        monitorequipmentlastdata.setHospitalcode(hospitalcode);
        //数据存储队列
        MonitorequipmentlastdataDto convert = BeanConverter.convert(monitorequipmentlastdata, MonitorequipmentlastdataDto.class);
        log.info("缓存数据入队列得时间:{},数据信息:{}",
                DateUtils.designatedAreaDateLog(DateUtils.getChinaTime(),hospitalInfoDto.getZone()),JSONUtil.toJsonStr(convert));
        snDeviceRedisApi.updateSnCurrentInfo(convert);
    }

    @Override
    public void saveHaverLastData(String messageContent) {
        HarvesterDto harvesterDto = JSONUtil.toBean(messageContent, HarvesterDto.class);
        Harvester convert = BeanConverter.convert(harvesterDto, Harvester.class);
        harvesterRepository.save(convert);
    }
}
