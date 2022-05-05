package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.po.Hospitalofreginfo;
import com.hc.service.HospitalService;
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
 * Created by 16956 on 2018-08-05.
 */
@Api(value = "医院信息", description = "医院信息Api")
@RestController
@RequestMapping(value = "/api/hospitalInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class HospitalInfoController {
    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/addHospitalInfo")
    @ApiOperation(value = "添加医院",response = Hospitalofreginfo.class)
    public ApiResponse<Hospitalofreginfo> addHospitalInfo(@RequestBody @Valid Hospitalofreginfo hospitalofreginfo ){
        return hospitalService.addHosptalInfo(hospitalofreginfo);
    }
    @PostMapping("/updateHospitalInfo")
    @ApiOperation(value = "修改医院信息",response = Hospitalofreginfo.class)
    public ApiResponse<Hospitalofreginfo> updateHospitalInfo(@RequestBody @Valid Hospitalofreginfo hospitalofreginfo ){
        return hospitalService.updateHospital(hospitalofreginfo);
    }
    @PostMapping("/deleteHospitalInfo")
    @ApiOperation(value = "删除医院信息",response = Hospitalofreginfo.class)
    public ApiResponse<String> deleteHospitalInfo(@RequestBody @Valid Hospitalofreginfo hospitalofreginfo ){
        return hospitalService.deleteHospital(hospitalofreginfo);
    }
    @GetMapping("/getHospitalInfoPage")
    @ApiOperation(value = "分页模糊查询医院信息",response = Hospitalofreginfo.class)
    public ApiResponse<Page<Hospitalofreginfo>> selectHospitalInfoPage(@ApiParam(name = "fuzzy", value = "模糊查询参数", required = false)
                                                                           @RequestParam(value = "fuzzy", required = false) String fuzzy,
                                                                       @ApiParam(name = "pagesize", value = "每页显示条目数", required = true)
                                                                           @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                                       @ApiParam(name = "pagenum", value = "当前页码数", required = true)
                                                                           @RequestParam(value = "pagenum", required = true) Integer pagenum){
        return hospitalService.getHospitalInfoPage(fuzzy,pagesize,pagenum);
    }
    @GetMapping("/getHospitalInfo")
    @ApiOperation(value = "查询所有医院信息",response = Hospitalofreginfo.class)
    public ApiResponse<List<Hospitalofreginfo>> selectHospitalInfo(){
        return hospitalService.getHospitalInfo();
    }
}
