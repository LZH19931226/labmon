package com.hc.serviceimpl;

import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.device.ProbeRedisApi;
import com.hc.model.WarningDateModel;
import com.hc.model.WarningModel;
import com.hc.model.WarningMqModel;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.util.RegularUtil;
import com.hc.po.Monitorinstrument;
import com.hc.service.MessageSendService;
import com.hc.service.WarningRuleService;
import com.hc.service.WarningService;
import com.hc.utils.JsonUtil;
import com.hc.utils.LowHighVerify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by 16956 on 2018-08-09.
 */
@Service
@Slf4j
public class WarningServiceImpl implements WarningService {
    @Autowired
    private ProbeRedisApi probeRedisApi;
    @Autowired
    private WarningrecordRepository warningrecordDao;
    @Autowired
    private WarningRuleService warningRuleService;
    @Autowired
    private MessageSendService messageSendService;

    @Override
    public WarningModel produceWarn(WarningMqModel warningMqModel, Monitorinstrument monitorinstrument, Date date, Integer instrumentconfigid, String unit) {
        // redis缓存中取  当前探头监控类型数据   高低值
        String data = warningMqModel.getCurrrentData();
        String data1 = warningMqModel.getCurrentData1();
        String hospitalcode = monitorinstrument.getHospitalcode();
        InstrumentInfoDto probe = probeRedisApi.getProbeRedisInfo(hospitalcode, monitorinstrument.getInstrumentno() + ":" + instrumentconfigid).getResult();
        if (null == probe) {
            log.info("缓存探头信息不存在:{}", JsonUtil.toJson(monitorinstrument));
            return null;
        }
        String warningphone = probe.getWarningPhone();
        /*1.判断该设备是否开启报警服务*/
        if (StringUtils.equals("0", warningphone)) {
            //不启用报警，直接过滤信息
            return null;
        }
        WarningModel warningModel = new WarningModel();
        String equipmentname = probe.getEquipmentName();
        String instrumentparamconfigNO = probe.getInstrumentParamConfigNO();
        String equipmentno = probe.getEquipmentNo();
        WarningDateModel warningDateModel = new WarningDateModel();
        warningDateModel.setDate(date);
        Warningrecord warningrecord = new Warningrecord();
        warningrecord.setEquipmentno(equipmentno);
        warningModel.setInstrumentparamconfigNO(instrumentparamconfigNO);
        /*2.探头类型数据范围判断*/
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
            case 42:
            case 43:
                //表示CO2  、氧气、温度  、 培养箱湿度
                if (!RegularUtil.checkContainsNumbers(data)) {
                    //未接传感器
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为:" + data);
                    warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    return null;
                } else {
                    if (StringUtils.isNotEmpty(data1)) {
                        //MT200M 新程序，两路温度判断
                        //老版本mt200m判断逻辑生产周大于20年15周为新的mt200m报警逻辑更改
                        String sn = monitorinstrument.getSn();
                        String proSn = sn.substring(0, 4);
                        String sns = sn.substring(4, 6);
                        if (Integer.parseInt(proSn) < 2031) {
                            //当一路温度值存在异常，整个值无效
                            // 当两个值相差3度，值无效
                            if (!RegularUtil.checkContainsNumbers(data1) || Math.abs(new Double(data) - new Double(data1)) > 3) {
                                warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：超出量程范围");
                                warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "超出量程范围" + "]");
                                warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                                warningrecord.setInputdatetime(date);
                                warningrecord.setHospitalcode(hospitalcode);
                                warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                                warningrecordDao.save(warningrecord);
                                log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                                return null;
                            }
                            if (LowHighVerify.verify(probe, data) && LowHighVerify.verify(probe, data1)) {
                                warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                                warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                                warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                                warningrecord.setInputdatetime(date);
                                warningrecord.setHospitalcode(hospitalcode);
                                warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                                warningrecordDao.save(warningrecord);
                                log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                                break;
                            }
                            break;
                        } else {
                            if (!StringUtils.equals(sns, "17")) {
                                if (!RegularUtil.checkContainsNumbers(data1) || Math.abs(new Double(data) - new Double(data1)) > 3) {
                                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：超出量程范围");
                                    warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "超出量程范围" + "]");
                                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                                    warningrecord.setInputdatetime(date);
                                    warningrecord.setHospitalcode(hospitalcode);
                                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                                    warningrecordDao.save(warningrecord);
                                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                                    return null;
                                }
                                if (LowHighVerify.verify(probe, data) && LowHighVerify.verify(probe, data1)) {
                                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                                    warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                                    warningrecord.setInputdatetime(date);
                                    warningrecord.setHospitalcode(hospitalcode);
                                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                                    warningrecordDao.save(warningrecord);
                                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                                    break;
                                }
                                break;
                            }
                            //获取二路温度探头设置的值
                            InstrumentInfoDto mt200mHighLimit = probeRedisApi.getProbeRedisInfo(hospitalcode, monitorinstrument.getInstrumentno() + ":" + 14).getResult();
                            //大于最大值
                            if (LowHighVerify.verifyMt200m(probe.getHighLimit(), data) && LowHighVerify.verifyMt200m(mt200mHighLimit.getHighLimit(), data1)) {
                                warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                                warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                                warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                                warningrecord.setInputdatetime(date);
                                warningrecord.setHospitalcode(hospitalcode);
                                warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                                warningrecordDao.save(warningrecord);
                                log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                                break;
                            }
                            break;
                        }
                    }
                    //高低值判断
                    if (LowHighVerify.verify(probe, data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                        warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                        warningrecord.setInputdatetime(date);
                        warningrecord.setHospitalcode(hospitalcode);
                        warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                        warningrecordDao.save(warningrecord);
                        log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    }
                }
                break;
            case 10:
                if (StringUtils.equals("1", data)) {
                    warningrecord.setWarningremark("市电异常");
                    warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "市电异常" + "]");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                }
                break;
            case 11:
                if (StringUtils.equals("1", data)) {
                    data = "1.00";
                } else {
                    data = "0.00";
                }
                if (probe.getLowLimit().toString().equals(data)) {
                    warningrecord.setWarningremark(equipmentname + "报警信号异常");
                    warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "报警信息异常" + "]");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
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
                if (!RegularUtil.checkContainsNumbers(data)) {
                    //已接传感器，但未校准
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为:未获取到数据");
                    warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "未获取到数据" + "]");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    break;
                }
                if (LowHighVerify.verify(probe, data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                    warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                }
                break;
            case 23:
            case 24:
                if (!RegularUtil.checkContainsNumbers(data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为:" + data);
                    warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    return null;
                } else {
                    if (LowHighVerify.verify(probe, data)) {
                        warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常数据为:" + data);
                        warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + data + "]");
                        warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                        warningrecord.setInputdatetime(date);
                        warningrecord.setHospitalcode(hospitalcode);
                        warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                        warningrecordDao.save(warningrecord);
                        log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    }
                    break;
                }
            case 25:
                if ("A".equals(data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：未获取到数据");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    return null;
                } else if ("B".equals(data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：流量控制关闭");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    return null;
                } else if ("C".equals(data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：气体流量不稳定");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    return null;
                } else if ("D".equals(data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：气口压力低");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    return null;
                } else if ("E".equals(data)) {
                    //产生报警
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：未获取到数据");
                    warningrecord.setWarningvalue(equipmentname + ":" + unit + " [" + "未获取到数据" + "]");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    break;
                } else if ("F".equals(data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：总开关关闭，但未断电");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                    return null;
                    //I M O为98协议上传的气流状态 需要报警的模型
                } else if ("I".equals(data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：发生漏气报警事件");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                } else if ("M".equals(data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：设备漏气报警");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                } else if ("O".equals(data)) {
                    warningrecord.setWarningremark(equipmentname + "的" + unit + "异常," + "异常原因为：设备气压低报警");
                    warningrecord.setInstrumentparamconfigno(instrumentparamconfigNO);
                    warningrecord.setInputdatetime(date);
                    warningrecord.setHospitalcode(hospitalcode);
                    warningrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    warningrecordDao.save(warningrecord);
                    log.info("产生一条报警记录：" + equipmentname + unit + "数据异常：" + JsonUtil.toJson(warningrecord));
                } else {
                    break;
                }
            default:
                break;
        }
        if (StringUtils.isNotEmpty(warningrecord.getPkid())) {
            //存在报警  、判断当前监控探头类型是否禁用报警
            if (StringUtils.isNotEmpty(probe.getWarningPhone())) {
                if ("1".equals(probe.getWarningPhone())) {
                    //启用报警
                    warningModel = warningRuleService.warningRule(hospitalcode, warningrecord.getPkid(), data, probe, warningrecord.getWarningremark());
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            //未产生报警记录，正常值情况，就删除
            probeRedisApi.removeProbeRedisInfo(hospitalcode, instrumentparamconfigNO);
            //将设备状态信息推送到mq
            EquipmentState equipmentState = new EquipmentState();
            equipmentState.setInstrumentConfigNo(instrumentparamconfigNO);
            equipmentState.setEquipmentNo(equipmentno);
            equipmentState.setInstrumentNo(monitorinstrument.getInstrumentno());
            equipmentState.setState(SysConstants.NORMAL);
            String json = JsonUtil.toJson(equipmentState);
            log.info("推送报警设备状态{}",JsonUtil.toJson(json));
            messageSendService.send(json);
            return null;
        }
        return warningModel;
    }
}
