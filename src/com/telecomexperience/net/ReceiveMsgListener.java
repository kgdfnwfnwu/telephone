package com.telecomexperience.net;

/**
 * 接收消息监听的listener接口
 * @author ccf
 *
 */
public interface ReceiveMsgListener {
	public boolean receive(ChatMessage msg);

}
