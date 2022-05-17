package com.hc.serviceImpl;

import com.hc.po.Monitorequipment;
import com.hc.po.Monitorinstrument;
import com.hc.mapper.MonitorInstrumentMapper;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.service.MonitorinstrumentService;
import com.hc.utils.JsonUtil;
import com.redis.util.RedisTemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;

@Service
public class MonitorinstrumentServiceImpl implements MonitorinstrumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorinstrumentServiceImpl.class);

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;

    @Override
    public Monitorinstrument saveMonitorinstrument(String SN, String mt600sn, ParamaterModel paramaterModel) {

        //  根据sn 号查询医院编号   K hospitalcode:sn   --   K 设备sn  --  value   Monitorinstrument
        //根据MT600 sn号查询医院编号
            BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("hospital:sn");
            String channel;
            String o = (String) objectObjectObjectBoundHashOperations.get(mt600sn);
            if (StringUtils.isEmpty(o)) {
                Monitorinstrument monitorinstrumentTest = monitorInstrumentMapper.selectHospitalCodeBySn(mt600sn);
                LOGGER.info("instrument中MT600信息：" + JsonUtil.toJson(paramaterModel) + "查询MT600SN信息：" + JsonUtil.toJson(monitorinstrumentTest));
                if (monitorinstrumentTest == null) {
                    return null;
                } else {
                    //同步缓存
                    objectObjectObjectBoundHashOperations.put(mt600sn, JsonUtil.toJson(monitorinstrumentTest));
                }
            }
            // 不做自动注册
            //判断当前传送数据设备是否注册到医院
            String   sn = SN.substring(4, 6);
            //获取MT型
            String instrumenttypename = MTcheck(sn);
            Monitorinstrument monitorinstrument1 = new Monitorinstrument();
            //根据sn查询当前设备是否警用还是启用
            Monitorequipment cliva = monitorInstrumentMapper.isCliva(SN);
            LOGGER.info("当前设备是否启用状态："+SN+";状态："+JsonUtil.toJson(cliva));
            if (cliva == null) {
                return null;
            }
            Boolean clientvisible = cliva.getClientvisible();
            if (!clientvisible) {
                // 未启用
                  LOGGER.info("设备未启用SN号：" + SN);
                return null;
            }
            if (StringUtils.equalsAny(instrumenttypename, "MT300", "MT300LITE", "MT700")) {
                //存在则判断是不是传的开关量
                if (StringUtils.equals(paramaterModel.getCmdid(), "8d")) {
                    if (StringUtils.isEmpty(paramaterModel.getDOOR())) {
                        return null;
                    }
                    switch (paramaterModel.getDOOR()) {
                        case "1":
                            channel = "1";
                            BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperation = redisTemplateUtil.boundHashOps("DOOR:" + channel);
                            String s = (String) objectObjectObjectBoundHashOperation.get(SN);
                            if (StringUtils.isNotEmpty(s)) {
                                monitorinstrument1 = JsonUtil.toBean(s, Monitorinstrument.class);
                            } else {
                                //		monitorRegisterHospitalService.instrumentRegister(instrumenttypename, paramaterModel, monitorinstrument, channel);
                                return null;
                            }
                            break;
                        case "2":
                            channel = "2";
                            BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperationss = redisTemplateUtil.boundHashOps("DOOR:" + channel);
                            String p = (String) objectObjectObjectBoundHashOperationss.get(SN);
                            if (StringUtils.isNotEmpty(p)) {
                                monitorinstrument1 = JsonUtil.toBean(p, Monitorinstrument.class);
                            } else {
                                return null;
                            }
                        default:
                            break;
                    }
                } else {
                    //MT300  MT30LITE非开关量
                        LOGGER.info("instrument中查询MT700信息：" + JsonUtil.toJson(paramaterModel));
                        String o1 = (String) objectObjectObjectBoundHashOperations.get(SN);
                        if (StringUtils.isNotEmpty(o1)) {
                            monitorinstrument1 = JsonUtil.toBean(o1, Monitorinstrument.class);
                        } else {
                            //查询数据库是否存在探头
                            monitorinstrument1 = monitorInstrumentMapper.selectHospitalCodeBySn(SN);
                            if (monitorinstrument1 == null) {
                                LOGGER.info("当前设备未注册到医院：" + SN);
                                return null;
                            } else {
                                //同步缓存
                                objectObjectObjectBoundHashOperations.put(SN, JsonUtil.toJson(monitorinstrument1));
                            }
                        }
                }
            } else {
                    String o1 = (String) objectObjectObjectBoundHashOperations.get(SN);
                    if (StringUtils.isNotEmpty(o1)) {
                        monitorinstrument1 = JsonUtil.toBean(o1, Monitorinstrument.class);
                    } else {
                        monitorinstrument1 = monitorInstrumentMapper.selectHospitalCodeBySn(SN);
                        //      LOGGER.info("第三段代码获取数据：" + JsonUtil.toJson(monitorinstrument1));
                        //    String s  = monitorinstrument1.getInstrumentno();
                        if (monitorinstrument1 == null) {
                            //               LOGGER.info("当前设备未注册到医院：" + SN);
                            return null;
                        } else {
                            //同步缓存
                            objectObjectObjectBoundHashOperations.put(SN, JsonUtil.toJson(monitorinstrument1));
                        }
                    }
            }

            if (StringUtils.isEmpty(monitorinstrument1.getEquipmentno())) {
                return null;
            }
            return monitorinstrument1;
    }


    public String MTcheck(String id) {

        switch (id) {
            case "01":
                return "D-CK100";
            case "02":
                return "MT200L";
            case "03":
                return "D-CK900";
            case "04":
                return "MT100";
            case "05":
                return "MT200";
            case "06":
                return "MT300";
            case "07":
                return "MT300DC";
            case "08":
                return "MT300MIX";
            case "09":
                return "C400";
            case "10":
                return "MT500";
            case "11":
                return "MT600";
            case "12":
                return "MT400";
            case "13":
                return "MT100F";
            case "14":
                return "MT300LITE";
            case "15":
                return "MT700";
            case "16":
                return "MT300S";
            case "17":
                return "MT200M";
            case "18":
                return "MT200LM";
            case "98":
                return "MTHX";
            case "97":
                return "MTHX";
            case "96":
                return "MTHX";

        }
        return null;

    }


}
