package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.entity.Monitorinstrumenttype;
import com.hc.model.RequestModel.InstrumentInfoModel;
import com.hc.model.RequestModel.InstrumentPageModel;
import com.hc.model.ResponseModel.AllInstrumentInfoModel;
import com.hc.service.InstrumentInfoService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by 16956 on 2018-08-07.
 */
@Api(value = "探头信息", description = "探头信息Api")
@RestController
@RequestMapping(value = "/api/instrumentInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class InstrumentInfoController {

    @Autowired
    private InstrumentInfoService instrumentInfoService;

    @GetMapping("/showInsType")
    @ApiOperation(value = "显示探头设备类型")
    public ApiResponse<List<Monitorinstrumenttype>> showInstrumentType(){
        return instrumentInfoService.showInstrumentType();
    }

    @PostMapping("/addInstrumentParam")
    @ApiOperation(value = "添加探头类型信息")
    public ApiResponse<String> addInstrumentParam(@RequestBody @Valid InstrumentInfoModel equipmentInfoModel) {
        return instrumentInfoService.addInstrumentParam(equipmentInfoModel);
    }
    @PostMapping("/addInstrument")
    @ApiOperation(value = "添加探头信息")
    public ApiResponse<String> addInstrument(@RequestBody @Valid InstrumentInfoModel equipmentInfoModel) {
        return instrumentInfoService.addInstrument(equipmentInfoModel);
    }

    @PostMapping("/updateInstrument")
    @ApiOperation(value = "修改探头信息")
    public ApiResponse<String> updateInstrument(@RequestBody @Valid InstrumentInfoModel equipmentInfoModel) {
        return instrumentInfoService.updateInstrument(equipmentInfoModel);

    }

    @PostMapping("/deleteInstrument")
    @ApiOperation(value = "删除探头信息")
    public ApiResponse<String> deleteInstrument(@RequestBody @Valid InstrumentInfoModel equipmentInfoModel) {
        return instrumentInfoService.deleteInstrument(equipmentInfoModel);

    }

    @PostMapping("/selectInstrumentPage")
    @ApiOperation(value = "分页模糊查询探头信息")
    public ApiResponse<Page<AllInstrumentInfoModel>> selectInstrumentPage(@RequestBody @Valid InstrumentPageModel instrumentPageModel) {
        return instrumentInfoService.selectInstrumentPage(instrumentPageModel);

    }


}
