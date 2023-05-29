package com.universe.interceptor;

import com.universe.holder.UserSessionHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Optional;

/**
 * @author Nick Liu
 * @date 2023/5/29
 */
@Slf4j
public class FetchHeaderChannelInterceptor implements ChannelInterceptor {

	private static final String USER_ID = "User-ID";

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		// 如果是连接请求，记录userId
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			Optional.ofNullable(accessor.getFirstNativeHeader(USER_ID)).ifPresent(userId -> {
				log.info("Stomp header [{}] detected: {}", USER_ID, userId);
				UserSessionHolder.addUserID(userId);
			});
		}
		return message;
	}
}
