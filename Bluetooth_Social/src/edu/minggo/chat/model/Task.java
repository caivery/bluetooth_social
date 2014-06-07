package edu.minggo.chat.model;

import java.util.Map;

public class Task {
	private int taskID;//
	@SuppressWarnings("rawtypes")
	private Map taskParam;//
	public static final int TASK_GET_PAIR_DEVICE = 0;    //��ȡ��Ե������豸
	public static final int TASK_DETECT_DEVICE=1;        //̽�������豸
	public static final int TASK_GET_DETECT_DEVICE = 2;  //��ȡ̽�������豸
	public static final int TASK_CONECT_DEVICE = 3;      //���������豸
	public static final int TASK_TRANSFER_STRING_MESSAGE = 4;  //�����ַ�����Ϣ
	public static final int TASK_TRANSFER_PHOTO_MEASSAGE = 5;  //������ͼƬ��Ϣ
	public static final int TASK_TRANSFER_VOICE_MESSAGE = 6;   //����������Ϣ
	public static final int TASK_LOGIN_SUCCESS = 7; //��½�ɹ���ҳ��ʼ��ʾ
	public static final int TASK_CHAT_ONRESUME = 8; //ҳ�����»ص���ǰ
	public static final int TASK_SEND_MESSAGE = 9;  //������Ϣ
	public static final int TASK_INIT_SERVICE = 10; //��ʼ������ϵͳ�ķ���
	public static final int TASK_SEND_IMAGE = 11;//����ͼƬ
	public static final int TASK_SEND_SOUND = 12;//��������
	public static final int TASK_SEND_LOCATION = 19;//����λ��
	public static final int TASK_GET_USER_HOMETIMEINLINE = 13;//��ȡ�����б�
	public static final int TASK_GET_MY_INFORMATION = 14;//��ȡ�Լ���Ϣ
	public static final int TASK_MODIFY_MYINFO = 15;//��ȡ�Լ���Ϣ
	public static final int TASK_MYPHOTO_ADD = 16;//�����Ƭ�����
	public static final int TASK_GET_USER_ADDRESS = 17;//ͨѶ¼�б�
	public static final int TASK_REFREAH_GALLERY = 18;//�������
	public static final int TASK_CHESS_NEXT = 20;//�������
	
	public Task(int id, @SuppressWarnings("rawtypes") Map param) {
		this.taskID = id;
		this.taskParam = param;
	}
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	@SuppressWarnings("rawtypes")
	public Map getTaskParam() {
		return taskParam;
	}
	public void setTaskParam(@SuppressWarnings("rawtypes") Map taskParam) {
		this.taskParam = taskParam;
	}
	
}
