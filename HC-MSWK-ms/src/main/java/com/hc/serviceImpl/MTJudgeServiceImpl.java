package com.hc.serviceImpl;

import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorinstrument;
import com.hc.mapper.InstrumentMonitorInfoMapper;
import com.hc.mapper.MonitorInstrumentMapper;
import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.service.MTJudgeService;
import com.hc.service.MonitorinstrumentService;
import com.hc.utils.JsonUtil;
import com.redis.util.RedisTemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;

/**
 * Created by 16956 on 2018-08-21.
 */
@Service
public class MTJudgeServiceImpl implements MTJudgeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MTJudgeServiceImpl.class);

    @Autowired
    private MonitorinstrumentService monitorinstrumentservice;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private InstrumentMonitorInfoMapper instrumentMonitorInfoMapper;
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;

    @Override
    public Monitorinstrument mtJudge(ParamaterModel model) {
        //获取到数据
    //    LOGGER.info("A系列信息："+JsonUtil.toJson(model));
        BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("hospital:sn");
        String SN = model.getSN();
        String cmdid = model.getCmdid();
        String type = null;
        try {
            type = cmdid.substring(0, 1);
        } catch (Exception e) {
            LOGGER.error("cmdid为空：" + e.getMessage());
        }
        Monitorinstrument monitorinstrument = new Monitorinstrument();
        switch (type) {
            case "7":
                //MT500通道
                Monitorequipment cliva = monitorInstrumentMapper.isCliva(SN);
                //    LOGGER.info("当前设备是否启用状态："+SN+";状态："+cliva);
                if (cliva == null) {
                    return null;
                }
                Boolean clientvisible = cliva.getClientvisible();
                if (!clientvisible) {
                    return null;
                }
                String o = (String) objectObjectObjectBoundHashOperations.get(model.getSN());
                if (StringUtils.isNotEmpty(o)) {
                    //判断当前探头是否注册到医院 从数据库中查询
                    monitorinstrument = JsonUtil.toBean(o, Monitorinstrument.class);
                    if (StringUtils.isEmpty(monitorinstrument.getEquipmentno())) {
                        return null;
                    }
                } else {
                    monitorinstrument = monitorInstrumentMapper.selectHospitalCodeBySn(model.getSN());

                    if (StringUtils.isEmpty(monitorinstrument.getInstrumentno())) {
                        LOGGER.info("当前探头关联的设备未注册到医院，SN号为：" + model.getSN());
                        return null;
                    } else {
                        //同步到redis缓存中
                        objectObjectObjectBoundHashOperations.put(model.getSN(), JsonUtil.toJson(monitorinstrument));
                    }

                }
                break;
            case "8":
                // MT600通道
                //通道id
                String channelId1 = model.getChannelId();

                //根据通道id查询MT600sn 号
                String MT600SN = (String) redisTemplateUtil.boundValueOps(channelId1 + ":" + ParamaterModel.class.getSimpleName()).get();

                if (StringUtils.isEmpty(MT600SN)) {
                    String o1 = (String) objectObjectObjectBoundHashOperations.get(model.getSN());
                    if (StringUtils.isEmpty(o1)) {
                        LOGGER.info("检测不到MT600SN号:" + JsonUtil.toJson(model) + ",MT600SN: " + MT600SN);
                        return null;
                    } else {
                        // 根据sn 号查询 mt600SN号
                        MT600SN = instrumentMonitorInfoMapper.getMT600SN(model.getSN());
                        if (StringUtils.isEmpty(MT600SN)) {
                            LOGGER.info("检测不到MT600SN号:" + JsonUtil.toJson(model));
                            return null;
                        } else {
                            //同步MT600SN到redis
                            Monitorinstrument monitorinstrument1 = monitorInstrumentMapper.selectHospitalCodeBySn(MT600SN);
                            objectObjectObjectBoundHashOperations.put(MT600SN, JsonUtil.toJson(monitorinstrument1));
                        }
                    }
                }
                monitorinstrument = monitorinstrumentservice.saveMonitorinstrument(SN, MT600SN, model);
                break;
            case "9":
            case "a":  //新增A开头
                // MT600通道
                //通道id
                String channelId = model.getChannelId();

                //根据通道id查询MT600sn 号
                String MT600SN1 = (String) redisTemplateUtil.boundValueOps(channelId + ":" + ParamaterModel.class.getSimpleName()).get();
   //             LOGGER.info("A系列信息1："+MT600SN1+"A系列信息"+JsonUtil.toJson(model));
                if (StringUtils.isEmpty(MT600SN1)) {
                    String o1 = (String) objectObjectObjectBoundHashOperations.get(model.getSN());
                    if (StringUtils.isEmpty(o1)) {
                        LOGGER.info("检测不到MT600SN号:" + JsonUtil.toJson(model) + ",MT600SN: " + MT600SN1);
                        return null;
                    } else {
                        // 根据sn 号查询 mt600SN号
                        MT600SN1 = instrumentMonitorInfoMapper.getMT600SN(model.getSN());
                        if (StringUtils.isEmpty(MT600SN1)) {
                            LOGGER.info("检测不到MT600SN号:" + JsonUtil.toJson(model));
                            return null;
                        }
                    }
                }
                monitorinstrument = monitorinstrumentservice.saveMonitorinstrument(SN, MT600SN1, model);
                break;
            default:
                break;
        }
        return monitorinstrument;
    }
}
