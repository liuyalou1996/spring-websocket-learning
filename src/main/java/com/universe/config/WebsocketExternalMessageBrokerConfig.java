package com.universe.config;

import com.universe.interceptor.UserAuthenticationChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Websocket连接外部消息代理配置
 * @author Nick Liu
 * @date 2023/9/6
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketExternalMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// 拦截器配置
		registration.interceptors(new UserAuthenticationChannelInterceptor());
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket") // WebSocket握手端口
			.addInterceptors(new HttpSessionHandshakeInterceptor())
			.setAllowedOriginPatterns("*") // 设置跨域
			.withSockJS(); // 开启SockJS回退机制
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app") // 发送到服务端目的地前缀
			.enableStompBrokerRelay("/topic") // 开启外部消息代理，指定消息订阅前缀
			.setRelayHost("175.178.107.127") // 外部消息代理
			.setRelayPort(61613)
			.setSystemLogin("admin")
			.setSystemPasscode("admin")
			.setClientLogin("admin")
			.setClientPasscode("admin")
			.setVirtualHost("/stomp");
	}
}
