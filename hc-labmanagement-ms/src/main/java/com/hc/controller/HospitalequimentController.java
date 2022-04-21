package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.HospitalequimentApplication;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.vo.equimenttype.HospitalequimentVo;
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
@Api(tags = "医院设备类型管理")
@RequestMapping("/hospitalequimentType")
public class HospitalequimentController {

    @Autowired
    private HospitalequimentApplication hospitalequimentApplication;


    @PostMapping("/addHospitalEquimentType")
    @ApiOperation("新增医院设备类型")
    public void addHospitalEquimentType(@RequestBody HospitalEquimentTypeCommand hospitalEquimentTypeCommand){
        hospitalequimentApplication.addHospitalEquimentType(hospitalEquimentTypeCommand);
    }

    @PostMapping("/updateHospitalEquimentType")
    @ApiOperation("编辑医院设备类型")
    public void updateHospitalEquimentType(@RequestBody HospitalEquimentTypeCommand hospitalEquimentTypeCommand){
        hospitalequimentApplication.updateHospitalEquimentType(hospitalEquimentTypeCommand);
    }

    @PostMapping("/selectHospitalEquimentType")
    @ApiOperation("查询医院设备类型信息接口")
    public Page<HospitalequimentVo> selectHospitalEquimentType(@RequestBody HospitalEquimentTypeCommand hospitalEquimentTypeCommand){
        return hospitalequimentApplication.selectHospitalEquimentType(hospitalEquimentTypeCommand);
    }


    @GetMapping("/deleteHospitalEquimentType")
    @ApiOperation("删除医院设备类型信息接口")
    public void deleteHospitalEquimentType(@RequestParam(name = "hospitalCode") String hospitalCode,
                                           @RequestParam(name = "equipmenttypeid") String equipmenttypeid){
        hospitalequimentApplication.deleteHospitalEquimentType(hospitalCode,equipmenttypeid);
    }


}
