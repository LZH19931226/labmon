package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.OperationLogCommand;
import com.hc.command.labmanagement.model.hospital.HospitalCommand;
import com.hc.command.labmanagement.model.hospital.HospitalEquimentTypeInfoCommand;
import com.hc.command.labmanagement.model.hospital.InstrumentparamconfigLogCommand;
import com.hc.command.labmanagement.model.hospital.MonitorEquipmentLogCommand;
import com.hc.command.labmanagement.operation.*;
import com.hc.command.labmanagement.user.UserRightInfoCommand;
import com.hc.command.labmanagement.user.UserRightLogCommand;
import com.hc.dto.OperationlogDTO;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.po.OperationlogPo;
import com.hc.po.OperationlogdetailPo;
import com.hc.repository.OperationlogRepository;
import com.hc.repository.OperationlogdetailRepository;
import com.hc.service.HospitalequimentService;
import com.hc.service.MonitorinstrumentService;
import com.hc.service.OperationlogService;
import com.hc.vo.backlog.OperationlogVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OperationlogServiceImpl implements OperationlogService {

    @Autowired
    private OperationlogRepository operationlogRepository;

    @Autowired
    private OperationlogdetailRepository operationlogdetailRepository;

    /**
     * 添加用户日志信息
     * @param userRightInfoCommand
     */
    @Override
    public void addUserRightLog(UserRightInfoCommand userRightInfoCommand) {
        List<OperationlogdetailPo> operationlogdetails = new ArrayList<>();
        UserRightLogCommand nowUserRight = userRightInfoCommand.getNewUserRight();
        UserRightLogCommand oldUserRight = userRightInfoCommand.getOldUserRight();
        boolean flag = false;
        //比对消息
        //用户名
        String username = oldUserRight.getUsername();
        String username1 = nowUserRight.getUsername();
        if (!StringUtils.equals(username, username1)) {
            flag = true;
            //用户名发生变更
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
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
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("pwd");
            operationlogdetail.setFiledcaption("密码");
            operationlogdetail.setFiledvalue(pwd1);//当前值
            operationlogdetail.setFiledvalueprev(pwd);//历史值
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);
        }
        //用户类型
        String usertype = oldUserRight.getUserType();
        String usertype1 = nowUserRight.getUserType();
        if (!StringUtils.equals(usertype1, usertype)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("usertype");
            operationlogdetail.setFiledcaption("用户类型");
            operationlogdetail.setFiledvalue(usertype1);//当前值
            operationlogdetail.setFiledvalueprev(usertype);//历史值
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);
        }
        //是否可用
        Long isuse = oldUserRight.getIsUse();
        Long isuse1 = nowUserRight.getIsUse();

        if (!Objects.equals(isuse, isuse1)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("isuse");
            operationlogdetail.setFiledcaption("用户是否可用");
            operationlogdetail.setFiledvalue(isuse1!=null?(isuse1==1L?"1":"0"):"0");
            operationlogdetail.setFiledvalueprev(isuse!=null?(isuse==1L?"1":"0"):"0");
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);
        }

        //手机号码
        String phonenum = oldUserRight.getPhoneNum();
        String phonenum1 = nowUserRight.getPhoneNum();
        if (!StringUtils.equals(phonenum, phonenum1)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("phonenum");
            operationlogdetail.setFiledcaption("手机号码");
            operationlogdetail.setFiledvalue(phonenum1);//当前值
            operationlogdetail.setFiledvalueprev(phonenum);//历史值
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);
        }

        //用户昵称
        String nickname = oldUserRight.getNickname();
        String nickname1 = nowUserRight.getNickname();
        if(!StringUtils.equals(nickname,nickname1)){
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("nickname");
            operationlogdetail.setFiledcaption("用户昵称");
            operationlogdetail.setFiledvalue(nickname1);//当前值
            operationlogdetail.setFiledvalueprev(nickname);//历史值
            operationlogdetail.setComment(username);
            operationlogdetails.add(operationlogdetail);
        }

        if (flag) {
            OperationlogPo operationlog = new OperationlogPo();
            operationlog.setFunctionname("人员管理");
            //根据用户名获取医院账户
            //      String hospitalName = operationlogdetaiMapper.getHospitalName(userid);
            //查询医院名称
            String hospitalName = userRightInfoCommand.getHospitalName();
            String operationType = userRightInfoCommand.getOperationType();
            String username2 = userRightInfoCommand.getUsername();
            String type = userRightInfoCommand.getType();
            operationlog.setHospitalname(hospitalName);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("userright");
            operationlog.setUsername(username2);
            operationlog.setPlatform(type);
            addOperationLogInfo(operationlog, operationlogdetails);
        }
    }

    /**
     * 添加医院日志信息
     * @param hospitalOperationLogCommand
     */
    @Override
    public void addHospitalOperationlog(HospitalOperationLogCommand hospitalOperationLogCommand) {

        String name = hospitalOperationLogCommand.getUsername();
        HospitalCommand nowHospitalInfo = hospitalOperationLogCommand.getNewHospitalCommand();
        HospitalCommand oldHospitalInfo = hospitalOperationLogCommand.getOldHospitalCommand();
        String operationType = hospitalOperationLogCommand.getOperationType();
        String hospitalname = "";
        if(StringUtils.equals(operationType, OperationLogEunmDerailEnum.REMOVE.getCode())){
            hospitalname = oldHospitalInfo.getHospitalName();
        }else{
            hospitalname = nowHospitalInfo.getHospitalName();
        }
        String type = hospitalOperationLogCommand.getType();



        List<OperationlogdetailPo> operationlogdetails = new ArrayList<>();
        boolean flag = false;

        String hospitalname1 = oldHospitalInfo.getHospitalName();//原名称
        String hospitalname2 = nowHospitalInfo.getHospitalName();//当前名称
        if (!StringUtils.equals(hospitalname1, hospitalname2)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("hospitalname");
            operationlogdetail.setFiledcaption("医院简称");
            operationlogdetail.setFiledvalue(hospitalname2);//当前值
            operationlogdetail.setFiledvalueprev(hospitalname1);//历史值
            operationlogdetails.add(operationlogdetail);
        }

        String hospitalfullname1 = oldHospitalInfo.getHospitalFullName();
        String hospitalfullname2 = nowHospitalInfo.getHospitalFullName();
        if (!StringUtils.equals(hospitalfullname1, hospitalfullname2)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("hospitalfullname");
            operationlogdetail.setFiledcaption("医院全称");
            operationlogdetail.setFiledvalue(hospitalfullname2);//当前值
            operationlogdetail.setFiledvalueprev(hospitalfullname1);//历史值
            operationlogdetails.add(operationlogdetail);
        }

        String isenabl1 = oldHospitalInfo.getIsEnable();//是否可用
        String isenabl2 = nowHospitalInfo.getIsEnable();//当前是否可用
        if (!StringUtils.equals(isenabl1, isenabl2)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("isenable");
            operationlogdetail.setFiledcaption("是否可用");
            operationlogdetail.setFiledvalue(isenabl2);//当前值
            operationlogdetail.setFiledvalueprev(isenabl1);//历史值
            operationlogdetails.add(operationlogdetail);
        }

        String alwayalarm1 = oldHospitalInfo.getAlwaysAlarm();//是否全天报警
        String alwayalarm2 = nowHospitalInfo.getAlwaysAlarm();
        if (!StringUtils.equals(alwayalarm1, alwayalarm2)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("alwayalarm");
            operationlogdetail.setFiledcaption("是否全天报警");
            operationlogdetail.setFiledvalue(alwayalarm2);//当前值
            operationlogdetail.setFiledvalueprev(alwayalarm1);//历史值
            operationlogdetails.add(operationlogdetail);
        }

        if (flag) {
            OperationlogPo operationlog = new OperationlogPo();
            operationlog.setFunctionname("医院管理");
            //根据用户名获取医院账户
            //查询医院名称
            operationlog.setHospitalname(hospitalname);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("hospitalofreginfo");
            operationlog.setUsername(name);
            operationlog.setPlatform(type);
            addOperationLogInfo(operationlog, operationlogdetails);
        }
    }

    /**
     * 添加医院设备类型日志信息
     * @param hospitalEquipmentOperationLogCommand
     */
    @Override
    public void addHospitalEquipmentOperationLogCommand(HospitalEquipmentOperationLogCommand hospitalEquipmentOperationLogCommand) {
        List<OperationlogdetailPo> operationlogdetails = new ArrayList<>();
        boolean flag = false;
        HospitalEquimentTypeInfoCommand nowEquipmentTypeInfo = hospitalEquipmentOperationLogCommand.getNewHospitalEquimentTypeCommand();
        HospitalEquimentTypeInfoCommand oldEquipmentTypeInfo = hospitalEquipmentOperationLogCommand.getOldHospitalEquimentTypeCommand();
        String operationType = hospitalEquipmentOperationLogCommand.getOperationType();

        String equipmenttypeid = oldEquipmentTypeInfo.getEquipmentTypeId();//设备类型原始值
        String equipmenttypeid1 = nowEquipmentTypeInfo.getEquipmentTypeId();//设备类型当前值

        if (!StringUtils.equals(equipmenttypeid1, equipmenttypeid)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("equipmenttypeid");
            operationlogdetail.setFiledcaption("设备类型");
            operationlogdetail.setFiledvalue(equipmenttypeid1);//当前值
            operationlogdetail.setFiledvalueprev(equipmenttypeid);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        String isvisible = oldEquipmentTypeInfo.getIsVisible();//是否可用
        String isvisible1 = nowEquipmentTypeInfo.getIsVisible();//是否可用当前值
        if (!StringUtils.equals(isvisible, isvisible1)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
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
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("timeout");
            operationlogdetail.setFiledcaption("是否设置超时报警");
            operationlogdetail.setFiledvalue(timeout1);//当前值
            operationlogdetail.setFiledvalueprev(timeout);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        Integer timeouttime = oldEquipmentTypeInfo.getTimeoutTime();//超时报警时间间隔
        Integer timeouttime1 = nowEquipmentTypeInfo.getTimeoutTime();//超时报警时间间隔
        if (!Objects.equals(timeouttime, timeouttime1)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("timeouttime");
            operationlogdetail.setFiledcaption("超时报警时长");
            operationlogdetail.setFiledvalue(timeouttime1==null?null:timeouttime1.toString());//当前值
            operationlogdetail.setFiledvalueprev(timeouttime==null?null:timeouttime.toString());//历史值
            operationlogdetails.add(operationlogdetail);
        }
        if (flag) {
            OperationlogPo operationlog = new OperationlogPo();
            operationlog.setFunctionname("设备类型管理");
            String hospitalName = hospitalEquipmentOperationLogCommand.getHospitalName();
            String name = hospitalEquipmentOperationLogCommand.getUsername();
            String type = hospitalEquipmentOperationLogCommand.getType();
            //根据用户名获取医院账户
            //      String hospitalName = operationlogdetaiMapper.getHospitalName(userid);
            //查询医院名称
            operationlog.setHospitalname(hospitalName);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("hospitalequiment");
            operationlog.setUsername(name);
            operationlog.setPlatform(type);
            addOperationLogInfo(operationlog, operationlogdetails);
        }
    }

    /**
     * 添加监控设备日志信息
     * @param monitorEquipmentLogInfoCommand
     */
    @Override
    public void addMonitorEquipmentLogInfo(MonitorEquipmentLogInfoCommand monitorEquipmentLogInfoCommand) {
        List<OperationlogdetailPo> operationlogdetails = new ArrayList<>();

        MonitorEquipmentLogCommand nowEquipmentInfoModel = monitorEquipmentLogInfoCommand.getNewMonitorEquipmentLogCommand();
        MonitorEquipmentLogCommand oldEquipmentInfoModel = monitorEquipmentLogInfoCommand.getOldMonitorEquipmentLogCommand();

        String operationType = monitorEquipmentLogInfoCommand.getOperationType();


        boolean flag = false;
        String equipmentname = oldEquipmentInfoModel.getEquipmentName();//设备名称
        String equipmentname1 = nowEquipmentInfoModel.getEquipmentName();//当前设备名称
        if (!StringUtils.equals(equipmentname1, equipmentname)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("equipmentname");
            operationlogdetail.setFiledcaption("设备名称");
            operationlogdetail.setFiledvalue(equipmentname1);//当前值
            operationlogdetail.setFiledvalueprev(equipmentname);//历史值
            operationlogdetails.add(operationlogdetail);
        }
        Long clientvisible = oldEquipmentInfoModel.getClientVisible();//是否可用
        Long clientvisible1 = nowEquipmentInfoModel.getClientVisible();//是否可用

        if (!(Objects.equals(clientvisible, clientvisible1))) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("clientvisible");
            operationlogdetail.setFiledcaption("是否可用");
            operationlogdetail.setFiledvalue(clientvisible1==null?"":clientvisible1.toString());//当前值
            operationlogdetail.setFiledvalueprev(clientvisible==null?"":clientvisible.toString());//历史值
            operationlogdetails.add(operationlogdetail);
        }

        String oldAddress = oldEquipmentInfoModel.getAddress();//地址信息
        String newAddress = nowEquipmentInfoModel.getAddress();//地址信息
        if(StringUtils.isNotBlank(oldAddress) && StringUtils.isNotBlank(newAddress) && !StringUtils.equals(oldAddress,newAddress)){
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("address");
            operationlogdetail.setFiledcaption("地址信息");
            operationlogdetail.setFiledvalue(StringUtils.isBlank(newAddress) ? "" : newAddress);//当前值
            operationlogdetail.setFiledvalueprev(StringUtils.isBlank(oldAddress) ? "" : oldAddress);//历史值
            operationlogdetails.add(operationlogdetail);

        }

        String oldRemark = oldEquipmentInfoModel.getRemark();
        String nowRemark = nowEquipmentInfoModel.getRemark();

        if(StringUtils.isNotBlank(oldRemark) && StringUtils.isNotBlank(nowRemark) && !StringUtils.equals(oldRemark,nowRemark)){
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("remark");
            operationlogdetail.setFiledcaption("备注");
            operationlogdetail.setFiledvalue(StringUtils.isBlank(nowRemark) ? "":nowRemark);//当前值
            operationlogdetail.setFiledvalueprev(StringUtils.isBlank(oldRemark) ? "":oldRemark);//历史值
            operationlogdetails.add(operationlogdetail);
        }

        if (flag) {
            OperationlogPo operationlog = new OperationlogPo();
            String hospitalName = monitorEquipmentLogInfoCommand.getHospitalName();
            String username = monitorEquipmentLogInfoCommand.getUsername();
            String type = monitorEquipmentLogInfoCommand.getType();
            operationlog.setFunctionname("设备管理");
            operationlog.setHospitalname(hospitalName);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("monitorequipment");
            operationlog.setUsername(username);
            operationlog.setEquipmentname(equipmentname1);
            operationlog.setPlatform(type);
            addOperationLogInfo(operationlog, operationlogdetails);
        }
    }

    /**
     * 添加探头日志信息
     * @param instrumentparamconfigInfoCommand
     */
    @Override
    public void addInstrumentparamconfig(InstrumentParamConfigInfoCommand instrumentparamconfigInfoCommand) {
        List<OperationlogdetailPo> operationlogdetails = new ArrayList<>();
        InstrumentparamconfigLogCommand nowInstrumentInfoModel = instrumentparamconfigInfoCommand.getNewInstrumentparamconfigLogCommand();
        InstrumentparamconfigLogCommand oldInstrumentInfoModel = instrumentparamconfigInfoCommand.getOldInstrumentparamconfigLogCommand();

        String operationType = instrumentparamconfigInfoCommand.getOperationType();

        boolean flag = false;
        String sn = oldInstrumentInfoModel.getSn();
        String sn1 = nowInstrumentInfoModel.getSn();
        String instrumentName = instrumentparamconfigInfoCommand.getInstrumentName();
        if (StringUtils.isBlank(sn) && StringUtils.isBlank(sn1) && !StringUtils.equals(sn1, sn)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("sn");
            operationlogdetail.setFiledcaption("SN");
            operationlogdetail.setFiledvalue(sn1);//当前值
            operationlogdetail.setFiledvalueprev(sn);//历史值
            operationlogdetail.setComment(instrumentName);
            operationlogdetails.add(operationlogdetail);
        }
        //最低限值
        BigDecimal lowlimit = oldInstrumentInfoModel.getLowlimit();
        BigDecimal lowlimit1 = nowInstrumentInfoModel.getLowlimit();
        if (lowlimit != null && lowlimit1 != null) {
            if (lowlimit.compareTo(lowlimit1) != 0) {
                //值发生了改变
                flag = true;
                OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
                operationlogdetail.setFiledname("lowlimit");
                operationlogdetail.setFiledcaption("最低值");
                operationlogdetail.setFiledvalue(lowlimit1.toString());//当前值
                operationlogdetail.setFiledvalueprev(lowlimit.toString());//历史值
                operationlogdetails.add(operationlogdetail);
            }
        } else {
            //有值存在空
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
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
        //最高限值
        BigDecimal highlimit = oldInstrumentInfoModel.getHighlimit();
        BigDecimal highlimit1 = nowInstrumentInfoModel.getHighlimit();
        if (highlimit != null && highlimit1 != null) {
            if (highlimit.compareTo(highlimit1) != 0) {
                //值发生了改变
                flag = true;
                OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
                operationlogdetail.setFiledname("highlimit");
                operationlogdetail.setFiledcaption("最高值");
                operationlogdetail.setFiledvalue(highlimit1.toString());//当前值
                operationlogdetail.setFiledvalueprev(highlimit.toString());//历史值
                operationlogdetail.setComment(instrumentName);
                operationlogdetails.add(operationlogdetail);
            }
        } else {
            //有值存在空
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
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
        //矫正正负值
        String calibration = oldInstrumentInfoModel.getCalibration();
        String calibration1 = nowInstrumentInfoModel.getCalibration();
        if (StringUtils.isNotBlank(calibration) && StringUtils.isNotBlank(calibration1) && !StringUtils.equals(calibration1, calibration)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("calibration");
            operationlogdetail.setFiledcaption("校正正负值");
            operationlogdetail.setFiledvalue(calibration1);//当前值
            operationlogdetail.setFiledvalueprev(calibration);//历史值
            operationlogdetail.setComment(instrumentName);
            operationlogdetails.add(operationlogdetail);
        }
        //通道
        String channel = oldInstrumentInfoModel.getChannel();
        String channel1 = nowInstrumentInfoModel.getChannel();
        if (!(StringUtils.isEmpty(channel) && StringUtils.isEmpty(channel1))) {
            if (!StringUtils.equals(channel, channel1)) {
                flag = true;
                OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
                operationlogdetail.setFiledname("channel");
                operationlogdetail.setFiledcaption("通道");
                operationlogdetail.setFiledvalue(channel1);//当前值
                operationlogdetail.setFiledvalueprev(channel);//历史值
                operationlogdetail.setComment(instrumentName);
                operationlogdetails.add(operationlogdetail);
            }
        }
        //智能报警次数
        Integer alarmtime = oldInstrumentInfoModel.getAlarmtime();
        Integer alarmtime1 = nowInstrumentInfoModel.getAlarmtime();
        if ( alarmtime != null && alarmtime1 != null && !Objects.equals(alarmtime, alarmtime1)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("alarmtime");
            operationlogdetail.setFiledcaption("智能报警次数");
            operationlogdetail.setFiledvalue(String.valueOf(alarmtime1));//当前值
            operationlogdetail.setFiledvalueprev(String.valueOf(alarmtime));//历史值
            operationlogdetail.setComment(instrumentName);
            operationlogdetails.add(operationlogdetail);
        }
        //禁用启用报警
        String warningphone = oldInstrumentInfoModel.getWarningphone();
        String warningphone1 = nowInstrumentInfoModel.getWarningphone();
        if (StringUtils.isNotBlank(warningphone) && StringUtils.isNotBlank(warningphone1) && !StringUtils.equals(warningphone1, warningphone)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("warningphone");
            operationlogdetail.setFiledcaption("报警禁用启用");
            operationlogdetail.setFiledvalue(warningphone1);//当前值
            operationlogdetail.setFiledvalueprev(warningphone);//历史值
            operationlogdetail.setComment(instrumentName);
            operationlogdetails.add(operationlogdetail);
        }

        if (flag) {
            String hospitalName = instrumentparamconfigInfoCommand.getHospitalName();
            String equipmentName = instrumentparamconfigInfoCommand.getEquipmentName();
            String username = instrumentparamconfigInfoCommand.getUsername();
            String type = instrumentparamconfigInfoCommand.getType();
            OperationlogPo operationlog = new OperationlogPo();
            operationlog.setFunctionname("探头管理");
            operationlog.setHospitalname(hospitalName);
            operationlog.setEquipmentname(equipmentName);
            operationlog.setOpeartiontype(operationType);
            operationlog.setOperationtime(new Date());
            operationlog.setTablename("instrumentparamconfig");
            operationlog.setUsername(username);
            operationlog.setPlatform(type);
            addOperationLogInfo(operationlog, operationlogdetails);
        }
    }

    /**
     * 保存日志和详细日志的方法
     * @param operationlog
     * @param operationlogdetailPos
     */
    public void addOperationLogInfo(OperationlogPo operationlog,List<OperationlogdetailPo> operationlogdetailPos){
        // 执行日志表插入操作
        operationlog.setLogid(UUID.randomUUID().toString().replaceAll("-", ""));
        operationlogRepository.save(operationlog);
        if (CollectionUtils.isNotEmpty(operationlogdetailPos)){
            //执行日志信息表插入操作
            for (OperationlogdetailPo operationlogdetail : operationlogdetailPos) {
                operationlogdetail.setDetailid(UUID.randomUUID().toString().replaceAll("-", ""));
                operationlogdetail.setLogid(operationlog.getLogid());
            }
            operationlogdetailRepository.saveBatch(operationlogdetailPos);
        }
    }

    @Override
    public void addExportLog(ExportLogCommand exportLogCommand) {
        OperationlogPo operationlogPo = new OperationlogPo();
        operationlogPo.setLogid(UUID.randomUUID().toString().replaceAll("-", ""));
        operationlogPo.setFunctionname(exportLogCommand.getFunctionName());
        operationlogPo.setOpeartiontype(exportLogCommand.getOperationType());
        operationlogPo.setUsername(exportLogCommand.getUsername());
        operationlogPo.setOperationtime(new Date());
        operationlogPo.setHospitalname(exportLogCommand.getHospitalName());
        operationlogRepository.save(operationlogPo);
    }

    /**
     * 分页获取日志信息
     *  @param page
     * @param operationLogCommand
     * @return
     */
    @Override
    public List<OperationlogDTO> findAllLogInfo(Page<OperationlogVo> page, OperationLogCommand operationLogCommand) {
        return operationlogRepository.findAllLogInfo(page,operationLogCommand);
    }
}
