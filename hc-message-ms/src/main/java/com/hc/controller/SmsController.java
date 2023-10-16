package com.hc.controller;

import com.hc.util.AmazonConnectUtil;
import com.hc.util.SendEmailMessage;
import com.hc.util.SendMessage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sms")
@Slf4j
public class SmsController {


	@Autowired
	private AmazonConnectUtil amazonConnectUtil;

    @Autowired
	private SendMessage shortMessage;

    @Autowired
	private SendEmailMessage sendEmailMessage;

	@GetMapping("/phone")
	@ApiOperation("电话号码")
	public void testPhone(String phone,String message,String messageTitle){
		amazonConnectUtil.makeCall(phone,message,messageTitle);
	}

	@GetMapping("/mailbox")
	@ApiOperation("邮件")
	public void testMailbox(String email,String message){
		sendEmailMessage.emailMessage("",message,email);
	}


	@GetMapping("/shortMessage")
	@ApiOperation("短信")
	public void testShortMessage(String phone,String message){
		shortMessage.sendShortMessage(message,phone);
	}


	@GetMapping("/code")
	@ApiOperation("短信验证码获取接口")
	public void senMessagecode(@RequestParam("phonenum")String phontnum, @RequestParam("code")String code) {
		String message = "Your code is "+code+", It is only valid for 15 mins. If you did not make this request, please ignore this message";
		log.info(message);
		shortMessage.sendShortMessage(message, phontnum);
	}

	@GetMapping("/upsRemind")
	@ApiOperation("恢复市电提醒")
	public void upsRemind(@RequestParam("phonenum")String phontnum, @RequestParam("eqname")String eqname){
		String message = eqname+" The mains has been restored";
		log.info(message);
		shortMessage.sendShortMessage(message,phontnum);
	}



}
