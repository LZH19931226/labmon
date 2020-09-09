package com.hc.interpector;//package com.hc.interceptor;

import com.hc.config.BaseConfiguration;
import com.hc.config.RedisTemplateUtil;
import com.hc.model.ResModel;
import com.hc.units.JsonUtil;
import com.hc.units.TokenHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //return true;
        String url = request.getRequestURI();        // 用户访问地址
        String token = request.getHeader("token");
        //获取用户token
        HttpSession session = request.getSession();
        ResModel resModel = new ResModel();
        logger.info("LoginInterceptor 调用接口！================== Url:" + url + ";Token:" + token + ";SessionID:" + session.getId());
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = request.getServletPath();

        if (url.indexOf("/error") > -1) {
            logger.error("页面发生错误 跳转到 /error 页面中");
            return true;
        }
        //if (path.matches(Const.NO_INTERCEPTOR_PATH) || path.matches(".*/api/.*") ||path.matches(baseConfiguration.getPublicPath())) {
        if (path.matches(Const.NO_INTERCEPTOR_PATH) || path.matches(baseConfiguration.getPublicPath()) ||
                path.matches(".*/api/userBackInfo/userLogin")|| path.matches("/swagger-resources/configuration/ui")
                || path.matches("/swagger-resources") || path.matches("/v2/api-docs")
                || path.matches(".*/api/equipmentInfo/a") || path.matches(".*/api/repairinfo/*")
                || path.matches(".*/api/alarmStatistics/exporeExcle") || path.matches(".*/api/alarmStatistics/exporeExcles")
                || path.matches(".*/api/repairinfo/selectPageInfo")  ) {
            logger.info("匹配上了，直接放行：" + path);
//                System.out.println("匹配上了  path 直接放行: " + path);
            //              return true;
        } else {
            logger.info("未匹配 NO_INTERCEPTOR_PATH path : " + path);

//            if (StringUtils.isEmpty(token)) {//数据是空
//                //res.put("code", HttpServletResponse.SC_PAYMENT_REQUIRED);  //402 :当前账户未登录，请重新登录
//                resModel.setCode(HttpServletResponse.SC_PAYMENT_REQUIRED);
//                resModel.setMessage("Unauthorized");
//
//                response.setContentType("application/json;charset=UTF-8");
//                response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
//                response.getWriter().write(JsonUtil.toJson(resModel));
//                return false;
//            } else {//有 Token，解析
                //Map<String, Object> newUser = userRightrServcie.findUserByToken(tokenValue);
                    /*String id = TokenHelper.getUserID(token);
                    if(id == null) throw new Exception("用户账号异常！");
                    Map<String, Object> newUser = userRightrServcie.findUserByUserId(id);*/
//                String userId = TokenHelper.getUserID(token);
//                //根据userId在缓存中获取token
//                String redisToken = (String) redisTemplateUtil.boundValueOps(userId).get();
//                if (org.apache.commons.lang3.StringUtils.isEmpty(redisToken)) {
////                    res.put("code", HttpServletResponse.SC_PAYMENT_REQUIRED);  //402 :当前账户未登录，请重新登录
////                    res.put("message", "Unauthorized");
//                    resModel.setCode(HttpServletResponse.SC_PAYMENT_REQUIRED);
//                    resModel.setMessage("Unauthorized");
//                    response.setContentType("application/json;charset=UTF-8");
//                    response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
//                    response.getWriter().write(JsonUtil.toJson(resModel));
//                    return false;
//                }
//                if (StringUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
//                    //同一用户同一token
//                    // 401 当前账户已在其他位置登录，请重新登录
//                    logger.info("redisToken:" + redisToken + "  传递token:" + token);
////                    res.put("code", HttpServletResponse.SC_UNAUTHORIZED);
////                    res.put("message", "Unauthorized");
//                    resModel.setCode(HttpServletResponse.SC_UNAUTHORIZED);
//                    resModel.setMessage("Unauthorized");
//                    response.setContentType("application/json;charset=UTF-8");
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    response.getWriter().write(JsonUtil.toJson(resModel));
//                    return false;
//                }
//                redisTemplateUtil.boundValueOps(userId+userId).set(redisToken,1, TimeUnit.HOURS);
                return  true;
//            }
        }

        response.setHeader("token", token);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}