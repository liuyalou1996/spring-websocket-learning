package com.universe.config;

import com.universe.handler.EchoWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @author Nick Liu
 * @date 2023/4/27
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// HttpSessionHandshakeInterceptor可以将HttpSession中的属性转换为WebsocketSession的属性
		registry.addHandler(new EchoWebSocketHandler(), "/echo")
			.addInterceptors(new HttpSessionHandshakeInterceptor());
	}

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		// 指定单次最大可传输的文本消息大小为4 * 1024字符
		container.setMaxTextMessageBufferSize(4 * 1024);
		// 指定单次最大可传输的二进制消息大小为8 * 1024字节
		container.setMaxBinaryMessageBufferSize(8 * 1024);
		return container;
	}
}
