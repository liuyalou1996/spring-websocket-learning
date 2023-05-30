package com.universe.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * @author Nick Liu
 * @date 2023/5/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StompAuthenticatedUser implements Principal {

	/**
	 * 用户唯一ID
	 */
	private String userId;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 用于指定用户消息推送的标识
	 * @return
	 */
	@Override
	public String getName() {
		return this.userId;
	}

}
