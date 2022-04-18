package com.hc.controller;

import com.hc.appliction.MonitorequipmenttypeApplication;
import com.hc.vo.equimenttype.MonitorequipmenttypeVo;
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
@Api(tags = "设备类型信息管理")
@RequestMapping("/monitorequipmenttype")
public class MonitorequipmenttypeController {

    @Autowired
    private MonitorequipmenttypeApplication monitorequipmenttypeApplication;


    @GetMapping("/getAllmonitorequipmentType")
    @ApiOperation("获取默认设备类型配置")
    public List<MonitorequipmenttypeVo> getAllmonitorequipmentType(){
        return monitorequipmenttypeApplication.getAllmonitorequipmentType();
    }


}
