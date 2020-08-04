package com.hc.controller;

import com.hc.bean.ApiResponse;
import com.hc.dao.InstrumentparamconfigDao;
import com.hc.dao.MonitorequipmentDao;
import com.hc.dao.UserScheduLingDao;
import com.hc.entity.Monitorequipment;
import com.hc.entity.UserScheduLing;
import com.hc.entity.Userright;
import com.hc.my.common.core.bean.InstrumentMonitorInfoModel;
import com.hc.my.common.core.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.hc.Message.MoblieMessageUtil;
import com.hc.Message.SingleCallByTtsUtils;

import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class MyController {
	
	@Autowired
	private MoblieMessageUtil moblieMessageUtil;
	
	@Autowired
	private SingleCallByTtsUtils singleCallByTtsUtils;

	@Autowired
	private MonitorequipmentDao monitorequipmentDao;

	@Autowired
	private InstrumentparamconfigDao instrumentparamconfigDao;

	@Autowired
	private UserScheduLingDao userScheduLingDao;


	@GetMapping("/tests")
	public ApiResponse<List<Monitorequipment>> get(){
		List<Monitorequipment> monitorequipmentByHospitalcode = monitorequipmentDao.getMonitorequipmentByHospitalcode();
		ApiResponse<List<Monitorequipment>> apiResponse = new ApiResponse<>();
		apiResponse.setResult(monitorequipmentByHospitalcode);
		return apiResponse;
	}
	
	
	@GetMapping("/bj")
	@ApiOperation("测试报警短信接口")
	public SendSmsResponse test(String phontnum,String instrumentname,String unit,String value) {
		
		 SendSmsResponse sendmsg = moblieMessageUtil.sendmsg(phontnum, instrumentname, unit, value);
		 return sendmsg;
	}
	@GetMapping("/bjs")
	@ApiOperation("测试报警短信接口")
	public SendSmsResponse test1(String phontnum,String instrumentname,String unit,String value,int time) {

		SendSmsResponse sendmsg = moblieMessageUtil.sendmsg1(phontnum, instrumentname, unit, value,time);
		return sendmsg;
	}

	@GetMapping("/code")
	@ApiOperation("测试验证码短信接口")
	public SendSmsResponse testcode(@RequestParam("phonenum")String phontnum, @RequestParam("code")String code) {

		SendSmsResponse sendSmsResponse = moblieMessageUtil.senCode(phontnum, code);
		return sendSmsResponse;
	}



	@GetMapping("/test")
	@ApiOperation("测试语音接口")
	public SingleCallByTtsResponse test1(String mobile,String instrumentname) {
		SingleCallByTtsResponse sendSms = singleCallByTtsUtils.sendSms(mobile, instrumentname);
		return sendSms;
	}
	@GetMapping("/test2")
	@ApiOperation("测试语音接口")
	public String test2(String mobile,String instrumentname) {
		singleCallByTtsUtils.sendSms(mobile, instrumentname);
		return "电话拨打成功";
	}
	@GetMapping("/test3")
	@ApiOperation("测试语音接口")
	public String test3(String mobile,String instrumentname) {
		singleCallByTtsUtils.sendSms("13296607819", "测试打电话");
		singleCallByTtsUtils.sendSms("18108674918", "测试打电话");
		return "电话拨打成功";
	}

	@GetMapping("asdadsa")
	public List<UserScheduLing> test22(String v){
		Date date = new Date();
		String today = DateUtils.paseDate(date);
		List<UserScheduLing> userScByHosSt1 = userScheduLingDao.findUserScByHosSt("H0010", today,DateUtils.getYesterday(date));
		List<UserScheduLing> lings = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(userScByHosSt1)) {
			UserScheduLing userScheduLing = userScByHosSt1.get(userScByHosSt1.size() - 1);
			Date starttime = userScheduLing.getStarttime();
			Date endtime = userScheduLing.getEndtime();
			if (date.compareTo(endtime)>0){
				System.out.println("大于最大时间无");
			}else if (date.compareTo(starttime)>=0 && date.compareTo(endtime)<=0){
				lings = userScByHosSt1.stream().filter(s -> s.getStarttime().compareTo(starttime) == 0 && s.getEndtime().compareTo(endtime) == 0).collect(Collectors.toList());
			}else if (date.compareTo(starttime)<0){
				lings = userScByHosSt1.stream().filter(s -> s.getStarttime().compareTo(starttime) < 0).collect(Collectors.toList());
			}
		}
		return lings;
 	}

}
