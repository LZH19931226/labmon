package com.hc.controller;


import com.hc.model.ResponseModel.AllInstrumentInfoModel;
import com.hc.service.InstrumentInfoService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "清理设备缓存", description = "清理设备缓存")
@RestController
@RequestMapping(value = "/api/clearEquipment", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ClearEquipmentController {

    @Autowired
    private InstrumentInfoService instrumentInfoService;

    /**
     * 清理设备缓存
     * @param instrumentInfoModel
     * @return
     */
    @PostMapping
    public ApiResponse<String> cleanEquipment(@RequestBody AllInstrumentInfoModel instrumentInfoModel) {
        return instrumentInfoService.cleanEquipment(instrumentInfoModel);
    }
}
