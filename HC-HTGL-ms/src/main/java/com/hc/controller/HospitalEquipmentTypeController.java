package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.po.Monitorequipmenttype;
import com.hc.model.RequestModel.EquipmentTypeInfoModel;
import com.hc.model.ResponseModel.HospitalEquipmentTypeInfoModel;
import com.hc.service.EquipmentTypeService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Api(value = "设备类型信息", description = "设备类型信息Api")
@RestController
@RequestMapping(value = "/api/equipmentTypeInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class HospitalEquipmentTypeController {

    @Autowired
    private EquipmentTypeService equipmentTypeService;

    @PostMapping("/addEquipmentType")
    @ApiOperation(value = "添加设备类型信息")
    public ApiResponse<String> addEquipmentType(@RequestBody @Valid EquipmentTypeInfoModel equipmentTypeInfoModel) {
        return equipmentTypeService.addEquipmentType(equipmentTypeInfoModel);
    }

    @PostMapping("/updateEquipmentType")
    @ApiOperation(value = "修改设备类型信息")
    public ApiResponse<String> updateEquipmentType(@RequestBody @Valid EquipmentTypeInfoModel equipmentTypeInfoModel) {
        return equipmentTypeService.updateEquipmentType(equipmentTypeInfoModel);

    }

    @PostMapping("/deleteEquipmentType")
    @ApiOperation(value = "删除设备类型信息")
    public ApiResponse<String> deleteEquipmentType(@RequestBody @Valid EquipmentTypeInfoModel equipmentTypeInfoModel) {
        return equipmentTypeService.deleteEquipmentType(equipmentTypeInfoModel);

    }

    @GetMapping("/selectEquipmentTypePage")
    @ApiOperation(value = "分页模糊设备类型信息")
    public ApiResponse<Page<HospitalEquipmentTypeInfoModel>> selectEquipmentPage(@ApiParam(name = "fuzzy", value = "模糊查询参数", required = false)
                                                                                 @RequestParam(value = "fuzzy", required = false) String fuzzy,
                                                                                 @ApiParam(name = "pagesize", value = "每页显示条目数", required = true)
                                                                                 @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                                                 @ApiParam(name = "pagenum", value = "当前页码数", required = true)
                                                                                 @RequestParam(value = "pagenum", required = true) Integer pagenum,
                                                                                 @ApiParam(name = "hospitalcode", value = "模糊查询参数", required = false)
                                                                                 @RequestParam(value = "hospitalcode", required = false) String hospitalcode) {
        return equipmentTypeService.showAllHospitalEquipmentTypePage(fuzzy, hospitalcode, pagesize, pagenum);

    }

    @GetMapping("/selectAllEquipmentType")
    @ApiOperation(value = "展示所有设备类型信息")
    public ApiResponse<List<Monitorequipmenttype>> selectAllEquipmentType() {
        return equipmentTypeService.selectEquipmentType();

    }

    @GetMapping("/selectAllEquipmentTypeByCode")
    @ApiOperation(value = "展示当前医院所有设备类型信息")
    public ApiResponse<List<Monitorequipmenttype>> selectAllEquipmentTypeByCode(@ApiParam(name = "hospitalcode", value = "模糊查询参数", required = true)
                                                                                @RequestParam(value = "hospitalcode", required = true) String hospitalcode) {
        return equipmentTypeService.selectHospitalEquipment(hospitalcode);

    }

}
