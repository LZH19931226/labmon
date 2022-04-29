package com.hc.labmanagent;

import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.my.common.core.bean.ApplicationName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ApplicationName.LABMANAGENMENT)
public interface OperationlogApi {

    @PostMapping("/operationlog/addHospitalOperationlog")
    void addHospitalOperationlog(@RequestBody HospitalOperationLogCommand hospitalOperationLogCommand);


}
