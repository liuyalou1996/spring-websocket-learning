package com.universe.web.controller;

import com.universe.holder.UserSessionHolder;
import com.universe.pojo.dto.WebSocketMsgDTO;
import com.universe.pojo.vo.WebSocketMsgVO;
import com.universe.util.FastJsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Nick Liu
 * @date 2023/5/29
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate simpMessagingTemplate;

	@GetMapping("/page/chat")
	public ModelAndView turnToChatPage() {
		return new ModelAndView("/chat");
	}

	@MessageMapping("/chat/group")
	@SendTo("/topic/chat/group")
	public WebSocketMsgVO groupChat(WebSocketMsgDTO webSocketMsgDTO) {
		log.info("Group chat message received: {}", FastJsonUtils.toJsonString(webSocketMsgDTO));
		String content = String.format("来自[%s]的群聊消息: %s", webSocketMsgDTO.getName(), webSocketMsgDTO.getContent());
		return WebSocketMsgVO.builder().content(content).build();
	}

	@MessageMapping("/chat/private")
	@SendToUser("/topic/chat/private")
	public WebSocketMsgVO privateChat(WebSocketMsgDTO webSocketMsgDTO) {
		log.info("Private chat message received: {}", FastJsonUtils.toJsonString(webSocketMsgDTO));
		String content = "私聊消息回复：" + webSocketMsgDTO.getContent();
		return WebSocketMsgVO.builder().content(content).build();
	}

	@Scheduled(fixedRate = 10 * 1000)
	public void pushMessageAtFixedRate() {
		WebSocketMsgVO webSocketMsgVO = WebSocketMsgVO.builder()
			.content(String.format("定时推送的消息, 时间:%s", LocalDateTime.now()))
			.build();
		Set<String> userIdSet = UserSessionHolder.getAllUserID();
		userIdSet.forEach(userId -> {
			SimpMessageHeaderAccessor header = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
			header.setSessionId(null);
			log.info("开始推送消息给指定用户, userId: {}, 消息内容:{}", userId, FastJsonUtils.toJsonString(webSocketMsgVO));
			simpMessagingTemplate.convertAndSendToUser(userId, "/chat/push", webSocketMsgVO, header.getMessageHeaders());
		});
	}

}
