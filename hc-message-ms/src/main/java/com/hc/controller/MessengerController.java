package com.hc.controller;

import com.hc.business.MessengerService;
import com.hc.model.NotifyMessage;
import com.hc.my.common.core.domain.P2PNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 11:28
 * 描述:
 **/
@RestController
@RequestMapping("/message")
public class MessengerController {

    @Autowired
    private MessengerService messengerService;



    @PostMapping("/p2PNotify")
    public  String send(@RequestBody P2PNotify notify){
        NotifyMessage broadcast = new NotifyMessage();
        broadcast.setMessageBodys(notify.getMessageBodys());
        broadcast.setMessageCover(notify.getMessageCover());
        broadcast.setMessageIntro(notify.getMessageIntro());
        broadcast.setMessageTitle(notify.getMessageTitle());
        broadcast.setParams(notify.getParams());
        broadcast.setServiceNo(notify.getServiceNo());
        broadcast.setPrincipals(getPrincipal(Collections.singletonList(notify.getUserId())));
        broadcast.setChannels(notify.getChannels());
        return messengerService.send(broadcast);

    }

    private Map<String, Object> getPrincipal(List<String> userIds) {
        Map<String, Object> map = new HashMap<>(16);
        userIds.forEach(s-> map.put(s,s));
        return map;
    }

}
