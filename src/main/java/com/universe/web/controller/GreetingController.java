package com.universe.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Nick Liu
 * @date 2023/5/1
 */
@Slf4j
@Controller
public class GreetingController {

	@GetMapping("/page/greeting")
	public ModelAndView turnToGreetingPage() {
		return new ModelAndView("greeting");
	}

	@MessageMapping("/greeting")
	public String sayGreeting(String name) {
		log.info("Message received: {}", name);
		return "Hello, " + name;
	}
}
