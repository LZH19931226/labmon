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
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.my.common.core.util.SoundLightUtils;
import com.hc.po.Userright;
import com.hc.service.*;
import com.hc.tcp.SoundLightApi;
import com.hc.utils.JsonUtil;
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
    private SendrecordService sendrecordService;
    @Autowired
    private UserrightDao userrightDao;
    @Autowired
    private SoundLightApi soundLightApi;
    @Autowired
    private HospitalRedisApi hospitalRedisApi;
    @Autowired
    private MessageSendService messageSendService;
    @Autowired
    private AlmMsgService almMsgService;
    @Autowired
    private ProbeRedisApi probeRedisApi;
    @Autowired
    private WarningRuleService warningRuleService;
    @Autowired
    private WarningrecordRepository warningrecordRepository;

    @Autowired
    private SendTimeoutRecordService sendTimeoutRecordService;

    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME)
    public void onMessage1(String messageContent) {
        msctMessage(messageContent);

    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME1)
    public void onMessage2(String messageContent) {
        msctMessage(messageContent);

    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME2)
    public void onMessage3(String messageContent) {
        msctMessage(messageContent);
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
        List<Userright> userrightByHospitalcodeAAndTimeout = userrightDao.getUserrightByHospitalcodeAAndTimeout(hospitalcode);
        if (CollectionUtils.isEmpty(userrightByHospitalcodeAAndTimeout)) {
            return;
        }
        //通知信息
        sendrecordService.pushTimeOutNotification(userrightByHospitalcodeAAndTimeout,hospitalName,eqTypeName.toString(),count.toString());
        //保存到数据库
        sendTimeoutRecordService.saveTimeOutRecord(userrightByHospitalcodeAAndTimeout,hospitalcode,eqTypeName.toString(),count.toString());
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
        //判断探头是否满足量程范围
        Warningrecord warningrecord = warningService.checkProbeLowLimit(probe, warningAlarmDo);
        if (null!=warningrecord){
            //判断探头是否满足报警规则
            WarningModel model = warningRuleService.warningRule(hospitalcode, warningrecord, probe, warningAlarmDo);
            if (null!=model) {
                model.setLogId(logId);
                HospitalInfoDto hospitalInfoDto = hospitalRedisApi.findHospitalRedisInfo(hospitalcode).getResult();
                //判断该医院当天是否有人员排班,给判断和未排班的人员集合赋值
                List<Userright> userList =almMsgService.addUserScheduLing(hospitalcode);
                if (CollectionUtils.isEmpty(userList)){
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER14.getCode()),JsonUtil.toJson(model),logId);
                    return;
                }
                //异步推送报警短信
                warningrecord = sendrecordService.pushNotification(userList, model, hospitalInfoDto);
                //如果该医院开启了声光报警则需要推送声光报警指令
                if(StringUtils.isBlank(hospitalInfoDto.getSoundLightAlarm()) || !StringUtils.equals(hospitalInfoDto.getSoundLightAlarm(), DictEnum.TURN_ON.getCode())){
                    soundLightApi.sendMsg(sn,SoundLightUtils.TURN_ON_ROUND_LIGHT_COMMAND);
                }
                //将设备状态信息推送到mq
                sendEquimentProbeStatus(monitorinstrument,model,hospitalcode,warningAlarmDo.getLogId());
            }
            //不满足报警规则,但是超量程的数据也需要记录
            warningrecordRepository.save(warningrecord);
        }
    }

    //推送探头当前报警状态
    public void sendEquimentProbeStatus( MonitorinstrumentDo monitorinstrument , WarningModel model,String hospitalcode,String logId){
        EquipmentState equipmentState = new EquipmentState();
        equipmentState.setState(SysConstants.IN_ALARM);
        equipmentState.setEquipmentNo(monitorinstrument.getEquipmentno());
        equipmentState.setInstrumentNo(monitorinstrument.getInstrumentno());
        equipmentState.setInstrumentConfigNo(model.getInstrumentparamconfigNO());
        equipmentState.setInstrumentConfigId(model.getInstrumentConfigId());
        equipmentState.setHospitalCode(hospitalcode);
        equipmentState.setSn(model.getSn());
        String json = JsonUtil.toJson(equipmentState);
        messageSendService.send(json);
        ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER18.getCode()),JsonUtil.toJson(equipmentState),logId);
    }


}
