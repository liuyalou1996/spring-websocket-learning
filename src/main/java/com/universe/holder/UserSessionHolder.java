package com.universe.holder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Nick Liu
 * @date 2023/5/29
 */
public class UserSessionHolder {

	private static final Map<String, String> USER_SESSION_MAP = new LinkedHashMap<>();

	public static void addSession(String userId, String sessionId) {
		USER_SESSION_MAP.put(userId, sessionId);
	}

	public static void listAllSessionID(BiConsumer<String, String> consumer) {
		USER_SESSION_MAP.forEach(consumer);
	}

}
