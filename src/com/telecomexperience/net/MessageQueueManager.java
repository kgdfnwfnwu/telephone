package com.telecomexperience.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.State;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.util.Log;

public class MessageQueueManager {

	private static final String TAG = "MessageQueueManager";
	private BlockingQueue<MessageData> mUserList = new ArrayBlockingQueue<MessageData>(
			50);
	private GetUserTask mTask = new GetUserTask();
	
	public DatagramPacket udpSendPacket ;
	public DatagramSocket udpSocket;
	private MessageQueueManager() {
	}

	private static MessageQueueManager instance = new MessageQueueManager();

	public static MessageQueueManager getInstance() {
		return instance;
	}

	public void send(String sendStr, InetAddress sendto, int sendPort) {
		MessageData messageData = new MessageData(sendStr, sendto, sendPort);
		doGetUser(messageData);
	}

	private void putMessageData(MessageData data) throws InterruptedException {
		mUserList.put(data);
	}

	private void doGetUser(MessageData data) {
		if (data != null) {
			try {
				putMessageData(data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		State state = mTask.getState();
		try {
			if (Thread.State.NEW == state) {
				mTask.start();
			} else if (Thread.State.TERMINATED == state) {
				mTask = new GetUserTask();
				mTask.start();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private class GetUserTask extends Thread {
		private volatile boolean mTaskTerminated = false;
		private static final int TIMEOUT = 5;
		long fid;
		MessageData data = null;
		private byte[] sendBuffer = null;

		public void run() {
			while (!mTaskTerminated) {
				try {
					data = mUserList.poll();
					if(data!=null){
						try {
							sendBuffer = data.sendStr.getBytes("gbk");
							// 构造发送的UDP数据包
							udpSendPacket = new DatagramPacket(sendBuffer, sendBuffer.length,  data.sendto,  data.sendPort);
							udpSocket.send(udpSendPacket);	//发送udp数据包
							Log.i(TAG, "成功向IP为" +  data.sendto.getHostAddress() + "发送UDP数据：" +  data.sendStr);
							udpSendPacket = null;
							
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.e(TAG, "sendUdpData(String sendStr, int port)....系统不支持GBK编码");
						} catch (IOException e) {	//发送UDP数据包出错
							// TODO Auto-generated catch block
							e.printStackTrace();
							udpSendPacket = null;
							Log.e(TAG, "sendUdpData(String sendStr, int port)....发送UDP数据包失败");
						}
					}
					
				} catch (RuntimeException e) {
					e.printStackTrace();
				} finally {
					if (mUserList.size() <= 0) {
						mTaskTerminated = true;
					}
				}
			}
		}
	}

}

class MessageData {
	String sendStr;
	InetAddress sendto;
	int sendPort;

	public MessageData(String sendStr, InetAddress sendto, int sendPortk) {
		this.sendStr = sendStr;
		this.sendto = sendto;
		this.sendPort = sendPortk;
	}
}