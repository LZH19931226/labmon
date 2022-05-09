package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.OperationLogCommand;
import com.hc.command.labmanagement.hospital.HospitalCommand;
import com.hc.command.labmanagement.hospital.HospitalEquimentTypeInfoCommand;
import com.hc.command.labmanagement.hospital.InstrumentparamconfigLogCommand;
import com.hc.command.labmanagement.hospital.MonitorEquipmentLogCommand;
import com.hc.command.labmanagement.operation.HospitalEquipmentOperationLogCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.command.labmanagement.operation.InstrumentParamConfigInfoCommand;
import com.hc.command.labmanagement.operation.MonitorEquipmentLogInfoCommand;
import com.hc.command.labmanagement.user.UserRightInfoCommand;
import com.hc.command.labmanagement.user.UserRightLogCommand;
import com.hc.dto.OperationlogDTO;
import com.hc.po.OperationlogPo;
import com.hc.po.OperationlogdetailPo;
import com.hc.repository.OperationlogRepository;
import com.hc.repository.OperationlogdetailRepository;
import com.hc.service.OperationlogService;
import com.hc.vo.backlog.OperationlogVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OperationlogServiceImpl implements OperationlogService {

    @Autowired
    private OperationlogRepository operationlogRepository;

    @Autowired
    private OperationlogdetailRepository operationlogdetailRepository;


    @Override
    public void addUserRightLog(UserRightInfoCommand userRightInfoCommand) {
        List<OperationlogdetailPo> operationlogdetails = new ArrayList<>();
        UserRightLogCommand nowUserRight = userRightInfoCommand.getNewUserRight();
        UserRightLogCommand oldUserRight = userRightInfoCommand.getOldUserRight();

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
        if (null == isuse) {
            isuse = 1L;
        }

        Long isuse1 = nowUserRight.getIsUse();
        if (null == isuse1) {
            isuse1 = 1L;
        }

        if (isuse == isuse1) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("isuse");
            operationlogdetail.setFiledcaption("用户是否可用");
            if (isuse1==1L) {
                operationlogdetail.setFiledvalue("1");//当前值
            } else {
                operationlogdetail.setFiledvalue("0");//当前值
            }
            if (isuse==1L) {
                operationlogdetail.setFiledvalueprev("1");//历史值
            } else {
                operationlogdetail.setFiledvalueprev("0");//当前值
            }
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

    @Override
    public void addHospitalOperationlog(HospitalOperationLogCommand hospitalOperationLogCommand) {

        String name = hospitalOperationLogCommand.getUsername();
        HospitalCommand nowHospitalInfo = hospitalOperationLogCommand.getNewHospitalCommand();
        String hospitalname = nowHospitalInfo.getHospitalName();
        String type = hospitalOperationLogCommand.getType();
        String operationType = hospitalOperationLogCommand.getOperationType();
        HospitalCommand oldHospitalInfo = hospitalOperationLogCommand.getOldHospitalCommand();

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
        if (timeouttime == null) {
            timeouttime = 0;
        }
        if (timeouttime1 == null) {
            timeouttime1 = 0;
        }
        if (timeouttime.equals(timeouttime1)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("timeouttime");
            operationlogdetail.setFiledcaption("超时报警时长");
            operationlogdetail.setFiledvalue(timeouttime1.toString());//当前值
            operationlogdetail.setFiledvalueprev(timeouttime.toString());//历史值
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
        if (null == clientvisible) {
            clientvisible = 2L;
        }
        Long clientvisible1 = nowEquipmentInfoModel.getClientVisible();//是否可用
        if (null == clientvisible1) {
            clientvisible1 = 2L;
        }
        if (!(clientvisible == clientvisible1)) {
            flag = true;
            String old = "可用";
            String now = "可用";
            if (clientvisible==2l) {
                old = "不可用";
            }
            if (clientvisible1==2L) {
                now = "不可用";
            }
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("clientvisible");
            operationlogdetail.setFiledcaption("是否可用");
            operationlogdetail.setFiledvalue(now);//当前值
            operationlogdetail.setFiledvalueprev(old);//历史值
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
//        if(StringUtils.equals(operationType,OperationLogEunmDerailEnum.ADD.getCode())){
//            oldInstrumentInfoModel = new InstrumentparamconfigLogCommand();
//        }
//        if(StringUtils.equals(operationType,OperationLogEunmDerailEnum.EDIT.getCode())){
//            String instrumentparamconfigno = instrumentparamconfigInfoCommand.getInstrumentparamconfigno();
//            InstrumentparamconfigDTO instrumentparamconfigDTO = instrumentparamconfigService.selectInstrumentparamconfigInfo(instrumentparamconfigno);
//            oldInstrumentInfoModel =  BeanConverter.convert(instrumentparamconfigDTO,InstrumentparamconfigLogCommand.class);
//            MonitorinstrumentDTO monitorinstrumentDTO = monitorinstrumentService.selectMonitorByIno(instrumentparamconfigInfoCommand.getInstrumentNo());
//            oldInstrumentInfoModel.setSn(monitorinstrumentDTO.getSn());
//        }
        boolean flag = false;
        String sn = oldInstrumentInfoModel.getSn();
        String sn1 = nowInstrumentInfoModel.getSn();
        String instrumentName = instrumentparamconfigInfoCommand.getInstrumentName();
        if (!StringUtils.equals(sn1, sn)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("sn");
            operationlogdetail.setFiledcaption("SN");
            operationlogdetail.setFiledvalue(sn1);//当前值
            operationlogdetail.setFiledvalueprev(sn);//历史值
            operationlogdetail.setComment(instrumentName);
            operationlogdetails.add(operationlogdetail);
        }

        BigDecimal lowlimit = oldInstrumentInfoModel.getLowlimit();//最低限值

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

        BigDecimal highlimit = oldInstrumentInfoModel.getHighlimit();//最高限值
        BigDecimal highlimit1 = nowInstrumentInfoModel.getHighlimit();//最高限值（当前）
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

        String calibration = oldInstrumentInfoModel.getCalibration();//矫正正负值
        String calibration1 = nowInstrumentInfoModel.getCalibration();
        if (!StringUtils.equals(calibration1, calibration)) {
            flag = true;
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("calibration");
            operationlogdetail.setFiledcaption("校正正负值");
            operationlogdetail.setFiledvalue(calibration1);//当前值
            operationlogdetail.setFiledvalueprev(calibration);//历史值
            operationlogdetail.setComment(instrumentName);
            operationlogdetails.add(operationlogdetail);
        }
        String channel = oldInstrumentInfoModel.getChannel();//通道
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
            OperationlogdetailPo operationlogdetail = new OperationlogdetailPo();
            operationlogdetail.setFiledname("alarmtime");
            operationlogdetail.setFiledcaption("智能报警次数");
            operationlogdetail.setFiledvalue(channel1);//当前值
            operationlogdetail.setFiledvalueprev(channel);//历史值
            operationlogdetail.setComment(instrumentName);
            operationlogdetails.add(operationlogdetail);
        }
        String warningphone = oldInstrumentInfoModel.getWarningphone();//禁用启用报警
        String warningphone1 = nowInstrumentInfoModel.getWarningphone();
        if (!StringUtils.equals(warningphone1, warningphone)) {
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