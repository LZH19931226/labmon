package com.hc.controller;

import com.hc.listenter.SocketMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msct")
public class TestMsct {

    @Autowired
    private SocketMessageListener socketMessageListener;

   @GetMapping("/")
   public void test(String message){
       socketMessageListener.msctMessage(message);
   }


}
