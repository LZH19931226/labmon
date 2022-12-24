package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.OperationLogCommand;
import com.hc.command.labmanagement.operation.ExportLogCommand;
import com.hc.command.labmanagement.operation.HospitalEquipmentOperationLogCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.command.labmanagement.user.UserRightInfoCommand;
import com.hc.dto.OperationlogDTO;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.struct.Context;
import com.hc.service.OperationlogService;
import com.hc.vo.backlog.OperationlogVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 系统操作日志表
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class OperationlogApplication {

    @Autowired
    private OperationlogService operationlogService;

    @Transactional(rollbackFor = Exception.class)
    public void addHospitalOperationlog(HospitalOperationLogCommand hospitalOperationLogCommand) {
        operationlogService.addHospitalOperationlog(hospitalOperationLogCommand);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addHospitalEquipmentOperationLogCommand(HospitalEquipmentOperationLogCommand hospitalEquipmentOperationLogCommand) {
        operationlogService.addHospitalEquipmentOperationLogCommand(hospitalEquipmentOperationLogCommand);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addUserRightLog(UserRightInfoCommand userRightInfoCommand) {
        operationlogService.addUserRightLog(userRightInfoCommand);
    }

    @Transactional(rollbackFor = Exception.class)
    public Page<OperationlogVo> findAllLogInfo(OperationLogCommand operationLogCommand) {
        Date begintime = operationLogCommand.getBegintime();
        Date endtime = operationLogCommand.getEndtime();
        if(ObjectUtils.isEmpty(begintime) || ObjectUtils.isEmpty(endtime)){
            throw new IedsException(LabSystemEnum.START_TIME_OR_END_TIME_CANNOT_BE_EMPTY);
        }
        if (endtime.before(begintime)) {
            throw new IedsException(LabSystemEnum.END_TIME_CANNOT_BE_EARLIER_THAN_START_TIME);
        }
        Long pageSize = operationLogCommand.getPageSize();
        Long pageCurrent = operationLogCommand.getPageCurrent();
        Page<OperationlogVo> page = new Page<>(pageCurrent,pageSize);
        List<OperationlogDTO> operationlogDTO =  operationlogService.findAllLogInfo(page,operationLogCommand);
        List<OperationlogVo> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(operationlogDTO)){
            operationlogDTO.forEach(res->{
                OperationlogVo build = OperationlogVo.builder()
                        .opeartiontype(editOperateType(res.getOpeartiontype()))
                        .functionname(editFunctionName(res.getFunctionname()))
                        .hospitalname(res.getHospitalname())
                        .equipmentname(res.getEquipmentname())
                        .username(res.getUsername())
                        .operationtime(res.getOperationtime())
                        .logid(res.getLogid())
                        .build();
                list.add(build);
            });
        }
        page.setRecords(list);
        return page;
    }

    public String editOperateType(String code){
        OperationLogEunmDerailEnum from = OperationLogEunmDerailEnum.from(code);
        return Context.IsCh() ? from.getMessage() : from.name();
    }

    public String editFunctionName(String message){
        if(!Context.IsCh()){
            OperationLogEunm operationLogEunm = OperationLogEunm.from(message);
            return operationLogEunm.name();
        }
        return message;
    }


    public void addExportLog(ExportLogCommand exportLogCommand) {
        operationlogService.addExportLog(exportLogCommand);
    }
}
