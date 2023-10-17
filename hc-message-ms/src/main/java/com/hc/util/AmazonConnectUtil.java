package com.hc.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class AmazonConnectUtil {


    @Value("${amaon.contactflowid}")
    private String contactFlowId;
    @Value("${amaon.instanceid}")
    private String instanceId;
    @Value("${amaon.sourcephonenumber}")
    private String sourcePhoneNumber;
    @Value("${amaon.accesskeyid}")
    private String accessKeyId;
    @Value("${amaon.secretaccesskey}")
    private String secretAccessKey;
    @Value("${amaon.contactflowidft}")
    private String contactFlowIdFt;


    public void makeCall(String phoneNumber,String message,String messageTitle) {

        AwsCredentialsProvider awsCredentialsProvider  = () -> new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return accessKeyId;
            }

            @Override
            public String secretAccessKey() {
                return secretAccessKey;
            }
        };
        String flowId = null;
        log.info("contactflowidft:{}",contactFlowIdFt);
        if (!StringUtils.equals(messageTitle,"en")){
            flowId=contactFlowIdFt;
        }else {
            flowId=contactFlowId;
        }

        ConnectClient connectClient = ConnectClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(awsCredentialsProvider)
                .build();
        Map<String,String> messageMap =  new HashMap<>();
        messageMap.put("Message",message);
        log.info("拨打电话:{},手机:{}",message,phoneNumber);
        StartOutboundVoiceContactResponse response = connectClient.startOutboundVoiceContact(
                StartOutboundVoiceContactRequest.builder()
                        .destinationPhoneNumber(phoneNumber)
                        .contactFlowId(flowId)
                        .attributes(messageMap)
                        .instanceId(instanceId)
                        .sourcePhoneNumber("+"+sourcePhoneNumber)
                        .build()
        );
        log.info("拨打电话成功:{}",response);
    }

    public static void main(String[] agrs){

    }

}
