package com.hc.my.common.core.struct;

import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.struct.contextEnum.ContextEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class Context {


    private Context() {
    }


    public static String getUserId(){
        HttpServletRequest request = getRequest();
        String userId = request.getHeader("userId");
        if(StringUtils.isBlank(userId)){
            throw new IedsException(ContextEnum.NOT_LOGGED_IN.getMessage());
        }
        return userId;
    }


    /**
     * 获取Request
     * @return
     */
    private static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    }

}
