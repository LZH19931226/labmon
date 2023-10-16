package com.hc.listenter;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.device.ProbeRedisApi;
import com.hc.exchange.BaoJinMsg;
import com.hc.hospital.HospitalRedisApi;
import com.hc.mapper.UserrightDao;
import com.hc.model.TimeoutEquipment;
import com.hc.model.WarningModel;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.my.common.core.constant.enums.ElkLogDetail;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.domain.MonitorinstrumentDo;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.WarningrecordRedisInfo;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.my.common.core.util.SoundLightUtils;
import com.hc.po.Userright;
import com.hc.service.*;
import com.hc.tcp.SoundLightApi;
import com.hc.utils.JsonUtil;
import com.hc.warning.WarningApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableBinding(BaoJinMsg.class)
@Slf4j
public class SocketMessageListener {

    @Autowired
    private WarningService warningService;
    @Autowired
    private UserrightDao userrightDao;
    @Autowired
    private SoundLightApi soundLightApi;
    @Autowired
    private HospitalRedisApi hospitalRedisApi;
    @Autowired
    private AlmMsgService almMsgService;
    @Autowired
    private ProbeRedisApi probeRedisApi;
    @Autowired
    private WarningRuleService warningRuleService;
    @Autowired
    private SendTimeoutRecordService sendTimeoutRecordService;

    @Autowired
    private WarningApi warningApi;

    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME)
    public void onMessage1(String messageContent) {
        try {
            msctMessage(messageContent);
        }catch (Exception e){
          e.printStackTrace();
          return;
        }
    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME1)
    public void onMessage2(String messageContent) {
        try {
            msctMessage(messageContent);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME2)
    public void onMessage3(String messageContent) {
        try {
            msctMessage(messageContent);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    /**
     * 监听超时报警信息
     *
     * @param message
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME4)
    public void onMessage5(String message) {
        JSONArray objects = JSONUtil.parseArray(message);
        List<TimeoutEquipment> timeoutEquipmentList = JSONUtil.toList(objects, TimeoutEquipment.class);
        if (CollectionUtils.isEmpty(timeoutEquipmentList)) {
            return;
        }
        StringBuilder eqTypeName = new StringBuilder();
        StringBuilder count = new StringBuilder();
        String hospitalName = timeoutEquipmentList.get(0).getHospitalname();
         String hospitalcode = timeoutEquipmentList.get(0).getHospitalcode();
         int size = timeoutEquipmentList.size();
        for (int i = 0; i < size; i++) {
            String equipmentTypeName = timeoutEquipmentList.get(i).getEquipmenttypename();
            String count1 = timeoutEquipmentList.get(i).getCount();
            if(StringUtils.isBlank(equipmentTypeName) || StringUtils.isBlank(count1)){
                return;
            }
            if(i == size-1){
                eqTypeName.append(equipmentTypeName);
                count.append(count1);
            }else {
                eqTypeName.append(equipmentTypeName).append("/");
                count.append(count1).append("/");
            }
        }
        if(StringUtils.isBlank(hospitalName) || StringUtils.isBlank(eqTypeName.toString()) || StringUtils.isBlank(count.toString())){
            return;
        }
        List<Userright> userrights = userrightDao.getUserrightByHospitalcodeAAndTimeout(hospitalcode);
        if (CollectionUtils.isEmpty(userrights)) {
            return;
        }
        //通知信息
        warningService.pushTimeOutNotification(userrights,hospitalName,eqTypeName.toString(),count.toString(),hospitalcode,null);
        //保存到数据库
        sendTimeoutRecordService.saveTimeOutRecord(userrights,hospitalcode,eqTypeName.toString(),count.toString());
    }

    public void msctMessage(String message) {
        if (StringUtils.isEmpty(message)){
            return;
        }
        WarningAlarmDo warningAlarmDo = JsonUtil.toBean(message, WarningAlarmDo.class);
        String logId = warningAlarmDo.getLogId();
        ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER05.getCode()),message,logId);
        Integer instrumentconfigid = warningAlarmDo.getInstrumentconfigid();
        MonitorinstrumentDo monitorinstrument = warningAlarmDo.getMonitorinstrument();
        String hospitalcode = monitorinstrument.getHospitalcode();
        String sn = monitorinstrument.getSn();
        //获取探头对象
        InstrumentInfoDto probe = probeRedisApi.getProbeRedisInfo(hospitalcode, monitorinstrument.getInstrumentno() + ":" + instrumentconfigid).getResult();
        if (null == probe) {
            ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER06.getCode()), JsonUtil.toJson(monitorinstrument), warningAlarmDo.getLogId());
            return;
        }
        //获取医院信息
        HospitalInfoDto hospitalInfoDto = hospitalRedisApi.findHospitalRedisInfo(hospitalcode).getResult();
        //判断探头是否满足量程范围
        Warningrecord warningrecord = warningService.checkProbeLowLimit(probe, warningAlarmDo,hospitalInfoDto);
        if (null!=warningrecord){
            //判断探头是否满足报警规则
            WarningModel model = warningRuleService.warningRule(hospitalcode, warningrecord, probe, warningAlarmDo);
            if (null!=model) {
                model.setLogId(logId);
                //判断该医院当天是否有人员排班,给判断和未排班的人员集合赋值
                List<Userright> userList =almMsgService.addUserScheduLing(hospitalcode);
                if (CollectionUtils.isEmpty(userList)){
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER14.getCode()),JsonUtil.toJson(model),logId);
                    return;
                }
                //异步推送报警短信
                warningrecord = warningService.pushNotification(userList, model, hospitalInfoDto);
                //如果该医院开启了声光报警则需要推送声光报警指令
                String soundLightAlarm = hospitalInfoDto.getSoundLightAlarm();
                if(StringUtils.isNotEmpty(soundLightAlarm)){
                    if (StringUtils.equals(soundLightAlarm, DictEnum.TURN_ON.getCode())){
                        soundLightApi.sendMsg(sn,SoundLightUtils.TURN_ON_ROUND_LIGHT_COMMAND);
                    }
                }

            }
            //不满足报警规则,但是超量程的数据也需要记录
            //将信息推送到redis,再批量插入
            WarningrecordRedisInfo convert = BeanConverter.convert(warningrecord, WarningrecordRedisInfo.class);
            warningApi.add(convert);
        }
    }
}
