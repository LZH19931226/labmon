package com.hc.my.common.core.struct;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class Context {


    private Context() {
    }


    public static String getUserId(){
        HttpServletRequest request = getRequest();
        return request.getHeader("userId");
    }


    /**
     * 获取Request
     * @return
     */
    private static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    }

}
