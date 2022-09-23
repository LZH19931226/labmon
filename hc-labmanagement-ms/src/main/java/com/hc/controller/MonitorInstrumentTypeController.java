package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.MonitorInstrumentTypeApplication;
import com.hc.application.command.MonitorInstrumentTypeCommand;
import com.hc.dto.MonitorinstrumenttypeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@RestController
@Api(tags = "")
@RequestMapping("/monitorInstrumentType")
public class MonitorInstrumentTypeController {

    @Autowired
    public MonitorInstrumentTypeApplication monitorinstrumenttypeApplication;

    @PostMapping("/add")
    @ApiOperation("添加设备")
    public void add(@RequestBody MonitorInstrumentTypeCommand monitorInstrumentTypeCommand){
        monitorinstrumenttypeApplication.add(monitorInstrumentTypeCommand);
    }

    @PostMapping("/list")
    @ApiOperation("分页获取设备")
    public Page<MonitorinstrumenttypeDTO> list(@RequestBody MonitorInstrumentTypeCommand monitorInstrumentTypeCommand){
        return monitorinstrumenttypeApplication.list(monitorInstrumentTypeCommand);
    }

    @PutMapping("/update")
    @ApiOperation("修改设备信息")
    public void edit(@RequestBody MonitorInstrumentTypeCommand monitorInstrumentTypeCommand){
        monitorinstrumenttypeApplication.edit(monitorInstrumentTypeCommand);
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除设备信息")
    public void remove(@RequestParam("instrumentTypeId")String instrumentTypeId){
        monitorinstrumenttypeApplication.remove(instrumentTypeId);
    }
}
