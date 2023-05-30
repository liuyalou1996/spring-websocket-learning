package com.universe.config;

import com.universe.interceptor.UserAuthenticationChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @author Nick Liu
 * @date 2023/5/1
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket") // WebSocket握手端口
			.addInterceptors(new HttpSessionHandshakeInterceptor())
			.setAllowedOriginPatterns("*") // 设置跨域
			.withSockJS(); // 开启SockJS回退机制
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new UserAuthenticationChannelInterceptor());
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		// 这里我们设置入站消息最大为8K
		registry.setMessageSizeLimit(8 * 1024);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app") // 发送到服务端目的地前缀
			.enableSimpleBroker("/topic");// 开启简单消息代理，指定消息订阅前缀
	}

}
