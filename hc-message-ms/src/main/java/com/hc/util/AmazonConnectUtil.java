package com.hc.util;

import lombok.extern.slf4j.Slf4j;
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

    public void makeCall(String phoneNumber,String message) {

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
        log.info("accessKeyId:{}",accessKeyId);
        log.info("secretAccessKey:{}",secretAccessKey);
        ConnectClient connectClient = ConnectClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(awsCredentialsProvider)
                .build();
        Map<String,String> messageMap =  new HashMap<>();
        messageMap.put("Message",message);
        StartOutboundVoiceContactResponse response = connectClient.startOutboundVoiceContact(
                StartOutboundVoiceContactRequest.builder()
                        .destinationPhoneNumber(phoneNumber)
                        .contactFlowId(contactFlowId)
                        .attributes(messageMap)
                        .instanceId(instanceId)
                        .sourcePhoneNumber(sourcePhoneNumber)
                        .build()
        );

        System.out.println(response);
    }

    public static void main(String[] agrs){
    }

}
