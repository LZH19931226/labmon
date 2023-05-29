package com.hc.labmanagent;

import com.hc.command.labmanagement.model.HospitalEquipmentTypeModel;
import com.hc.command.labmanagement.operation.ExportLogCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.command.labmanagement.user.UserRightInfoCommand;
import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hc
 */
@FeignClient(value = ApplicationName.LABMANAGENMENT)
public interface OperationlogApi {

    /**
     * 添加医院日志
     * @param hospitalOperationLogCommand
     */
    @PostMapping("/operationlog/addHospitalOperationlog")
    void addHospitalOperationlog(@RequestBody HospitalOperationLogCommand hospitalOperationLogCommand);

    @PostMapping("/hospitalequimentType/addHospitalEquimentType")
    void addHospitalEquimentTypeLog(@RequestBody HospitalOperationLogCommand hospitalOperationLogCommand);

    @PostMapping("/operationlog/addUserRightLogInfo")
    void addUserRightLog(@RequestBody UserRightInfoCommand userRightInfoCommand);

    @GetMapping("/hospitalequimentType/findHospitalEuipmentTypeInfo")
    ApiResponse<List<HospitalEquipmentTypeModel>> findHospitalEquipmentTypeByCode(@RequestParam("hospitalCode")String hospitalCode);

    @PostMapping("/operationlog/addExportLog")
     void addExportLog(@RequestBody ExportLogCommand exportLogCommand);
}
