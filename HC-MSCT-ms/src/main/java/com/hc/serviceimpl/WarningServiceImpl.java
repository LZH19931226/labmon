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
import com.hc.service.WarningRuleService;
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
        /*1.判断该设备是否开启报警服务*/
        if (StringUtils.equals("0", warningphone)) {
            //不启用报警，直接过滤信息
            ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER07.getCode()), JsonUtil.toJson(probe), warningAlarmDo.getLogId());
            return null;
        }
        Integer instrumentConfigId = probe.getInstrumentConfigId();
        String instrumentparamconfigNO = probe.getInstrumentParamConfigNO();
        String equipmentno = probe.getEquipmentNo();
        Warningrecord warningrecord = new Warningrecord();
        warningrecord.setEquipmentno(equipmentno);
        warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
        warningrecord.setInputdatetime(new Date());
        warningrecord.setHospitalcode(hospitalcode);
        warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
        warningrecord.setWarningValue(data);
        warningrecord.setLowLimit(probe.getLowLimit().toString());
        warningrecord.setHighLimit(probe.getHighLimit().toString());
        //高低值比较探头
        if (ProbeConfigIds.lowHighRuleInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == lowHighRule(warningrecord, warningAlarmDo, probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo);
                return null;
            }
            //市电比较探头
        } else if (ProbeConfigIds.mainsInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == mainsRule(warningrecord, warningAlarmDo,probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo);
                return null;
            }
            //报警信号比较探头
        } else if (ProbeConfigIds.alarmSignalInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == alarmSignalRule(warningrecord, warningAlarmDo, probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo);
                return null;
            }
        }
        //气体比较探头
        else if (ProbeConfigIds.gasInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == gasRule(warningrecord, warningAlarmDo, probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo);
                return null;
            }
        }
        //气流比较探头
        else if (ProbeConfigIds.airFlowInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null == airFlowRule(warningrecord, warningAlarmDo, probe)) {
                sendEquimentProbeStatus(probe, warningAlarmDo);
                return null;
            }
        } else {
            return null;
        }
        return warningrecord;
    }

    //需要将报警原因,报警通知到得人员,反写过去
    public void sendEquimentProbeStatus(InstrumentInfoDto probe, WarningAlarmDo warningAlarmDo) {
        MonitorinstrumentDo monitorinstrument = warningAlarmDo.getMonitorinstrument();
        //未产生报警记录，正常值情况，就删除
        probeRedisApi.removeProbeWarnInfo(monitorinstrument.getHospitalcode(), probe.getInstrumentParamConfigNO());
        //将设备状态信息推送到mq
        EquipmentState equipmentState = new EquipmentState();
        equipmentState.setInstrumentConfigNo(probe.getInstrumentParamConfigNO());
        equipmentState.setEquipmentNo(monitorinstrument.getEquipmentno());
        equipmentState.setInstrumentNo(monitorinstrument.getInstrumentno());
        equipmentState.setState(SysConstants.NORMAL);
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
        if ("A".equals(data)) {
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:未获取到数据");
            return null;
        } else if ("B".equals(data)) {
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:流量控制关闭");
            return null;
        } else if ("C".equals(data)) {
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:气体流量不稳定");
            return null;
        } else if ("D".equals(data)) {
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:气口压力低");
            return null;
        } else if ("E".equals(data)) {
            //产生报警
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:未获取到数据");
        } else if ("F".equals(data)) {
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:总开关关闭，但未断电");
            return null;
            //I M O为98协议上传的气流状态 需要报警的模型
        } else if ("I".equals(data)) {
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:发生漏气报警事件");
        } else if ("M".equals(data)) {
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:设备漏气报警");
        } else if ("O".equals(data)) {
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:设备气压低报警");
        } else {
            return null;
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
        warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:" + data);
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
        warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:" + (data.equals("1.00")?"常闭":"常开"));
        return warningrecord;
    }

    private Warningrecord mainsRule(Warningrecord warningrecord, WarningAlarmDo warningAlarmDo, InstrumentInfoDto probe) {
        String data = warningAlarmDo.getCurrrentData();
        String equipmentname = probe.getEquipmentName();
        String unit = warningAlarmDo.getUnit();
        if (!StringUtils.equals("1", data)) {
            return null;
        }
        warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:" + "市电异常");
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
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常原因为:" + data);
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
                warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:" + data);
            } else {
                if (!StringUtils.equals(sns, "17")) {
                    if (!checkProbeValue(warningAlarmDo, probe)) {
                        return null;
                    }
                    warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:" + data);
                }
                //获取二路温度探头设置的值
                InstrumentInfoDto mt200mHighLimit = probeRedisApi.getProbeRedisInfo(hospitalcode, monitorinstrument.getInstrumentno() + ":" + 14).getResult();
                //大于最大值
                if (LowHighVerify.verifyMt200m(probe.getHighLimit(), data) && LowHighVerify.verifyMt200m(mt200mHighLimit.getHighLimit(), data1)) {
                    warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:" + data);
                } else {
                    return null;
                }
            }
        }
        //高低值判断
        if (LowHighVerify.verify(probe, data)) {
            warningrecord.setWarningremark(equipmentname + ":" + unit + "异常," + "异常数据为:" + data);
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
        for (Userright userright : list) {
            String reminders = userright.getReminders();
            String phonenum = userright.getPhonenum();
            String role = userright.getRole();
            String equipmentName = warningModel.getEquipmentname();
            String hospitalName = hospitalInfoDto.getHospitalName();
            String unit = warningModel.getUnit();
            String value = warningModel.getValue();
            //1为运维后台人员
            if (StringUtils.isNotEmpty(role) && StringUtils.equals(role, "1")) {
                equipmentName = hospitalName + equipmentName;
            }
            //不报警
            if (StringUtils.equals(reminders, DictEnum.UNOPENED_CONTACT_DETAILS.getCode()) || StringUtils.isEmpty(phonenum)) {
                continue;
            }
            if (StringUtils.isEmpty(reminders) || StringUtils.equals(DictEnum.PHONE_SMS.getCode(), reminders)) {
                //拨打电话短信
                buildP2PNotify(phonenum, equipmentName, unit, value, Arrays.asList(NotifyChannel.SMS, NotifyChannel.PHONE));
                mailCallUser.append(phonenum).append("/");
                phoneCallUser.append(phonenum).append("/");
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER17.getCode()), JsonUtil.toJson(userright), logId);
            } else if (StringUtils.equals(reminders, DictEnum.PHONE.getCode())) {
                buildP2PNotify(phonenum, equipmentName, unit, value, Collections.singletonList(NotifyChannel.PHONE));
                phoneCallUser.append(phonenum).append("/");
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER15.getCode()), JsonUtil.toJson(userright), logId);
            } else if (StringUtils.equals(reminders, DictEnum.SMS.getCode())) {
                buildP2PNotify(phonenum, equipmentName, unit, value, Collections.singletonList(NotifyChannel.SMS));
                mailCallUser.append(phonenum).append("/");
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER16.getCode()), JsonUtil.toJson(userright), logId);
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
    public void pushTimeOutNotification(List<Userright> userrights, String hospitalName, String eqTypeName, String count) {
        for (Userright userright : userrights) {
            String phonenum = userright.getPhonenum();
            if (StringUtils.isEmpty(phonenum)) {
                continue;
            }
            String timeoutwarning = userright.getTimeoutwarning();//超时报警方式
            // 超时报警
            if (StringUtils.isBlank(timeoutwarning) || StringUtils.equals(timeoutwarning, "0")) {
                buildTimeOutP2PNotify(phonenum, eqTypeName, "超时", hospitalName,Arrays.asList(NotifyChannel.SMS, NotifyChannel.PHONE),count);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER21.getCode()), JsonUtil.toJson(userright), null);
            } else if (StringUtils.equals(timeoutwarning, "1")) {
                buildTimeOutP2PNotify(phonenum, eqTypeName, "超时", hospitalName,Collections.singletonList(NotifyChannel.PHONE),count);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER19.getCode()), JsonUtil.toJson(userright), null);
            } else if (StringUtils.equals(timeoutwarning, "2")) {
                buildTimeOutP2PNotify(phonenum, eqTypeName, "超时", hospitalName,Collections.singletonList(NotifyChannel.SMS),count);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER20.getCode()), JsonUtil.toJson(userright), null);
            }
        }
    }


    public void buildP2PNotify(String phone, String equipmentname, String unit, String value, List<NotifyChannel> notifyChannels) {
        P2PNotify p2PNotify = new P2PNotify();
        p2PNotify.setUserId(phone);
        p2PNotify.setMessageTitle(equipmentname);
        p2PNotify.setMessageCover(unit);
        if("LIQUIDLEVEL".equals(unit)){
            p2PNotify.setMessageCover("液位");
        }
        p2PNotify.setMessageIntro(value);
        p2PNotify.setChannels(notifyChannels);
        p2PNotify.setMessageBodys(PushType.USUAL_ALARM.name());
        p2PNotify.setServiceNo("1");
        messageApi.send(p2PNotify);
    }

    public void buildTimeOutP2PNotify(String phone,String equipmentname,String unit,String hospitalName,List<NotifyChannel> notifyChannels,String count) {
        P2PNotify p2PNotify = new P2PNotify();
        p2PNotify.setUserId(phone);
        p2PNotify.setMessageTitle(equipmentname);
        p2PNotify.setMessageCover(unit);
        if("LIQUIDLEVEL".equals(unit)){
            p2PNotify.setMessageCover("液位");
        }
        p2PNotify.setMessageIntro(hospitalName);
        p2PNotify.setChannels(notifyChannels);
        p2PNotify.setMessageBodys(PushType.TIMEOUT_ALARM.name());
        Map<String, String> paramsMap = new HashMap<String, String>() {
            {
                put("timeout", count);
            }
        };
        p2PNotify.setServiceNo("1");
        p2PNotify.setParams(paramsMap);
        messageApi.send(p2PNotify);
    }

}
