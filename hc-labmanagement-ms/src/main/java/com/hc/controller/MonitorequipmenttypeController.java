package com.hc.controller;

import com.hc.application.MonitorequipmenttypeApplication;
import com.hc.vo.equimenttype.MonitorEquipmentTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<MonitorEquipmentTypeVo> getAllmonitorequipmentType(){
        return monitorequipmenttypeApplication.getAllmonitorequipmentType();
    }


}
