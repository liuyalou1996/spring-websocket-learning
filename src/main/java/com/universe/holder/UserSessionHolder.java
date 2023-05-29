package com.universe.holder;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Nick Liu
 * @date 2023/5/29
 */
public class UserSessionHolder {

	private static final Set<String> USER_ID_SET = new LinkedHashSet<>();

	public static void addUserID(String userId) {
		USER_ID_SET.add(userId);
	}

	public static Set<String> getAllUserID() {
		return USER_ID_SET;
	}
}
