package com.hc.serviceimpl;

import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.device.ProbeRedisApi;
import com.hc.hospital.HospitalRedisApi;
import com.hc.labmanagent.ProbeInfoApi;
import com.hc.model.WarningModel;
import com.hc.my.common.core.constant.enums.ElkLogDetail;
import com.hc.my.common.core.domain.MonitorinstrumentDo;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.service.AlmMsgService;
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
    private ProbeInfoApi probeInfoApi;
    @Autowired
    private HospitalRedisApi hospitalRedisApi;
    @Autowired
    private AlmMsgService almMsgService;
    /**
     * 进来的默认都是启用报警的
     * 先判断医院   、  在进行判断是否三次报警
     */
    @Override
    public WarningModel warningRule(String hospitalcode, Warningrecord warningrecord,InstrumentInfoDto probe, WarningAlarmDo warningAlarmDo) {
        String logId = warningAlarmDo.getLogId();
        MonitorinstrumentDo monitorinstrument = warningAlarmDo.getMonitorinstrument();
        WarningModel warningModel = new WarningModel();
        warningModel.setWarningrecord(warningrecord);
        String data = warningrecord.getWarningValue();
        String pkid = warningrecord.getPkid();
        /*3.医院报警关联 如果是市电则直接报警*/
        //市电是立即报警
        Integer instrumentConfigId = probe.getInstrumentConfigId();
        if (instrumentConfigId != null && instrumentConfigId.equals(10) && "1".equals(data)) {
            ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER08.getCode()), JsonUtil.toJson(probe), logId);
            warningModel.setPkid(warningrecord.getPkid());
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
            ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER09.getCode()), JsonUtil.toJson(probe), logId);
            return null;
        } else {
            if (warningRecord.size() < alarmtime-1) {
                probeRedisApi.addProbeWarnInfo(buildProbeWarnInfo(hospitalcode, instrumentParamConfigNO, data));
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER09.getCode()), JsonUtil.toJson(probe), logId);
                return null;
            } else {//这里已确认报警检测到异样已经有三次了
                probeRedisApi.removeProbeWarnInfo(hospitalcode, instrumentParamConfigNO);
                warningModel.setPkid(pkid);
                warningModel.setValue(data);
                warningModel.setEquipmentname(probe.getEquipmentName());
                warningModel.setUnit(probe.getInstrumentConfigName());
                warningModel.setHospitalcode(hospitalcode);
                warningModel.setInstrumentConfigId(String.valueOf(probe.getInstrumentConfigId()));
                warningModel.setInstrumentparamconfigNO(probe.getInstrumentParamConfigNO());
            }
        }
        //医院设置报警时间间隔规则
        if (alarmRuleHosInterval(probe, hospitalcode, logId)) {
            return null;
        }
        //设备时段规则
        if (!almMsgService.warningTimeBlockRule(monitorinstrument,warningrecord)) {
            ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER13.getCode()), JsonUtil.toJson(probe),logId);
            return null;
        }
        return warningModel;
    }

    public boolean alarmRuleHosInterval(InstrumentInfoDto probe, String hospitalcode, String logId) {
        Date warningtime = probe.getWarningtime();
        if (null != warningtime) {
            //根据医院设置得报警时间间隔
            HospitalInfoDto hospital = hospitalRedisApi.findHospitalRedisInfo(hospitalcode).getResult();
            String timeInterval = hospital.getTimeInterval();
            if (StringUtils.isEmpty(timeInterval)) {
                //未设置间隔时间则默认是60分钟
                timeInterval = "60";
            }
            double datePoorMin = TimeHelper.getDatePoorMin(warningtime);
            if (datePoorMin > Double.parseDouble(timeInterval)) {
                //可以报警
                probeInfoApi.editWarningTime(probe.getInstrumentParamConfigNO(), TimeHelper.getNowDate(new Date()));
                probe.setWarningtime(new Date());
                //同步缓存
                probeRedisApi.addProbeRedisInfo(probe);
                return false;
            } else {
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER11.getCode()), JsonUtil.toJson(probe), logId);
                return true;
            }
        } else {
            probeInfoApi.editWarningTime(probe.getInstrumentParamConfigNO(), TimeHelper.getNowDate(new Date()));
            probe.setWarningtime(new Date());
            //同步缓存
            probeRedisApi.addProbeRedisInfo(probe);
            return false;
        }
    }


    private WarningRecordDto buildProbeWarnInfo(String hospitalcode, String instrumentParamConfigNO, String value) {
        WarningRecordDto warningRecordDto = new WarningRecordDto();
        warningRecordDto.setInputdatetime(new Date());
        warningRecordDto.setHospitalcode(hospitalcode);
        warningRecordDto.setInstrumentparamconfigNO(instrumentParamConfigNO);
        warningRecordDto.setWarningvalue(value);
        return warningRecordDto;
    }
    public static void  main(String[] args){
        System.out.println(1<1);
    }
}
