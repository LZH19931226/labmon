package com.hc.controller;

import com.hc.bean.MTOnlineBean;
import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.service.MTOnlineBeanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@Api(value="硬件互通接口")
public class SocketMsgController {
	
	@Autowired
	private MTOnlineBeanService  service;
	
	@GetMapping("/sendMsg")
	@ApiOperation("向指定的通道里面发送信息,MT600sn号,具体命令字符,发送指令类型1：声光报警开启 2：声光报警关闭")
	public ApiResponse<String> sendMsg(String MId, String cmd){
		return service.sendMsg(MId,cmd);
	}
	
	@GetMapping("/cs")
	@ApiOperation("测试指令")
	private List<ParamaterModel> get(String data) {
      return service.paseData(data);
	}


	@GetMapping("/Channels")
	@ApiOperation("获取所有通道id,对应的MT600型号")
	private  List<MTOnlineBean> getll(){
		return  service.getall();
	}


}
