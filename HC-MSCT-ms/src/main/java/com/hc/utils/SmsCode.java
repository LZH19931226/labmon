package com.hc.utils;

public class SmsCode {


	
	public static String  SmsCodeParse(String code) {
		switch (code) {
		case "OK":
			 return "请求成功";
		case "isp.RAM_PERMISSION_DENY":
			 return "RAM权限DENY";
		case "isv.OUT_OF_SERVICE":
			 return "业务停机";
		case "isv.PRODUCT_UN_SUBSCRIPT":
			 return "未开通云通信产品的阿里云客户";
		case "isv.PRODUCT_UNSUBSCRIBE":
			 return "产品未开通";
		case "isv.ACCOUNT_NOT_EXISTS":
			 return "账户不存在";
		case "isv.ACCOUNT_ABNORMAL":
			 return "账户异常";
		case "isv.SMS_TEMPLATE_ILLEGAL":
			 return "短信模板不合法";
		case "isv.SMS_SIGNATURE_ILLEGAL":
			 return "短信签名不合法";
		case "isv.INVALID_PARAMETERS":
			 return "参数异常";
		case "isp.SYSTEM_ERROR":
			 return "系统错误";
		case "isv.MOBILE_NUMBER_ILLEGAL":
			 return "非法手机号";
		case "isv.MOBILE_COUNT_OVER_LIMIT":
			 return "手机号码数量超过限制";
		case "isv.TEMPLATE_MISSING_PARAMETERS":
			 return "模板缺少变量";
		case "isv.BUSINESS_LIMIT_CONTROL":
			 return "业务限流";
		case "isv.INVALID_JSON_PARAM":
			 return "JSON参数不合法，只接受字符串值";
		case "isv.BLACK_KEY_CONTROL_LIMIT":
			 return "黑名单管控";
		case "isv.PARAM_LENGTH_LIMIT":
			 return "参数超出长度限制";
		case "isv.PARAM_NOT_SUPPORT_URL":
			 return "不支持URL";
		case "isv.AMOUNT_NOT_ENOUGH":
			 return "账户余额不足成功";
		}
		
		return "未知错误码";
		
	}
	
	
	
}
