package com.hc.serviceimpl;

import com.hc.device.ProbeRedisApi;
import com.hc.hospital.HospitalRedisApi;
import com.hc.labmanagent.ProbeInfoApi;
import com.hc.model.WarningModel;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import com.hc.service.WarningRuleService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by 16956 on 2018-08-10.
 */
@Service
@Slf4j
public class WarningRuleServiceImpl implements WarningRuleService {
    @Autowired
    private ProbeRedisApi probeRedisApi;
    @Autowired
    private ProbeInfoApi  probeInfoApi;
    @Autowired
    private HospitalRedisApi hospitalRedisApi;
    /**
     * 进来的默认都是启用报警的
     * 先判断医院   、  在进行判断是否三次报警
     */
    @Override
    public WarningModel warningRule(String hospitalcode, String pkid, String data, InstrumentInfoDto probe) {
        WarningModel warningModel = new WarningModel();
        /*3.医院报警关联 如果是市电则直接报警*/
        //市电是立即报警
        Integer instrumentConfigId = probe.getInstrumentConfigId();
        if( instrumentConfigId != null && instrumentConfigId.equals(10) && "1".equals(data) ){
            warningModel.setPkid(pkid);
            warningModel.setValue(data);
            warningModel.setEquipmentname(probe.getEquipmentName());
            warningModel.setUnit(probe.getInstrumentName());
            warningModel.setHospitalcode(hospitalcode);
            return warningModel;
        }
        /*4.报警次数设置*/
        //未满足三次报警次数不推送
        Integer alarmtime = probe.getAlarmTime();
        if (null == alarmtime) {
            alarmtime = 3;
        }
        String instrumentParamConfigNO = probe.getInstrumentParamConfigNO();
        List<WarningRecordDto> warningRecord = probeRedisApi.getProbeWarnInfo(hospitalcode, instrumentParamConfigNO).getResult();
        //判断是否三次报警
        if (CollectionUtils.isEmpty(warningRecord)) {
            probeRedisApi.addProbeWarnInfo(buildProbeWarnInfo(hospitalcode, instrumentParamConfigNO, data));
            log.info("当前设备产生报警记录但是还未报警通知:{}", JsonUtil.toJson(probe));
            return null;
        } else {
            if (warningRecord.size()<alarmtime){
                probeRedisApi.addProbeWarnInfo(buildProbeWarnInfo(hospitalcode, instrumentParamConfigNO, data));
                log.info("当前设备产生报警记录但是还未报警通知:{}", JsonUtil.toJson(probe));
                return null;
            }else {//这里已确认报警检测到异样已经有三次了
                probeRedisApi.removeProbeWarnInfo(hospitalcode, instrumentParamConfigNO);
                warningModel.setPkid(pkid);
                warningModel.setValue(data);
                warningModel.setEquipmentname(probe.getEquipmentName());
                warningModel.setUnit(probe.getInstrumentName());
                warningModel.setHospitalcode(hospitalcode);
            }
        }
        Date warningtime = probe.getWarningtime();
        if (null!=warningtime) {
             //根据医院设置得报警时间间隔
            HospitalInfoDto hospital = hospitalRedisApi.findHospitalRedisInfo(hospitalcode).getResult();
            String timeInterval = hospital.getTimeInterval();
            if (StringUtils.isEmpty(timeInterval)){
                //未设置间隔时间则默认是60分钟
                timeInterval= "60";
            }
            double datePoorMin = TimeHelper.getDatePoorMin(warningtime);
            if (datePoorMin > Double.parseDouble(timeInterval)) {
                //可以报警
                probeInfoApi.editWarningTime(instrumentParamConfigNO,TimeHelper.getNowDate(new Date()));
                probe.setWarningtime(new Date());
                //同步缓存
                probeRedisApi.addProbeRedisInfo(probe);
            } else {
                return null;
            }
        } else {
            probeInfoApi.editWarningTime(instrumentParamConfigNO,TimeHelper.getNowDate(new Date()));
            probe.setWarningtime(new Date());
            //同步缓存
            probeRedisApi.addProbeRedisInfo(probe);
        }
        return warningModel;
    }

    private WarningRecordDto buildProbeWarnInfo(String hospitalcode, String instrumentParamConfigNO, String value) {
        WarningRecordDto warningRecordDto = new WarningRecordDto();
        warningRecordDto.setInputdatetime(new Date());
        warningRecordDto.setHospitalcode(hospitalcode);
        warningRecordDto.setInstrumentparamconfigNO(instrumentParamConfigNO);
        warningRecordDto.setWarningvalue(value);
        return warningRecordDto;
    }
}
