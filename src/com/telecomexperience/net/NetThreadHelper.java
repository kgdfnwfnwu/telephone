package com.telecomexperience.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.telecomexperience.ServerApplication;
import com.telecomexperience.data.GlobalVar;
import com.telecomexperience.utils.NetWorkUtils;
import com.telecomexperience.utils.SendBroadcastMsg;

/**
 * 飞鸽的网络通信辅助类
 * 实现UDP通信以及UDP端口监听
 * 端口监听采用多线程方式
 * 
 * 单例模式
 * @author ccf
 * 
 * V1.0 2012/2/14，寂寞的情人节版本，嘿嘿
 *
 */

public class NetThreadHelper implements Runnable{
	public static final String TAG = "NetThreadHelper";
	
	private static NetThreadHelper instance;
	
	private static final int BUFFERLENGTH = 1024; //缓冲大小
	private boolean onWork = false;	//线程工作标识
	private String selfName;
	private String selfGroup;
	
	private Thread udpThread = null;	//接收UDP数据线程
	private DatagramSocket udpSocket = null;	//用于接收和发送udp数据的socket
	private DatagramPacket udpSendPacket = null;	//用于发送的udp数据包
	private DatagramPacket udpResPacket = null;	//用于接收的udp数据包
	private byte[] resBuffer = new byte[BUFFERLENGTH];	//接收数据的缓存
	private byte[] sendBuffer = null;
	
	private Map<String,User> users;	//当前所有用户的集合，以IP为KEY
	private int userCount = 0; //用户个数。注意，此项值只有在调用getSimpleExpandableListAdapter()才会更新，目的是与adapter中用户个数保持一致
	
	private Queue<ChatMessage> receiveMsgQueue;	//消息队列,在没有聊天窗口时将接收的消息放到这个队列中
	
	private NetThreadHelper(){
		users = new HashMap<String, User>();
		receiveMsgQueue = new ConcurrentLinkedQueue<ChatMessage>();
		
		selfName = getSelfName();
		selfGroup = "Android";
		sendHeart();
		
	}
	
	public void setSelfName(String selfName) {
		this.selfName = selfName;
	}

	public String getSelfName(){
		return PreferenceManager.getDefaultSharedPreferences(ServerApplication.mContext).getString("name", "电信体验系统");
	}
	
	
	public static NetThreadHelper newInstance(){
		if(instance == null)
			instance = new NetThreadHelper();
		
		return instance;
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			sendHeart();
			super.handleMessage(msg);
		}
	};
	
	public void sendHeart(){
		boolean bool = connectSocket(); // 开始监听数据
		if (bool) {
			noticeOnline(); // 广播上线
			handler.sendEmptyMessageDelayed(0, 1000*3);
		}
		
	}
	
	public Map<String, User> getUsers(){
		return users;
	}
	
	public int getUserCount(){
		return userCount;
	}
	
	public Queue<ChatMessage> getReceiveMsgQueue(){
		return receiveMsgQueue;
	}
	
	/**
	 * 发送上线广播
	 */
	public void noticeOnline(){	// 发送上线广播
		IpMessageProtocol ipmsgSend = new IpMessageProtocol();
		ipmsgSend.setVersion(String.valueOf(IpMessageConst.VERSION));
		ipmsgSend.setSenderName(selfName);
		ipmsgSend.setSenderHost(selfGroup);
		ipmsgSend.setCommandNo(IpMessageConst.IPMSG_BR_SERVERENTRY);	//上线命令
		ipmsgSend.setAdditionalSection(selfName + "\0" );	//附加信息里加入用户名和分组信息
		
		InetAddress broadcastAddr;
		try {
			broadcastAddr = InetAddress.getByName("255.255.255.255");	//广播地址
			sendUdpData(ipmsgSend.getProtocolString()+"\0", broadcastAddr, IpMessageConst.PORT);	//发送数据
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "noticeOnline()....广播地址有误");
		}
		
	}
	
	/**
	 * 发送下线广播
	 */
	private void noticeOffline(){	//发送下线广播
		IpMessageProtocol ipmsgSend = new IpMessageProtocol();
		ipmsgSend.setVersion(String.valueOf(IpMessageConst.VERSION));
		ipmsgSend.setSenderName(selfName);
		ipmsgSend.setSenderHost(selfGroup);
		ipmsgSend.setCommandNo(IpMessageConst.IPMSG_BR_EXIT);	//下线命令
		ipmsgSend.setAdditionalSection(selfName + "\0" + selfGroup);	//附加信息里加入用户名和分组信息
		
		InetAddress broadcastAddr;
		try {
			broadcastAddr = InetAddress.getByName("255.255.255.255");	//广播地址
			sendUdpData(ipmsgSend.getProtocolString() + "\0", broadcastAddr, IpMessageConst.PORT);	//发送数据
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "noticeOnline()....广播地址有误");
		}

	}
	
	/**
	 * 通知在线
	 */
	public void refreshUsers(){	//刷新在线用户
		users.clear();	//清空在线用户列表
		noticeOnline(); //发送上线通知
		SendBroadcastMsg.getInstance().sendBroadStatusMsg();
	}
	
	@Override
	public void run() {
		try{
			while(onWork){
				try {
					if(udpSocket==null) {
						connectSocket();
						return;
					}
					udpSocket.receive(udpResPacket);
				} catch (IOException e) {
					onWork = false;
					if(udpResPacket != null){
						udpResPacket = null;
					}
					if(udpSocket != null){
						udpSocket.close();
						udpSocket = null;
					}
					
					udpThread = null;
					Log.e(TAG, "UDP数据包接收失败！线程停止");
					break;
				} 
				
				if(udpResPacket.getLength() == 0){
					Log.i(TAG, "无法接收UDP数据或者接收到的UDP数据为空");
					continue;
				}
				String ipmsgStr = "";
				try {
					ipmsgStr = new String(resBuffer, 0, udpResPacket.getLength(),"gbk");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(TAG, "接收数据时，系统不支持GBK编码");
				}//截取收到的数据
				Log.i(TAG, "接收到的UDP数据内容为:" + ipmsgStr);
				IpMessageProtocol ipmsgPro = new IpMessageProtocol(ipmsgStr);	//
				int commandNo = ipmsgPro.getCommandNo();
				int commandNo2 = 0x000000FF & commandNo;	//获取命令字
				String senderIp = udpResPacket.getAddress().getHostAddress();	//得到发送者IP
				String senderName = ipmsgPro.getSenderName();	//得到发送者的名称
				switch(commandNo2){
				case IpMessageConst.IPMSG_BR_ENTRY:	{	//收到客户端上线数据包，添加用户，并回送IPMSG_ANSENTRY应答。
					addUser(ipmsgPro);	//添加用户
					
					/**
					 *	MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_BR_ENTRY); 
					 */
					
					//下面构造回送报文内容
					IpMessageProtocol ipmsgSend = new IpMessageProtocol();
					ipmsgSend.setVersion(String.valueOf(IpMessageConst.VERSION));
					ipmsgSend.setSenderName(selfName);
					ipmsgSend.setSenderHost(selfGroup);
					ipmsgSend.setCommandNo(IpMessageConst.IPMSG_ANSENTRY);	//回送报文命令
					ipmsgSend.setAdditionalSection(selfName + "\0" );	//附加信息里加入用户名和分组信息
					
					sendUdpData(ipmsgSend.getProtocolString(), udpResPacket.getAddress(), udpResPacket.getPort());	//发送数据
				}	
					break;
				
				case IpMessageConst.IPMSG_ANSENTRY:	{	//收到上线应答，更新在线用户列表
					addUser(ipmsgPro);
					
	//				MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_ANSENTRY);
				}	
					break;
				
				case IpMessageConst.IPMSG_BR_EXIT:{	//收到下线广播，删除users中对应的值
					String userIp = udpResPacket.getAddress().getHostAddress();
					users.remove(userIp);
	//				MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_BR_EXIT);
					SendBroadcastMsg.getInstance().sendBroadStatusMsg();
					Log.i(TAG, "根据下线报文成功删除ip为" + userIp + "的用户");
				}	
					break;
				
				case IpMessageConst.IPMSG_SENDMSG:{ //收到消息，处理
					
					String additionStr = ipmsgPro.getAdditionalSection();	//得到附加信息
					Date time = new Date();	//收到信息的时间
					String msgTemp;		//直接收到的消息，根据加密选项判断是否是加密消息
					String msgStr;		//解密后的消息内容
					
					//以下是命令的附加字段的判断
					
					//若有命令字传送验证选项，则需回送收到消息报文
					if( (commandNo & IpMessageConst.IPMSG_SENDCHECKOPT) == IpMessageConst.IPMSG_SENDCHECKOPT){
						//构造通报收到消息报文
						IpMessageProtocol ipmsgSend = new IpMessageProtocol();
						ipmsgSend.setVersion("" +IpMessageConst.VERSION);	//通报收到消息命令字
						ipmsgSend.setCommandNo(IpMessageConst.IPMSG_RECVMSG);
						ipmsgSend.setSenderName(selfName);
						ipmsgSend.setSenderHost(selfGroup);
						ipmsgSend.setAdditionalSection(ipmsgPro.getPacketNo() + "\0");	//附加信息里是确认收到的包的编号
						
						sendUdpData(ipmsgSend.getProtocolString(), udpResPacket.getAddress(), udpResPacket.getPort());	//发送数据
					}
					
					String[] splitStr = additionStr.split("\0"); //使用"\0"分割，若有附加文件信息，则会分割出来
					msgTemp = splitStr[0]; //将消息部分取出		
					
					//是否有加密选项，暂缺
					msgStr = msgTemp;
					
					// 若只是发送消息，处理消息
					ChatMessage msg = new ChatMessage(senderIp, senderName, msgStr, time);
					
	//				if(!receiveMsg(msg)){	//没有聊天窗口对应的activity
						receiveMsgQueue.add(msg);	// 添加到信息队列
	//					MyFeiGeBaseActivity.playMsg();		播放声音
						//之后可以做些UI提示的处理，用sendMessage()来进行，暂缺
	//					MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_SENDMSG);	//更新主界面UI
	//				}
				}
					break;
				case IpMessageConst.IPMSG_SENDPUSHMSG:
					
					User user = new User();
					user.setIp(senderIp);
					user.setUserName(senderName);
					GlobalVar.getInstance().setUser(user);
					SendBroadcastMsg.getInstance().sendBroadPushMsg();
					break;
				case IpMessageConst.IPMSG_SENDSHAKEMSG:
					User user1 = new User();
					user1.setIp(senderIp);
					user1.setUserName(senderName);
					GlobalVar.getInstance().setUser(user1);
					SendBroadcastMsg.getInstance().sendBroadShakeMsg();
					break;
				case IpMessageConst.IPMSG_RELEASEFILES:{ //拒绝接受文件
	//				MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_RELEASEFILES);
				}
					break;
					
				}	//end of switch
				
				if(udpResPacket != null){	//每次接收完UDP数据后，重置长度。否则可能会导致下次收到数据包被截断。
					udpResPacket.setLength(BUFFERLENGTH);
				}
				
			}
			
			if(udpResPacket != null){
				udpResPacket = null;
			}
			
			if(udpSocket != null){
				udpSocket.close();
				udpSocket = null;
			}
			udpThread = null;
		}
		catch(Exception e){
			
		}
		catch(Error e){
			
		}
	}
	
	/**
	 * 监听端口,接收UDP数据
	 * @return
	 */
	public boolean connectSocket(){	//监听端口，接收UDP数据
		boolean result = false;		
		try {
			if(udpSocket == null){
				udpSocket = new DatagramSocket(IpMessageConst.PORT);	//绑定端口
				Log.i(TAG, "connectSocket()....绑定UDP端口" + IpMessageConst.PORT + "成功");
			}
			if(udpResPacket == null)
				udpResPacket = new DatagramPacket(resBuffer, BUFFERLENGTH);
			onWork = true;  //设置标识为线程工作
			startThread();	//启动线程接收udp数据
			result = true;
		} catch (SocketException e) {
			e.printStackTrace();
			disconnectSocket();
			Log.e(TAG, "connectSocket()....绑定UDP端口" + IpMessageConst.PORT + "失败");
		}		
		return result;
	}
	
	/**
	 * 停止监听UDP数据
	 */
	private void disconnectSocket(){	// 停止监听UDP数据
		onWork = false;	// 设置线程运行标识为不运行
		stopThread();
	}
	

	/**
	 * 停止线程
	 */
	private void stopThread() {	//停止线程
		if(udpThread != null){
			udpThread.interrupt();	//若线程堵塞，则中断
		}
		Log.i(TAG, "停止监听UDP数据");
	}

	/**
	 * 启动线程
	 */
	private void startThread() {	//启动线程
		if(udpThread == null){
			udpThread = new Thread(this);
			udpThread.start();
			Log.i(TAG, "正在监听UDP数据");
		}
	}
	
	/**
	 * 发送数据包
	 * @param sendStr
	 * @param sendto
	 * @param sendPort
	 */
	public synchronized void sendUdpData(String sendStr, InetAddress sendto, int sendPort){	//发送UDP数据包的方法
		MessageQueueManager.getInstance().udpSocket = udpSocket;
		MessageQueueManager.getInstance().send(sendStr, sendto, sendPort);
	}
	
	/**
	 * 添加用户
	 * @param ipmsgPro
	 */
	private synchronized void addUser(IpMessageProtocol ipmsgPro){ //添加用户到Users的Map中
		String userIp = udpResPacket.getAddress().getHostAddress();
		if(null==userIp||userIp.equals(NetWorkUtils.getLocalIpAddress())){
			return;			
		}
		User user = new User();
//		user.setUserName(ipmsgPro.getSenderName());
		user.setAlias(ipmsgPro.getSenderName());	//别名暂定发送者名称
		
		String extraInfo = ipmsgPro.getAdditionalSection();
		String[] userInfo = extraInfo.split("\0");	//对附加信息进行分割,得到用户名和分组名
		if(userInfo.length < 1){
			user.setUserName(ipmsgPro.getSenderName());
			user.setGroupName("对方未分组好友");
		}else if (userInfo.length == 1){
			user.setUserName(userInfo[0]);
			user.setGroupName("对方未分组好友");
		}else{
			user.setUserName(userInfo[0]);
			user.setGroupName(userInfo[1]);
		}
		user.setIp(userIp);
		user.setHostName(ipmsgPro.getSenderHost());
		users.put(userIp, user);
		SendBroadcastMsg.getInstance().sendBroadStatusMsg();
		Log.i(TAG, "成功添加ip为" + userIp + "的用户");
	}
	
}
