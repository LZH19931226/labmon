package com.hc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.util.HashMap;
import java.util.List;
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

       //makeCall("+8613164197389","ok");


//        Region region = Region.US_EAST_1;
//        ConnectClient connectClient = ConnectClient.builder()
//                .region(region)
//                .build();
//        getUsers(connectClient, instanceId);
    }

    public static void getUsers( ConnectClient connectClient, String instanceId) {
        try {
            ListUsersRequest usersRequest = ListUsersRequest.builder()
                    .instanceId(instanceId)
                    .maxResults(10)
                    .build();

            ListUsersResponse response = connectClient.listUsers(usersRequest);
            List<UserSummary> users = response.userSummaryList();
            for (UserSummary user: users) {
                System.out.println("The user name of the user is "+user.username());
                System.out.println("The user id is  "+user.id());
            }

        } catch (ConnectException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }

}
