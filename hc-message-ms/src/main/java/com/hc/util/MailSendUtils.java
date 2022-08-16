package com.hc.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailSendUtils {
	
	//短信API产品名称（短信产品名固定，无需修改）
	@Value("${mail.accessKey}")
	private String accessKey;
	
	@Value("${mail.accessSecret}")
	private String accessSecret;
	
	@Value("${mail.AccountName}")
	private String AccountName;
	
	@Value("${mail.FromAlias}")
	private String FromAlias;
	
	@Value("${mail.TagName}")
	private String TagName;
	
	public SingleSendMailResponse sendMail(String address,String title,String text) {
		
		// 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",accessKey, accessSecret);
        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        //try {
        //DefaultProfile.addEndpoint("dm.ap-southeast-1.aliyuncs.com", "ap-southeast-1", "Dm",  "dm.ap-southeast-1.aliyuncs.com");
        //} catch (ClientException e) {
        //e.printStackTrace();
        //}
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        SingleSendMailResponse httpResponse=null;
        try {
            //request.setVersion("2017-06-22");// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
               request.setAccountName(AccountName);
               request.setReplyToAddress(true);//使用管理控制台中配置的回信地址（状态必须是验证通过）。
               request.setAddressType(1); //取值范围 0~1: 0 为随机账号；1 为发信地址。
               request.setFromAlias(FromAlias);//发信人昵称,长度小于 15 个字符。 
               request.setTagName(TagName);
               request.setToAddress(address);//目标地址，多个 email 地址可以用逗号分隔，最多100个地址。
               request.setSubject(title);//邮件主题，建议填写。
               request.setHtmlBody(text);//邮件 html 正文。
               request.setTextBody(text);//邮件 text 正文。
               httpResponse = client.getAcsResponse(request);
        }catch (ServerException e) {
        	e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
        
        return httpResponse;
		
	}

}
