package com.hc.gateway.filters.pre;
//package gateway.filters.pre;
//
//
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.jp.api.gateway.feign.service.AuthorizationService;
//import com.jp.model.CacheResult;
//import com.jp.service.CacheService;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//
////@Component
//public class AuthorizationFilter extends ZuulFilter {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationFilter.class);
//
//    private static final String SERVICE_NAME_REGEX = "jp-(.+)-([A-Za-z]+)";// 取出url中service名称
//
//    @Autowired
//    private CacheService redisClusterCacheService;
//
//    @Autowired
//    private AuthorizationService authorizationService;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public boolean shouldFilter() {
////        return RequestContext.getCurrentContext().getRequest().getRequestURL().toString()
////                .indexOf("jp-authorization-microservice") < 0;
//    	return false;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public Object run() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        String url = request.getRequestURL().toString();
//        String accessToken = request.getHeader("accessToken");
//
//        // header中是否携带token
//        if (StringUtils.isEmpty(accessToken)) {
//            shutdown(ctx, HttpStatus.UNAUTHORIZED.value(), "unauthorized");
//        } else {
//            CacheResult cacheResult = redisClusterCacheService.getString(accessToken);
//            // 缓存中是否存在token 不存在通过认证服务器进行校验
//            if (null == cacheResult.getValue()) {
//                LOGGER.debug("can't get cache token detail by {}", accessToken);
//                if (!this.checkAndCacheToken(ctx, accessToken, url)) {
//                    shutdown(ctx, HttpStatus.UNAUTHORIZED.value(), "invalid token");
//                }
//            } else {// 存在判断是否有服务权限
//                // TODO test
//                Map<String, ?> clientDetails = null;
//                try {
//                    clientDetails =
//                            objectMapper.readValue(cacheResult.getValue().toString(), Map.class);
//                } catch (Exception e) {
//                    shutdown(ctx, HttpStatus.UNAUTHORIZED.value(), "parse token error");
//                }
//                if (!checkResourcePermission(clientDetails.get("scope").toString(), url)) {
//                    shutdown(ctx, HttpStatus.UNAUTHORIZED.value(), "invalid token");
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    @Override
//    public int filterOrder() {
//        return 2;
//    }
//
//    /**
//     * 向认证服务器发送校验请求 校验正确后token和client信息缓存到redis
//     * 
//     * @param ctx
//     * @param accessToken
//     * @param url
//     * @return
//     */
//    private boolean checkAndCacheToken(RequestContext ctx, String accessToken, String url) {
//        Map<String, ?> tokenDetails = authorizationService.checkToken(accessToken);
//        if (tokenDetails.containsKey("error")) {
//            LOGGER.debug("invalid token {}", accessToken);
//            return false;
//        } else {
//            redisClusterCacheService.save(accessToken, tokenDetails,
//                    Integer.valueOf(tokenDetails.get("exp").toString()));
//            if (tokenDetails.containsKey("scope")
//                    && checkResourcePermission(tokenDetails.get("scope").toString(), url)) {
//            } else {
//                shutdown(ctx, HttpStatus.UNAUTHORIZED.value(), "permission denied");
//            }
//        }
//        LOGGER.info("clientId:{} username:{} request:{}", tokenDetails.get("client_id"),
//                tokenDetails.get("user_name"), url);
//        return true;
//    }
//
//    /**
//     * 终止这次请求
//     * 
//     * @param ctx
//     * @param code
//     * @param errorMessage
//     */
//    private void shutdown(RequestContext ctx, int code, String errorMessage) {
//        ctx.setResponseStatusCode(code);
//        ctx.setSendZuulResponse(false);
//        throw new RuntimeException(errorMessage);
//    }
//
//    /**
//     * 判断客户端是否有访问当前服务权限
//     * 
//     * @param resourceIds
//     * @param url
//     * @return
//     */
//    private boolean checkResourcePermission(String resourceIds, String url) {
//        if (StringUtils.isNotEmpty(resourceIds)) {
//            String resourceId;
//            Pattern pattern = Pattern.compile(SERVICE_NAME_REGEX);
//            Matcher matcher = pattern.matcher(url);
//            if (matcher.find()) {
//                resourceId = matcher.group(0);
//                if (resourceIds.indexOf(resourceId) >= 0) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//}
