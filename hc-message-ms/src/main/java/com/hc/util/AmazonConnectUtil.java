package com.hc.util;

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
    static String contactFlowId = "63c72727-8db6-4e0d-b1cb-d1b4d36d98f9";
    static  String instanceId = "07d6eae2-6962-48cf-979d-1b46cd92c957";
    static String sourcePhoneNumber = "+12524864515";

    public static void makeCall(String phoneNumber,String message) {

        AwsCredentialsProvider awsCredentialsProvider  = () -> new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return "AKIAT5ILFNYECNWNL3X6";
            }

            @Override
            public String secretAccessKey() {
                return "Dt+GM5FyawSbWV1WUsfb/Zs8cf0YEXkP/U9WYDNE";
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

       makeCall("+8613164197389","ok");


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
