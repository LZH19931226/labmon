package com.hc.handler;

import com.hc.provider.WebSocketMapping;
import com.hc.provider.WebSocketSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiuZhiHao
 * @date 2021/5/28 9:57
 * 描述:
 **/

@Component
@WebSocketMapping("/"+"socket")
@Slf4j
public class EchoHandler implements WebSocketHandler {

    @Autowired
    private ConcurrentHashMap<String, WebSocketSender> senderMap;

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        HandshakeInfo handshakeInfo = session.getHandshakeInfo();
        Map<String, String> queryMap = getQueryMap(handshakeInfo.getUri().getQuery());
        String id = queryMap.getOrDefault("id", "defaultId");
        log.info("websocket连接:{}",id);
        Mono<Void> input = session.receive().map(WebSocketMessage::getPayloadAsText).map(msg -> id + ": " + msg)
                .doOnNext(System.out::println).then();

        Mono<Void> output = session.send(Flux.create(sink -> senderMap.put(id, new WebSocketSender(session, sink))));
        /**
         * Mono.zip() 会将多个 Mono 合并为一个新的 Mono，任何一个 Mono 产生 error 或 complete 都会导致合并后的 Mono
         * 也随之产生 error 或 complete，此时其它的 Mono 则会被执行取消操作。
         */
        return Mono.zip(input, output).then();
    }

    private Map<String, String> getQueryMap(String queryStr) {
        Map<String, String> queryMap = new HashMap<>();
        if (!StringUtils.isEmpty(queryStr)) {
            String[] queryParam = queryStr.split("&");
            Arrays.stream(queryParam).forEach(s -> {
                String[] kv = s.split("=", 2);
                String value = kv.length == 2 ? kv[1] : "";
                queryMap.put(kv[0], value);
            });
        }
        return queryMap;
    }


}
