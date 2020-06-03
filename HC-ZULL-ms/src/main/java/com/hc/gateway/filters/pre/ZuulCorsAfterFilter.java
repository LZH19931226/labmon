//package com.hc.gateway.filters.pre;
//
//
//import java.util.Collection;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ZuulCorsAfterFilter extends ZuulFilter{
//
//	private Logger logger = LoggerFactory.getLogger(getClass());
//
//	@Override
//	public boolean shouldFilter() {
//		return true;
//	}
//
//	@Override
//	public Object run() {
//		logger.debug("跨域请求后处理过滤器启动");
//		RequestContext ctx = RequestContext.getCurrentContext();
//		HttpServletResponse response = ctx.getResponse();
//		Collection<String> headers=response.getHeaderNames();
//		for(String name:headers) {
//			logger.debug("请求头："+name+" 对应值："+response.getHeaders(name));
//		}
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		logger.debug("成功设置跨域响应头：Access-Control-Allow-Origin=*");
//		return null;
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
//	@Override
//	public int filterOrder() {
//		return 1;
//	}
//
//}
