package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.InstrumentMonitorApplication;
import com.hc.application.command.InstrumentMonitorCommand;
import com.hc.dto.InstrumentmonitorDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.omg.CORBA.PUBLIC_MEMBER;
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
@RequestMapping("/instrumentMonitor")
public class InstrumentMonitorController{

    @Autowired
    private InstrumentMonitorApplication instrumentMonitorApplication;

    @PostMapping("/add")
    @ApiOperation("添加高低值字典")
    public void add(@RequestBody InstrumentMonitorCommand instrumentMonitorCommand){
        instrumentMonitorApplication.add(instrumentMonitorCommand);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询高低值字典")
    public Page<InstrumentmonitorDTO> list(@RequestBody InstrumentMonitorCommand instrumentMonitorCommand){
        return instrumentMonitorApplication.list(instrumentMonitorCommand);
    }

    @PutMapping("/update")
    @ApiOperation("更新高低值字典")
    public void edit(@RequestBody InstrumentMonitorCommand instrumentMonitorCommand){
        instrumentMonitorApplication.edit(instrumentMonitorCommand);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除高低值字典")
    public void remove(@RequestParam("instrumentTypeId")Integer instrumentTypeId,@RequestParam("instrumentConfigId")Integer instrumentConfigId){
        instrumentMonitorApplication.remove(instrumentTypeId,instrumentConfigId);
    }
}
