package com.hc.controller;

import com.hc.service.MTOnlineBeanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tcp")
public class SocketMsgController {

	@Autowired
	private MTOnlineBeanService mtOnlineBeanService;

	@GetMapping("/sendMsg")
	@ApiOperation("暂时只支持mt600/mt1100设备查询对应服务器通道信息")
	public void sendMsg(String sn, String cmd,String message){
		mtOnlineBeanService.sendMsg(sn,cmd,message);
	}




}
