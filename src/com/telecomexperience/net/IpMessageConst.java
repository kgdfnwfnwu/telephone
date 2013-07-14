package com.telecomexperience.net;

/**
 * 飞鸽协议常量
 * @author ccf
 * 2012/2/10
 */
public class IpMessageConst {
	public static final int VERSION = 0x001;		// 版本号
	public static final int PORT = 0x0971;			// 端口号，飞鸽协议默认端口2425
	
	// 命令
	public static final int IPMSG_NOOPERATION		 = 0x00000000;	//不进行任何操作
	public static final int IPMSG_BR_ENTRY			 = 0x00000001;	//用户上线
	public static final int IPMSG_BR_EXIT		 	 = 0x00000002;	//用户退出
	public static final int IPMSG_ANSENTRY			 = 0x00000003;	//通报在线
	public static final int IPMSG_BR_ABSENCE		 = 0x00000004;	//改为缺席模式
	
	public static final int IPMSG_BR_SERVERENTRY			 = 0x00000005;	//服务端用户上线
	public static final int IPMSG_BR_SERVEREXIT		 	 	 = 0x00000006;	//服务端用户退出
	public static final int IPMSG_SERVERANSENTRY			 = 0x00000007;	//服务端通报在线
	
	public static final int IPMSG_BR_ISGETLIST		 = 0x00000010;	//寻找有效的可以发送用户列表的成员
	public static final int IPMSG_OKGETLIST			 = 0x00000011;	//通知用户列表已经获得
	public static final int IPMSG_GETLIST			 = 0x00000012;	//用户列表发送请求
	public static final int IPMSG_ANSLIST			 = 0x00000013;	//应答用户列表发送请求
	
	public static final int IPMSG_SENDPUSHMSG 		 = 0x00000014;	//推送消息
	public static final int IPMSG_SENDSHAKEMSG 		 = 0x00000015;	//摇一摇骰子消息
	
	public static final int IPMSG_SENDPUSHFEEDBACKMSG 		 = 0x00000016;	//推送消息
	public static final int IPMSG_SENDSHAKEFEEDBACKMSG 		 = 0x00000017;	//摇一摇骰子消息
	public static final int IPMSG_SENDSWITCHSHAKEMSG 		 = 0x00000018;	//摇一摇骰子消息
	
	public static final int IPMSG_SENDMSG 			 = 0x00000020;	//发送消息
	public static final int IPMSG_RECVMSG 			 = 0x00000021;	//通报收到消息
	public static final int IPMSG_READMSG 			 = 0x00000030;	//消息打开通知
	public static final int IPMSG_DELMSG 			 = 0x00000031;	//消息丢弃通知
	public static final int IPMSG_ANSREADMSG		 = 0x00000032;	//消息打开确认通知（version-8中添加）
	
	public static final int IPMSG_GETINFO			 = 0x00000040;	//获得IPMSG版本信息
	public static final int IPMSG_SENDINFO			 = 0x00000041;	//发送IPMSG版本信息
	
	public static final int IPMSG_GETABSENCEINFO	 = 0x00000050;	//获得缺席信息
	public static final int IPMSG_SENDABSENCEINFO	 = 0x00000051;	//发送缺席信息
	
	public static final int IPMSG_GETFILEDATA		 = 0x00000060;	//文件传输请求
	public static final int IPMSG_RELEASEFILES		 = 0x00000061;	//丢弃附加文件
	public static final int IPMSG_GETDIRFILES		 = 0x00000062;	//附着统计文件请求
	
	
	/* option for all command */
	public static final int IPMSG_ABSENCEOPT 		 = 0x00000100;	//缺席模式
	public static final int IPMSG_SERVEROPT 		 = 0x00000200;	//服务器（保留）
	public static final int IPMSG_DIALUPOPT 		 = 0x00010000;	//发送给个人
	public static final int IPMSG_FILEATTACHOPT 	 = 0x00200000;	//附加文件
	public static final int IPMSG_ENCRYPTOPT		 = 0x00400000;	//加密
	
	/*  option for send command  */
	public static final int IPMSG_SENDCHECKOPT = 0x00000100;	//传送验证
	public static final int IPMSG_SECRETOPT = 0x00000200;		//密封的消息
	public static final int IPMSG_BROADCASTOPT = 0x00000400;	//广播
	public static final int IPMSG_MULTICASTOPT = 0x00000800;	//多播
	public static final int IPMSG_NOPOPUPOPT = 0x00001000;		//（不再有效）
	public static final int IPMSG_AUTORETOPT = 0x00002000;		//自动应答(Ping-pong protection)
	public static final int IPMSG_RETRYOPT = 0x00004000;		//重发标识（用于请求用户列表时）
	public static final int IPMSG_PASSWORDOPT = 0x00008000;		//密码
	public static final int IPMSG_NOLOGOPT = 0x00020000;		//没有日志文件
	public static final int IPMSG_NEWMUTIOPT = 0x00040000;		//新版本的多播（保留）
	public static final int IPMSG_NOADDLISTOPT = 0x00080000;	//不添加用户列表 Notice to the members outside of BR_ENTRY
	public static final int IPMSG_READCHECKOPT = 0x00100000;	//密封消息验证（version8中添加）
	public static final int IPMSG_SECRETEXOPT = (IPMSG_READCHECKOPT|IPMSG_SECRETOPT);
	
	
	

}
