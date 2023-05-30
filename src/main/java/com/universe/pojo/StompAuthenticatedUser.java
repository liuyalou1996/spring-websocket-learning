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

	private String userId;

	private String nickName;

	@Override
	public String getName() {
		return this.userId;
	}

}
