package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentConfigCommand;
import com.hc.appliction.InstrumentConfigApplication;
import com.hc.dto.InstrumentConfigDTO;
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
@RequestMapping("/instrumentConfig")
public class InstrumentConfigController {

    @Autowired
    private InstrumentConfigApplication instrumentconfigApplication;

    @PostMapping("/add")
    @ApiOperation("添加监控参数类型")
    public void add(@RequestParam("instrumentConfigName") String instrumentConfigName){
        instrumentconfigApplication.save(instrumentConfigName);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询监控参数类型")
    public Page<InstrumentConfigDTO> list(@RequestBody InstrumentConfigCommand instrumentConfigCommand){
        return instrumentconfigApplication.list(instrumentConfigCommand);
    }

    @PutMapping("/update")
    public void edit(@RequestBody InstrumentConfigCommand instrumentConfigCommand){
        instrumentconfigApplication.edit(instrumentConfigCommand);
    }
}
