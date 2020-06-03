package com.hc.serviceImpl;

import com.hc.bean.ParamaterModel;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.InstrumentParamConfigDao;
import com.hc.dao.MonitorInstrumentDao;
import com.hc.entity.Instrumentparamconfig;
import com.hc.entity.Monitorinstrument;
import com.hc.mapper.InstrumentMonitorInfoMapper;
import com.hc.mapper.MonitroInstrumentTypeMapper;
import com.hc.model.ResponseModel.InstrumentMonitorInfoModel;
import com.hc.service.MonitorRegisterHospitalService;
import com.hc.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 16956 on 2018-08-30.
 */
@Service
public class MonitorRegisterHospitalServiceImpl implements MonitorRegisterHospitalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorRegisterHospitalServiceImpl.class);
    @Autowired
    private MonitorInstrumentDao monitorInstrumentDao;
    @Autowired
    private MonitroInstrumentTypeMapper monitroInstrumentTypeMapper;
    @Autowired
    private InstrumentMonitorInfoMapper instrumentMonitorInfoMapper;
    @Autowired
    private InstrumentParamConfigDao instrumentParamConfigDao;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Override
    public void instrumentRegister(String instrumenttypename, ParamaterModel paramaterModel, Monitorinstrument monitorinstrument, String channel) {
        try {
            Monitorinstrument monitorinstrument1 = new Monitorinstrument();
            String SN = paramaterModel.getSN();
            Integer instrumenttypeid = monitroInstrumentTypeMapper.selectInfoByTypeName(instrumenttypename);
            if (org.springframework.util.StringUtils.isEmpty(instrumenttypeid)) {
                LOGGER.info("当前SN：" + SN + "对应监控类型未在数据库注册，请联系管理员");
                return;
            }
            monitorinstrument1.setInstrumentno(UUID.randomUUID().toString().replaceAll("-", ""));
            monitorinstrument1.setInstrumenttypeid(instrumenttypeid);
            monitorinstrument1.setSn(SN);
            monitorinstrument1.setHospitalcode(monitorinstrument.getHospitalcode());
            monitorinstrument1.setChannel(channel);
            monitorinstrument1.setAlarmtime(3);
            //将当前监控设备注册到医院
            monitorinstrument1 = monitorInstrumentDao.save(monitorinstrument1);
            // 添加redis缓存
            if (StringUtils.isEmpty(channel)) {
                BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("hospital:sn");
                objectObjectObjectBoundHashOperations.put(SN, JsonUtil.toJson(monitorinstrument1));
            } else {
                BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("DOOR:" + channel);
                objectObjectObjectBoundHashOperations.put(SN, JsonUtil.toJson(monitorinstrument1));
            }
            //添加探头监控类型
            List<InstrumentMonitorInfoModel> list = new ArrayList<InstrumentMonitorInfoModel>();
            list = instrumentMonitorInfoMapper.selectInfo(instrumenttypeid);
            //添加探头类型
            List<Instrumentparamconfig> list1 = new ArrayList<Instrumentparamconfig>();
            for (InstrumentMonitorInfoModel instrumentMonitorInfoModel : list) {
                //添加探头参数
                Instrumentparamconfig instrumentparamconfig = new Instrumentparamconfig();
                instrumentparamconfig.setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""));
                //默认是不报警
                instrumentparamconfig.setWarningphone("0");
                instrumentparamconfig.setHighlimit(instrumentMonitorInfoModel.getHighlimit());
                instrumentparamconfig.setLowlimit(instrumentMonitorInfoModel.getLowlimit());
                instrumentparamconfig.setInstrumentconfigid(instrumentMonitorInfoModel.getInstrumentconfigid());
                instrumentparamconfig.setInstrumentno(monitorinstrument1.getInstrumentno());
                instrumentparamconfig.setInstrumenttypeid(instrumentMonitorInfoModel.getInstrumenttypeid());
                //默认智能报警次数为3
                instrumentparamconfig.setAlarmtime(3);
                list1.add(instrumentparamconfig);
            }
            instrumentParamConfigDao.save(list1);
        }catch (Exception e){
            LOGGER.error("注册探头失败,原因："+e.getMessage());
        }
    }
}
