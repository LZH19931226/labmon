//package com.hc.gateway.filters.pre;
//
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//
///**
// * 异常测试过滤器
// *
// * @author zhangyanyan
// *
// */
////@Component
//public class ThrowExceptionFilter extends ZuulFilter {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ThrowExceptionFilter.class);
//
//    @Override
//    public boolean shouldFilter() {
//        return false;
//    }
//
//    @Override
//    public Object run() {
//        LOGGER.info("this is pre filter, it will throw a RuntimeException");
//        RequestContext ctx = RequestContext.getCurrentContext();
//        try {
//            doSomething();
//        } catch (Exception e) {
//            ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            ctx.set("error.exception", e);
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
//        return 0;
//    }
//
//    private void doSomething() {
//        throw new RuntimeException("Exist some errors");
//    }
//
//}
