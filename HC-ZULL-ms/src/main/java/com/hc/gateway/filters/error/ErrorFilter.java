package com.hc.gateway.filters.error;
//package gateway.filters.error;
//
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//
//@Component
//public class ErrorFilter extends ZuulFilter {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorFilter.class);
//
//    @Override
//    public String filterType() {
//        return "error";
//    }
//
//    @Override
//    public int filterOrder() {
//        return 10;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() {
//        RequestContext context = RequestContext.getCurrentContext();
//        Throwable throwable = context.getThrowable();
//        LOGGER.error("[ErrorFilter] error message: {}", throwable.getCause().getMessage());
//        context.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        context.set("error.exception", throwable.getCause());
//        return null;
//    }
//
//}
