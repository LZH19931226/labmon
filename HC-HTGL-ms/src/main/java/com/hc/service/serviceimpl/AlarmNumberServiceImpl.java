package com.hc.service.serviceimpl;

import com.hc.config.RedisTemplateUtil;
import com.hc.po.Monitorequipmentlastdata;
import com.hc.po.Monitorinstrument;
import com.hc.mapper.laboratoryFrom.MonitorInstrumentMapper;
import com.hc.model.AbnormalDataModel;
import com.hc.model.ResponseModel.AlarmData;
import com.hc.model.ResponseModel.AlarmEquipmentTypeInfo;
import com.hc.model.ResponseModel.ShowData;
import com.hc.model.ShowModel;
import com.hc.service.AlarmNumberService;
import com.hc.units.JsonUtil;
import com.hc.units.TimeHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * type :   1  无数据   2  超时   3  数据异常
 * Created by xxf on 2018/10/10.
 */
@Service
public class AlarmNumberServiceImpl implements AlarmNumberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmNumberServiceImpl.class);
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;


    /**
     * 环境
     *
     * @param hospitalcode
     * @param equipmentno
     * @param equipmentname
     * @param alarmEquipmentTypeInfo
     */
    @Override
    public void equipmentType1(String hospitalname, String hospitalcode, String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo) {
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        alarmEquipmentTypeInfo.setNumber(alarmEquipmentTypeInfo.getNumber() + 1);
        AlarmData alarmData = new AlarmData();
        String o = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, equipmentno);
        List<AbnormalDataModel> listModel = new ArrayList<>();
        //获取SN
        Monitorinstrument snByEquipmentno = monitorInstrumentMapper.getSnByEquipmentno(equipmentno);
        String sn = null;
        String mtName = null;
        if (null != snByEquipmentno) {
            sn = snByEquipmentno.getSn();
            mtName = getMtNameBySn(sn);
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (StringUtils.isEmpty(o)) {
            // 无数据
            alarmEquipmentTypeInfo.setDatanone(alarmEquipmentTypeInfo.getDatanone() + 1);
            //存入redis
            alarmData.setType("1");
            alarmData.setHospitalname(hospitalname);
            alarmData.setMonitorequipmentlastdata(null);
            alarmData.setEquipmentname(equipmentname);
            redisTemplateUtil.boundListOps(hospitalcode + "+" + "1").rightPush(JsonUtil.toJson(alarmData));
            nodata(sn, hospitalname, equipmentname, listModel, mtName,"环境");


        } else {
            Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(o, Monitorequipmentlastdata.class);
            alarmData.setWarningTime(monitorequipmentlastdata.getInputdatetime());
            //判断时间是否超时
            double datePoor = TimeHelper.getDatePoor(new Date(), monitorequipmentlastdata.getInputdatetime());
            if (datePoor > 90) {
                //超时
                alarmEquipmentTypeInfo.setDataovertime(alarmEquipmentTypeInfo.getDataovertime() + 1);
                alarmData.setType("2");
                alarmData.setHospitalname(hospitalname);
                alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                alarmData.setEquipmentname(equipmentname);
                redisTemplateUtil.boundListOps(hospitalcode + "+" + "1").rightPush(JsonUtil.toJson(alarmData));
                //objectObjectObjectHashOperations.put(hospitalcode + "+" + "1", equipmentno, JsonUtil.toJson(map));\
                timeout(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"环境");
            }

        }


    }

    public String getMtNameBySn(String sn) {
        //根据SN返回MT名称
        String SN = sn.substring(4, 6);


        switch (SN) {
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
            case "19":
                return "MT200PLUS";
            case "20":
                return "MC20";
            case "21":
                return "MT300DCLite";
            case "18":
                return "MT200LM";
            case "32":
                return "MT1100";

            case "98":
                return "MTHX";
            case "97":
                return "MTHX";
            case "96":
                return "MTHX";
            case "99":
                return "有线设备";
            default:
                return "SN未对应MT类型";


        }
    }


    /**
     * 培养箱
     *
     * @param hospitalcode
     * @param equipmentno
     * @param equipmentname
     * @param alarmEquipmentTypeInfo
     * @param list
     */
    @Override
    public void equipmentType2(String hospitalname, String hospitalcode, String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo, List list) {
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        alarmEquipmentTypeInfo.setNumber(alarmEquipmentTypeInfo.getNumber() + 1);
        AlarmData alarmData = new AlarmData();
        String o = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, equipmentno);
        List<AbnormalDataModel> listModel = new ArrayList<>();
        //获取SN
        Monitorinstrument snByEquipmentno = monitorInstrumentMapper.getSnByEquipmentno(equipmentno);
        String sn = null;
        String mtName = null;
        if (null != snByEquipmentno) {
            sn = snByEquipmentno.getSn();
            mtName = getMtNameBySn(sn);
        }
        if (StringUtils.isEmpty(o)) {
            // 无数据
            alarmEquipmentTypeInfo.setDatanone(alarmEquipmentTypeInfo.getDatanone() + 1);
            //存入redis
            alarmData.setType("1");
            alarmData.setHospitalname(hospitalname);
            alarmData.setMonitorequipmentlastdata(null);
            alarmData.setEquipmentname(equipmentname);
            redisTemplateUtil.boundListOps(hospitalcode + "+" + "2").rightPush(JsonUtil.toJson(alarmData));
            nodata(sn, hospitalname, equipmentname, listModel, mtName,"培养箱");
            //objectObjectObjectHashOperations.put(hospitalcode + "+" + "2", equipmentno, JsonUtil.toJson(map));
        } else {
            Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(o, Monitorequipmentlastdata.class);
            alarmData.setWarningTime(monitorequipmentlastdata.getInputdatetime());
            //判断时间是否超时
            double datePoor = TimeHelper.getDatePoor(new Date(), monitorequipmentlastdata.getInputdatetime());
            if (StringUtils.equals(mtName, "MT100")) {
                String currentqc = monitorequipmentlastdata.getCurrentqc();// QC电量
                if (StringUtils.isNotEmpty(currentqc)) {
                    LOGGER.info("当前MT100电量：" + currentqc + "  当前设备code:" + equipmentno);

                    int i = new BigDecimal(currentqc).compareTo(new BigDecimal("30"));
                    if (i == -1) {
                        //此时电量少于百分之三十
                        lowqc(hospitalcode, sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"培养箱");

                    }
                }
            }
            if (list.contains(monitorequipmentlastdata.getCurrenttemperature()) || list.contains(monitorequipmentlastdata.getCurrentcarbondioxide()) || list.contains(monitorequipmentlastdata.getCurrento2())) {
                //数据异常
                alarmEquipmentTypeInfo.setInstrumentab(alarmEquipmentTypeInfo.getInstrumentab() + 1);
                alarmData.setType("3");
                alarmData.setHospitalname(hospitalname);
                alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                alarmData.setEquipmentname(equipmentname);
                dataan(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"培养箱");
            } else {
                if (datePoor > 90) {
                    //超时
                    alarmEquipmentTypeInfo.setDataovertime(alarmEquipmentTypeInfo.getDataovertime() + 1);
                    alarmData.setType("2");
                    alarmData.setHospitalname(hospitalname);
                    alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                    alarmData.setEquipmentname(equipmentname);
                    redisTemplateUtil.boundListOps(hospitalcode + "+" + "2").rightPush(JsonUtil.toJson(alarmData));
                    //objectObjectObjectHashOperations.put(hospitalcode + "+" + "2", equipmentno, JsonUtil.toJson(map));
                    timeout(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"培养箱");
                }
            }


            // 判断数据是否异常


        }

    }

    //超时
    public void timeout(String sn, String hospitalName, String equipmentname, Monitorequipmentlastdata monitorequipmentlastdata, List<AbnormalDataModel> listModel, String mtName,String equipmenttypeid) {


        AbnormalDataModel abnormalDataModel = new AbnormalDataModel();
        abnormalDataModel.setAbnormaltype("超时");
        abnormalDataModel.setEquipmentname(equipmentname);
        abnormalDataModel.setSn(sn);
        abnormalDataModel.setHospitalname(hospitalName);
        abnormalDataModel.setMtName(mtName);
        abnormalDataModel.setEquipmenttypeid(equipmenttypeid);
        abnormalDataModel.setInputdatetime(TimeHelper.getCurrentDateTimes(monitorequipmentlastdata.getInputdatetime()));
        abnormalDataModel.setAbnormaldetails("当前设备上传数据超时");

        String abnormal = (String) redisTemplateUtil.boundValueOps("abnormal").get();
        if (StringUtils.isNotEmpty(abnormal)) {
            listModel = JsonUtil.toList(abnormal, AbnormalDataModel.class);
        }
        listModel.add(abnormalDataModel);
        redisTemplateUtil.boundValueOps("abnormal").set(JsonUtil.toJson(listModel));
    }

    public void lowqc(String hospitalcode, String sn, String hospitalName, String equipmentname, Monitorequipmentlastdata monitorequipmentlastdata, List<AbnormalDataModel> listModel, String mtName,String equipmenttypeid) {


        AbnormalDataModel abnormalDataModel = new AbnormalDataModel();
        abnormalDataModel.setAbnormaltype("低电量预警");
        abnormalDataModel.setEquipmentname(equipmentname);
        abnormalDataModel.setSn(sn);
        abnormalDataModel.setHospitalname(hospitalName);
        abnormalDataModel.setMtName(mtName);
        abnormalDataModel.setEquipmenttypeid(equipmenttypeid);
        abnormalDataModel.setInputdatetime(TimeHelper.getCurrentDateTimes(monitorequipmentlastdata.getInputdatetime()));
        abnormalDataModel.setAbnormaldetails("当前设备电量过低");

        String abnormal = (String) redisTemplateUtil.boundValueOps("abnormal").get();
        if (StringUtils.isNotEmpty(abnormal)) {
            listModel = JsonUtil.toList(abnormal, AbnormalDataModel.class);
        }
        listModel.add(abnormalDataModel);
        redisTemplateUtil.boundValueOps("abnormal").set(JsonUtil.toJson(listModel));
        redisTemplateUtil.boundListOps(hospitalcode + "+qc").rightPush("1");
    }

    //无数据上传
    public void nodata(String sn, String hospitalName, String equipmentname, List<AbnormalDataModel> listModel, String mtName,String equipmenttypeid) {
        AbnormalDataModel abnormalDataModel = new AbnormalDataModel();
        abnormalDataModel.setAbnormaltype("未上传数据");
        abnormalDataModel.setEquipmentname(equipmentname);
        abnormalDataModel.setSn(sn);
        abnormalDataModel.setHospitalname(hospitalName);
        abnormalDataModel.setMtName(mtName);
        abnormalDataModel.setEquipmenttypeid(equipmenttypeid);
        // abnormalDataModel.setInputdatetime(monitorequipmentlastdata.getInputdatetime());
        abnormalDataModel.setAbnormaldetails("当前设备未上传数据");

        String abnormal = (String) redisTemplateUtil.boundValueOps("abnormal").get();
        if (StringUtils.isNotEmpty(abnormal)) {
            listModel = JsonUtil.toList(abnormal, AbnormalDataModel.class);
        }
        listModel.add(abnormalDataModel);
        redisTemplateUtil.boundValueOps("abnormal").set(JsonUtil.toJson(listModel));
    }

    //数据异常
    public void dataan(String sn, String hospitalName, String equipmentname, Monitorequipmentlastdata monitorequipmentlastdata, List<AbnormalDataModel> listModel, String mtName,String equipmenttypeid) {
        AbnormalDataModel abnormalDataModel = new AbnormalDataModel();
        abnormalDataModel.setAbnormaltype("数据异常");
        abnormalDataModel.setEquipmentname(equipmentname);
        abnormalDataModel.setSn(sn);
        abnormalDataModel.setHospitalname(hospitalName);
        abnormalDataModel.setMtName(mtName);
        abnormalDataModel.setEquipmenttypeid(equipmenttypeid);
        abnormalDataModel.setInputdatetime(TimeHelper.getCurrentDateTimes(monitorequipmentlastdata.getInputdatetime()));
        abnormalDataModel.setAbnormaldetails("当前设备上传数据数据异常");

        String abnormal = (String) redisTemplateUtil.boundValueOps("abnormal").get();
        if (StringUtils.isNotEmpty(abnormal)) {
            listModel = JsonUtil.toList(abnormal, AbnormalDataModel.class);
        }
        listModel.add(abnormalDataModel);
        redisTemplateUtil.boundValueOps("abnormal").set(JsonUtil.toJson(listModel));
    }

    @Override
    public void equipmentType3(String hospitalname, String hospitalcode, String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo, List list) {
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        alarmEquipmentTypeInfo.setNumber(alarmEquipmentTypeInfo.getNumber() + 1);
        AlarmData alarmData = new AlarmData();
        String o = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, equipmentno);
        List<AbnormalDataModel> listModel = new ArrayList<>();
        //获取SN
        Monitorinstrument snByEquipmentno = monitorInstrumentMapper.getSnByEquipmentno(equipmentno);
        String sn = null;
        String mtName = null;
        if (null != snByEquipmentno) {
            sn = snByEquipmentno.getSn();
            mtName = getMtNameBySn(sn);
        }
        if (StringUtils.isEmpty(o)) {
            // 无数据
            alarmEquipmentTypeInfo.setDatanone(alarmEquipmentTypeInfo.getDatanone() + 1);
            //存入redis
            alarmData.setType("1");
            alarmData.setHospitalname(hospitalname);
            alarmData.setMonitorequipmentlastdata(null);
            alarmData.setEquipmentname(equipmentname);
            redisTemplateUtil.boundListOps(hospitalcode + "+" + "3").rightPush(JsonUtil.toJson(alarmData));
            nodata(sn, hospitalname, equipmentname, listModel, mtName,"液氮罐");

            //objectObjectObjectHashOperations.put(hospitalcode + "+" + "3", equipmentno, JsonUtil.toJson(map));
        } else {
            Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(o, Monitorequipmentlastdata.class);
            alarmData.setWarningTime(monitorequipmentlastdata.getInputdatetime());
            //判断时间是否超时
            double datePoor = TimeHelper.getDatePoor(new Date(), monitorequipmentlastdata.getInputdatetime());
            LOGGER.info("时间：" + monitorequipmentlastdata.getInputdatetime() + "datePoor:" + String.valueOf(datePoor));

            if (list.contains(monitorequipmentlastdata.getCurrenttemperature())) {
                //数据异常
                alarmEquipmentTypeInfo.setInstrumentab(alarmEquipmentTypeInfo.getInstrumentab() + 1);
                alarmData.setType("3");
                alarmData.setHospitalname(hospitalname);
                alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                alarmData.setEquipmentname(equipmentname);
                redisTemplateUtil.boundListOps(hospitalcode + "+" + "3").rightPush(JsonUtil.toJson(alarmData));
                dataan(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"液氮罐");
                //objectObjectObjectHashOperations.put(hospitalcode + "+" + "3", equipmentno, JsonUtil.toJson(map));
            } else {

                if (datePoor > 181 && StringUtils.equals("H0006", hospitalcode)) {
                    //超时   山大三小时超时
                    alarmEquipmentTypeInfo.setDataovertime(alarmEquipmentTypeInfo.getDataovertime() + 1);
                    alarmData.setType("2");
                    alarmData.setHospitalname(hospitalname);
                    alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                    alarmData.setEquipmentname(equipmentname);
                    redisTemplateUtil.boundListOps(hospitalcode + "+" + "3").rightPush(JsonUtil.toJson(alarmData));
                    timeout(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"液氮罐");
                    return;
                }
                if (datePoor > 90 && !StringUtils.equals("H0006", hospitalcode)) {
                    alarmEquipmentTypeInfo.setDataovertime(alarmEquipmentTypeInfo.getDataovertime() + 1);
                    alarmData.setType("2");
                    alarmData.setHospitalname(hospitalname);
                    alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                    alarmData.setEquipmentname(equipmentname);
                    redisTemplateUtil.boundListOps(hospitalcode + "+" + "3").rightPush(JsonUtil.toJson(alarmData));
                    timeout(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"液氮罐");
                }
            }


            // 判断数据是否异常


        }
    }

    @Override
    public void equipmentType4(String hospitalname, String hospitalcode, String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo, List list) {
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        alarmEquipmentTypeInfo.setNumber(alarmEquipmentTypeInfo.getNumber() + 1);
        AlarmData alarmData = new AlarmData();
        String o = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, equipmentno);
        List<AbnormalDataModel> listModel = new ArrayList<>();
        //获取SN
        Monitorinstrument snByEquipmentno = monitorInstrumentMapper.getSnByEquipmentno(equipmentno);
        String sn = null;
        String mtName = null;
        if (null != snByEquipmentno) {
            sn = snByEquipmentno.getSn();
            mtName = getMtNameBySn(sn);
        }
        if (StringUtils.isEmpty(o)) {
            // 无数据
            alarmEquipmentTypeInfo.setDatanone(alarmEquipmentTypeInfo.getDatanone() + 1);
            //存入redis
            alarmData.setType("1");
            alarmData.setHospitalname(hospitalname);
            alarmData.setMonitorequipmentlastdata(null);
            alarmData.setEquipmentname(equipmentname);
            redisTemplateUtil.boundListOps(hospitalcode + "+" + "4").rightPush(JsonUtil.toJson(alarmData));
            nodata(sn, hospitalname, equipmentname, listModel, mtName,"冰箱");

        } else {
            Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(o, Monitorequipmentlastdata.class);
            alarmData.setWarningTime(monitorequipmentlastdata.getInputdatetime());
            //判断时间是否超时
            double datePoor = TimeHelper.getDatePoor(new Date(), monitorequipmentlastdata.getInputdatetime());
            if (StringUtils.equals(mtName, "MT100")) {
                String currentqc = monitorequipmentlastdata.getCurrentqc();// QC电量
                if (StringUtils.isNotEmpty(currentqc)) {
                    LOGGER.info("当前MT100电量：" + currentqc + "  当前设备code:" + equipmentno);
                    int i = new BigDecimal(currentqc).compareTo(new BigDecimal("30"));
                    if (i == -1) {
                        //此时电量少于百分之三十
                        lowqc(hospitalcode, sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"冰箱");

                    }
                }
            }
            if (list.contains(monitorequipmentlastdata.getCurrenttemperature())) {
                //数据异常
                alarmEquipmentTypeInfo.setInstrumentab(alarmEquipmentTypeInfo.getInstrumentab() + 1);
                alarmData.setType("3");
                alarmData.setHospitalname(hospitalname);
                alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                alarmData.setEquipmentname(equipmentname);
                redisTemplateUtil.boundListOps(hospitalcode + "+" + "4").rightPush(JsonUtil.toJson(alarmData));
                dataan(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"冰箱");
            } else {
                if (datePoor > 90) {
                    //超时
                    alarmEquipmentTypeInfo.setDataovertime(alarmEquipmentTypeInfo.getDataovertime() + 1);
                    alarmData.setType("2");
                    alarmData.setHospitalname(hospitalname);
                    alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                    alarmData.setEquipmentname(equipmentname);
                    redisTemplateUtil.boundListOps(hospitalcode + "+" + "4").rightPush(JsonUtil.toJson(alarmData));
                    timeout(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"冰箱");
                }
            }
            // 判断数据是否异常
        }
    }

    @Override
    public void equipmentType5(String hospitalname, String hospitalcode, String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo, List list) {
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        alarmEquipmentTypeInfo.setNumber(alarmEquipmentTypeInfo.getNumber() + 1);
        AlarmData alarmData = new AlarmData();
        String o = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, equipmentno);
        List<AbnormalDataModel> listModel = new ArrayList<>();
        //获取SN
        Monitorinstrument snByEquipmentno = monitorInstrumentMapper.getSnByEquipmentno(equipmentno);
        String sn = null;
        String mtName = null;
        if (null != snByEquipmentno) {
            sn = snByEquipmentno.getSn();
            mtName = getMtNameBySn(sn);
        }
        if (StringUtils.isEmpty(o)) {
            // 无数据
            alarmEquipmentTypeInfo.setDatanone(alarmEquipmentTypeInfo.getDatanone() + 1);
            //存入redis
            alarmData.setType("1");
            alarmData.setHospitalname(hospitalname);
            alarmData.setMonitorequipmentlastdata(null);
            alarmData.setEquipmentname(equipmentname);
            redisTemplateUtil.boundListOps(hospitalcode + "+" + "5").rightPush(JsonUtil.toJson(alarmData));
            nodata(sn, hospitalname, equipmentname, listModel, mtName,"操作台");
        } else {
            Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(o, Monitorequipmentlastdata.class);
            alarmData.setWarningTime(monitorequipmentlastdata.getInputdatetime());
            //判断时间是否超时
            double datePoor = TimeHelper.getDatePoor(new Date(), monitorequipmentlastdata.getInputdatetime());
            if (StringUtils.equals(mtName, "MT100")) {
                String currentqc = monitorequipmentlastdata.getCurrentqc();// QC电量
                if (StringUtils.isNotEmpty(currentqc)) {
                    LOGGER.info("当前MT100电量：" + currentqc + "  当前设备code:" + equipmentno);
                    int i = new BigDecimal(currentqc).compareTo(new BigDecimal("30"));
                    if (i == -1) {
                        //此时电量少于百分之三十
                        lowqc(hospitalcode, sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"操作台");

                    }
                }
            }
            if (list.contains(monitorequipmentlastdata.getCurrenttemperature())) {
                //数据异常
                alarmEquipmentTypeInfo.setInstrumentab(alarmEquipmentTypeInfo.getInstrumentab() + 1);
                alarmData.setType("3");
                alarmData.setHospitalname(hospitalname);
                alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                alarmData.setEquipmentname(equipmentname);
                redisTemplateUtil.boundListOps(hospitalcode + "+" + "5").rightPush(JsonUtil.toJson(alarmData));
                dataan(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"操作台");
            } else {
                if (datePoor > 90) {
                    //超时
                    alarmEquipmentTypeInfo.setDataovertime(alarmEquipmentTypeInfo.getDataovertime() + 1);
                    alarmData.setType("2");
                    alarmData.setHospitalname(hospitalname);
                    alarmData.setMonitorequipmentlastdata(monitorequipmentlastdata);
                    alarmData.setEquipmentname(equipmentname);
                    redisTemplateUtil.boundListOps(hospitalcode + "+" + "5").rightPush(JsonUtil.toJson(alarmData));
                    timeout(sn, hospitalname, equipmentname, monitorequipmentlastdata, listModel, mtName,"操作台");
                }
            }


            // 判断数据是否异常


        }
    }

    @Override
    public void equipmentType6(String hospitalcode,String equipmentno, String equipmentname, AlarmEquipmentTypeInfo alarmEquipmentTypeInfo) {
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        alarmEquipmentTypeInfo.setNumber(alarmEquipmentTypeInfo.getNumber() + 1);
        String o = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, equipmentno);
        String type = "0";
        if (StringUtils.isEmpty(o)) {
            //表示市电异常 或者无市电
            type = "1";
            //存入redis中  市电不存入
        } else {
            Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(o, Monitorequipmentlastdata.class);
            if ("1".equals(monitorequipmentlastdata.getCurrentups())) {
                type = "1";
                //存入redis  市电不存入
            }
        }
        alarmEquipmentTypeInfo.setType(type);
    }

    @Override
    public List<ShowData> one(String hospitalname, String equipmentname, String equipmentno) {
        List<ShowData> list = new ArrayList<ShowData>();
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String co2 = (String) objectObjectObjectHashOperations.get("CO2", equipmentno);
        if (StringUtils.isNotEmpty(co2)) {
            ShowModel showModel = JsonUtil.toBean(co2, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        String o2 = (String) objectObjectObjectHashOperations.get("O2", equipmentno);
        if (StringUtils.isNotEmpty(o2)) {
            ShowModel showModel = JsonUtil.toBean(o2, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        String jq = (String) objectObjectObjectHashOperations.get("JQ", equipmentno);
        if (StringUtils.isNotEmpty(jq)) {
            ShowModel showModel = JsonUtil.toBean(jq, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        String temp = (String) objectObjectObjectHashOperations.get("TEMP", equipmentno);
        if (StringUtils.isNotEmpty(temp)) {
            ShowModel showModel = JsonUtil.toBean(temp, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        String voc = (String) objectObjectObjectHashOperations.get("VOC", equipmentno);
        if (StringUtils.isNotEmpty(voc)) {
            ShowModel showModel = JsonUtil.toBean(voc, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        String rh = (String) objectObjectObjectHashOperations.get("RH", equipmentno);
        if (StringUtils.isNotEmpty(rh)) {
            ShowModel showModel = JsonUtil.toBean(rh, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        String PM25 = (String) objectObjectObjectHashOperations.get("PM25", equipmentno);
        if (StringUtils.isNotEmpty(PM25)) {
            ShowModel showModel = JsonUtil.toBean(PM25, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        String PM10 = (String) objectObjectObjectHashOperations.get("PM10", equipmentno);
        if (StringUtils.isNotEmpty(PM10)) {
            ShowModel showModel = JsonUtil.toBean(PM10, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        String press = (String) objectObjectObjectHashOperations.get("PRESS", equipmentno);
        if (StringUtils.isNotEmpty(press)) {
            ShowModel showModel = JsonUtil.toBean(press, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        return list;
    }

    @Override
    public List<ShowData> two(String hospitalname, String equipmentname, String equipmentno) {
        List<ShowData> list = new ArrayList<ShowData>();
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String co2 = (String) objectObjectObjectHashOperations.get("CO2", equipmentno);
        if (StringUtils.isNotEmpty(co2)) {
            ShowModel showModel = JsonUtil.toBean(co2, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        String o2 = (String) objectObjectObjectHashOperations.get("O2", equipmentno);
        if (StringUtils.isNotEmpty(o2)) {
            ShowModel showModel = JsonUtil.toBean(o2, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }

        String temp = (String) objectObjectObjectHashOperations.get("TEMP", equipmentno);
        if (StringUtils.isNotEmpty(temp)) {
            ShowModel showModel = JsonUtil.toBean(temp, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }


        return list;
    }

    @Override
    public List<ShowData> three(String hospitalname, String equipmentname, String equipmentno) {
        List<ShowData> list = new ArrayList<ShowData>();
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String temp = (String) objectObjectObjectHashOperations.get("TEMP", equipmentno);
        if (StringUtils.isNotEmpty(temp)) {
            ShowModel showModel = JsonUtil.toBean(temp, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        return list;
    }

    @Override
    public List<ShowData> four(String hospitalname, String equipmentname, String equipmentno) {
        List<ShowData> list = new ArrayList<ShowData>();
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String temp = (String) objectObjectObjectHashOperations.get("TEMP", equipmentno);
        if (StringUtils.isNotEmpty(temp)) {
            ShowModel showModel = JsonUtil.toBean(temp, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        return list;
    }

    @Override
    public List<ShowData> five(String hospitalname, String equipmentname, String equipmentno) {
        List<ShowData> list = new ArrayList<ShowData>();
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String temp = (String) objectObjectObjectHashOperations.get("TEMP", equipmentno);
        if (StringUtils.isNotEmpty(temp)) {
            ShowModel showModel = JsonUtil.toBean(temp, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        return list;
    }

    @Override
    public List<ShowData> six(String hospitalname, String equipmentname, String equipmentno) {
        List<ShowData> list = new ArrayList<ShowData>();
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String temp = (String) objectObjectObjectHashOperations.get("UPS", equipmentno);
        if (StringUtils.isNotEmpty(temp)) {
            ShowModel showModel = JsonUtil.toBean(temp, ShowModel.class);
            ShowData showData = new ShowData();
            showData.setHospitalname(hospitalname);
            showData.setEquipmentname(equipmentname);
            showData.setData(showModel.getData());
            showData.setInputdatetime(showModel.getInputdatetime());
            showData.setUnit(showModel.getUnit());
            list.add(showData);
        }
        return list;
    }
}
