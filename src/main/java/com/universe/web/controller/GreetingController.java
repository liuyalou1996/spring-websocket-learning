package com.universe.web.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @author Nick Liu
 * @date 2023/5/1
 */
@Controller
public class GreetingController {

	@MessageMapping("/greeting")
	public String sayGreeting(String name) {
		return String.format("Hello, %s", name);
	}
}
