package com.hc.controller;

import com.hc.util.AmazonConnectUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sms")
public class SmsController {


	@Autowired
	private AmazonConnectUtil amazonConnectUtil;

	@GetMapping("/phone")
	@ApiOperation("电话号码")
	public void testPhone(String phone,String message){
		amazonConnectUtil.makeCall(phone,message);
	}
}
