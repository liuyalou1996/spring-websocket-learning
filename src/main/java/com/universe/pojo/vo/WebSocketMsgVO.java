package com.universe.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nick Liu
 * @date 2023/5/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMsgVO {

	private String content;
}
