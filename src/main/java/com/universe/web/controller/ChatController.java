package com.universe.web.controller;

import com.universe.pojo.StompAuthenticatedUser;
import com.universe.pojo.dto.WebSocketMsgDTO;
import com.universe.pojo.vo.WebSocketMsgVO;
import com.universe.util.FastJsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nick Liu
 * @date 2023/5/29
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpUserRegistry simpUserRegistry;
	private final SimpMessagingTemplate simpMessagingTemplate;

	/**
	 * 模板引擎为Thymeleaf，需要加上spring-boot-starter-thymeleaf依赖，
	 * @return
	 */
	@GetMapping("/page/chat")
	public ModelAndView turnToChatPage() {
		return new ModelAndView("chat");
	}

	/**
	 * 群聊消息处理
	 * 这里我们通过@SendTo注解指定消息目的地为"/topic/chat/group"，如果不加该注解则会自动发送到"/topic" + "/chat/group"
	 * @param webSocketMsgDTO 请求参数，消息处理器会自动将JSON字符串转换为对象
	 * @return 消息内容，方法返回值将会广播给所有订阅"/topic/chat/group"的客户端
	 */
	@MessageMapping("/chat/group")
	@SendTo("/topic/chat/group")
	public WebSocketMsgVO groupChat(WebSocketMsgDTO webSocketMsgDTO) {
		log.info("Group chat message received: {}", FastJsonUtils.toJsonString(webSocketMsgDTO));
		String content = String.format("来自[%s]的群聊消息: %s", webSocketMsgDTO.getName(), webSocketMsgDTO.getContent());
		return WebSocketMsgVO.builder().content(content).build();
	}

	/**
	 * 私聊消息处理
	 * 这里我们通过@SendToUser注解指定消息目的地为"/topic/chat/private"，发送目的地默认会拼接上"/user/"前缀
	 * 实际发送目的地为"/user/topic/chat/private"
	 * @param webSocketMsgDTO 请求参数，消息处理器会自动将JSON字符串转换为对象
	 * @return 消息内容，方法返回值将会基于SessionID单播给指定用户
	 */
	@MessageMapping("/chat/private")
	@SendToUser("/topic/chat/private")
	public WebSocketMsgVO privateChat(WebSocketMsgDTO webSocketMsgDTO) {
		log.info("Private chat message received: {}", FastJsonUtils.toJsonString(webSocketMsgDTO));
		String content = "私聊消息回复：" + webSocketMsgDTO.getContent();
		return WebSocketMsgVO.builder().content(content).build();
	}

	/**
	 * 定时消息推送，这里我们会列举所有在线的用户，然后单播给指定用户。
	 * 通过SimpMessagingTemplate实例可以在任何地方推送消息。
	 */
	@Scheduled(fixedRate = 10 * 1000)
	public void pushMessageAtFixedRate() {
		log.info("当前在线人数: {}", simpUserRegistry.getUserCount());
		if (simpUserRegistry.getUserCount() <= 0) {
			return;
		}

		// 这里的Principal为StompAuthenticatedUser实例
		Set<StompAuthenticatedUser> users = simpUserRegistry.getUsers().stream()
			.map(simpUser -> StompAuthenticatedUser.class.cast(simpUser.getPrincipal()))
			.collect(Collectors.toSet());

		users.forEach(authenticatedUser -> {
			String userId = authenticatedUser.getUserId();
			String nickName = authenticatedUser.getNickName();
			WebSocketMsgVO webSocketMsgVO = new WebSocketMsgVO();
			webSocketMsgVO.setContent(String.format("定时推送的私聊消息, 接收人: %s, 时间: %s", nickName, LocalDateTime.now()));

			log.info("开始推送消息给指定用户, userId: {}, 消息内容:{}", userId, FastJsonUtils.toJsonString(webSocketMsgVO));
			simpMessagingTemplate.convertAndSendToUser(userId, "/topic/chat/push", webSocketMsgVO);
		});
	}

}
