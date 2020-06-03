package com.hc.utils;

public class CallByTtsCode {

	public static String  SmsCallBy(String code) {
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
		case "isv.TTS_TEMPLATE_ILLEGAL":
			 return "TTS模板不合法";
		case "isv.DISPLAY_NUMBER_ILLEGAL":
			 return "号显不合法";
		case "isv.TEMPLATE_MISSING_PARAMETERS":
			 return "文本转语音模板参数缺失";
		case "isv.BLACK_KEY_CONTROL_LIMIT":
			 return "模板变量中存在黑名单关键字";
		case "isv.INVALID_PARAMETERS":
			 return "参数异常";
		case "isv.PARAM_NOT_SUPPORT_URL":
			 return "变量不支持url参数";
		case "isp.SYSTEM_ERROR":
			 return "系统错误";
		case "isv.MOBILE_NUMBER_ILLEGAL":
			 return "号码格式非法";
		case "isv.BUSINESS_LIMIT_CONTROL":
			 return "触发流控";
		case "isv.PARAM_LENGTH_LIMIT":
			 return "参数超出长度限制";
		}
		
		return "未知错误码";
		
	}
	
	
	
}
