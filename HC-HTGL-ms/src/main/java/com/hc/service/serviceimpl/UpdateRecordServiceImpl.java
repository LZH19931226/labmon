package com.hc.service.serviceimpl;

import com.hc.entity.Hospitalofreginfo;
import com.hc.entity.Operationlog;
import com.hc.entity.Operationlogdetail;
import com.hc.entity.Userright;
import com.hc.mapper.laboratoryFrom.OperationlogdetaiMapper;
import com.hc.model.RequestModel.EquipmentInfoModel;
import com.hc.model.RequestModel.EquipmentTypeInfoModel;
import com.hc.model.RequestModel.InstrumentInfoModel;
import com.hc.service.OperationlogService;
import com.hc.service.UpdateRecordService;
import com.hc.units.JsonUtil;
import com.hc.units.TimeHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 15350 on 2020/5/21.
 */
@Service
public class UpdateRecordServiceImpl implements UpdateRecordService {
    @Autowired
    private OperationlogdetaiMapper operationlogdetaiMapper;
    @Autowired
    private OperationlogService operationlogService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateRecordServiceImpl.class);

    @Override
    public void updateUser(String hospitalname, String name, Userright oldUserRight, Userright nowUserRight, String type, String operationType) {
        //加锁
        LOGGER.info("原始数据：" + JsonUtil.toJson(oldUserRight) + "新数据：" + JsonUtil.toJson(nowUserRight));
        List<Operationlogdetail> operationlogdetails = new ArrayList<>();
        String userid = null;
        String userid1 = oldUserRight.getUserid();
        String userid2 = nowUserRight.getUserid();
        if (StringUtils.isNotEmpty(userid1)) {
            userid = userid1;
        }
        if (StringUtils.isNotEmpty(userid2)) {
            userid = userid2;
        }

        boolean flag = false;
        //比对消息
        //用户名
        String username = oldUserRight.getUsername();
        String username1 = nowUserRight.getUsername();
        if (!StringUtils.equals(username, username1)) {
            flag = true;
            //用户名发生变更
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("username");
            operationlogdetail.setFiledcaption("用户名");
            operationlogdetail.setFiledvalue(username1);//当前值
            operationlogdetail.setFiledvalueprev(username);//历史值
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);

        }
        //密码
        String pwd = oldUserRight.getPwd();
        String pwd1 = nowUserRight.getPwd();
        if (!StringUtils.equals(pwd, pwd1)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("pwd");
            operationlogdetail.setFiledcaption("密码");
            operationlogdetail.setFiledvalue(pwd1);//当前值
            operationlogdetail.setFiledvalueprev(pwd);//历史值
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);
        }
        //用户类型
        String usertype = oldUserRight.getUsertype();
        String usertype1 = nowUserRight.getUsertype();
        if (!StringUtils.equals(usertype1, usertype)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("usertype");
            operationlogdetail.setFiledcaption("用户类型");
            operationlogdetail.setFiledvalue(usertype1);//当前值
            operationlogdetail.setFiledvalueprev(usertype);//历史值
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);
        }
        //是否可用
        Boolean isuse = oldUserRight.getIsuse();
        if (null == isuse) {
            isuse = false;
        }

        Boolean isuse1 = nowUserRight.getIsuse();
        if (null == isuse1) {
            isuse1 = false;
        }

        if (!isuse && isuse1) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("isuse");
            operationlogdetail.setFiledcaption("用户是否可用");
            if (isuse1) {
                operationlogdetail.setFiledvalue("1");//当前值
            } else {
                operationlogdetail.setFiledvalue("0");//当前值
            }
            if (isuse) {
                operationlogdetail.setFiledvalueprev("1");//历史值
            } else {
                operationlogdetail.setFiledvalueprev("0");//当前值
            }
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);
        }


        //手机号码
        String phonenum = oldUserRight.getPhonenum();
        String phonenum1 = nowUserRight.getPhonenum();
        if (!StringUtils.equals(phonenum, phonenum1)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("phonenum");
            operationlogdetail.setFiledcaption("手机号码");
            operationlogdetail.setFiledvalue(phonenum1);//当前值
            operationlogdetail.setFiledvalueprev(phonenum);//历史值
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);
        }

        if (flag) {
            Operationlog operationlog = new Operationlog();
            operationlog.setFunctionname("人员管理");
            //根据用户名获取医院账户
            //      String hospitalName = operationlogdetaiMapper.getHospitalName(userid);
            //查询医院名称
            operationlog.setHospitalname(hospitalname);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("userright");
            operationlog.setUsername(name);
            operationlog.setPlatform(type);
            operationlogService.addOperationLogInfo(operationlog, operationlogdetails);

        }


    }

    @Override
    public void updateHospotal(String hospitalname, String name, Hospitalofreginfo oldHospitalInfo, Hospitalofreginfo nowHospitalInfo, String type, String operationType) {


        List<Operationlogdetail> operationlogdetails = new ArrayList<>();

        LOGGER.info("原始数据：" + JsonUtil.toJson(oldHospitalInfo) + "新数据：" + JsonUtil.toJson(nowHospitalInfo));

        boolean flag = false;
        String hospitalcode = null;
        String hospitalcode1 = oldHospitalInfo.getHospitalcode();
        String hospitalcode2 = nowHospitalInfo.getHospitalcode();
        if (StringUtils.isNotEmpty(hospitalcode1)) {
            hospitalcode = hospitalcode1;
        }
        if (StringUtils.isNotEmpty(hospitalcode2)) {
            hospitalcode = hospitalcode2;
        }
        String hospitalname1 = oldHospitalInfo.getHospitalname();//原名称
        String hospitalname2 = nowHospitalInfo.getHospitalname();//当前名称
        if (!StringUtils.equals(hospitalname1, hospitalname2)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("hospitalname");
            operationlogdetail.setFiledcaption("医院简称");
            operationlogdetail.setFiledvalue(hospitalname2);//当前值
            operationlogdetail.setFiledvalueprev(hospitalname1);//历史值
            operationlogdetails.add(operationlogdetail);
        }


        String hospitalfullname1 = oldHospitalInfo.getHospitalfullname();
        String hospitalfullname2 = nowHospitalInfo.getHospitalfullname();
        if (!StringUtils.equals(hospitalfullname1, hospitalfullname2)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("hospitalfullname");
            operationlogdetail.setFiledcaption("医院全称");
            operationlogdetail.setFiledvalue(hospitalfullname2);//当前值
            operationlogdetail.setFiledvalueprev(hospitalfullname1);//历史值
            operationlogdetails.add(operationlogdetail);
        }

        String isenabl1 = oldHospitalInfo.getIsenable();//是否可用
        String isenabl2 = nowHospitalInfo.getIsenable();//当前是否可用
        if (!StringUtils.equals(isenabl1, isenabl2)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("isenable");
            operationlogdetail.setFiledcaption("是否可用");
            operationlogdetail.setFiledvalue(isenabl2);//当前值
            operationlogdetail.setFiledvalueprev(isenabl1);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        String alwayalarm1 = oldHospitalInfo.getAlwayalarm();//是否全天报警
        String alwayalarm2 = nowHospitalInfo.getAlwayalarm();
        if (!StringUtils.equals(alwayalarm1, alwayalarm2)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("alwayalarm");
            operationlogdetail.setFiledcaption("是否全天报警");
            operationlogdetail.setFiledvalue(alwayalarm2);//当前值
            operationlogdetail.setFiledvalueprev(alwayalarm1);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        Date begintime1 = oldHospitalInfo.getBegintime();//开始时间
        Date begintime2 = nowHospitalInfo.getBegintime();
        if (null != begintime1 && null != begintime2) {
            //两个时间比较
            String currentDateTimes = TimeHelper.getCurrentDateTimes(begintime1);
            String currentDateTimes1 = TimeHelper.getCurrentDateTimes(begintime2);
            if (!StringUtils.equals(currentDateTimes, currentDateTimes1)) {
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("begintime");
                operationlogdetail.setFiledcaption("报警禁用开始时间");
                operationlogdetail.setFiledvalue(currentDateTimes1);//当前值
                operationlogdetail.setFiledvalueprev(currentDateTimes);//历史值
                operationlogdetails.add(operationlogdetail);
            }
        }
        Date endtime1 = oldHospitalInfo.getEndtime();//结束时间
        Date endtime2 = nowHospitalInfo.getEndtime();//结束时间
        if (null != endtime1 && null != endtime2) {
            //两个时间比较
            String currentDateTimes = TimeHelper.getCurrentDateTimes(endtime1);
            String currentDateTimes1 = TimeHelper.getCurrentDateTimes(endtime2);
            if (!StringUtils.equals(currentDateTimes, currentDateTimes1)) {
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("endtime");
                operationlogdetail.setFiledcaption("报警禁用结束时间");
                operationlogdetail.setFiledvalue(currentDateTimes1);//当前值
                operationlogdetail.setFiledvalueprev(currentDateTimes);//历史值
                operationlogdetails.add(operationlogdetail);
            }
        }
        if (flag) {
            Operationlog operationlog = new Operationlog();
            operationlog.setFunctionname("医院管理");
            //根据用户名获取医院账户
            //      String hospitalName = operationlogdetaiMapper.getHospitalName(userid);
            //查询医院名称
            operationlog.setHospitalname(hospitalname);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("hospitalofreginfo");
            operationlog.setUsername(name);
            operationlog.setPlatform(type);
            operationlogService.addOperationLogInfo(operationlog, operationlogdetails);
        }


    }

    @Override
    public void updateEquipmentType(String hospitalname, String name, EquipmentTypeInfoModel oldEquipmentTypeInfo, EquipmentTypeInfoModel nowEquipmentTypeInfo, String type, String operationType) {
        List<Operationlogdetail> operationlogdetails = new ArrayList<>();
        boolean flag = false;
        String equipmenttypeid = oldEquipmentTypeInfo.getEquipmenttypeid();//设备类型原始值
        String equipmenttypeid1 = nowEquipmentTypeInfo.getEquipmenttypeid();//设备类型当前值
        if (!StringUtils.equals(equipmenttypeid1, equipmenttypeid)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("equipmenttypeid");
            operationlogdetail.setFiledcaption("设备类型");
            operationlogdetail.setFiledvalue(equipmenttypeid1);//当前值
            operationlogdetail.setFiledvalueprev(equipmenttypeid);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        String isvisible = oldEquipmentTypeInfo.getIsvisible();//是否可用
        String isvisible1 = nowEquipmentTypeInfo.getIsvisible();//是否可用当前值
        if (!StringUtils.equals(isvisible, isvisible1)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("isvisible");
            operationlogdetail.setFiledcaption("是否可用");
            operationlogdetail.setFiledvalue(isvisible1);//当前值
            operationlogdetail.setFiledvalueprev(isvisible);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        String timeout = oldEquipmentTypeInfo.getTimeout();//超时报警
        String timeout1 = nowEquipmentTypeInfo.getTimeout();//超时报警
        if (!StringUtils.equals(timeout, timeout1)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("timeout");
            operationlogdetail.setFiledcaption("是否设置超时报警");
            operationlogdetail.setFiledvalue(timeout1);//当前值
            operationlogdetail.setFiledvalueprev(timeout);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        Integer timeouttime = oldEquipmentTypeInfo.getTimeouttime();//超时报警时间间隔
        Integer timeouttime1 = nowEquipmentTypeInfo.getTimeouttime();//超时报警时间间隔
        if (timeouttime == null) {
            timeouttime = 0;
        }
        if (timeouttime1 == null) {
            timeouttime1 = 0;
        }
        if (timeouttime != timeouttime1) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("timeouttime");
            operationlogdetail.setFiledcaption("超时报警时长");
            operationlogdetail.setFiledvalue(timeouttime1.toString());//当前值
            operationlogdetail.setFiledvalueprev(timeouttime.toString());//历史值
            operationlogdetails.add(operationlogdetail);
        }
        if (flag) {
            Operationlog operationlog = new Operationlog();
            operationlog.setFunctionname("设备类型管理");
            //根据用户名获取医院账户
            //      String hospitalName = operationlogdetaiMapper.getHospitalName(userid);
            //查询医院名称
            operationlog.setHospitalname(hospitalname);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("hospitalequiment");
            operationlog.setUsername(name);
            operationlog.setPlatform(type);
            operationlogService.addOperationLogInfo(operationlog, operationlogdetails);
        }


    }

    @Override
    public void updateEquipmentMonitor(String equipmentnames, String hospitalname, String name, EquipmentInfoModel oldEquipmentInfoModel, EquipmentInfoModel nowEquipmentInfoModel, String type, String operationType) {
        List<Operationlogdetail> operationlogdetails = new ArrayList<>();
        boolean flag = false;
        String equipmentname = oldEquipmentInfoModel.getEquipmentname();//设备名称
        String equipmentname1 = nowEquipmentInfoModel.getEquipmentname();//当前设备名称
        if (!StringUtils.equals(equipmentname1, equipmentname)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("equipmentname");
            operationlogdetail.setFiledcaption("设备名称");
            operationlogdetail.setFiledvalue(equipmentname1);//当前值
            operationlogdetail.setFiledvalueprev(equipmentname);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        Boolean clientvisible = oldEquipmentInfoModel.isClientvisible();//是否可用
        if (null == clientvisible) {
            clientvisible = false;
        }
        Boolean clientvisible1 = nowEquipmentInfoModel.isClientvisible();//是否可用
        if (null == clientvisible1) {
            clientvisible1 = false;
        }
        if (!(clientvisible && clientvisible1)) {
            flag = true;
            String old = "可用";
            String now = "可用";
            if (!clientvisible) {
                old = "不可用";
            }
            if (!clientvisible1) {
                now = "不可用";
            }
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("clientvisible");
            operationlogdetail.setFiledcaption("是否可用");
            operationlogdetail.setFiledvalue(now);//当前值
            operationlogdetail.setFiledvalueprev(old);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        if (flag) {
            Operationlog operationlog = new Operationlog();
            operationlog.setFunctionname("设备管理");
            operationlog.setHospitalname(hospitalname);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("monitorequipment");
            operationlog.setUsername(name);
            operationlog.setEquipmentname(equipmentnames);
            operationlog.setPlatform(type);
            operationlogService.addOperationLogInfo(operationlog, operationlogdetails);
        }

    }

    @Override
    public void updateInstrumentMonitor(String instrumentname, String equipmentname, String hospitalname, String name, InstrumentInfoModel oldInstrumentInfoModel, InstrumentInfoModel nowInstrumentInfoModel, String type, String operationType) {
        List<Operationlogdetail> operationlogdetails = new ArrayList<>();
        boolean flag = false;
        String sn = oldInstrumentInfoModel.getSn();
        String sn1 = nowInstrumentInfoModel.getSn();

        if (!StringUtils.equals(sn1, sn)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("sn");
            operationlogdetail.setFiledcaption("SN");
            operationlogdetail.setFiledvalue(sn1);//当前值
            operationlogdetail.setFiledvalueprev(sn);//历史值
            operationlogdetail.setComment(instrumentname);
            operationlogdetails.add(operationlogdetail);
        }

        BigDecimal lowlimit = oldInstrumentInfoModel.getLowlimit();//最低限值

        BigDecimal lowlimit1 = nowInstrumentInfoModel.getLowlimit();
        if (lowlimit != null && lowlimit1 != null) {
            if (lowlimit.compareTo(lowlimit1) != 0) {
                //值发生了改变
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("lowlimit");
                operationlogdetail.setFiledcaption("最低值");
                operationlogdetail.setFiledvalue(lowlimit1.toString());//当前值
                operationlogdetail.setFiledvalueprev(lowlimit.toString());//历史值
                operationlogdetails.add(operationlogdetail);
            }
        } else {
            //有值存在空
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("lowlimit");
            operationlogdetail.setFiledcaption("最低值");
            if (null != lowlimit1) {
                operationlogdetail.setFiledvalue(lowlimit1.toString());//当前值
            }
            if (null != lowlimit) {
                operationlogdetail.setFiledvalueprev(lowlimit.toString());//历史值
            }
            operationlogdetails.add(operationlogdetail);
        }


        BigDecimal highlimit = oldInstrumentInfoModel.getHighlimit();//最高限值
        BigDecimal highlimit1 = nowInstrumentInfoModel.getHighlimit();//最高限值（当前）
        if (highlimit != null && highlimit1 != null) {
            if (highlimit.compareTo(highlimit1) != 0) {
                //值发生了改变
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("highlimit");
                operationlogdetail.setFiledcaption("最高值");
                operationlogdetail.setFiledvalue(highlimit1.toString());//当前值
                operationlogdetail.setFiledvalueprev(highlimit.toString());//历史值
                operationlogdetail.setComment(instrumentname);
                operationlogdetails.add(operationlogdetail);
            }
        } else {
            //有值存在空
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("highlimit");
            operationlogdetail.setFiledcaption("最高值");
            if (null != highlimit1) {
                operationlogdetail.setFiledvalue(highlimit1.toString());//当前值
            }
            if (null != highlimit) {
                operationlogdetail.setFiledvalueprev(highlimit.toString());//历史值
            }
            operationlogdetails.add(operationlogdetail);
        }


        String calibration = oldInstrumentInfoModel.getCalibration();//矫正正负值
        String calibration1 = nowInstrumentInfoModel.getCalibration();
        if (!StringUtils.equals(calibration1, calibration)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("calibration");
            operationlogdetail.setFiledcaption("校正正负值");
            operationlogdetail.setFiledvalue(calibration1);//当前值
            operationlogdetail.setFiledvalueprev(calibration);//历史值
            operationlogdetail.setComment(instrumentname);
            operationlogdetails.add(operationlogdetail);
        }
        String channel = oldInstrumentInfoModel.getChannel();//通道
        String channel1 = nowInstrumentInfoModel.getChannel();
        if (!(StringUtils.isEmpty(channel) && StringUtils.isEmpty(channel1))) {
            if (!StringUtils.equals(channel, channel1)) {
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("channel");
                operationlogdetail.setFiledcaption("通道");
                operationlogdetail.setFiledvalue(channel1);//当前值
                operationlogdetail.setFiledvalueprev(channel);//历史值
                operationlogdetail.setComment(instrumentname);
                operationlogdetails.add(operationlogdetail);
            }
        }
        Integer alarmtime = oldInstrumentInfoModel.getAlarmtime();//智能报警次数
        if (alarmtime == null) {
            alarmtime = 0;
        }
        Integer alarmtime1 = nowInstrumentInfoModel.getAlarmtime();
        if (alarmtime1 == null) {
            alarmtime1 = 0;
        }
        if (alarmtime != alarmtime1) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("alarmtime");
            operationlogdetail.setFiledcaption("智能报警次数");
            operationlogdetail.setFiledvalue(channel1);//当前值
            operationlogdetail.setFiledvalueprev(channel);//历史值
            operationlogdetail.setComment(instrumentname);
            operationlogdetails.add(operationlogdetail);
        }
        String warningphone = oldInstrumentInfoModel.getWarningphone();//禁用启用报警
        String warningphone1 = nowInstrumentInfoModel.getWarningphone();
        if (!StringUtils.equals(warningphone1, warningphone)) {
            flag = true;
            Operationlogdetail operationlogdetail = new Operationlogdetail();
            operationlogdetail.setFiledname("warningphone");
            operationlogdetail.setFiledcaption("报警禁用启用");
            operationlogdetail.setFiledvalue(warningphone1);//当前值
            operationlogdetail.setFiledvalueprev(warningphone);//历史值
            operationlogdetail.setComment(instrumentname);
            operationlogdetails.add(operationlogdetail);
        }
        if (flag) {
            Operationlog operationlog = new Operationlog();
            operationlog.setFunctionname("探头管理");
            operationlog.setHospitalname(hospitalname);
            operationlog.setEquipmentname(equipmentname);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("instrumentparamconfig");
            operationlog.setUsername(name);
            operationlog.setPlatform(type);
            operationlogService.addOperationLogInfo(operationlog, operationlogdetails);
        }


    }

    public static void main(String args[]) {
        BigDecimal b = null;
        BigDecimal a = new BigDecimal("1");
        //int i = b.compareTo(a);
        System.out.println(b.toString());
    }
}
