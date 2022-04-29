package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.InstrumentparamconfigApplication;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@RestController
@Api(tags = "探头信息")
@RequestMapping("/instrumentparamconfig")
public class InstrumentparamconfigController {

    @Autowired
    private InstrumentparamconfigApplication instrumentparamconfigApplication;


    @PostMapping("/addinstrumentparamconfig")
    @ApiOperation("新增探头信息")
    public  void  addInstrumentparamconfig(@RequestBody InstrumentparamconfigCommand instrumentparamconfigCommand ){
        instrumentparamconfigApplication.insertInstrumentParamConfig(instrumentparamconfigCommand);
    }


    @PostMapping("/updateinstrumentparamconfig")
    @ApiOperation("修改探头信息")
    public  void  updateInstrumentparamconfig(@RequestBody InstrumentparamconfigCommand instrumentparamconfigCommand ){
        instrumentparamconfigApplication.editInstrumentParamConfig(instrumentparamconfigCommand);
    }

    @DeleteMapping("/{instrumentparamconfigno}")
    @ApiOperation("删除探头信息")
    public void deleteInstrumentparamconfig(@PathVariable("instrumentparamconfigno")String instrumentparamconfigno){
        instrumentparamconfigApplication.removeInstrumentParamConfig(instrumentparamconfigno);
    }

    @PostMapping("/selectInstrumentparamconfig")
    @ApiOperation("查询探头信息")
    public Page<InstrumentparamconfigVo> selectInstrumentparamconfig(@RequestBody InstrumentparamconfigCommand instrumentparamconfigCommand){
        return instrumentparamconfigApplication.findInstrumentParamConfig(instrumentparamconfigCommand);
    }

    @GetMapping("/selectInstrumentparamconfigByEqNo")
    @ApiOperation("通过设备id获取设备对应监测类型")
    public List<InstrumentparamconfigVo>  selectInstrumentparamconfigByEqNo(@RequestParam(value ="equipmentNo")String equipmentNo){
        return instrumentparamconfigApplication.selectInstrumentParamConfigByEqNo(equipmentNo);
    }
}
