package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.entity.Monitorinstrument;
import com.hc.entity.Repairinfo;
import com.hc.service.RepairinfoService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 15350 on 2020/5/27.
 */
@Api(value = "维修记录信息查询", description = "维修记录信息查询Api")
@RestController
@RequestMapping(value = "/api/repairinfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class RepairinfoController {
    @Autowired
    private RepairinfoService repairinfoService;


    @GetMapping("/getsn")
    public ApiResponse<List<Monitorinstrument>> getSn(@ApiParam(name = "equipmentno", value = "模糊查询参数", required = true)
                                                          @RequestParam(value = "equipmentno", required = true) String equipmentno){
        return repairinfoService.getSnByEquipmentNo(equipmentno);
    }

    @PostMapping("/addRepairinfo")
    public ApiResponse<Repairinfo> addRepairinfo(@RequestBody Repairinfo repairinfo) {
        return repairinfoService.addRepairinfo(repairinfo);
    }

    @PostMapping("/updateRepairinfo")
    public ApiResponse<String> updateRepairinfo(@RequestBody Repairinfo repairinfo) {
        return repairinfoService.updateRepairinfo(repairinfo);
    }

    @PostMapping("/deleteRepairinfo")
    public ApiResponse<String> deleteRepairinfo(@RequestBody Repairinfo repairinfo) {
        return repairinfoService.deleteRepairinfo(repairinfo);
    }

    @GetMapping("/selectPageInfo")
    public ApiResponse<Page<Repairinfo>> selectPageInfo(@ApiParam(name = "repairtype", value = "模糊查询参数", required = false)
                                                            @RequestParam(value = "repairtype", required = false) String repairtype,
                                                        @ApiParam(name = "pagesize", value = "每页显示条目数", required = true)
                                                            @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                        @ApiParam(name = "pagenum", value = "当前页码数", required = true)
                                                            @RequestParam(value = "pagenum", required = true) Integer pagenum,
                                                        @ApiParam(name = "equipmentname", value = "医院编码", required = false)
                                                            @RequestParam(value = "equipmentname", required = false) String equipmentname,
                                                        @ApiParam(name = "equipmenttype", value = "设备类型编码", required = false)
                                                            @RequestParam(value = "equipmenttype", required = false) String equipmenttype,
                                                        @ApiParam(name = "begindate", value = "设备类型编码", required = false)
                                                            @RequestParam(value = "begindate", required = false) String begindate,
                                                        @ApiParam(name = "enddate", value = "设备类型编码", required = false)
                                                            @RequestParam(value = "enddate", required = false) String enddate,
                                                        @ApiParam(name = "hospitalname", value = "设备类型编码", required = false)
                                                            @RequestParam(value = "hospitalname", required = false) String hospitalname){
        return repairinfoService.selectPageInfo(pagesize,pagenum,begindate,enddate,hospitalname,equipmenttype,equipmentname,repairtype);
    }


}
