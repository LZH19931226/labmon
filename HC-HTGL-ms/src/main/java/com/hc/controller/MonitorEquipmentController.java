package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorinstrument;
import com.hc.entity.Monitorinstrumenttype;
import com.hc.model.RequestModel.EquipmentInfoModel;
import com.hc.model.RequestModel.InstrumentPageModel;
import com.hc.model.ResponseModel.AllInstrumentInfoModel;
import com.hc.model.ResponseModel.MonitorEquipmentInfoModel;
import com.hc.service.MonitorEquipmentService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by 16956 on 2018-08-07.
 */
@Api(value = "设备信息", description = "设备信息Api")
@RestController
@RequestMapping(value = "/api/equipmentInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MonitorEquipmentController {
    @Autowired
    private MonitorEquipmentService monitorEquipmentService;



    @PostMapping("/addEquipment")
    @ApiOperation(value = "添加设备信息")
    public ApiResponse<String> addEquipmentType(@RequestBody @Valid EquipmentInfoModel equipmentInfoModel) {
        return monitorEquipmentService.addMonitorEquipment(equipmentInfoModel);
    }
    @PostMapping("/addWireEquipment")
    @ApiOperation(value = "添加有线设备信息")
    public ApiResponse<String> addWireEquipmentType(@RequestBody @Valid EquipmentInfoModel equipmentInfoModel) {
        return monitorEquipmentService.addWiredMonitorEquipment(equipmentInfoModel);
    }
    @PostMapping("/updateEquipment")
    @ApiOperation(value = "修改设备信息")
    public ApiResponse<String> updateEquipmentType(@RequestBody @Valid EquipmentInfoModel equipmentInfoModel) {
        return monitorEquipmentService.updateMonitorEquipment(equipmentInfoModel);

    }

    @PostMapping("/deleteEquipment")
    @ApiOperation(value = "删除设备信息")
    public ApiResponse<String> deleteEquipmentType(@RequestBody @Valid EquipmentInfoModel equipmentInfoModel) {
        return monitorEquipmentService.deleteMonitroEquipment(equipmentInfoModel);

    }

    @GetMapping("/selectEquipmentPage")
    @ApiOperation(value = "分页模糊设备信息")
    public ApiResponse<Page<MonitorEquipmentInfoModel>> selectEquipmentPage(@ApiParam(name = "fuzzy", value = "模糊查询参数", required = false)
                                                                            @RequestParam(value = "fuzzy", required = false) String fuzzy,
                                                                            @ApiParam(name = "pagesize", value = "每页显示条目数", required = true)
                                                                            @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                                            @ApiParam(name = "pagenum", value = "当前页码数", required = true)
                                                                            @RequestParam(value = "pagenum", required = true) Integer pagenum,
                                                                            @ApiParam(name = "hospitalcode", value = "医院编码", required = false)
                                                                            @RequestParam(value = "hospitalcode", required = false) String hospitalcode,
                                                                            @ApiParam(name = "equipmenttypeid", value = "设备类型编码", required = false)
                                                                            @RequestParam(value = "equipmenttypeid", required = false) String equipmenttypeid) {
        return monitorEquipmentService.selectEquipmentInfoPage(fuzzy, hospitalcode, equipmenttypeid, pagesize, pagenum);

    }


    @GetMapping("/selectAllEquipmentByCode")
    @ApiOperation(value = "展示当前医院设备类型所有设备信息")
    public ApiResponse<List<Monitorequipment>> selectAllEquipmentTypeByCode(@ApiParam(name = "hospitalcode", value = "医院编码", required = true)
                                                                            @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                                            @ApiParam(name = "equipmenttypeid", value = "设备类型编码", required = true)
                                                                            @RequestParam(value = "equipmenttypeid", required = true) String equipmenttypeid) {
        return monitorEquipmentService.selectEquipmentByCode(hospitalcode, equipmenttypeid);

    }

    @GetMapping("/showInstrumentType")
    @ApiOperation(value = "展示当前医院所有未绑定设备探头监控类型信息")
    public ApiResponse<List<Monitorinstrumenttype>> showInstrumentType(@ApiParam(name = "hospitalcode", value = "医院编码", required = true)
                                                                       @RequestParam(value = "hospitalcode", required = true) String hospitalcode) {
        return monitorEquipmentService.showInstrumentType(hospitalcode);
    }

    @GetMapping("/showInstrumentInfo")
    @ApiOperation(value = "展示当前医院未绑定设备探头sn号和探头编号信息")
    public ApiResponse<List<Monitorinstrument>> showInstrumentInfo(@ApiParam(name = "hospitalcode", value = "医院编码", required = true)
                                                                   @RequestParam(value = "hospitalcode", required = true) String hospitalcode,
                                                                   @ApiParam(name = "instrumenttypeid", value = "监控类型编码", required = true)
                                                                   @RequestParam(value = "instrumenttypeid", required = true) Integer instrumenttypeid,
                                                                   @ApiParam(name = "channel", value = "通道", required = true)
                                                                       @RequestParam(value = "channel", required = true) String channel){
        return monitorEquipmentService.showInstrumentInfo(hospitalcode,instrumenttypeid,channel);
    }


    @GetMapping("/searchEqByTwoYear")
    @ApiOperation(value = "查询设备")
    public void searchEqByTwoYear(HttpServletResponse response) {
         monitorEquipmentService.searchEqByTwoYear(response);

    }


}
