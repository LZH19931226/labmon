package com.hc.serviceimpl;

import com.hc.MessageApi;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.device.ProbeRedisApi;
import com.hc.model.WarningModel;
import com.hc.my.common.core.constant.enums.*;
import com.hc.my.common.core.domain.MonitorinstrumentDo;
import com.hc.my.common.core.domain.P2PNotify;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.my.common.core.probe.ProbeConfigIds;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.my.common.core.util.RegularUtil;
import com.hc.po.Userright;
import com.hc.service.MessageSendService;
import com.hc.service.WarningService;
import com.hc.utils.JsonUtil;
import com.hc.utils.LowHighVerify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 16956 on 2018-08-09.
 */
@Service
@Slf4j
public class WarningServiceImpl implements WarningService {
    @Autowired
    private ProbeRedisApi probeRedisApi;
    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private MessageApi messageApi;

    @Override
    public Warningrecord checkProbeLowLimit(InstrumentInfoDto probe, WarningAlarmDo warningAlarmDo) {
        String data = warningAlarmDo.getCurrrentData();
        MonitorinstrumentDo monitorinstrument = warningAlarmDo.getMonitorinstrument();
        String hospitalcode = monitorinstrument.getHospitalcode();
        String warningphone = probe.getWarningPhone();
        if (StringUtils.isEmpty(warningphone)) {
            warningphone = "1";
        }
        Integer instrumentConfigId = probe.getInstrumentConfigId();
        String instrumentParamconfigNO = probe.getInstrumentParamConfigNO();
        String equipmentno = probe.getEquipmentNo();
        Warningrecord warningrecord = new Warningrecord();
        warningrecord.setEquipmentno(equipmentno);
        warningrecord.setInstrumentparamconfigno(instrumentParamconfigNO);
        warningrecord.setInputdatetime(new Date());
        warningrecord.setHospitalcode(hospitalcode);
        warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
        warningrecord.setWarningValue(data);
        warningrecord.setLowLimit(probe.getLowLimit().toString());
        warningrecord.setHighLimit(probe.getHighLimit().toString());
        //高低值比较探头
        if (ProbeConfigIds.lowHighRuleInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == lowHighRule(warningrecord, warningAlarmDo, probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo,SysConstants.NORMAL);
                return null;
            }
            //市电比较探头
        } else if (ProbeConfigIds.mainsInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == mainsRule(warningrecord, warningAlarmDo,probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo,SysConstants.NORMAL);
                return null;
            }
            //报警信号比较探头
        } else if (ProbeConfigIds.alarmSignalInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == alarmSignalRule(warningrecord, warningAlarmDo, probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo,SysConstants.NORMAL);
                return null;
            }
        }
        //气体比较探头
        else if (ProbeConfigIds.gasInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == gasRule(warningrecord, warningAlarmDo, probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo,SysConstants.NORMAL);
                return null;
            }
        }
        //气流比较探头
        else if (ProbeConfigIds.airFlowInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == airFlowRule(warningrecord, warningAlarmDo, probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo,SysConstants.NORMAL);
                return null;
            }
        } else {
            return null;
        }
        //探头超过阈值,则认为异常
        sendEquimentProbeStatus(probe,warningAlarmDo,SysConstants.IN_ALARM);
        /*1.判断该设备是否开启报警服务*/
        if (StringUtils.equals("0", warningphone)) {
            //不启用报警，直接过滤信息
            ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER07.getCode()), JsonUtil.toJson(probe), warningAlarmDo.getLogId());
            return null;
        }
        return warningrecord;
    }

    //需要将报警原因,报警通知到得人员,反写过去
    public void sendEquimentProbeStatus(InstrumentInfoDto probe, WarningAlarmDo warningAlarmDo,String state) {
        MonitorinstrumentDo monitorinstrument = warningAlarmDo.getMonitorinstrument();
        //未产生报警记录，正常值情况，就删除
        if(StringUtils.equals(state,SysConstants.NORMAL)){
            probeRedisApi.removeProbeWarnInfo(monitorinstrument.getHospitalcode(), probe.getInstrumentParamConfigNO());
        }
        //将设备状态信息推送到mq
        EquipmentState equipmentState = new EquipmentState();
        equipmentState.setInstrumentConfigNo(probe.getInstrumentParamConfigNO());
        equipmentState.setEquipmentNo(monitorinstrument.getEquipmentno());
        equipmentState.setInstrumentNo(monitorinstrument.getInstrumentno());
        equipmentState.setState(state);
        equipmentState.setInstrumentConfigId(probe.getInstrumentConfigId() + "");
        equipmentState.setHospitalCode(monitorinstrument.getHospitalcode());
        equipmentState.setSn(monitorinstrument.getSn());
        String json = JsonUtil.toJson(equipmentState);
        messageSendService.send(json);
        ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER18.getCode()), JsonUtil.toJson(equipmentState), warningAlarmDo.getLogId());
    }

    private Warningrecord airFlowRule(Warningrecord warningrecord, WarningAlarmDo warningAlarmDo, InstrumentInfoDto probe) {
        String data = warningAlarmDo.getCurrrentData();
        String equipmentname = probe.getEquipmentName();
        String unit = warningAlarmDo.getUnit();
        if (RegularUtil.checkContainsNumbers(data)) {
            if (!LowHighVerify.verify(probe, data)) {
                return null;
            }
            warningrecord.setWarningremark(equipmentname + ":" +"Abnormal  "+unit+",abnormal data:" + data);
            return warningrecord;
        }else {
            if ("A".equals(data)) {
                warningrecord.setWarningremark(equipmentname + ":"+"Abnormal  "+  unit + "abnormal data:No data was obtained");
                return null;
            } else if ("B".equals(data)) {
                warningrecord.setWarningremark(equipmentname + ":" +"Abnormal  "+ unit  + "abnormal data:Flow control off");
                return null;
            } else if ("C".equals(data)) {
                warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit  + "abnormal data:The gas flow is unstable");
                return null;
            } else if ("D".equals(data)) {
                warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit  + "abnormal data:Low air port pressure");
                return null;
            } else if ("E".equals(data)) {
                //产生报警
                warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit +  "abnormal data:No data was obtained");
            } else if ("F".equals(data)) {
                warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit + "abnormal data:Main switch off, but not power off");
                return null;
                //I M O为98协议上传的气流状态 需要报警的模型
            } else if ("I".equals(data)) {
                warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit +  "abnormal data:An air leakage alarm occurred");
            } else if ("M".equals(data)) {
                warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit +  "abnormal data:Equipment leakage alarm");
            } else if ("O".equals(data)) {
                warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit + "abnormal data:Device pressure low alarm");
            } else {
                return null;
            }
        }
        return warningrecord;
    }

    private Warningrecord gasRule(Warningrecord warningrecord, WarningAlarmDo warningAlarmDo, InstrumentInfoDto probe) {
        String data = warningAlarmDo.getCurrrentData();
        String equipmentname = probe.getEquipmentName();
        String unit = warningAlarmDo.getUnit();
        if (!RegularUtil.checkContainsNumbers(data)) {
            return null;
        }
        if (!LowHighVerify.verify(probe, data)) {
            return null;
        }
        warningrecord.setWarningremark(equipmentname + ":" +"Abnormal  "+ unit +  "abnormal data:" + data);
        return warningrecord;
    }

    private Warningrecord alarmSignalRule(Warningrecord warningrecord, WarningAlarmDo warningAlarmDo, InstrumentInfoDto probe) {
        String data = warningAlarmDo.getCurrrentData();
        String equipmentname = probe.getEquipmentName();
        String unit = warningAlarmDo.getUnit();
        if (StringUtils.equals("1", data)) {
            data = "1.00";
        } else {
            data = "0.00";
        }
        if (probe.getLowLimit().compareTo(new BigDecimal(data)) != 0) {
            return null;
        }
        warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit +  "abnormal data:" + (data.equals("1.00")?"Normally open":"Normally closed"));
        return warningrecord;
    }

    private Warningrecord mainsRule(Warningrecord warningrecord, WarningAlarmDo warningAlarmDo, InstrumentInfoDto probe) {
        String data = warningAlarmDo.getCurrrentData();
        String equipmentname = probe.getEquipmentName();
        String unit = warningAlarmDo.getUnit();
        if (!StringUtils.equals("1", data)) {
            return null;
        }
        warningrecord.setWarningremark(equipmentname + ":" +"Abnormal  "+ unit +  "abnormal data:" + "Mains abnormal");
        return warningrecord;
    }

    public Warningrecord lowHighRule(Warningrecord warningrecord, WarningAlarmDo warningAlarmDo, InstrumentInfoDto probe) {
        String data = warningAlarmDo.getCurrrentData();
        String data1 = warningAlarmDo.getCurrentData1();
        String equipmentname = probe.getEquipmentName();
        String unit = warningAlarmDo.getUnit();
        String sn = warningAlarmDo.getSn();
        MonitorinstrumentDo monitorinstrument = warningAlarmDo.getMonitorinstrument();
        String hospitalcode = monitorinstrument.getHospitalcode();
        //表示CO2  、氧气、温度  、 培养箱湿度
        if (!RegularUtil.checkContainsNumbers(data)) {
            //未接传感器
            warningrecord.setWarningremark(equipmentname + ":" +"Abnormal  "+ unit +  "abnormal data:" + data);
            return null;
        }
        if (StringUtils.isNotEmpty(data1)) {
            //MT200M 新程序，两路温度判断
            //老版本mt200m判断逻辑生产周大于20年15周为新的mt200m报警逻辑更改
            String proSn = sn.substring(0, 4);
            String sns = sn.substring(4, 6);
            if (Integer.parseInt(proSn) < 2031) {
                //当一路温度值存在异常，整个值无效
                // 当两个值相差3度，值无效
                if (!checkProbeValue(warningAlarmDo, probe)) {
                    return null;
                }
                warningrecord.setWarningremark(equipmentname + ":" +"Abnormal  "+ unit +  "abnormal data:" + data);
                return warningrecord;
            } else {
                if (StringUtils.equals(sns, "17")){
                    InstrumentInfoDto mt200mHighLimit = probeRedisApi.getProbeRedisInfo(hospitalcode, monitorinstrument.getInstrumentno() + ":" + 14).getResult();
                    if(null!=mt200mHighLimit){
                        //大于最大值
                        if (LowHighVerify.verifyMt200m(probe.getHighLimit(), data) && LowHighVerify.verifyMt200m(mt200mHighLimit.getHighLimit(), data1)) {
                            warningrecord.setWarningremark(equipmentname + ":" +"Abnormal  "+ unit + "abnormal data:" + data);
                            return warningrecord;
                        } else {
                            return null;
                        }
                    }
                }else {
                    if (!checkProbeValue(warningAlarmDo, probe)) {
                        return null;
                    }
                    warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit +  "abnormal data:" + data);
                    return warningrecord;
                }
            }
        }
        //高低值判断
        if (LowHighVerify.verify(probe, data)) {
            warningrecord.setWarningremark(equipmentname + ":" + "Abnormal  "+unit +  "abnormal data:" + data);
        } else {
            return null;
        }
        return warningrecord;
    }

    public boolean checkProbeValue(WarningAlarmDo warningAlarmDo, InstrumentInfoDto probe) {
        String data = warningAlarmDo.getCurrrentData();
        String data1 = warningAlarmDo.getCurrentData1();
        if (!RegularUtil.checkContainsNumbers(data1) || Math.abs(new Double(data) - new Double(data1)) > 3) {
            return false;
        }
        return LowHighVerify.verify(probe, data) && LowHighVerify.verify(probe, data1);
    }


    @Override
    public Warningrecord pushNotification(List<Userright> list, WarningModel warningModel, HospitalInfoDto hospitalInfoDto) {
        String logId = warningModel.getLogId();
        //获取电话.
        StringBuilder phoneCallUser = new StringBuilder();
        StringBuilder mailCallUser = new StringBuilder();
        String warningremark = warningModel.getWarningrecord().getWarningremark();
        for (Userright userright : list) {
            String reminders = userright.getReminders();
            String phone = userright.getPhoneNum();
            String code = userright.getCode();
            String mailbox = userright.getMailbox();
            String hospitalCode = hospitalInfoDto.getHospitalCode();
            //不报警
            if (StringUtils.isEmpty(reminders)) {
                continue;
            }
            //根据设置的报警方式调整报警
            String[] reminder = reminders.split(",");
            for (String rem : reminder) {
                if (StringUtils.equals(rem,DictEnum.PHONE.getCode())&&StringUtils.isNotEmpty(phone)){
                    buildP2PNotify(code+phone, warningremark, Collections.singletonList(NotifyChannel.PHONE),hospitalCode,"1");
                    phoneCallUser.append(code).append(phone).append("/");
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER15.getCode()), JsonUtil.toJson(userright), logId);
                }
                if (StringUtils.equals(rem,DictEnum.SMS.getCode())&&StringUtils.isNotEmpty(phone)){
                    buildP2PNotify(code+phone, warningremark, Collections.singletonList(NotifyChannel.SMS),hospitalCode,"2");
                    mailCallUser.append(code).append(phone).append("/");
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER16.getCode()), JsonUtil.toJson(userright), logId);
                }
                if (StringUtils.equals(rem,DictEnum.MAILBOX.getCode())&&StringUtils.isNotEmpty(mailbox)){
                    buildP2PNotify(mailbox, warningremark, Collections.singletonList(NotifyChannel.MAIL),hospitalCode,"4");
                    mailCallUser.append(mailbox).append("/");
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER17.getCode()), JsonUtil.toJson(userright), logId);
                }
            }
        }
        //修改报警通知人
        Warningrecord warningrecord = warningModel.getWarningrecord();
        warningrecord.setPkid(warningrecord.getPkid());
        if(mailCallUser.length()>0
                && !"null".equals(mailCallUser.toString())
                && !"".equals(mailCallUser.toString())){
            mailCallUser.deleteCharAt(mailCallUser.length()-1);
            warningrecord.setMailCallUser(mailCallUser.toString());
        }

        if(phoneCallUser.length()>0
                && !"null".equals(phoneCallUser.toString())
                && !"".equals(phoneCallUser.toString())){
            phoneCallUser.deleteCharAt(phoneCallUser.length()-1);
            warningrecord.setPhoneCallUser(phoneCallUser.toString());
        }
        return warningrecord;
    }

    @Override
    public void pushTimeOutNotification(List<Userright> userrights, String hosName,String eqTypeName, String count,String hospitalCode) {
        for (Userright userright : userrights) {
            String phonenum = userright.getCode()+userright.getPhoneNum();
            String mailbox = userright.getMailbox();
            String timeoutwarning = userright.getTimeoutWarning();
            String warningremark = hosName+"Hospital has experienced "+eqTypeName+" device timeouts, with a total of "+count+" devices affected. Please log in to the system to view details";
            //根据设置的报警方式调整报警
            if (StringUtils.isEmpty(timeoutwarning)) {
                continue;
            }
            String[] reminder = timeoutwarning.split(",");
            for (String rem : reminder) {
                if (StringUtils.equals(rem,DictEnum.PHONE.getCode())&&StringUtils.isNotEmpty(phonenum)){
                    buildP2PNotify(phonenum, warningremark, Collections.singletonList(NotifyChannel.PHONE),hospitalCode,"1");
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER15.getCode()), JsonUtil.toJson(userright), null);
                }
                if (StringUtils.equals(rem,DictEnum.SMS.getCode())&&StringUtils.isNotEmpty(phonenum)){
                    buildP2PNotify(phonenum, warningremark, Collections.singletonList(NotifyChannel.SMS),hospitalCode,"2");
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER16.getCode()), JsonUtil.toJson(userright), null);
                }
                if (StringUtils.equals(rem,DictEnum.MAILBOX.getCode())&&StringUtils.isNotEmpty(mailbox)){
                    buildP2PNotify(mailbox, warningremark, Collections.singletonList(NotifyChannel.MAIL),hospitalCode,"4");
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER17.getCode()), JsonUtil.toJson(userright), null);
                }
            }
        }
    }


    //海外短信将整个message内容都放到信息体里面去
    public void buildP2PNotify(String phone,String message, List<NotifyChannel> notifyChannels,String hospitalCode,String servoceNo) {
        P2PNotify pNotify = new P2PNotify();
        pNotify.setServiceNo(servoceNo);
        pNotify.setUserId(phone);
        pNotify.setMessageTitle(null);
        pNotify.setMessageCover(message);
        pNotify.setMessageIntro(null);
        pNotify.setChannels(notifyChannels);
        pNotify.setMessageBodys(hospitalCode);
        messageApi.send(pNotify);
    }




}
