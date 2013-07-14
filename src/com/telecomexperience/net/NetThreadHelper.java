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
 * �ɸ������ͨ�Ÿ�����
 * ʵ��UDPͨ���Լ�UDP�˿ڼ���
 * �˿ڼ������ö��̷߳�ʽ
 * 
 * ����ģʽ
 * @author ccf
 * 
 * V1.0 2012/2/14����į�����˽ڰ汾���ٺ�
 *
 */

public class NetThreadHelper implements Runnable{
	public static final String TAG = "NetThreadHelper";
	
	private static NetThreadHelper instance;
	
	private static final int BUFFERLENGTH = 1024; //�����С
	private boolean onWork = false;	//�̹߳�����ʶ
	private String selfName;
	private String selfGroup;
	
	private Thread udpThread = null;	//����UDP�����߳�
	private DatagramSocket udpSocket = null;	//���ڽ��պͷ���udp���ݵ�socket
	private DatagramPacket udpSendPacket = null;	//���ڷ��͵�udp���ݰ�
	private DatagramPacket udpResPacket = null;	//���ڽ��յ�udp���ݰ�
	private byte[] resBuffer = new byte[BUFFERLENGTH];	//�������ݵĻ���
	private byte[] sendBuffer = null;
	
	private Map<String,User> users;	//��ǰ�����û��ļ��ϣ���IPΪKEY
	private int userCount = 0; //�û�������ע�⣬����ֵֻ���ڵ���getSimpleExpandableListAdapter()�Ż���£�Ŀ������adapter���û���������һ��
	
	private Queue<ChatMessage> receiveMsgQueue;	//��Ϣ����,��û�����촰��ʱ�����յ���Ϣ�ŵ����������
	
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
		return PreferenceManager.getDefaultSharedPreferences(ServerApplication.mContext).getString("name", "��������ϵͳ");
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
		boolean bool = connectSocket(); // ��ʼ��������
		if (bool) {
			noticeOnline(); // �㲥����
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
	 * �������߹㲥
	 */
	public void noticeOnline(){	// �������߹㲥
		IpMessageProtocol ipmsgSend = new IpMessageProtocol();
		ipmsgSend.setVersion(String.valueOf(IpMessageConst.VERSION));
		ipmsgSend.setSenderName(selfName);
		ipmsgSend.setSenderHost(selfGroup);
		ipmsgSend.setCommandNo(IpMessageConst.IPMSG_BR_SERVERENTRY);	//��������
		ipmsgSend.setAdditionalSection(selfName + "\0" );	//������Ϣ������û����ͷ�����Ϣ
		
		InetAddress broadcastAddr;
		try {
			broadcastAddr = InetAddress.getByName("255.255.255.255");	//�㲥��ַ
			sendUdpData(ipmsgSend.getProtocolString()+"\0", broadcastAddr, IpMessageConst.PORT);	//��������
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "noticeOnline()....�㲥��ַ����");
		}
		
	}
	
	/**
	 * �������߹㲥
	 */
	private void noticeOffline(){	//�������߹㲥
		IpMessageProtocol ipmsgSend = new IpMessageProtocol();
		ipmsgSend.setVersion(String.valueOf(IpMessageConst.VERSION));
		ipmsgSend.setSenderName(selfName);
		ipmsgSend.setSenderHost(selfGroup);
		ipmsgSend.setCommandNo(IpMessageConst.IPMSG_BR_EXIT);	//��������
		ipmsgSend.setAdditionalSection(selfName + "\0" + selfGroup);	//������Ϣ������û����ͷ�����Ϣ
		
		InetAddress broadcastAddr;
		try {
			broadcastAddr = InetAddress.getByName("255.255.255.255");	//�㲥��ַ
			sendUdpData(ipmsgSend.getProtocolString() + "\0", broadcastAddr, IpMessageConst.PORT);	//��������
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "noticeOnline()....�㲥��ַ����");
		}

	}
	
	/**
	 * ֪ͨ����
	 */
	public void refreshUsers(){	//ˢ�������û�
		users.clear();	//��������û��б�
		noticeOnline(); //��������֪ͨ
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
					Log.e(TAG, "UDP���ݰ�����ʧ�ܣ��߳�ֹͣ");
					break;
				} 
				
				if(udpResPacket.getLength() == 0){
					Log.i(TAG, "�޷�����UDP���ݻ��߽��յ���UDP����Ϊ��");
					continue;
				}
				String ipmsgStr = "";
				try {
					ipmsgStr = new String(resBuffer, 0, udpResPacket.getLength(),"gbk");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(TAG, "��������ʱ��ϵͳ��֧��GBK����");
				}//��ȡ�յ�������
				Log.i(TAG, "���յ���UDP��������Ϊ:" + ipmsgStr);
				IpMessageProtocol ipmsgPro = new IpMessageProtocol(ipmsgStr);	//
				int commandNo = ipmsgPro.getCommandNo();
				int commandNo2 = 0x000000FF & commandNo;	//��ȡ������
				String senderIp = udpResPacket.getAddress().getHostAddress();	//�õ�������IP
				String senderName = ipmsgPro.getSenderName();	//�õ������ߵ�����
				switch(commandNo2){
				case IpMessageConst.IPMSG_BR_ENTRY:	{	//�յ��ͻ����������ݰ�������û���������IPMSG_ANSENTRYӦ��
					addUser(ipmsgPro);	//����û�
					
					/**
					 *	MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_BR_ENTRY); 
					 */
					
					//���湹����ͱ�������
					IpMessageProtocol ipmsgSend = new IpMessageProtocol();
					ipmsgSend.setVersion(String.valueOf(IpMessageConst.VERSION));
					ipmsgSend.setSenderName(selfName);
					ipmsgSend.setSenderHost(selfGroup);
					ipmsgSend.setCommandNo(IpMessageConst.IPMSG_ANSENTRY);	//���ͱ�������
					ipmsgSend.setAdditionalSection(selfName + "\0" );	//������Ϣ������û����ͷ�����Ϣ
					
					sendUdpData(ipmsgSend.getProtocolString(), udpResPacket.getAddress(), udpResPacket.getPort());	//��������
				}	
					break;
				
				case IpMessageConst.IPMSG_ANSENTRY:	{	//�յ�����Ӧ�𣬸��������û��б�
					addUser(ipmsgPro);
					
	//				MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_ANSENTRY);
				}	
					break;
				
				case IpMessageConst.IPMSG_BR_EXIT:{	//�յ����߹㲥��ɾ��users�ж�Ӧ��ֵ
					String userIp = udpResPacket.getAddress().getHostAddress();
					users.remove(userIp);
	//				MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_BR_EXIT);
					SendBroadcastMsg.getInstance().sendBroadStatusMsg();
					Log.i(TAG, "�������߱��ĳɹ�ɾ��ipΪ" + userIp + "���û�");
				}	
					break;
				
				case IpMessageConst.IPMSG_SENDMSG:{ //�յ���Ϣ������
					
					String additionStr = ipmsgPro.getAdditionalSection();	//�õ�������Ϣ
					Date time = new Date();	//�յ���Ϣ��ʱ��
					String msgTemp;		//ֱ���յ�����Ϣ�����ݼ���ѡ���ж��Ƿ��Ǽ�����Ϣ
					String msgStr;		//���ܺ����Ϣ����
					
					//����������ĸ����ֶε��ж�
					
					//���������ִ�����֤ѡ���������յ���Ϣ����
					if( (commandNo & IpMessageConst.IPMSG_SENDCHECKOPT) == IpMessageConst.IPMSG_SENDCHECKOPT){
						//����ͨ���յ���Ϣ����
						IpMessageProtocol ipmsgSend = new IpMessageProtocol();
						ipmsgSend.setVersion("" +IpMessageConst.VERSION);	//ͨ���յ���Ϣ������
						ipmsgSend.setCommandNo(IpMessageConst.IPMSG_RECVMSG);
						ipmsgSend.setSenderName(selfName);
						ipmsgSend.setSenderHost(selfGroup);
						ipmsgSend.setAdditionalSection(ipmsgPro.getPacketNo() + "\0");	//������Ϣ����ȷ���յ��İ��ı��
						
						sendUdpData(ipmsgSend.getProtocolString(), udpResPacket.getAddress(), udpResPacket.getPort());	//��������
					}
					
					String[] splitStr = additionStr.split("\0"); //ʹ��"\0"�ָ���и����ļ���Ϣ�����ָ����
					msgTemp = splitStr[0]; //����Ϣ����ȡ��		
					
					//�Ƿ��м���ѡ���ȱ
					msgStr = msgTemp;
					
					// ��ֻ�Ƿ�����Ϣ��������Ϣ
					ChatMessage msg = new ChatMessage(senderIp, senderName, msgStr, time);
					
	//				if(!receiveMsg(msg)){	//û�����촰�ڶ�Ӧ��activity
						receiveMsgQueue.add(msg);	// ��ӵ���Ϣ����
	//					MyFeiGeBaseActivity.playMsg();		��������
						//֮�������ЩUI��ʾ�Ĵ�����sendMessage()�����У���ȱ
	//					MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_SENDMSG);	//����������UI
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
				case IpMessageConst.IPMSG_RELEASEFILES:{ //�ܾ������ļ�
	//				MyFeiGeBaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_RELEASEFILES);
				}
					break;
					
				}	//end of switch
				
				if(udpResPacket != null){	//ÿ�ν�����UDP���ݺ����ó��ȡ�������ܻᵼ���´��յ����ݰ����ضϡ�
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
	 * �����˿�,����UDP����
	 * @return
	 */
	public boolean connectSocket(){	//�����˿ڣ�����UDP����
		boolean result = false;		
		try {
			if(udpSocket == null){
				udpSocket = new DatagramSocket(IpMessageConst.PORT);	//�󶨶˿�
				Log.i(TAG, "connectSocket()....��UDP�˿�" + IpMessageConst.PORT + "�ɹ�");
			}
			if(udpResPacket == null)
				udpResPacket = new DatagramPacket(resBuffer, BUFFERLENGTH);
			onWork = true;  //���ñ�ʶΪ�̹߳���
			startThread();	//�����߳̽���udp����
			result = true;
		} catch (SocketException e) {
			e.printStackTrace();
			disconnectSocket();
			Log.e(TAG, "connectSocket()....��UDP�˿�" + IpMessageConst.PORT + "ʧ��");
		}		
		return result;
	}
	
	/**
	 * ֹͣ����UDP����
	 */
	private void disconnectSocket(){	// ֹͣ����UDP����
		onWork = false;	// �����߳����б�ʶΪ������
		stopThread();
	}
	

	/**
	 * ֹͣ�߳�
	 */
	private void stopThread() {	//ֹͣ�߳�
		if(udpThread != null){
			udpThread.interrupt();	//���̶߳��������ж�
		}
		Log.i(TAG, "ֹͣ����UDP����");
	}

	/**
	 * �����߳�
	 */
	private void startThread() {	//�����߳�
		if(udpThread == null){
			udpThread = new Thread(this);
			udpThread.start();
			Log.i(TAG, "���ڼ���UDP����");
		}
	}
	
	/**
	 * �������ݰ�
	 * @param sendStr
	 * @param sendto
	 * @param sendPort
	 */
	public synchronized void sendUdpData(String sendStr, InetAddress sendto, int sendPort){	//����UDP���ݰ��ķ���
		MessageQueueManager.getInstance().udpSocket = udpSocket;
		MessageQueueManager.getInstance().send(sendStr, sendto, sendPort);
	}
	
	/**
	 * ����û�
	 * @param ipmsgPro
	 */
	private synchronized void addUser(IpMessageProtocol ipmsgPro){ //����û���Users��Map��
		String userIp = udpResPacket.getAddress().getHostAddress();
		if(null==userIp||userIp.equals(NetWorkUtils.getLocalIpAddress())){
			return;			
		}
		User user = new User();
//		user.setUserName(ipmsgPro.getSenderName());
		user.setAlias(ipmsgPro.getSenderName());	//�����ݶ�����������
		
		String extraInfo = ipmsgPro.getAdditionalSection();
		String[] userInfo = extraInfo.split("\0");	//�Ը�����Ϣ���зָ�,�õ��û����ͷ�����
		if(userInfo.length < 1){
			user.setUserName(ipmsgPro.getSenderName());
			user.setGroupName("�Է�δ�������");
		}else if (userInfo.length == 1){
			user.setUserName(userInfo[0]);
			user.setGroupName("�Է�δ�������");
		}else{
			user.setUserName(userInfo[0]);
			user.setGroupName(userInfo[1]);
		}
		user.setIp(userIp);
		user.setHostName(ipmsgPro.getSenderHost());
		users.put(userIp, user);
		SendBroadcastMsg.getInstance().sendBroadStatusMsg();
		Log.i(TAG, "�ɹ����ipΪ" + userIp + "���û�");
	}
	
}
