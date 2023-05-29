package com.hc.service.impl;

import com.hc.command.labmanagement.operation.ExportLogCommand;
import com.hc.dto.HospitalInfoDto;
import com.hc.dto.UserRightDto;
import com.hc.labmanagent.OperationlogApi;
import com.hc.service.ExportLogService;
import com.hc.service.HospitalInfoService;
import com.hc.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportLogServiceImpl implements ExportLogService {

    @Autowired
    private UserRightService userRightService;
    @Autowired
    private HospitalInfoService hospitalInfoService;
    @Autowired
    private OperationlogApi operationlogApi;

    @Override
    public void buildLogInfo(String userId, String fileName,String exportCode,String platform) {
        ExportLogCommand exportLogCommand = new ExportLogCommand();
        UserRightDto userRightDto =  userRightService.getUserRightInfoByUserId(userId);
        if(null != userRightDto){
            String hospitalCode = userRightDto.getHospitalCode();
            HospitalInfoDto hospitalInfoDto = hospitalInfoService.selectOne(hospitalCode);
            String username = userRightDto.getUsername();
            exportLogCommand.setHospitalCode(hospitalCode);
            exportLogCommand.setUsername(username);
            exportLogCommand.setHospitalName(hospitalInfoDto.getHospitalName());
        }
        exportLogCommand.setOperationType(exportCode);
        exportLogCommand.setMenuName(fileName);
        exportLogCommand.setFunctionName(fileName);
        exportLogCommand.setPlatform(platform);
        operationlogApi.addExportLog(exportLogCommand);
    }

}
