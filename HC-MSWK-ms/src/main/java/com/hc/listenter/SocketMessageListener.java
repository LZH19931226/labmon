package com.hc.listenter;

import cn.hutool.json.JSONUtil;
import com.hc.device.ProbeRedisApi;
import com.hc.exchange.SocketMessage;
import com.hc.my.common.core.constant.enums.ElkLogDetail;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.my.common.core.util.DateUtils;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.my.common.core.util.UniqueHash;
import com.hc.po.Instrumentparamconfig;
import com.hc.po.Monitorinstrument;
import com.hc.service.InstrumentMonitorInfoService;
import com.hc.service.InstrumentparamconfigService;
import com.hc.service.MTJudgeService;
import com.hc.service.MessagePushService;
import com.hc.tcp.TcpClientApi;
import com.hc.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;


@Component
@EnableBinding(SocketMessage.class)
@Slf4j
public class SocketMessageListener {

    @Autowired
    private MessagePushService service;
    @Autowired
    private MTJudgeService mtJudgeService;
    @Autowired
    private InstrumentMonitorInfoService instrumentMonitorInfoService;
    @Autowired
    private TcpClientApi tcpClientApi;
    @Autowired
    private ProbeRedisApi probeRedisApi;
    @Autowired
    private InstrumentparamconfigService instrumentparamconfigService;


    @StreamListener(SocketMessage.EXCHANGE_NAME)
    public void onMessage(String messageContent) {
        mswkMessage(messageContent, "1");
    }


    @StreamListener(SocketMessage.EXCHANGE_NAME2)
    public void onMessage1(String messageContent) {
        mswkMessage(messageContent, "2");
    }


    @StreamListener(SocketMessage.EXCHANGE_NAME3)
    public void onMessage2(String messageContent) {
        mswkMessage(messageContent, "3");
    }

    @StreamListener(SocketMessage.EQUIPMENT_STATE_INFO)
    public void onMessage3(String messageContent) {
        log.info("数据处理中心服务接收到数据:" + messageContent);
        process(messageContent);
    }

    public void mswkMessage(String messageContent, String topic) {
        if (StringUtils.isEmpty(messageContent)){
            return;
        }
        ParamaterModel model = JsonUtil.toBean(messageContent, ParamaterModel.class);
        String logId = model.getLogId();
        //该数据生命周期id
        ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSWK_SERIAL_NUMBER01.getCode()),messageContent,logId);
        //MT500  MT600判断
        //废弃掉自动注册功能,探头未未注册或者探头禁用则过滤数据
        //废弃掉通道600抵对应关联关系查询,若通道对用600未注册处理逻辑
        Monitorinstrument monitorinstrument = mtJudgeService.checkProbe(model);
        if (monitorinstrument == null) {
            return;
        }
        //多个500数据重复上传问题,已经注册得设同步缓存
        if (repeatDatafilter(model)) {
            return;
        }
        //执行数据写入 、 报警推送
        List<WarningAlarmDo> warningAlarmDos = instrumentMonitorInfoService.save(model, monitorinstrument);
        //报警消息处理
        if (CollectionUtils.isNotEmpty(warningAlarmDos)) {
            for (WarningAlarmDo warningAlarmDo : warningAlarmDos) {
                warningAlarmDo.setLogId(logId);
                switch (topic) {
                    case "1":
                        service.pushMessage1(JsonUtil.toJson(warningAlarmDo));
                        break;
                    case "2":
                        service.pushMessage2(JsonUtil.toJson(warningAlarmDo));
                        break;
                    case "3":
                        service.pushMessage3(JsonUtil.toJson(warningAlarmDo));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //解决多个一包数据经过多个500重复上传问题,一包数据30秒内是要一条
    public boolean repeatDatafilter(ParamaterModel data) {
        try {
            String sn = data.getSN();
            String cmdid = data.getCmdid();
            ParamaterModel snInfo = tcpClientApi.getSnBychannelId(sn, cmdid).getResult();
            if (null != snInfo) {
                //判断30秒内重复数据
                Date nowTime = snInfo.getNowTime();
                String data1 = snInfo.getData();
                //大于30秒
                if (DateUtils.calculateIntervalTime(new Date(), nowTime, 30)) {
                    //大于30秒解决一个设备多个sn数据同步数据问题 相同命令
                    tcpClientApi.addDeviceChannel(data);
                    return false;
                }
                //小于30秒相同命令对比内容,内容一致不保存数据,不一致保存数据更新缓存
                if (!data.getData().equals(data1)) {
                    tcpClientApi.addDeviceChannel(data);
                }
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSWK_SERIAL_NUMBER01.getCode()), JsonUtil.toJson(data),data.getLogId());
                return true;
            } else {
                //同步sn数据缓存
                tcpClientApi.addDeviceChannel(data);
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }

    private void process(String messageContent) {
        EquipmentState equipmentState = JSONUtil.toBean(messageContent, EquipmentState.class);
        String newState = equipmentState.getState();
        String instrumentNo = equipmentState.getInstrumentNo();
        String instrumentConfigId = equipmentState.getInstrumentConfigId();
        String instrumentConfigNo = equipmentState.getInstrumentConfigNo();
        String hospitalCode = equipmentState.getHospitalCode();
        InstrumentInfoDto instrumentInfoDto = probeRedisApi.getProbeRedisInfo(hospitalCode, instrumentNo + ":" + instrumentConfigId).getResult();
        if(ObjectUtils.isEmpty(instrumentInfoDto)){
            return;
        }
        String oldState = instrumentInfoDto.getState();
        if (StringUtils.isEmpty(oldState)){
            oldState="0";
        }
        switch (newState){
            //从不报警变成报警
            case SysConstants.IN_ALARM:
                //判断缓存中的信息是否相同 不相同时更新
                if(SysConstants.IN_ALARM.equals(oldState)){
                    return;
                }
                Instrumentparamconfig instrumentparamconfig = new Instrumentparamconfig();
                instrumentparamconfig.setInstrumentparamconfigno(instrumentConfigNo);
                instrumentparamconfig.setState(newState);
                instrumentparamconfigService.updateInfo(instrumentparamconfig);
                instrumentInfoDto.setState(newState);
                probeRedisApi.addProbeRedisInfo(instrumentInfoDto);
                break;
            case SysConstants.NORMAL:
                //从报警恢复正常
                //判断缓存中的设备状态是否相同 不同时做更改
//                if( SysConstants.NORMAL.equals(oldState)){
//                    return;
//                }
                //同步更新缓存和数据库
                Instrumentparamconfig instrumentParamConfigInfo = new Instrumentparamconfig();
                instrumentParamConfigInfo.setInstrumentparamconfigno(instrumentConfigNo);
                instrumentParamConfigInfo.setState(newState);
                instrumentparamconfigService.updateInfo(instrumentParamConfigInfo);
                instrumentInfoDto.setState(newState);
                probeRedisApi.addProbeRedisInfo(instrumentInfoDto);
                break;
            default:
                break;

        }
    }
}
