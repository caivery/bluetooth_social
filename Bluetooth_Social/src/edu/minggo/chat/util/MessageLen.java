package edu.minggo.chat.util;
/**
 * ������Ϣʵ��byte����ת���ַ�����eg��00000018��
 * @author minggo
 * @created 2013-2-21����06:33:41
 */
public class MessageLen {
	/**
	 * ��ȡ8λ�����γ����ַ���
	 * @param i
	 * @return
	 */
	public static String getLenght(byte[] i) {
		int len = i.length;
		int j = len/10;
		if(j==0) {
			return "0000000"+len;
		}else if(0<j&&j<10){
			return "000000"+len;
		}else if (10<=j&&j<100) {
			return "00000"+len;
		}else if (10<=j&&j<100) {
			return "00000"+len;
		}else if (100<=j&&j<1000) {
			return "0000"+len;
		}else if (1000<=j&&j<10000) {
			return "000"+len;
		}else if (10000<=j&&j<100000) {
			return "00"+len;
		}else if (100000<=j&&j<1000000) {
			return "0"+len;
		}else if (1000000<=j&&j<10000000) {
			return ""+len;
		}else{
			return "";
		}
	}
	/**
	 * ��¼��ʱ��ת���ɳ���Ϊ3���ַ�����eg:009��
	 * @param recordTime
	 * @return
	 */
	public static String getRecordTime(int recordTime){
		
		int j = recordTime/10;
		if(j==0) {
			return "00"+recordTime;
		}else if(0<j&&j<10){
			return "0"+recordTime;
		}else if (10<=j&&j<100) {
			return ""+recordTime;
		}else{
			return "";
		}
	}
}
