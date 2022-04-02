//package com.hc.filter;
//
//import com.hc.common.constants.Global;
//import com.hc.common.exception.IedsException;
//import com.hc.properties.IgnoreWhiteProperties;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
///**
// * @author LiuZhiHao
// * @date 2019/10/16 16:13
// * 描述: token拦截,路径放开
// **/
//
//@Component
//public class AuthSignFilter implements GlobalFilter, Ordered {
//    @Autowired
//    private IgnoreWhiteProperties properties;
//
//    private static final String TOKEN_NULL_ERROR = "token为空";
//
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        List<String> whites = properties.getWhites();
//        ServerHttpRequest serverHttpRequest = exchange.getRequest();
//        String uri = serverHttpRequest.getURI().getPath();
//        //检查白名单（配置）
//        if (!CollectionUtils.isEmpty(whites) && whites.contains(uri)){
//            return chain.filter(exchange);
//        }
//
//        HttpHeaders headers = serverHttpRequest.getHeaders();
//        List<String> strings = headers.get(Global.HANDLER_TOKEN);
//        if (CollectionUtils.isEmpty(strings)) {
//            throw new IedsException(TOKEN_NULL_ERROR);
//        }
////        String token = strings.stream().findFirst().get();
////        ServerHttpRequest host = exchange.getRequest().mutate()
////                .header("Authorization", token).build();
////        ServerWebExchange build = exchange.mutate().request(host).build();
////        return chain.filter(build);
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder(){
//        return Ordered.LOWEST_PRECEDENCE;
//    }
//
//}