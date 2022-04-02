//package com.hc.filter;
//
///**
// * @author LiuZhiHao
// * @date 2021/5/24 16:13
// * 描述:
// **/
//
//import com.google.common.base.Joiner;
//import com.google.common.collect.Lists;
//import lombok.extern.slf4j.Slf4j;
//import org.reactivestreams.Publisher;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//
//@Component
//@Slf4j
//public class WrapperResponseGlobalFilter implements GlobalFilter, Ordered {
//
//
//    private static Joiner joiner = Joiner.on("");
//
//    @Override
//    public int getOrder() {
//        // -1 is response write filter, must be called before that
//        return -2;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpResponse originalResponse = exchange.getResponse();
//        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
//        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//                if (body instanceof Flux) {
//                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
//                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {//解决返回体分段传输
//                        List<String> list = Lists.newArrayList();
//                        dataBuffers.forEach(dataBuffer -> {
//                            byte[] content = new byte[dataBuffer.readableByteCount()];
//                            dataBuffer.read(content);
//                            DataBufferUtils.release(dataBuffer);
//                            try {
//                                list.add(new String(content, StandardCharsets.UTF_8));
//                            } catch (Exception e) {
//                                log.error("--list.add--error", e);
//                            }
//                        });
//                        String responseData = joiner.join(list);
//                        byte[] uppedContent = new String(responseData.getBytes(), StandardCharsets.UTF_8).getBytes();
//                        originalResponse.getHeaders().setContentLength(uppedContent.length);
//                        return bufferFactory.wrap(uppedContent);
//                    }));
//                }
//                // if body is not a flux. never got there.
//                return super.writeWith(body);
//            }
//        };
//        // replace response with decorator
//        return chain.filter(exchange.mutate().response(decoratedResponse).build());
//    }
//
//
//}
