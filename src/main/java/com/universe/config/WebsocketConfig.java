package com.universe.config;

import com.universe.handler.EchoWebsocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @author Nick Liu
 * @date 2023/4/27
 */
// @Configuration
// @EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// HttpSessionHandshakeInterceptor可以将HttpSession中的属性转换为WebsocketSession的属性
		registry.addHandler(new EchoWebsocketHandler(), "/echo")
			.addInterceptors(new HttpSessionHandshakeInterceptor());
	}

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(4 * 1024);
		container.setMaxBinaryMessageBufferSize(8 * 1024);
		return container;
	}
}
