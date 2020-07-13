package com.hc.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hc.utils.JsonUtil;


@Component
public class MoblieMessageUtil {


    private static final Logger log = LoggerFactory.getLogger(MoblieMessageUtil.class);


    //短信API产品名称（短信产品名固定，无需修改）
    @Value("${sms.product}")
    private String product;
    //短信API产品域名（接口地址固定，无需修改）
    @Value("${sms.domain}")
    private String domain;

    @Value("${sms.accessKeyId}")
    private String accessKeyId;

    @Value("${sms.accessKeySecret}")
    private String accessKeySecret;
    // 短信签名
    @Value("${sms.signName}")
    private String signName;

    //    @Value("${timing.phone}")
//    private String phone;
    public SendSmsResponse timingsms(String total,String normaltotal,String abnormal,String phone,String type) {
        //通过手机号判断是否港澳台  "00"开头 港澳台
        boolean isLocalphone = !phone.startsWith("00");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e1) {
            e1.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_190794846");
        request.setTemplateParam("{\"total\":\"" + total + "\", \"normaltotal\":\"" + normaltotal + "\", \"abnormal\":\"" + abnormal+ "\", \"type\":\"" + type+"\" }");



        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = null;
        try {
            log.info("短信发送的请求" + JsonUtil.toJson(request));
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信返回的请求" + JsonUtil.toJson(sendSmsResponse));
        } catch (Exception e) {
            log.error(e + "");
        }
        return sendSmsResponse;
    }



    public SendSmsResponse sendmsg1(String phontnum, String instrumentname, String unit, String value, int time) {
        //通过手机号判断是否港澳台  "00"开头 港澳台
        boolean isLocalphone = !phontnum.startsWith("00");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e1) {
            e1.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phontnum);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到

        switch (unit) {

            case "超时":
                request.setTemplateCode("SMS_175240648");
                request.setTemplateParam("{\"hospitalname\":\"" + value + "\", \"equipmentname\":\"" + instrumentname + "\", \"ta\":\"" + time  + "\" }");
                break;
            case "禁用":
                request.setTemplateCode("SMS_175070824");
                request.setTemplateParam("{\"hospitalcode\":\"" + value + "\", \"equipmentname\":\"" + instrumentname +  "\" }");
                break;
            case "解除":
                request.setTemplateCode("SMS_175070830");
                request.setTemplateParam("{  \"equipmentname\":\"" + instrumentname +  "\" }");
                break;


        }


        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = null;
        try {
            log.info("短信发送的请求" + JsonUtil.toJson(request));
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信返回的请求" + JsonUtil.toJson(sendSmsResponse));
        } catch (Exception e) {
            log.error(e + "");
        }
        return sendSmsResponse;
    }

    public SendSmsResponse sendmsg(String phontnum, String instrumentname, String unit, String value) {
        //通过手机号判断是否港澳台  "00"开头 港澳台
        boolean isLocalphone = !phontnum.startsWith("00");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e1) {
            e1.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phontnum);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        if (unit == "UPS") {
            if (isLocalphone)
                request.setTemplateCode("SMS_141595921");
            else
                request.setTemplateCode("SMS_141605631");
        } else {
            switch (unit) {
                case "CO2": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141605875");
                    else
                        request.setTemplateCode("SMS_141595623");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + value + "\"}");
                    break;
                }
                case "RH": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141605874");
                    else
                        request.setTemplateCode("SMS_141615554");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + value + "\"}");
                    break;
                }
                case "TEMP": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + value + "\"}");
                    break;
                }
                case "TEMP1": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "一路温度" + value + "\"}");
                    break;
                }
                case "TEMP2": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "二路温度" + value + "\"}");
                    break;
                }
                case "TEMP3": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "三路温度" + value + "\"}");
                    break;
                }
                case "TEMP4": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "四路温度" + value + "\"}");
                    break;
                }
                case "TEMP5": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "五路温度" + value + "\"}");
                    break;
                }
                case "TEMP6": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "六路温度" + value + "\"}");
                    break;
                }
                case "TEMP7": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "七路温度" + value + "\"}");
                    break;
                }
                case "TEMP8": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "八路温度" + value + "\"}");
                    break;
                }
                case "TEMP9": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "九路温度" + value + "\"}");
                    break;
                }
                case "TEMP10": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "十路温度" + value + "\"}");
                    break;
                }
                case "LEFTTEMP": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "左仓室" + value + "\"}");
                    break;
                }
                case "RIGHTTEMP": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "右仓室" + value + "\"}");
                    break;
                }
                case "DIFFTEMP": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "温差" + value + "\"}");
                    break;
                }
                case "LEFTCOVERTEMP":{
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "左盖板温度" + value + "\"}");
                    break;
                }
                case "LEFTENDTEMP":{
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "左底板温度" + value + "\"}");
                    break;
                }
                case "左气流":{
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "左气流" + value + "\"}");
                    break;
                }
                case "RIGHTCOVERTEMP":{
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "右盖板温度" + value + "\"}");
                    break;
                }
                case "RIGHTENDTEMP":{
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "右底板温度" + value + "\"}");
                    break;
                }
                case "右气流":{
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141615792");
                    else
                        request.setTemplateCode("SMS_141580614");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"AbnormalValue\":\"" + "右气流" + value + "\"}");
                    break;
                }
                case "DOOR": {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_147437360");
                    else
                        request.setTemplateCode("SMS_147437371");
                    request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\"}");
                    break;
                }
                case "超时":
                    request.setTemplateCode("SMS_175120392");
                    request.setTemplateParam("{\"hospitalname\":\"" +  value+ "\", \"equipmentname\":\""  + instrumentname + "\"}");
                    break;
                case "禁用":
                    request.setTemplateCode("SMS_175070824");
                    request.setTemplateCode("{\"hospitalname\":\"" +  value+ "\", \"equipmentname\":\""  + instrumentname + "\"}");
                default: {
                    if (isLocalphone)
                        request.setTemplateCode("SMS_141595963");
                    else
                        request.setTemplateCode("SMS_141605914");
                    if (StringUtils.equals(unit,"QC")){
                        request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"Unit\":\"" + "电量" + "\", \"AbnormalValue\":\"" + value + "\"}");
                    }else if (StringUtils.equals(unit,"QCL")){
                        request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"Unit\":\"" + "锁电量" + "\", \"AbnormalValue\":\"" + value + "\"}");
                    }else {
                        request.setTemplateParam("{\"InstrumentName\":\"" + instrumentname + "\", \"Unit\":\"" + unit + "\", \"AbnormalValue\":\"" + value + "\"}");
                    }
                }
            }
        }

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = null;
        try {
            log.info("短信发送的请求" + JsonUtil.toJson(request));
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信返回的请求" + JsonUtil.toJson(sendSmsResponse));
        } catch (Exception e) {
            log.error(e + "");
        }
        return sendSmsResponse;
    }

    /*
     * 验证码发送接口
     */
    public SendSmsResponse senCode(String phone, String code) {

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e1) {
            e1.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //模板
        request.setTemplateCode("SMS_141640120");
        //模板json
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse acsResponse = null;
        try {
            log.info("短信发送的请求" + JsonUtil.toJson(request));
            acsResponse = acsClient.getAcsResponse(request);
            log.info("短信返回的请求" + JsonUtil.toJson(acsResponse));
        } catch (ClientException e) {
            log.error(e + "");
        }

        return acsResponse;
    }




    /*
     * 短信详情查询接口Demo
     */

    public QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("15000000000");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }


}