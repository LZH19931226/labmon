//package com.hc.gateway.filters.pre;
//
//
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//
//@Component
//public class ZuulCorsFilter extends ZuulFilter {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(ZuulCorsFilter.class);
//
//	@Override
//	public Object run() {
//
//        RequestContext ctx = RequestContext.getCurrentContext();
//
//        HttpServletResponse response = ctx.getResponse();
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html;charset=UTF-8");
//		return null;
//	}
//
//	@Override
//	public boolean shouldFilter() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public int filterOrder() {
//		// TODO Auto-generated method stub
//		return 3;
//	}
//
//	/**
//	 * pre：在请求被路由之前调用
//	 * routing：在路由被请求时调用
//	 * post：在routing和error过滤器之后被调用
//	 * error：处理请求时发生错误时调用
//	 */
//	@Override
//	public String filterType() {
//		// TODO Auto-generated method stub
//		return "pre";
//	}
//
//}
