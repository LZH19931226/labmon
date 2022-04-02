package com.hc.controller;

import com.hc.bean.MTOnlineBean;
import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.service.MTOnlineBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class SocketMsgController {
	
	@Autowired
	private MTOnlineBeanService  service;
	
	@GetMapping("/sendMsg")
	public ApiResponse<String> sendMsg(String MId, String cmd){
		return service.sendMsg(MId,cmd);
	}
	
	@GetMapping("/cs")
	private List<ParamaterModel> get(String data) {
      return service.paseData(data);
	}


	@GetMapping("/Channels")
	private  List<MTOnlineBean> getll(){
		return  service.getall();
	}


}
