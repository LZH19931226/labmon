package com.hc.my.common.core.message;

public class MailCode {
	

	public static String  MailCodePase(String code) {
		switch (code) {
		case "InvalidMailAddress.NotFound":
			 return "发信地址不存在";
		case "InvalidMailAddressStatus.Malformed":
			 return "发信地址状态不正确 。";
		case "InvalidToAddress":
			 return "目标地址不正确";
		case "InvalidBody":
			 return "邮件正文不正确。textBody 或 htmlBody 不能同时为空";
		case "InvalidSendMail.Spam":
			 return "本次发送操作被反垃圾系统检测为垃圾邮件，禁止发送。请仔细检查邮件内容和域名状态等";
		case "InvalidSubject.Malformed":
			 return "邮件主题限制在 100 个字符以内";
		case "InvalidMailAddressDomain.Malformed":
			 return "发信地址的域名状态不正确，请检查 MX、SPF 配置是否正确";
		case "InvalidFromALias.Malformed":
			 return "发信人昵称不正确，请检查发信人昵称是否正确，长度应小于 15 个字符";
		}
		return "未知错误码";
	}
}
