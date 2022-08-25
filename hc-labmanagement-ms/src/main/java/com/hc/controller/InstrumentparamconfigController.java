package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.InstrumentparamconfigApplication;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.my.common.core.redis.dto.InstrumentmonitorDto;
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
    public void deleteInstrumentparamconfig(@PathVariable("instrumentparamconfigno")String[] instrumentparamconfignos){
        instrumentparamconfigApplication.removeInstrumentParamConfig(instrumentparamconfignos);
    }

    @PostMapping("/selectInstrumentparamconfig")
    @ApiOperation("查询探头信息")
    public Page<InstrumentparamconfigVo> selectInstrumentparamconfig(@RequestBody InstrumentparamconfigCommand instrumentparamconfigCommand){
        return instrumentparamconfigApplication.findInstrumentParamConfig(instrumentparamconfigCommand);
    }

    @GetMapping("/selectInstrumentparamconfigByEqNo")
    @ApiOperation("通过设备id获取设备对应探头监测类型")
    public List<InstrumentparamconfigVo>  selectInstrumentparamconfigByEqNo(@RequestParam(value ="equipmentNo")String equipmentNo){
        return instrumentparamconfigApplication.selectInstrumentParamConfigByEqNo(equipmentNo);
    }

    @GetMapping("/getEquipmentUnAddMonitorTypeByNo")
    @ApiOperation("获取设备未添加的监测类型")
    public List<InstrumentparamconfigVo> getEquipmentUnAddMonitorTypeByNo(@RequestParam(value ="equipmentNo")String equipmentNo){
       return  instrumentparamconfigApplication.getEquipmentUnAddMonitorTypeByNo(equipmentNo);
    }

    @GetMapping("/getInstrumentMonitorInfo")
    public List<InstrumentmonitorDto> selectInstrumentMonitorInfo(@RequestParam("hospitalCode") String hospitalCode){
        return instrumentparamconfigApplication.selectInstrumentMonitorInfo(hospitalCode);
    }

    /**
     * 更新最新一次的报警时间(用于每小时只报警一次)
     * @param instrumentParamConfigNo 探头检测信息id
     * @param warningTime 报警时间
     */
    @PutMapping("/editProbeWarningTime")
    public void editWarningTime(@RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo,@RequestParam("warningTime") String warningTime){
        instrumentparamconfigApplication.editWarningTime(instrumentParamConfigNo,warningTime);
    }
}
