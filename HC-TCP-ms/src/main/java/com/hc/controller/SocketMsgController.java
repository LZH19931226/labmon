package com.hc.controller;

import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.service.MTOnlineBeanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/tcp")
public class SocketMsgController {

	@Autowired
	private MTOnlineBeanService mtOnlineBeanService;



	@GetMapping("/sendMsg")
	@ApiOperation("暂时只支持mt600/mt1100设备查询对应服务器  通道信息")
	public void sendMsg(@RequestParam("sn") String sn, @RequestParam("message") String message){
		mtOnlineBeanService.sendMsg(sn,message);
	}

	@GetMapping("/pasedata")
	public  List<ParamaterModel>  paseData(String cmd){
		return mtOnlineBeanService.paseData(cmd);

	}
}
