package com.hc.controller;

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

	@GetMapping("/code")
	@ApiOperation("短信验证码获取接口")
	public void senMessagecode(@RequestParam("phonenum")String phontnum, @RequestParam("code")String code) {
		 moblieMessageUtil.senCode(phontnum, code);
	}
}
