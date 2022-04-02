package com.hc.provider;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

/**
 * @author LiuZhiHao
 * @date 2021/5/28 10:06
 * 描述:
 **/

public class WebSocketSender {
    private WebSocketSession session;
    private FluxSink<WebSocketMessage> sink;

    public WebSocketSender(WebSocketSession session, FluxSink<WebSocketMessage> sink) {
        this.session = session;
        this.sink = sink;
    }

    public void sendData(String data) {
        sink.next(session.textMessage(data));
    }
}
