package com.universe.interceptor;

import com.universe.pojo.StompAuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

/**
 * @author Nick Liu
 * @date 2023/5/29
 */
@Slf4j
public class UserAuthenticationChannelInterceptor implements ChannelInterceptor {

	private static final String USER_ID = "User-ID";
	private static final String USER_NAME = "User-Name";

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		// 如果是连接请求，记录userId
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String userID = accessor.getFirstNativeHeader(USER_ID);
			String username = accessor.getFirstNativeHeader(USER_NAME);

			log.info("Stomp User-Related headers found, userID: {}, username:{}", userID, username);
			accessor.setUser(new StompAuthenticatedUser(userID, username));
		}

		return message;
	}

}
