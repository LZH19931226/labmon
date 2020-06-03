package com.hc.controller;

import javax.validation.Valid;

import com.hc.model.UpdateDeviceTokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.hc.entity.Instrumentparamconfig;
import com.hc.model.PushSetModel;
import com.hc.model.ResponseModel.InstrumentParamConfigInfos;
import com.hc.service.InstrumentParamSetService;
import com.hc.utils.ApiResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by 16956 on 2018-08-05.
 */
@Api(value = "探头报警设置", description = "探头报警设置API")
@RestController
@RequestMapping(value = "/api/insParamSet", produces = {MediaType.APPLICATION_JSON_VALUE})
public class InstruParamSetController {
    @Autowired
    private InstrumentParamSetService instrumentParamSetService;



    @PostMapping("/pushSet")
    @ApiOperation(value = "推送设置")
    public ApiResponse<String> updatePushTime(@RequestBody @Valid PushSetModel pushSetModel) {
        return instrumentParamSetService.updatePushTime(pushSetModel);
    }

    @PostMapping("/warnPhone")
    @ApiOperation(value = "报警设置")
    public ApiResponse<String> warnPhone(@RequestBody @Valid Instrumentparamconfig instrumentparamconfig) {
        return instrumentParamSetService.updateWarningPhone(instrumentparamconfig);
    }
    @GetMapping("/equipmentClientvisible")
    public ApiResponse<String> equipmentClientvisible(@ApiParam(name = "equipmentno", value = "设备类型编码", required = true)
                                                          @RequestParam(value = "equipmentno", required = true) String equipmentno,
                                                      @ApiParam(name = "clientvisible", value = "医院编号", required = true)
                                                          @RequestParam(value = "clientvisible", required = true) String clientvisible,
                                                      @ApiParam(name = "username", value = "医院编号", required = false)
                                                          @RequestParam(value = "username", required = false) String username){
        return instrumentParamSetService.updateEquipmentClientvisible(equipmentno, clientvisible,username);
    }
    @GetMapping("/showInsParamConfigSet")
    @ApiOperation(value = "显示当前设备类型所有探头可用禁用信息")
    public ApiResponse<Page<InstrumentParamConfigInfos>> showInfo(@ApiParam(name = "equipmenttypeid", value = "设备类型编码", required = true)
                                                                  @RequestParam(value = "equipmenttypeid", required = true) String equipmenttypeid,
                                                                  @ApiParam(name = "hospitalcode", value = "医院编号", required = true)
                                                                  @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                                  @ApiParam(name = "pagesize", value = "每页显示行数", required = true)
                                                                  @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                                  @ApiParam(name = "pagenum", value = "当前页数", required = true)
                                                                  @RequestParam(value = "pagenum", required = true) Integer pagenum) {
        return instrumentParamSetService.showInsStart(hospitalcode,equipmenttypeid,pagesize,pagenum);
    }

    @GetMapping("/sendMessage")
    public String sendMessage(@ApiParam(name = "pkid", value = "报警编号", required = true)
                                               @RequestParam(value = "pkid", required = true) String pkid){
        return instrumentParamSetService.warningMessageSend(pkid);
    }
    @GetMapping("/sendMessages")
    public ApiResponse<String> sendMessages(){
        return instrumentParamSetService.addMt100Qc();
    }

    @PostMapping("/updateDevicetoken")
    public ApiResponse<String> updateDeviceToken(@RequestBody @Valid UpdateDeviceTokenModel updateDeviceTokenModel){
        return instrumentParamSetService.updateDeviceToken(updateDeviceTokenModel);
    }

}
