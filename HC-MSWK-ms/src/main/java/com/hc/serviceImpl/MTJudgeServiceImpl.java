package com.hc.serviceImpl;

import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.po.Monitorinstrument;
import com.hc.mapper.MonitorInstrumentMapper;
import com.hc.model.MonitorinstrumentModel;
import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.MTJudgeService;
import com.hc.utils.JsonUtil;
import com.redis.tcp.SnDeviceReidsSync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 16956 on 2018-08-21.
 */
@Service
@Slf4j
public class MTJudgeServiceImpl implements MTJudgeService {

    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;
    @Autowired
    private SnDeviceReidsSync snDeviceReidsSync;

    @Override
    public Monitorinstrument checkProbe(ParamaterModel model) {
        String sn = model.getSN();
        //先从缓存里面取,缓存里面取不出来再从数据库取,判断设备是否注册此处缓存需要后台管理做数据同步
        SnDeviceDto snDeviceDto = snDeviceReidsSync.getSnDeviceDto(sn);
        if (null==snDeviceDto){
            MonitorinstrumentModel monitorinstrumentModel = monitorInstrumentMapper.selectMonitorinstrumentInfoBySn(sn);
            if (null == monitorinstrumentModel) {
                log.info("当前设备未注册sn号:{},数据:{}",sn,JsonUtil.toJson(model));
                return null;
            }
            Boolean clientvisible = monitorinstrumentModel.getClientvisible();
            if (!clientvisible) {
                // 未启用
                log.info("设备未启用SN号:{}",sn);
                return null;
            }
            log.info("该设备同步缓存失败,数据库存在,缓存不存在:{}",JsonUtil.toJson(monitorinstrumentModel));
            return BeanConverter.convert(monitorinstrumentModel,Monitorinstrument.class);
        }
        Monitorinstrument monitorinstrument = new Monitorinstrument();
        BeanUtils.copyProperties(snDeviceDto,monitorinstrument);
        return monitorinstrument;
    }
}
