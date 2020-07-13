package com.hc.serviceimpl;

import com.hc.bean.WarningModel;
import com.hc.bean.WarningMqModel;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.MonitorinstrumentDao;
import com.hc.dao.WarningrecordDao;
import com.hc.entity.Monitorinstrument;
import com.hc.entity.Warningrecord;
import com.hc.model.ResponseModel.InstrumentMonitorInfoModel;
import com.hc.model.WarningDateModel;
import com.hc.service.AlmMsgService;
import com.hc.service.WarningRuleService;
import com.hc.service.WarningService;
import com.hc.utils.HttpUtil;
import com.hc.utils.JsonUtil;
import com.hc.utils.LowHighVerify;
import com.hc.utils.TimeHelper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 16956 on 2018-08-09.
 */
@Service
public class WarningServiceImpl implements WarningService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarningServiceImpl.class);
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private WarningrecordDao warningrecordDao;
    @Autowired
    private WarningRuleService warningRuleService;
    @Autowired
    private MonitorinstrumentDao monitorinstrumentDao;
    @Autowired
    private AlmMsgService almservice;

    @Override
    public WarningModel produceWarn(WarningMqModel warningMqModel, Monitorinstrument monitorinstrument, Date date, Integer instrumentconfigid, String unit) {
        // redis缓存中取  当前探头监控类型数据   高低值
        String data = warningMqModel.getCurrrentData();
        String data1 = warningMqModel.getCurrentData1();
        InstrumentMonitorInfoModel instrumentMonitorInfoModel = new InstrumentMonitorInfoModel();
        BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("hospital:instrumentparam");
        //存在
        String o = (String) objectObjectObjectBoundHashOperations.get(monitorinstrument.getInstrumentno() + ":" + instrumentconfigid.toString());
        if (StringUtils.isNotEmpty(o)) {
            instrumentMonitorInfoModel = JsonUtil.toBean(o, InstrumentMonitorInfoModel.class);
        } else {
            LOGGER.info("SN：" + monitorinstrument.getSn() + "设备：" + unit + "未绑定探头监控类型;" + "实际探头值为：" + JsonUtil.toJson(instrumentMonitorInfoModel));
            return null;
        }
        //温度  co2  o2 存在 ABCD
        String warningphone = instrumentMonitorInfoModel.getWarningphone();
        if (StringUtils.equals("0", warningphone)) {
            //不启用报警，直接过滤信息
            return null;
        }
        WarningModel warningModel;
        String hospitalcode = monitorinstrument.getHospitalcode();
        String equipmentname = instrumentMonitorInfoModel.getEquipmentname();
        String instrumentparamconfigNO = instrumentMonitorInfoModel.getInstrumentparamconfigNO();
        if (StringUtils.isEmpty(instrumentparamconfigNO)) {
            LOGGER.info("redis缓存失败，取不到探头类型编号：数据为" + JsonUtil.toJson(instrumentMonitorInfoModel));
            return null;
        }
        // 判断是否是多个500引起的报警问题
        WarningDateModel warningDateModel = new WarningDateModel();
        warningDateModel.setDate(date);
        Warningrecord warningrecord = new Warningrecord();
        warningrecord.setInstrumentparamconfigNO(instrumentparamconfigNO);
        warningrecord.setInputdatetime(date);
        warningrecord.setHospitalcode(hospitalcode);
        warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
        try {
            switch (instrumentconfigid) {
                case 1:
                case 2:
                case 4:
                case 5:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 36:
                case 37:
                case 38:
                case 39:
                    //表示CO2  、氧气、温度  、 培养箱湿度
                    if ("A".equals(data)) {
                        //未接传感器
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：未接传感器或者未获取到数据");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "未接传感器或者未获取到数据" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("B".equals(data)) {
                        //若超出量程范围
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：超出量程范围");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "超出量程范围" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("C".equals(data)) {
                        //已接传感器且已校准，但值无效
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：已接传感器且已校准，但值无效");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "值无效" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("D".equals(data)) {
                        //已接传感器，但未校准
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为:已接传感器，但未校准");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "未校准" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else {
                        if (StringUtils.isNotEmpty(data1)) {
                            //MT200M 新程序，两路温度判断
                            //当一路温度值存在异常，整个值无效
                            // 当两个值相差3度，值无效
                            LOGGER.info("设备名：" + equipmentname + " 温度值1：" + data + "温度值2：" + data1);
                            if (StringUtils.equalsAny(data1, "A", "B", "C", "D", "E") || Math.abs(new Double(data) - new Double(data1)) > 3) {
                                warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：超出量程范围");
                                warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "超出量程范围" + "]");
                                saveWarningrecord(warningrecord,equipmentname,unit);
                                return null;
                            }
                            if (LowHighVerify.verify(instrumentMonitorInfoModel, data) && LowHighVerify.verify(instrumentMonitorInfoModel, data1)) {
                                warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                                warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                                saveWarningrecord(warningrecord,equipmentname,unit);
                            }
                        }
                        //高低值判断
                        if (LowHighVerify.verify(instrumentMonitorInfoModel, data)) {
                            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                            warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                            saveWarningrecord(warningrecord,equipmentname,unit);
                            //天津和睦家： H33
                            if (StringUtils.endsWithAny(hospitalcode, "H0010", "H33") && instrumentconfigid == 2 && monitorinstrument.getInstrumenttypeid() == 8) {
                                //调用声光报警    根据 hospitalcode 查询mt600SN 号
                                Monitorinstrument monitorinstrumentByCode = monitorinstrumentDao.getMonitorinstrumentByCode(hospitalcode);
                                String MT600SN = monitorinstrumentByCode.getSn();
                                almservice.pushMessage(MT600SN);
                            }
                        } else {
                            if (StringUtils.endsWithAny(hospitalcode, "H0010", "H33") && instrumentconfigid == 2 && monitorinstrument.getInstrumenttypeid() == 8) {
                                Monitorinstrument monitorinstrumentByCode = monitorinstrumentDao.getMonitorinstrumentByCode(hospitalcode);
                                String MT600SN = monitorinstrumentByCode.getSn();
                                if (redisTemplateUtil.hasKey(MT600SN + "lgtalarm")) {
                                    String o2 = (String) redisTemplateUtil.boundValueOps(MT600SN + "lgtalarm").get();
                                    if (StringUtils.equals("1", o2)) {
                                        almservice.pushMessage1(MT600SN);
                                    }
                                }
                            }
                        }
                        break;
                    }
                case 10:
                    if (StringUtils.equals("1", data)) {
                        warningrecord.setWarningremark("市电异常");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "市电异常" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                    }
                    break;
                case 11:
                    if (StringUtils.equals("1", data)) {
                        data = "1.00";
                    } else {
                        data = "0.00";
                    }
                    if (instrumentMonitorInfoModel.getLowlimit().toString().equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "报警信号异常");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "报警信息异常" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                    }
                    break;
                case 3:
                case 6:
                case 7:
                case 8:
                case 9:
                case 12:
                case 26:
                case 27:
                case 28:
                case 35:
                    if (LowHighVerify.verify(instrumentMonitorInfoModel, data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                    }
                    break;
                case 23:
                case 24:
                    if ("A".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：未获取到数据");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "未获取到数据" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("B".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：温度控制关闭");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "温度控制关闭" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("C".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：仓室门开启");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "仓室门开启" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("D".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：温度控制异常");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "温度控制异常" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("E".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：总开关关闭");
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "总开关关闭" + "]");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else {
                        if (LowHighVerify.verify(instrumentMonitorInfoModel, data)) {
                            warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                            warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                            saveWarningrecord(warningrecord,equipmentname,unit);
                        }
                        break;
                    }
                case 25:
                    if ("A".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：未获取到数据");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("B".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：流量控制关闭");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("C".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：气体流量不稳定");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("D".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：气口压力低");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("E".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：无气或气流超出范围");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else if ("F".equals(data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：总开关关闭，但未断电");
                        saveWarningrecord(warningrecord,equipmentname,unit);
                        return null;
                    } else {
                        break;
                    }
                default:
                    break;
            }

        } catch (Exception e) {
            LOGGER.error("插入报警数据失败，原因：" + e.getMessage() + "数据为：" + JsonUtil.toJson(monitorinstrument) + "instrumentconfigid:" + instrumentconfigid.toString() + "instrumentMonitorInfoModel:" + instrumentMonitorInfoModel);
            return null;
        }
        if (StringUtils.isNotEmpty(warningrecord.getPkid())) {
            //存在报警  、判断当前监控探头类型是否禁用报警
            try {
                if (StringUtils.isNotEmpty(instrumentMonitorInfoModel.getWarningphone())) {

                    if ("1".equals(instrumentMonitorInfoModel.getWarningphone())) {
                        //启用报警
                        warningModel = warningRuleService.warningRule(hospitalcode, warningrecord.getPkid(), data, instrumentMonitorInfoModel, warningrecord.getWarningremark());
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                LOGGER.error("推送报警数据失败，原因：" + e.getMessage() + "数据为：" + JsonUtil.toJson(monitorinstrument) + "instrumentconfigid:" + instrumentconfigid.toString() + "instrumentMonitorInfoModel:" + instrumentMonitorInfoModel);
                return null;
            }
        } else {
            //未产生报警记录，正常值情况，就删除
            // 存在即删除
            try {
                if (redisTemplateUtil.hasKey(instrumentparamconfigNO)) {
                    redisTemplateUtil.delete(instrumentparamconfigNO);
                }
            } catch (Exception e) {
                LOGGER.error("删除报警数据失败，原因：" + e.getMessage() + "数据为：" + JsonUtil.toJson(monitorinstrument) + "instrumentconfigid:" + instrumentconfigid.toString() + "instrumentMonitorInfoModel:" + instrumentMonitorInfoModel);
                return null;
            }

            return null;
        }

        return warningModel;

    }
    private void  saveWarningrecord(Warningrecord warningrecord,String equipmentname,String unit){
        warningrecordDao.save(warningrecord);
        LOGGER.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
    }
}
