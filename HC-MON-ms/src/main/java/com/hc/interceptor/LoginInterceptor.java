package com.hc.interceptor;//package com.hc.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.hc.config.BaseConfiguration;
import com.hc.config.RedisTemplateUtil;
import com.hc.constant.Const;
import com.hc.utils.TokenHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定义拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    Logger logger = org.slf4j.LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    BaseConfiguration baseConfiguration;

    @Autowired
    RedisTemplateUtil redisTemplateUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = request.getRequestURI();
        String token = request.getParameter("token");
        //获取用户token
        HttpSession session = request.getSession();
        JSONObject res = new JSONObject();
        logger.info("LoginInterceptor 调用接口！================== Url:" + url + ";Token:"+token + ";SessionID:" + session.getId());
        String path = request.getServletPath();
        try {
            if(url.contains("/error")){
                logger.error("页面发生错误 跳转到 /error 页面中");
                return true;
            }
            if (path.matches(Const.NO_INTERCEPTOR_PATH) || path.matches(baseConfiguration.getPublicPath()) ||
                    path.matches(".*/api/userInfo/userLoginByAn") || path.matches(".*/api/userInfo/userLoginByAns") || path.matches(".*/api/userInfo/userLogin") || path.matches(".*/api/userInfo/loginOut")|| path.matches("/swagger-resources/configuration/ui")
                    || path.matches("/swagger-resources")|| path.matches("/v2/api-docs")
                    || path.matches(".*/apk/.*")|| path.matches(".*/api/insParamSet/sendMessage")|| path.matches(".*/api/insParamSet/sendMessages")||path.matches(".*/api/insParamSet/updateDevicetoken")
                    || path.matches(".*api/warningInfo/showData") || path.matches(".*/api/equipmentInfo/a")|| path.matches(".*/api/equipmentInfo/getEquipmentInfoByHospitalcode")) {
                logger.info("匹配上了，直接放行："+path);
            } else {
//                logger.info("未匹配 NO_INTERCEPTOR_PATH path : " + path);
//                //数据是空
//                if (StringUtils.isEmpty(token)) {
//                    logger.info("token为空");
//                    //402 :当前账户未登录，请重新登录
//                    res.put("code", HttpServletResponse.SC_PAYMENT_REQUIRED);
//                    res.put("message", "Unauthorized");
//                    response.setContentType("application/json;charset=UTF-8");
//                    response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
//                    response.getWriter().write(res.toJSONString());
//                    return false;
//                }else{//有 Token，解析
//                    String userId = TokenHelper.getUserID(token);
//                    //根据userId在缓存中获取token
//                    String redisToken = (String)redisTemplateUtil.boundValueOps(userId).get();
//                    if(StringUtils.isEmpty(redisToken) || !token.equals(redisToken)){
//                        //同一用户同一token
//                        // 401 当前账户已在其他位置登录，请重新登录
//                        logger.info("redisToken:"+redisToken+"  传递token:"+token);
//                        res.put("code", HttpServletResponse.SC_UNAUTHORIZED);
//                        res.put("message", "Unauthorized");
//                        response.setContentType("application/json;charset=UTF-8");
//                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                        response.getWriter().write(res.toJSONString());
//                        return false;
//                    }
//                }
                return  true;
            }

            response.setHeader("token", token);
        }catch (Exception e) {
            logger.error("Session 失效，原因：" + e.getMessage() + ";==========Url"+url);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

}