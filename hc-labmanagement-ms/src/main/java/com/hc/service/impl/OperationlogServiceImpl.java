package com.hc.service.impl;

import com.hc.command.labmanagement.hospital.HospitalCommand;
import com.hc.command.labmanagement.hospital.HospitalEquimentTypeCommand;
import com.hc.command.labmanagement.hospital.MonitorEquipmentLogCommand;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.operation.HospitalEquipmentOperationLogCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.command.labmanagement.operation.MonitorEquipmentLogInfoCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.labmanagent.HospitalInfoApi;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.OperationlogPo;
import com.hc.po.OperationlogdetailPo;
import com.hc.repository.OperationlogRepository;
import com.hc.repository.OperationlogdetailRepository;
import com.hc.service.HospitalequimentService;
import com.hc.service.MonitorEquipmentService;
import com.hc.service.OperationlogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    @Autowired
    private MonitorEquipmentService monitorEquipmentService;

    @Autowired
    private HospitalequimentService hospitalequimentService;

    @Override
    public void addHospitalOperationlog(HospitalOperationLogCommand hospitalOperationLogCommand) {

        String name = hospitalOperationLogCommand.getUsername();
        HospitalCommand nowHospitalInfo = hospitalOperationLogCommand.getNewHospitalCommand();
        String hospitalname = nowHospitalInfo.getHospitalName();
        String type = hospitalOperationLogCommand.getType();
        String operationType = hospitalOperationLogCommand.getOperationType();
        HospitalMadel oldHospitalInfo = null;

        if(StringUtils.equals(operationType, OperationLogEunmDerailEnum.EDIT.getCode())){
            String hospitalCode = nowHospitalInfo.getHospitalCode();
            oldHospitalInfo = hospitalInfoApi.findHospitalInfo(hospitalCode).getResult();
        }
        if(StringUtils.equals(operationType,OperationLogEunmDerailEnum.ADD.getCode())){
           oldHospitalInfo = new HospitalMadel();
        }
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
    public void addHospitalEquipmentOperationLogCommand(HospitalEquipmentOperationLogCommand hospitalEquipmentOperationLogCommand) {
        List<OperationlogdetailPo> operationlogdetails = new ArrayList<>();
        boolean flag = false;
        HospitalEquimentTypeCommand nowEquipmentTypeInfo = hospitalEquipmentOperationLogCommand.getHospitalEquimentTypeCommand();
        String operationType = hospitalEquipmentOperationLogCommand.getOperationType();
        HospitalEquimentTypeCommand oldEquipmentTypeInfo = null;
        if(StringUtils.equals(operationType,OperationLogEunmDerailEnum.ADD.getCode())){
            oldEquipmentTypeInfo = new HospitalEquimentTypeCommand();
        }
        if(StringUtils.equals(operationType,OperationLogEunmDerailEnum.EDIT.getCode())){
            String hospitalCode = nowEquipmentTypeInfo.getHospitalCode();
            String equipmentTypeId = nowEquipmentTypeInfo.getEquipmentTypeId();
            HospitalequimentDTO hospitalequimentDTO =  hospitalequimentService.selectHospitalEquimentInfoByCodeAndTypeId(hospitalCode,equipmentTypeId);
            System.out.println("======hospitalequimentDTO========");
            System.out.println(hospitalequimentDTO.toString());
            BeanConverter.convert(hospitalequimentDTO,HospitalEquimentTypeCommand.class);
        }
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
        MonitorEquipmentLogCommand nowEquipmentInfoModel = monitorEquipmentLogInfoCommand.getMonitorEquipmentLogCommand();
        MonitorEquipmentLogCommand oldEquipmentInfoModel = null;
        String operationType = monitorEquipmentLogInfoCommand.getOperationType();
        if(StringUtils.equals(operationType,OperationLogEunmDerailEnum.ADD.getCode())){
            oldEquipmentInfoModel = new MonitorEquipmentLogCommand();
        }
        if(StringUtils.equals(operationType,OperationLogEunmDerailEnum.EDIT.getCode())){
            String equipmentNo = monitorEquipmentLogInfoCommand.getEquipmentNo();
            MonitorEquipmentDto monitorEquipmentDto =  monitorEquipmentService.selectMonitorEquipmentInfoByNo(equipmentNo);
            oldEquipmentInfoModel =BeanConverter.convert(monitorEquipmentDto,MonitorEquipmentLogCommand.class);
        }
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
}