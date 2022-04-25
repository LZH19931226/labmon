package com.hc.controller;

import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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



    @PostMapping("/addinstrumentparamconfig")
    @ApiOperation("新增探头信息")
    public  void  addInstrumentparamconfig(@RequestBody InstrumentparamconfigCommand instrumentparamconfigCommand ){

    }


    @PostMapping("/updateinstrumentparamconfig")
    @ApiOperation("修改探头信息")
    public  void  updateInstrumentparamconfig(@RequestBody InstrumentparamconfigCommand instrumentparamconfigCommand ){

    }

    @DeleteMapping("/{instrumentparamconfigno}")
    @ApiOperation("删除探头信息")
    public void deleteInstrumentparamconfig(@PathVariable("instrumentparamconfigno")String instrumentparamconfigno){

    }

    @PostMapping("/selectInstrumentparamconfig")
    @ApiOperation("查询探头信息")
    public List<InstrumentparamconfigVo> selectInstrumentparamconfig(@RequestBody InstrumentparamconfigCommand instrumentparamconfigCommand){
        return null;
    }
}
