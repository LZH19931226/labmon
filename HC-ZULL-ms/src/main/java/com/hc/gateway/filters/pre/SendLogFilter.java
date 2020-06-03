//package com.hc.gateway.filters.pre;
//
//
//import javax.servlet.http.HttpServletRequest;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.ZuulFilter;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SendLogFilter extends ZuulFilter {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(SendLogFilter.class);
//
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    @Override
//    public int filterOrder() {
//        return 1;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        LOGGER.info("{} request to {} with param {}", request.getMethod(),
//                request.getRequestURL().toString(), request.getParameterMap());
//        return null;
//    }
//
//}
