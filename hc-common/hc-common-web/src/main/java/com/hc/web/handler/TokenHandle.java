package com.hc.web.handler;

import com.hc.my.common.core.bean.Audience;
import com.hc.my.common.core.exception.IError;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.jwt.JwtIgnore;
import com.hc.my.common.core.jwt.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author LiuZhiHao
 * @date 2019/10/16 9:25
 * 描述: 根据token获取用户信息，若token失效或者为空，则抛出异常
 **/
@Slf4j
public class TokenHandle implements HandlerInterceptor {

    @Resource
    private Audience audience;


    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse httpServletResponse, Object handler) {
        // 忽略带JwtIgnore注解的请求, 不做后续token认证校验
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            JwtIgnore jwtIgnore = handlerMethod.getMethodAnnotation(JwtIgnore.class);
//            if (jwtIgnore != null) {
//                return true;
//            }
//        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(JwtIgnore.class)) {
            JwtIgnore passToken = method.getAnnotation(JwtIgnore.class);
            if (passToken.required()) {
                return true;
            }
        }

        if (HttpMethod.OPTIONS.name().equals(req.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        // 获取请求头信息authorization信息
        String authHeader = req.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        log.info("## authHeader= {}", authHeader);
        if (StringUtils.isEmpty(authHeader)) {
            throw new IedsException(IError.TOKEN);
        }
        // 获取token
        String token = authHeader.substring(7);
        if (audience == null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getServletContext());
            audience = factory.getBean(Audience.class);
        }
        // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
        Claims claims = JwtTokenUtil.parseJWT(token, audience.getBase64Secret());
        if (null == claims) {
            throw new IedsException(IError.TOKEN);
        }
        return true;
    }
}
