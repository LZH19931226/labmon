package com.hc.controller;

import com.hc.util.AmazonConnectUtil;
import com.hc.util.MoblieMessageUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sms")
public class SmsController {

	@Autowired
	private MoblieMessageUtil moblieMessageUtil;

	@Autowired
	private AmazonConnectUtil amazonConnectUtil;

	@GetMapping("/code")
	@ApiOperation("短信验证码获取接口")
	public void senMessagecode(@RequestParam("phonenum")String phontnum, @RequestParam("code")String code) {
		 moblieMessageUtil.senCode(phontnum, code);
	}

	@GetMapping("/upsRemind")
	@ApiOperation("恢复市电提醒")
	public void upsRemind(@RequestParam("phonenum")String phontnum, @RequestParam("eqname")String eqname){
		moblieMessageUtil.upsRemind(phontnum,eqname);
	}


	@GetMapping("/phone")
	@ApiOperation("电话号码")
	public void testPhone(String phone,String message){
		amazonConnectUtil.makeCall(phone,message);
	}
}
