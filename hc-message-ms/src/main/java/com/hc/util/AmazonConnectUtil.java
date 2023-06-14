package com.hc.util;

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
public class AmazonConnectUtil {


    @Value("${amaon:contactflowid}")
    private String contactFlowId;
    @Value("${amaon:instanceid}")
    private String instanceId;
    @Value("${amaon:sourcephonenumber}")
    private String sourcePhoneNumber;
    @Value("${amaon:accesskeyid}")
    private String accessKeyId;
    @Value("${amaon:secretaccesskey}")
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
        AwsCredentialsProvider awsCredentialsProvider  = () -> new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return "AKIAT5ILFNYED5JYHHF6";
            }

            @Override
            public String secretAccessKey() {
                return "QlsOyaS5VTBakXk7zTHoSpw/t0pvm3FQjYuOBBxv";
            }
        };
        ConnectClient connectClient = ConnectClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(awsCredentialsProvider)
                .build();
        Map<String,String> messageMap =  new HashMap<>();
        messageMap.put("Message","1213");
        StartOutboundVoiceContactResponse response = connectClient.startOutboundVoiceContact(
                StartOutboundVoiceContactRequest.builder()
                        .destinationPhoneNumber("+8613164197389")
                        .contactFlowId("63c72727-8db6-4e0d-b1cb-d1b4d36d98f9")
                        .attributes(messageMap)
                        .instanceId("07d6eae2-6962-48cf-979d-1b46cd92c957")
                        .sourcePhoneNumber("+12524864515")
                        .build()
        );


    }

}
