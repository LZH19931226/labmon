package com.hc.controller;

import com.hc.util.AmazonConnectUtil;
import com.hc.util.SendEmailMessage;
import com.hc.util.SendMessage;
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

    @Autowired
	private SendMessage shortMessage;

    @Autowired
	private SendEmailMessage sendEmailMessage;

	@GetMapping("/phone")
	@ApiOperation("电话号码")
	public void testPhone(String phone,String message){
		amazonConnectUtil.makeCall(phone,message);
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





}
