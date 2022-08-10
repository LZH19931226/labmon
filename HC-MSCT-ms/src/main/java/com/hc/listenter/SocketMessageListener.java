package com.hc.listenter;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.exchange.BaoJinMsg;
import com.hc.hospital.HospitalRedisApi;
import com.hc.mapper.SendrecordDao;
import com.hc.mapper.UserrightDao;
import com.hc.model.TimeoutEquipment;
import com.hc.model.WarningModel;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.my.common.core.constant.enums.ElkLogDetail;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.domain.MonitorinstrumentDo;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.my.common.core.redis.dto.*;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.my.common.core.util.SoundLightUtils;
import com.hc.po.*;
import com.hc.service.*;
import com.hc.tcp.SoundLightApi;
import com.hc.utils.JsonUtil;
import com.hc.utils.UnitCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@EnableBinding(BaoJinMsg.class)
@Slf4j
public class SocketMessageListener {

    @Autowired
    private SendMesService sendMesService;
    @Autowired
    private WarningService warningService;
    @Autowired
    private SendrecordService sendrecordService;
    @Autowired
    private WarningrecordRepository warningrecordRepository;
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
            String eqTypeName = "";
            String count = "";
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
                    eqTypeName += equipmentTypeName;
                    count += count1;
                }else {
                    eqTypeName+=equipmentTypeName+"/";
                    count+=count1+"/";
                }
            }
            if(StringUtils.isBlank(hospitalName) || StringUtils.isBlank(eqTypeName) || StringUtils.isBlank(count)){
                return;
            }
            log.info("进入超时报警队列：" + message);
            List<Userright> userrightByHospitalcodeAAndTimeout = userrightDao.getUserrightByHospitalcodeAAndTimeout(hospitalcode);
            if (CollectionUtils.isEmpty(userrightByHospitalcodeAAndTimeout)) {
                return;
            }
            log.info("进入超时报警队列联系人：" + JsonUtil.toJson(userrightByHospitalcodeAAndTimeout));
            for (Userright userright : userrightByHospitalcodeAAndTimeout) {
                String phonenum = userright.getPhonenum();
                if (StringUtils.isEmpty(phonenum)) {
                    continue;
                }
                /**
                 * <option value="0">电话+短信</option>
                 * <option value="1">电话</option>
                 * <option value="2">短信</option>
                 * <option value="3">不报警</option>
                 */
                String timeoutwarning = userright.getTimeoutwarning();//超时报警方式
                // 超时报警
                if (StringUtils.isBlank(timeoutwarning) || StringUtils.equals(timeoutwarning, "0")){
                    log.info("拨打电话发送短信对象:{}",JsonUtil.toJson(userright));
                    sendMesService.callPhone2(userright.getPhonenum(),hospitalName, eqTypeName);
                    SendSmsResponse sendSmsResponse = sendMesService.sendMes1(phonenum, eqTypeName, "超时", hospitalName, count);
                    log.info("发送短信对象:{}",JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                } else if (StringUtils.equals(timeoutwarning, "1")) {
                    log.info("拨打电话发送短信对象:{}",JsonUtil.toJson(userright));
                    sendMesService.callPhone2(userright.getPhonenum(),hospitalName, eqTypeName);
                } else if (StringUtils.equals(timeoutwarning, "2")) {
                    SendSmsResponse sendSmsResponse = sendMesService.sendMes1(phonenum, eqTypeName, "超时", hospitalName, count);
                    log.info("发送短信对象:{}",JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                }
            }
    }

    public void msctMessage(String message) {
        if (StringUtils.isEmpty(message)){
            return;
        }
        WarningAlarmDo warningAlarmDo = JsonUtil.toBean(message, WarningAlarmDo.class);
        ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER05.getCode()),message,warningAlarmDo.getLogId());
        WarningModel model = warningService.produceWarn(warningAlarmDo);
        if (null==model) {
            return;
        }
        MonitorinstrumentDo monitorinstrument = warningAlarmDo.getMonitorinstrument();
        String sn = monitorinstrument.getSn();
        String equipmentname = model.getEquipmentname();
        String unit = UnitCase.caseUint(model.getUnit());
        String value = UnitCase.caseUint(model.getValue());
        String hospitalcode = model.getHospitalcode();
        HospitalInfoDto hospitalInfoDto = hospitalRedisApi.findHospitalRedisInfo(hospitalcode).getResult();
        if (null == hospitalInfoDto){
            return;
        }
        String hospitalName = hospitalInfoDto.getHospitalName();
        //判断该医院当天是否有人员排班,给判断和未排班的人员集合赋值
        List<UserRightRedisDto> list =almMsgService.addUserScheduLing(hospitalcode);
        if (CollectionUtils.isEmpty(list)){
            ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER14.getCode()),JsonUtil.toJson(model),warningAlarmDo.getLogId());
            return;
        }
        //产生报警记录
        Warningrecord warningrecord = model.getWarningrecord();
        warningrecordRepository.save(warningrecord);

        //获取电话.

        List<Sendrecord> sendrecords = new ArrayList<>();
        for (UserRightRedisDto userright : list) {
            String reminders = userright.getReminders();
            String phonenum = userright.getPhoneNum();
            String role = userright.getRole();
            String equipmentName = equipmentname;
            //1为运维后台人员
            if (StringUtils.isNotEmpty(role)&&StringUtils.equals(role,"1")){
                equipmentName = hospitalName + equipmentname;
            }
            //不报警
            if (StringUtils.equals(reminders,DictEnum.UNOPENED_CONTACT_DETAILS.getCode()) || StringUtils.isEmpty(phonenum)) {
                continue;
            }
            if (StringUtils.isEmpty(reminders) || StringUtils.equals(DictEnum.PHONE_SMS.getCode(),reminders)) {
                sendMesService.callPhone(phonenum, equipmentName);
                Sendrecord sendrecord = producePhoneRecord(phonenum, hospitalcode, equipmentName, unit, "1");
                sendrecords.add(sendrecord);
                sendMesService.sendMes(phonenum, equipmentName, unit, value);
                Sendrecord sendrecord1 = producePhoneRecord(phonenum, hospitalcode, equipmentName, unit, "0");
                sendrecords.add(sendrecord1);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER17.getCode()),JsonUtil.toJson(userright),warningAlarmDo.getLogId());
            } else if (StringUtils.equals(reminders, DictEnum.PHONE.getCode())) {
                sendMesService.callPhone(userright.getPhoneNum(), equipmentName);
                Sendrecord sendrecord = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "1");
                sendrecords.add(sendrecord);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER15.getCode()),JsonUtil.toJson(userright),warningAlarmDo.getLogId());
            } else if (StringUtils.equals(reminders,DictEnum.SMS.getCode())) {
                sendMesService.sendMes(userright.getPhoneNum(), equipmentName, unit, value);
                Sendrecord sendrecord = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "0");
                sendrecords.add(sendrecord);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER16.getCode()),JsonUtil.toJson(userright),warningAlarmDo.getLogId());

            }
        }
        if (CollectionUtils.isNotEmpty(sendrecords)) {
            sendrecordService.saveBatch(sendrecords);
        }

        //如果该医院开启了声光报警则需要推送声光报警指令
        if(StringUtils.isBlank(hospitalInfoDto.getSoundLightAlarm()) || !StringUtils.equals(hospitalInfoDto.getSoundLightAlarm(), DictEnum.TURN_ON.getCode())){
            soundLightApi.sendMsg(sn,SoundLightUtils.TURN_ON_ROUND_LIGHT_COMMAND);
        }
        //将设备状态信息推送到mq
        sendEquimentProbeStatus(monitorinstrument,model,hospitalcode,warningAlarmDo.getLogId());
    }




    //需要将报警原因,报警通知到得人员,反写过去
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


    public Sendrecord producePhoneRecord(String phone, String hospitalcode, String equipmentname, String
            unit, String type) {
        Sendrecord sendrecord = new Sendrecord();
        sendrecord.setPhonenum(phone);
        sendrecord.setCreatetime(new Date());
        sendrecord.setHospitalcode(hospitalcode);
        sendrecord.setSendtype(type);
        sendrecord.setEquipmentname(equipmentname);
        sendrecord.setUnit(unit);
        sendrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
        return sendrecord;
    }

}
