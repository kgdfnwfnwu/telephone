package com.telecomexperience.net;

/**
 * �ɸ�Э�鳣��
 * @author ccf
 * 2012/2/10
 */
public class IpMessageConst {
	public static final int VERSION = 0x001;		// �汾��
	public static final int PORT = 0x0971;			// �˿ںţ��ɸ�Э��Ĭ�϶˿�2425
	
	// ����
	public static final int IPMSG_NOOPERATION		 = 0x00000000;	//�������κβ���
	public static final int IPMSG_BR_ENTRY			 = 0x00000001;	//�û�����
	public static final int IPMSG_BR_EXIT		 	 = 0x00000002;	//�û��˳�
	public static final int IPMSG_ANSENTRY			 = 0x00000003;	//ͨ������
	public static final int IPMSG_BR_ABSENCE		 = 0x00000004;	//��Ϊȱϯģʽ
	
	public static final int IPMSG_BR_SERVERENTRY			 = 0x00000005;	//������û�����
	public static final int IPMSG_BR_SERVEREXIT		 	 	 = 0x00000006;	//������û��˳�
	public static final int IPMSG_SERVERANSENTRY			 = 0x00000007;	//�����ͨ������
	
	public static final int IPMSG_BR_ISGETLIST		 = 0x00000010;	//Ѱ����Ч�Ŀ��Է����û��б�ĳ�Ա
	public static final int IPMSG_OKGETLIST			 = 0x00000011;	//֪ͨ�û��б��Ѿ����
	public static final int IPMSG_GETLIST			 = 0x00000012;	//�û��б�������
	public static final int IPMSG_ANSLIST			 = 0x00000013;	//Ӧ���û��б�������
	
	public static final int IPMSG_SENDPUSHMSG 		 = 0x00000014;	//������Ϣ
	public static final int IPMSG_SENDSHAKEMSG 		 = 0x00000015;	//ҡһҡ������Ϣ
	
	public static final int IPMSG_SENDPUSHFEEDBACKMSG 		 = 0x00000016;	//������Ϣ
	public static final int IPMSG_SENDSHAKEFEEDBACKMSG 		 = 0x00000017;	//ҡһҡ������Ϣ
	public static final int IPMSG_SENDSWITCHSHAKEMSG 		 = 0x00000018;	//ҡһҡ������Ϣ
	
	public static final int IPMSG_SENDMSG 			 = 0x00000020;	//������Ϣ
	public static final int IPMSG_RECVMSG 			 = 0x00000021;	//ͨ���յ���Ϣ
	public static final int IPMSG_READMSG 			 = 0x00000030;	//��Ϣ��֪ͨ
	public static final int IPMSG_DELMSG 			 = 0x00000031;	//��Ϣ����֪ͨ
	public static final int IPMSG_ANSREADMSG		 = 0x00000032;	//��Ϣ��ȷ��֪ͨ��version-8����ӣ�
	
	public static final int IPMSG_GETINFO			 = 0x00000040;	//���IPMSG�汾��Ϣ
	public static final int IPMSG_SENDINFO			 = 0x00000041;	//����IPMSG�汾��Ϣ
	
	public static final int IPMSG_GETABSENCEINFO	 = 0x00000050;	//���ȱϯ��Ϣ
	public static final int IPMSG_SENDABSENCEINFO	 = 0x00000051;	//����ȱϯ��Ϣ
	
	public static final int IPMSG_GETFILEDATA		 = 0x00000060;	//�ļ���������
	public static final int IPMSG_RELEASEFILES		 = 0x00000061;	//���������ļ�
	public static final int IPMSG_GETDIRFILES		 = 0x00000062;	//����ͳ���ļ�����
	
	
	/* option for all command */
	public static final int IPMSG_ABSENCEOPT 		 = 0x00000100;	//ȱϯģʽ
	public static final int IPMSG_SERVEROPT 		 = 0x00000200;	//��������������
	public static final int IPMSG_DIALUPOPT 		 = 0x00010000;	//���͸�����
	public static final int IPMSG_FILEATTACHOPT 	 = 0x00200000;	//�����ļ�
	public static final int IPMSG_ENCRYPTOPT		 = 0x00400000;	//����
	
	/*  option for send command  */
	public static final int IPMSG_SENDCHECKOPT = 0x00000100;	//������֤
	public static final int IPMSG_SECRETOPT = 0x00000200;		//�ܷ����Ϣ
	public static final int IPMSG_BROADCASTOPT = 0x00000400;	//�㲥
	public static final int IPMSG_MULTICASTOPT = 0x00000800;	//�ಥ
	public static final int IPMSG_NOPOPUPOPT = 0x00001000;		//��������Ч��
	public static final int IPMSG_AUTORETOPT = 0x00002000;		//�Զ�Ӧ��(Ping-pong protection)
	public static final int IPMSG_RETRYOPT = 0x00004000;		//�ط���ʶ�����������û��б�ʱ��
	public static final int IPMSG_PASSWORDOPT = 0x00008000;		//����
	public static final int IPMSG_NOLOGOPT = 0x00020000;		//û����־�ļ�
	public static final int IPMSG_NEWMUTIOPT = 0x00040000;		//�°汾�Ķಥ��������
	public static final int IPMSG_NOADDLISTOPT = 0x00080000;	//������û��б� Notice to the members outside of BR_ENTRY
	public static final int IPMSG_READCHECKOPT = 0x00100000;	//�ܷ���Ϣ��֤��version8����ӣ�
	public static final int IPMSG_SECRETEXOPT = (IPMSG_READCHECKOPT|IPMSG_SECRETOPT);
	
	
	

}
