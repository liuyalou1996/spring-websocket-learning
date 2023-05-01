package com.universe.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author Nick Liu
 * @date 2023/5/1
 */
@Slf4j
public class EchoWebsocketHandler extends TextWebSocketHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String content = new String(message.asBytes(), StandardCharsets.UTF_8);

		log.info("Websocket message received: {}", content);
		session.sendMessage(message);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("Connection established, request uri:{}", session.getUri());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("Connection closed, request uri:{}", session.getUri());
	}
}
