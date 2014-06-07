package edu.minggo.chat.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class MyProviderMetaData {
	public static final String AUTHORITY = "edu.minggo.chat.database.MyContentProvider";
	//���ݿ������minggoProvider.db
	public static final String DATABASE_NAME = "minggoChat.db";
	//���ݿ�İ汾
	public static final int DATATBASE_VERSION = 1;
	//���ݿ��е�һ������ 
	public static final String USER_TABLE_NAME = "users";
	/**
	 * ʵ����BaseColumns����ӿں���Ѿ���_ID��������ˣ��Ͳ���Ҫ
	 * �Լ�����һ��_ID�����ԣ��������Ҫ�и����ԡ�
	 * @author Administrator
	 *
	 */
	public static final class UserTableMetaData implements BaseColumns{
		//����
		public static final String TABLE_NAME = "users";
		//����contentprovider��URI
		public static final Uri CONTENT_URI = 	Uri.parse("content://"+AUTHORITY+"/users");
		//��contentprovider�ķ��ʷ��ص�������ͣ����
		public static final String CONTENT_TYE = "vnd.android.cursor.dir/vnd.myprovider.user";
		//��contentprovider�ķ��ʷ��ص�������ͣ�һ�
		public static final String CONTENT_TYP_ITEM = "vnd.android.cursor.item/vnd.mprovider.user";
		//�����Ե�����
		public static final String USER_LOGINNAME = "loginname";
		public static final String USER_NAME = "name";
		public static final String USER_PASSWORD = "password";
		public static final String USER_TELEPHONE = "phone";
		public static final String USER_ICON = "icon";
		public static final String USER_EMAIL = "email";
		public static final String USER_SEX = "sex";
		public static final String USER_PROVINCE = "province";
		public static final String USER_AGE = "age";
		public static final String USER_HOBBY = "hobby";
		//����
		public static final String USER_MOTTO = "motto";
		public static final String USER_INTRODUCE = "introduce";
		public static final String USER_PERSONKIND = "personkind";
		
		//���õ�id�����ķ�ʽ����
		public static final String DEFAULT_SORT_ORDER = "_id desc";
	}
	
}
