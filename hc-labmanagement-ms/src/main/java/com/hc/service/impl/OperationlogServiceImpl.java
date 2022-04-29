package com.hc.service.impl;

import com.hc.command.labmanagement.hospital.HospitalCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.po.OperationlogPo;
import com.hc.po.OperationlogdetailPo;
import com.hc.repository.OperationlogRepository;
import com.hc.repository.OperationlogdetailRepository;
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



    @Override
    public void addHospitalOperationlog(HospitalOperationLogCommand hospitalOperationLogCommand) {
        String hospitalname = hospitalOperationLogCommand.getHospitalname();
        String name = hospitalOperationLogCommand.getUsernames();
        HospitalCommand oldHospitalInfo = hospitalOperationLogCommand.getOldHospitalCommand();
        HospitalCommand nowHospitalInfo = hospitalOperationLogCommand.getNewHospitalCommand();
        String type = hospitalOperationLogCommand.getType();
        String operationType = hospitalOperationLogCommand.getOperationType();


        List<OperationlogdetailPo> operationlogdetails = new ArrayList<>();
        boolean flag = false;
        String hospitalcode = null;
        String hospitalcode1 = oldHospitalInfo.getHospitalCode();
        String hospitalcode2 = nowHospitalInfo.getHospitalCode();
        if (StringUtils.isNotEmpty(hospitalcode1)) {
            hospitalcode = hospitalcode1;
        }
        if (StringUtils.isNotEmpty(hospitalcode2)) {
            hospitalcode = hospitalcode2;
        }
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
}