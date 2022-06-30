package com.hc.Message;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.*;
import com.aliyuncs.dyvmsapi.model.v20170525.IvrCallRequest.MenuKeyMap;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hc.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SingleCallByTtsUtils {


	private static final Logger log = LoggerFactory.getLogger(SingleCallByTtsUtils.class);


	//短信API产品名称（短信产品名固定，无需修改）
	@Value("${CallBy.product}")
	private String product;
	//短信API产品域名（接口地址固定，无需修改）
	@Value("${CallBy.domain}")
	private String domain;

	@Value("${CallBy.accessKeyId}")
	private String accessKeyId;

	@Value("${CallBy.accessKeySecret}")
	private String accessKeySecret;


	//必填-被叫显号,可在语音控制台中找到所购买的显号
	@Value("${CallBy.CalledShowNumber}")
	private String CalledShowNumber;
	//
	@Value("${CallBy.TtsID}")
	private String TtsID;





	 /**
       * 文本转语音外呼
     * @return
     * @throws ClientException
     */
	public  SingleCallByTtsResponse sendSms(String mobile,String instrumentname)  {
		 //设置访问超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient 暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e) {
			e.printStackTrace();
		}
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber(CalledShowNumber);
        //必填-被叫号码
        request.setCalledNumber(mobile);
        //必填-Tts模板ID
        request.setTtsCode("TTS_184620316");
         //可选-当模板中存在变量时需要设置此值
        request.setTtsParam("{\"Instrument_Name\":\""+ instrumentname +  "\"}");
        request.setTtsParam("{\"phonenum\":\""+ mobile +  "\"}");
//        //可选-音量 取值范围 0--200
//        request.setVolume(100);
//        //可选-播放次数
//        request.setPlayTimes(3);
//        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
//        request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SingleCallByTtsResponse singleCallByTtsResponse = null;
		try {
			log.info("语音发送的请求"+JsonUtil.toJson(request));
            singleCallByTtsResponse = acsClient.getAcsResponse(request);
			log.info("语音返回的请求"+JsonUtil.toJson(singleCallByTtsResponse));
		} catch (ClientException e) {
            log.error(e+"");
		}
        return singleCallByTtsResponse;
	}


    /**
     * 文本转语音外呼
     * @return
     * @throws ClientException
     */
    public  SingleCallByTtsResponse sendSms2(String mobile,String hospitalName ,String eqTypeName)  {
        //设置访问超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient 暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber(CalledShowNumber);
        //必填-被叫号码
        request.setCalledNumber(mobile);
        //必填-Tts模板ID
        request.setTtsCode("TTS_244300025");
        //可选-当模板中存在变量时需要设置此值
        request.setTtsParam("{\"hospitalName\":\"" + hospitalName + "\", \"eqTypeName\":\"" + eqTypeName + "\"}");
//        //可选-音量 取值范围 0--200
//        request.setVolume(100);
//        //可选-播放次数
//        request.setPlayTimes(3);
//        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
//        request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SingleCallByTtsResponse singleCallByTtsResponse = null;
        try {
            log.info("语音发送的请求"+JsonUtil.toJson(request));
            singleCallByTtsResponse = acsClient.getAcsResponse(request);
            log.info("语音返回的请求"+JsonUtil.toJson(singleCallByTtsResponse));
        } catch (ClientException e) {
            log.error(e+"");
        }
        return singleCallByTtsResponse;
    }





    /**
     * 文本转语音外呼 -- 回拨
     * @return
     * @throws ClientException
     */
    public  SingleCallByTtsResponse sendSmsReceive(String mobile)  {
        //设置访问超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient 暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        //request.setCalledShowNumber(CalledShowNumber);
        //必填-被叫号码
        request.setCalledNumber(mobile);
        //必填-Tts模板ID
        request.setTtsCode("TTS_135036499");
        //可选-当模板中存在变量时需要设置此值
        request.setTtsParam("{\"phonenum\":\""+ mobile +  "\"}");
//        //可选-音量 取值范围 0--200
//        request.setVolume(100);
//        //可选-播放次数
//        request.setPlayTimes(3);
//        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
//        request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SingleCallByTtsResponse singleCallByTtsResponse = null;
        try {
            log.info("语音发送的请求"+JsonUtil.toJson(request));
            singleCallByTtsResponse = acsClient.getAcsResponse(request);
            log.info("语音返回的请求"+JsonUtil.toJson(singleCallByTtsResponse));
        } catch (ClientException e) {
            log.error(e+"");
        }
        return singleCallByTtsResponse;
    }

    /**
          * 语音文件外呼  Demo
      * @return
      * @throws ClientException
      */
    public  SingleCallByVoiceResponse singleCallByVoice() throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SingleCallByVoiceRequest request = new SingleCallByVoiceRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber("025000000");
        //必填-被叫号码
        request.setCalledNumber("15000000000");
        //必填-语音文件ID
        request.setVoiceCode("3a7c382b-ee87-493f-bfa0-b9fd6f31f8bb.wav");
        //可选-外部扩展字段
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SingleCallByVoiceResponse singleCallByVoiceResponse = acsClient.getAcsResponse(request);

        return singleCallByVoiceResponse;
    }

    /**
     * 交互式语音应答   Demo
     * @return
     * @throws ClientException
     */
    public  IvrCallResponse ivrCall() throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        IvrCallRequest request = new IvrCallRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber("057156210000");
        //必填-被叫号码
        request.setCalledNumber("15000000000");
        request.setPlayTimes(3L);

        //必填-语音文件ID或者tts模板的模板号,有参数的模板需要设置模板变量的值
        //request.setStartCode("ebe3a2b5-c287-42a4-8299-fc40ae79a89f.wav");
        request.setStartCode("TTS_713900000");
        request.setStartTtsParams("{\"product\":\"aliyun\",\"code\":\"123\"}");
        List<MenuKeyMap> menuKeyMaps = new ArrayList<MenuKeyMap>();
        MenuKeyMap menuKeyMap1 = new MenuKeyMap();
        menuKeyMap1.setKey("1");
        menuKeyMap1.setCode("9a9d7222-670f-40b0-a3af.wav");
        menuKeyMaps.add(menuKeyMap1);
        MenuKeyMap menuKeyMap2 = new MenuKeyMap();
        menuKeyMap2.setKey("2");
        menuKeyMap2.setCode("44e3e577-3d3a-418f-932c.wav");
        menuKeyMaps.add(menuKeyMap2);
        MenuKeyMap menuKeyMap3 = new MenuKeyMap();
        menuKeyMap3.setKey("3");
        menuKeyMap3.setCode("TTS_71390000");
        menuKeyMap3.setTtsParams("{\"product\":\"aliyun\",\"code\":\"123\"}");
        menuKeyMaps.add(menuKeyMap3);
        request.setMenuKeyMaps(menuKeyMaps);
        //结束语可以使一个无参模板或者一个语音文件ID
        request.setByeCode("TTS_71400007");

        //可选-外部扩展字段
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        IvrCallResponse ivrCallResponse = acsClient.getAcsResponse(request);

        return ivrCallResponse;
    }

}
