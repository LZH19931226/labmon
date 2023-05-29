package com.hc.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.StartOutboundVoiceContactRequest;
import software.amazon.awssdk.services.connect.model.StartOutboundVoiceContactResponse;

import java.util.HashMap;
import java.util.Map;


@Component
public class AmazonConnectUtil {

    @Autowired
    private  ConnectClient connectClient;

    public void makeCall(String phoneNumber,String message) {
        String contactFlowId = "63c72727-8db6-4e0d-b1cb-d1b4d36d98f9";
        String instanceId = "07d6eae2-6962-48cf-979d-1b46cd92c957";
        String sourcePhoneNumber = "+12524864515";
        Map<String,String> messageMap =  new HashMap<>();
        messageMap.put("Message",message);
        StartOutboundVoiceContactResponse response = connectClient.startOutboundVoiceContact(
                StartOutboundVoiceContactRequest.builder()
                        .destinationPhoneNumber(phoneNumber)
                        .contactFlowId(contactFlowId)
                        .instanceId(instanceId)
                        .sourcePhoneNumber(sourcePhoneNumber)
                        .attributes(messageMap)
                        .build()
        );

        System.out.println(response);
    }

}
