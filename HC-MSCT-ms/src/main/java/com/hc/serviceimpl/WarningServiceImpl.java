package com.hc.serviceimpl;

import com.hc.clickhouse.po.Warningrecord;
import com.hc.device.ProbeRedisApi;
import com.hc.model.WarningModel;
import com.hc.my.common.core.constant.enums.ElkLogDetail;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.domain.MonitorinstrumentDo;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.my.common.core.util.RegularUtil;
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
    private WarningRuleService warningRuleService;
    @Autowired
    private MessageSendService messageSendService;

    private List<Integer> lowHighRuleInstrumentConfigIds = Arrays.asList(1, 2, 4, 5, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 42, 43);

    private List<Integer> mainsInstrumentConfigIds = Arrays.asList(10);

    private List<Integer> alarmSignalInstrumentConfigIds = Arrays.asList(11,44);

    private List<Integer> gasInstrumentConfigIds = Arrays.asList(3,6,7,8,9,12,26,27,28,35,23,24);

    private List<Integer> airFlowInstrumentConfigIds = Arrays.asList(25);

    @Override
    public WarningModel produceWarn(WarningAlarmDo warningAlarmDo) {
        // redis缓存中取  当前探头监控类型数据   高低值
        String data = warningAlarmDo.getCurrrentData();
        Integer instrumentconfigid = warningAlarmDo.getInstrumentconfigid();
        MonitorinstrumentDo monitorinstrument = warningAlarmDo.getMonitorinstrument();
        String hospitalcode = monitorinstrument.getHospitalcode();
        InstrumentInfoDto probe = probeRedisApi.getProbeRedisInfo(hospitalcode, monitorinstrument.getInstrumentno() + ":" + instrumentconfigid).getResult();
        if (null == probe) {
            ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER06.getCode()), JsonUtil.toJson(monitorinstrument), warningAlarmDo.getLogId());
            return null;
        }
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
        if (lowHighRuleInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null== lowHighRule(warningrecord,warningAlarmDo, probe)){
                sendEquimentProbeStatus(probe,warningAlarmDo);
                return null;
            }
            //市电比较探头
        }else if (mainsInstrumentConfigIds.contains(instrumentConfigId)){
            if (null== mainsRule(warningrecord,warningAlarmDo)){
                sendEquimentProbeStatus(probe,warningAlarmDo);
                return null;
            }
            //报警信号比较探头
        }else if (alarmSignalInstrumentConfigIds.contains(instrumentConfigId)){
            if (null== alarmSignalRule(warningrecord,warningAlarmDo,probe)){
                sendEquimentProbeStatus(probe,warningAlarmDo);
                return null;
            }
        }
        //气体比较探头
       else if (gasInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null== gasRule(warningrecord,warningAlarmDo,probe)){
                sendEquimentProbeStatus(probe,warningAlarmDo);
                return null;
            }
        }
        //气流比较探头
        else if (airFlowInstrumentConfigIds.contains(instrumentConfigId)) {
            if (null== airFlowRule(warningrecord,warningAlarmDo,probe)){
                sendEquimentProbeStatus(probe,warningAlarmDo);
                return null;
            }
        }else {
            return null;
        }
       return warningRuleService.warningRule(hospitalcode, warningrecord, probe, warningAlarmDo);
    }

    //需要将报警原因,报警通知到得人员,反写过去
    public void sendEquimentProbeStatus(InstrumentInfoDto probe,WarningAlarmDo warningAlarmDo){
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
        ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER18.getCode()),JsonUtil.toJson(equipmentState),warningAlarmDo.getLogId());
    }


    private Warningrecord airFlowRule(Warningrecord warningrecord, WarningAlarmDo warningAlarmDo, InstrumentInfoDto probe) {
        String data = warningAlarmDo.getCurrrentData();
        String equipmentname = probe.getEquipmentName();
        String unit = warningAlarmDo.getUnit();
        if ("A".equals(data)) {
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：未获取到数据");
            return null;
        } else if ("B".equals(data)) {
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：流量控制关闭");
            return null;
        } else if ("C".equals(data)) {
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：气体流量不稳定");
            return null;
        } else if ("D".equals(data)) {
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：气口压力低");
            return null;
        } else if ("E".equals(data)) {
            //产生报警
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：未获取到数据");
        } else if ("F".equals(data)) {
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：总开关关闭，但未断电");
            return null;
            //I M O为98协议上传的气流状态 需要报警的模型
        } else if ("I".equals(data)) {
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：发生漏气报警事件");
        } else if ("M".equals(data)) {
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：设备漏气报警");
        } else if ("O".equals(data)) {
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：设备气压低报警");
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
        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
        return warningrecord;
    }

    private Warningrecord alarmSignalRule(Warningrecord warningrecord, WarningAlarmDo warningAlarmDo,InstrumentInfoDto probe) {
        String data = warningAlarmDo.getCurrrentData();
        String equipmentname = probe.getEquipmentName();
        if (StringUtils.equals("1", data)) {
            data = "1.00";
        } else {
            data = "0.00";
        }
        if (probe.getLowLimit().compareTo(new BigDecimal(data))!=0) {
           return null;
        }
        warningrecord.setWarningremark(equipmentname + "报警信号异常");
        return warningrecord;
    }

    private Warningrecord mainsRule(Warningrecord warningrecord, WarningAlarmDo warningAlarmDo) {
        String data = warningAlarmDo.getCurrrentData();
        if (!StringUtils.equals("1", data)) {
           return null;
        }
        warningrecord.setWarningremark("市电异常");
        return warningrecord;
    }

    public Warningrecord lowHighRule(Warningrecord warningrecord,WarningAlarmDo warningAlarmDo,InstrumentInfoDto probe) {
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
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为:" + data);
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
                if (!checkProbeValue(warningAlarmDo,probe)){
                   return null;
                }
                warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
            } else {
                if (!StringUtils.equals(sns, "17")) {
                    if (!checkProbeValue(warningAlarmDo,probe)){
                        return null;
                    }
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                }
                //获取二路温度探头设置的值
                InstrumentInfoDto mt200mHighLimit = probeRedisApi.getProbeRedisInfo(hospitalcode, monitorinstrument.getInstrumentno() + ":" + 14).getResult();
                //大于最大值
                if (LowHighVerify.verifyMt200m(probe.getHighLimit(), data) && LowHighVerify.verifyMt200m(mt200mHighLimit.getHighLimit(), data1)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                } else {
                    return null;
                }
            }
        }
        //高低值判断
        if (LowHighVerify.verify(probe, data)) {
            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
        } else {
            return null;
        }
        return warningrecord;
    }

    public boolean  checkProbeValue(WarningAlarmDo warningAlarmDo,InstrumentInfoDto probe){
        String data = warningAlarmDo.getCurrrentData();
        String data1 = warningAlarmDo.getCurrentData1();
        if (!RegularUtil.checkContainsNumbers(data1) || Math.abs(new Double(data) - new Double(data1)) > 3) {
            return false;
        }
        return LowHighVerify.verify(probe, data) && LowHighVerify.verify(probe, data1);
    }


}
