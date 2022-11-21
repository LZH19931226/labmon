package com.hc.my.common.core.struct;

import com.hc.my.common.core.bean.Audience;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.jwt.JwtTokenUtil;
import com.hc.my.common.core.struct.contextEnum.ContextEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class Context {

 
    private Context() {
    }


    public static String getUserId(){
        HttpServletRequest request = getRequest();
        String userIdToken = request.getHeader("Authorization");
        String token = userIdToken.substring(7);
        String userId = JwtTokenUtil.getUserId(token,new Audience().getBase64Secret());
        if(StringUtils.isBlank(userId)){
            throw new IedsException(ContextEnum.NOT_LOGGED_IN.getMessage());
        }
        return userId;
    }

    public static String getLang(){
        HttpServletRequest request = getRequest();
        String userIdToken = request.getHeader("Authorization");
        if(StringUtils.isBlank(userIdToken) && "Bearer undefined".equals(userIdToken)){
            return null;
        }
        System.out.println(userIdToken);
        String token = userIdToken.substring(7);
        String lang = JwtTokenUtil.getLang(token,new Audience().getBase64Secret());
        if(StringUtils.isBlank(lang)){
            return null;
        }
        return lang;
    }


    /**
     * 获取Request
     * @return
     */
    private static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    }

}
