package edu.minggo.chat.model;

import java.io.Serializable;
import java.util.List;

public class WeiBoInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;	//΢������
	private String origtext;//ԭʼ����
	private String from;	//��Դ
	private String fromurl;	//��Դurl
	private String name;	//�������˻���
	private String nick;	//�������ǳ�
	private String head;	//������ͷ��url
	private int status;		//΢��״̬��0-������1-ϵͳɾ����2-����У�3-�û�ɾ����4-��ɾ��
	private int self;		//�Ƿ����ѷ��ĵ�΢����0-���ǣ�1-��
	private int type;		//΢�����ͣ�1-ԭ������2-ת�أ�3-˽�ţ�4-�ظ���5-�ջأ�6-�ἰ��7-����
	private int count;		//��ת�ش���
	private int mcount;		//��������
	private long timestamp;	//����ʱ��
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getOrigtext() {
		return origtext;
	}
	public void setOrigtext(String origtext) {
		this.origtext = origtext;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getFromurl() {
		return fromurl;
	}
	public void setFromurl(String fromurl) {
		this.fromurl = fromurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSelf() {
		return self;
	}
	public void setSelf(int self) {
		this.self = self;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getMcount() {
		return mcount;
	}
	public void setMcount(int mcount) {
		this.mcount = mcount;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}
	public WeiBoInfo getSource() {
		return source;
	}
	public void setSource(WeiBoInfo source) {
		this.source = source;
	}
	private List<String> image;//ͼƬ
	private WeiBoInfo source;
	@Override
	public String toString() {
		return "WeiBodinfo [text=" + text + ", origtext=" + origtext
				+ ", from=" + from + ", fromurl=" + fromurl + ", name=" + name
				+ ", nick=" + nick + ", head=" + head + ", status=" + status
				+ ", self=" + self + ", type=" + type + ", count=" + count
				+ ", mcount=" + mcount + ", timestamp=" + timestamp
				+ ", image=" + image + ", source=" + source + "]";
	}
	  
	
}
