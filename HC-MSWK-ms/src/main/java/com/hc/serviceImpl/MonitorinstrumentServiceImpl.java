package com.hc.serviceImpl;

import com.hc.config.RedisTemplateUtil;
import com.hc.dao.MonitorequipmentDao;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorinstrument;
import com.hc.mapper.MonitorInstrumentMapper;
import com.hc.model.MapperModel.TimeoutEquipment;
import com.hc.msctservice.MsctService;
import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.service.MessagePushService;
import com.hc.service.MonitorinstrumentService;
import com.hc.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

@Service
public class MonitorinstrumentServiceImpl implements MonitorinstrumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorinstrumentServiceImpl.class);

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private MsctService msctService;
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;
    @Autowired
    private MonitorequipmentDao monitorequipmentDao;
    @Autowired
    private MessagePushService messagePushService;

    @Override
    public Monitorinstrument saveMonitorinstrument(String SN, String mt600sn, ParamaterModel paramaterModel) {

        //  根据sn 号查询医院编号   K hospitalcode:sn   --   K 设备sn  --  value   Monitorinstrument
        //根据MT600 sn号查询医院编号
        try {

            BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("hospital:sn");
            String channel = null;
            Monitorinstrument monitorinstrument = new Monitorinstrument();
            String o = (String) objectObjectObjectBoundHashOperations.get(mt600sn);
            //  Monitorinstrument monitorinstrument4 = monitorInstrumentMapper.selectHospitalCodeBySn(mt600sn);
            //    LOGGER.info("测试从数据库查询数据：" + JsonUtil.toJson(monitorinstrument4) + "查询SN号：" + mt600sn);
            if (StringUtils.isNotEmpty(o)) {
                monitorinstrument = JsonUtil.toBean(o, Monitorinstrument.class);
            } else {
                LOGGER.info("当前探头关联的MT600设备未注册到医院，SN号为：" + mt600sn);


                if (StringUtils.equalsAny(mt600sn, "1821110012", "1821110018", "1832110005", "1809110003", "1821110021", "1832110013", "1832110034",
                        "1821110005", "1832110040", "1832110008", "1821110013", "1821110008", "1821110008", "1832110032"
                        , "1821110027", "1832110026", "1832110038", "1821110011", "1832110048", "1832110006", "1832110024",
                        "1821110019", "1832110013", "1821110009", "1821110020", "1821110018", "1821110021", "1821110023", "1832110003", "1821110016",
                        "1832110030", "1821110022", "1821110001", "1821110012", "1813110002", "1809110003", "1832110005", "1813110004",
                        "1821110002", "1813110005", "1813110020", "1821110007", "1813110026", "1809110002", "1813110001", "1832110033",
                        "1813110018", "1813110010", "1832110045", "1813110030", "1813110009", "1813110019", "1813110012", "1813110028", "1813110006",
                        "1821110015", "1813110016", "1821110025", "1821110030", "1832110047", "1813110022", "1821110010", "1813110007",
                        "1821110004", "1821110028", "1832110011")) {


                    try {
                        LOGGER.info("进入拨打程序");
                        msctService.test2("18108674918", "瑞迪斯存储探头值失效");
                        msctService.test2("17786499503", "瑞迪斯存储探头值失效");
                    } catch (Exception e) {
                        LOGGER.error("redis拨打电话异常：" + e.getMessage());
                    }
                }
                //查询数据库：
                Monitorinstrument monitorinstrumentTest = monitorInstrumentMapper.selectHospitalCodeBySn(mt600sn);
         //       LOGGER.info("从数据库查询数据：" + JsonUtil.toJson(monitorinstrumentTest) + "查询SN号：" + mt600sn);
                //    String instrumentno = monitorinstrumentTest.getInstrumentno();

                if (monitorinstrumentTest == null) {
                    return null;
                } else {
                    //同步缓存
      //              LOGGER.info("执行同步缓存：");
                    objectObjectObjectBoundHashOperations.put(mt600sn, JsonUtil.toJson(monitorinstrumentTest));
                }
            }
            // 不做自动注册
            //判断当前传送数据设备是否注册到医院
            String sn = "01";
            try {
                sn = SN.substring(4, 6);
            } catch (Exception e) {
               LOGGER.error("SN号异常：" + SN);
            }

            //获取MT型
            String instrumenttypename = MTcheck(sn);
            Monitorinstrument monitorinstrument1 = new Monitorinstrument();
            //根据sn查询当前设备是否警用还是启用
            Monitorequipment cliva = monitorInstrumentMapper.isCliva(SN);
            //    LOGGER.info("当前设备是否启用状态："+SN+";状态："+cliva);
            if (cliva == null){
                return null;
            }
            Boolean clientvisible = cliva.getClientvisible();
            if ( !clientvisible) {
                String equipmentno = cliva.getEquipmentno();
                // 未启用
              //  LOGGER.info("设备未启用SN号：" + SN);
                HashOperations<Object, Object, Object> redisTemple = redisTemplateUtil.opsForHash();
                if (redisTemple.hasKey("disable","equipmentno:"+equipmentno)){
                    //表示当前设备之前禁用，现在数据重新上传，又启用了
                    redisTemple.delete("disable","equipmentno:"+equipmentno);
                    //启用设备
                    monitorequipmentDao.updateMonitorequipmentIsAble(equipmentno);
                    // 报警通知
                    //查询当前设备
                    TimeoutEquipment one = monitorInstrumentMapper.getOne(equipmentno);
                    one.setDisabletype("4");//解除报警
                    messagePushService.pushMessage5(JsonUtil.toJson(one));
                }
                return null;
            }

            if (StringUtils.equalsAny(instrumenttypename, "MT300", "MT300LITE", "MT700")) {
                //存在则判断是不是传的开关量
                if (StringUtils.equals(paramaterModel.getCmdid(),"8d")) {
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
                                //		monitorRegisterHospitalService.instrumentRegister(instrumenttypename, paramaterModel, monitorinstrument, channel);
                                return null;
                            }
                            break;
                    }
                } else {
                    //MT300  MT30LITE非开关量
                    try {
                        String o1 = (String) objectObjectObjectBoundHashOperations.get(SN);
                        if (StringUtils.isNotEmpty(o1)) {
                            monitorinstrument1 = JsonUtil.toBean(o1, Monitorinstrument.class);
                        } else {
                            //	monitorRegisterHospitalService.instrumentRegister(instrumenttypename, paramaterModel, monitorinstrument, channel);
                            //查询数据库是否存在探头
                            monitorinstrument1 = monitorInstrumentMapper.selectHospitalCodeBySn(SN);
                            //      String s  = monitorinstrument1.getInstrumentno();
                            if (monitorinstrument1 == null) {
                                LOGGER.info("当前设备未注册到医院：" + SN);
                                return null;
                            } else {
                                //同步缓存

                                objectObjectObjectBoundHashOperations.put(SN, JsonUtil.toJson(monitorinstrument1));
                            }


                        }
                    } catch (Exception e) {
                        LOGGER.error("第二块代码块获取异常" + e.getMessage() + JsonUtil.toJson(paramaterModel));
                    }
                }

            } else {
                try {
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
                } catch (Exception e) {
                    LOGGER.error("第三块代码块获取异常" + e.getMessage() + JsonUtil.toJson(paramaterModel));
                }
            }

            if (StringUtils.isEmpty(monitorinstrument1.getEquipmentno())) {
        //        LOGGER.info("当前设备探头SN号未绑定设备: " + SN);
                return null;    //数据不做插入 ，不做报警服务  只做添加探头
            }
            return monitorinstrument1;

        } catch (Exception e) {
            LOGGER.error("判断当前探头是否注册失败,原因：" + e.getMessage() + "数据格式为：" + JsonUtil.toJson(paramaterModel));
            return null;
        }

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

//    public static void main(String args[]) {
//        Monitorinstrument monitorinstrument = new Monitorinstrument();
//        monitorinstrument = null;
//        if (StringUtils.isEmpty(monitorinstrument.getInstrumentno())) {
//            System.out.println("哈哈");
//        }
//    }

}
