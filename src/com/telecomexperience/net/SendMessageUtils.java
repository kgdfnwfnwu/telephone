package com.telecomexperience.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.util.Log;

import com.telecomexperience.data.GlobalVar;

public class SendMessageUtils {
	
	/**
	 * 发送摇一摇的消息
	 */
	public static void sendShakeMessage(String result) {
		try {
			User user = GlobalVar.getInstance().getUser();
			if (user == null)
				return;
			String receiverIp = user.getIp();

			if (null == receiverIp || "".equals(receiverIp)) {
				return;
			}
			// 发送消息
			IpMessageProtocol sendMsg = new IpMessageProtocol();
			sendMsg.setVersion(String.valueOf(IpMessageConst.VERSION));
			sendMsg.setSenderName(NetThreadHelper.newInstance().getSelfName());
			sendMsg.setSenderHost("Android");
			sendMsg.setCommandNo(IpMessageConst.IPMSG_SENDSHAKEFEEDBACKMSG);
			sendMsg.setAdditionalSection(result);
			InetAddress sendto = null;
			try {
				sendto = InetAddress.getByName(receiverIp);
			} catch (UnknownHostException e) {
				Log.e("MyFeiGeChatActivity", "发送地址有误");
			}
			if (sendto != null)
				NetThreadHelper.newInstance().sendUdpData(sendMsg.getProtocolString() + "\0",	sendto, IpMessageConst.PORT);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送推送的消息
	 */
	public static void sendPushMessage(String result) {
		try {
			User user = GlobalVar.getInstance().getUser();
			if (user == null)
				return;
			String receiverIp = user.getIp();

			if (null == receiverIp || "".equals(receiverIp)) {
				return;
			}
			// 发送消息
			IpMessageProtocol sendMsg = new IpMessageProtocol();
			sendMsg.setVersion(String.valueOf(IpMessageConst.VERSION));
			sendMsg.setSenderName(NetThreadHelper.newInstance().getSelfName());
			sendMsg.setSenderHost("Android");
			sendMsg.setCommandNo(IpMessageConst.IPMSG_SENDPUSHFEEDBACKMSG);
			sendMsg.setAdditionalSection(result);
			InetAddress sendto = null;
			try {
				sendto = InetAddress.getByName(receiverIp);
			} catch (UnknownHostException e) {
				Log.e("MyFeiGeChatActivity", "发送地址有误");
			}
			if (sendto != null)
				NetThreadHelper.newInstance().sendUdpData(sendMsg.getProtocolString() + "\0",	sendto, IpMessageConst.PORT);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 发送推送的消息
	 */
	public static void sendSwitchShakeMessage() {
		try {
			User user = GlobalVar.getInstance().getUser();
			if (user == null)
				return;
			String receiverIp = user.getIp();

			if (null == receiverIp || "".equals(receiverIp)) {
				return;
			}
			// 发送消息
			IpMessageProtocol sendMsg = new IpMessageProtocol();
			sendMsg.setVersion(String.valueOf(IpMessageConst.VERSION));
			sendMsg.setSenderName(NetThreadHelper.newInstance().getSelfName());
			sendMsg.setSenderHost("Android");
			sendMsg.setCommandNo(IpMessageConst.IPMSG_SENDSWITCHSHAKEMSG);
			sendMsg.setAdditionalSection("");
			InetAddress sendto = null;
			try {
				sendto = InetAddress.getByName(receiverIp);
			} catch (UnknownHostException e) {
				Log.e("MyFeiGeChatActivity", "发送地址有误");
			}
			if (sendto != null)
				NetThreadHelper.newInstance().sendUdpData(sendMsg.getProtocolString() + "\0",	sendto, IpMessageConst.PORT);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}

}
