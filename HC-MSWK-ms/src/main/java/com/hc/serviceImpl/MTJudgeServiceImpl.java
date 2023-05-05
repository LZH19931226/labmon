package com.hc.serviceImpl;

import com.hc.device.SnDeviceRedisApi;
import com.hc.mapper.MonitorInstrumentMapper;
import com.hc.model.MonitorinstrumentModel;
import com.hc.my.common.core.constant.enums.ElkLogDetail;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.my.common.core.util.ObjectConvertUtils;
import com.hc.po.Monitorinstrument;
import com.hc.service.MTJudgeService;
import com.hc.tcp.TcpClientApi;
import com.hc.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 16956 on 2018-08-21.
 */
@Service
@Slf4j
public class MTJudgeServiceImpl implements MTJudgeService {

    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;

    @Autowired
    private SnDeviceRedisApi snDeviceRedisSync;

    @Autowired
    private TcpClientApi tcpClientApi;

    @Override
    public List<Monitorinstrument> checkProbe(ParamaterModel model) {
        String sn = model.getSN();
        //先从缓存里面取,缓存里面取不出来再从数据库取,判断设备是否注册此处缓存需要后台管理做数据同步
        List<SnDeviceDto> snDevices = snDeviceRedisSync.getSnDeviceDto(sn).getResult();
        if (CollectionUtils.isEmpty(snDevices)) {
            List<MonitorinstrumentModel> monitorinstrumentModel = monitorInstrumentMapper.selectMonitorinstrumentInfoBySn(sn);
            if (CollectionUtils.isEmpty(monitorinstrumentModel)) {
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSWK_SERIAL_NUMBER02.getCode()), JsonUtil.toJson(model), model.getLogId());
                return null;
            }
            if (monitorinstrumentModel.size() == 1) {
                MonitorinstrumentModel monitorinstrument = monitorinstrumentModel.get(0);
                if (null == monitorinstrument) {
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSWK_SERIAL_NUMBER02.getCode()), JsonUtil.toJson(model), model.getLogId());
                    return null;
                }
                Boolean clientvisible = monitorinstrument.getClientvisible();
                if (!clientvisible) {
                    // 未启用
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSWK_SERIAL_NUMBER03.getCode()), JsonUtil.toJson(model), model.getLogId());
                    return null;
                }
                return Collections.singletonList(BeanConverter.convert(monitorinstrument, Monitorinstrument.class));
            } else {
                //获取启用的数量
                List<MonitorinstrumentModel> collect = monitorinstrumentModel.stream().filter(MonitorinstrumentModel::getClientvisible).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(collect)) {
                    // 未启用
                    ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSWK_SERIAL_NUMBER03.getCode()), JsonUtil.toJson(model), model.getLogId());
                    return null;
                }
                return BeanConverter.convert(collect, Monitorinstrument.class);
            }
        }
        return objectConversion(snDevices);
    }

    @Override
    public boolean filterHosChannel(String channelId, String hospitalcode) {
        Map<Object, Object> tcpClientMap = tcpClientApi.getAllClientInfo().getResult();
        if (tcpClientMap.isEmpty()) {
            return false;
        }
        String clientSn = ObjectConvertUtils.getKey(tcpClientMap, channelId);
        if (StringUtils.isEmpty(clientSn)) {
            log.info("无效通道,通道未绑定设备:{}",channelId);
            return false;
        }
        List<SnDeviceDto> snDevices = snDeviceRedisSync.getSnDeviceDto(clientSn).getResult();
        if (CollectionUtils.isEmpty(snDevices)) {
            return false;
        }
        String hospitalCode = snDevices.get(0).getHospitalCode();
        if (StringUtils.isEmpty(hospitalCode) || !StringUtils.equals(hospitalcode, hospitalCode)) {
            log.info("设备通道与MT1100上传通道不符");
            return false;
        }
        return true;
    }

    private List<Monitorinstrument> objectConversion(List<SnDeviceDto> snDevices) {
        List<Monitorinstrument> list = new ArrayList<>();
        for (SnDeviceDto snDeviceDto : snDevices) {
            if (ObjectUtils.isEmpty(snDeviceDto)) {
                continue;
            }
            String instrumentTypeId = snDeviceDto.getInstrumentTypeId();
            Monitorinstrument monitorinstrument = new Monitorinstrument();
            monitorinstrument.setChannel(snDeviceDto.getChannel());
            monitorinstrument.setEquipmentno(snDeviceDto.getEquipmentNo());
            monitorinstrument.setHospitalcode(snDeviceDto.getHospitalCode());
            monitorinstrument.setInstrumentno(snDeviceDto.getInstrumentNo());
            monitorinstrument.setInstrumenttypeid(StringUtils.isEmpty(instrumentTypeId) ? null : Integer.valueOf(instrumentTypeId));
            monitorinstrument.setSn(snDeviceDto.getSn());
            monitorinstrument.setInstrumentname(snDeviceDto.getInstrumentName());
            monitorinstrument.setClientvisible(0 != snDeviceDto.getClientVisible());
            list.add(monitorinstrument);
        }
        return list;
    }


}
