package edu.minggo.chat.database;

import edu.minggo.chat.model.MyPhoto;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//DatabaseHelper��Ϊһ������SQLite�������࣬�ṩ��������Ĺ��ܣ�
//��һ��getReadableDatabase(),getWritableDatabase()���Ի��SQLiteDatabse����ͨ���ö�����Զ����ݿ���в���
//�ڶ����ṩ��onCreate()��onUpgrade()�����ص����������������ڴ������������ݿ�ʱ�������Լ��Ĳ���

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;

	// ��SQLiteOepnHelper�����൱�У������иù��캯��
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		// ����ͨ��super���ø��൱�еĹ��캯��
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}
	
	
	// �ú������ڵ�һ�δ������ݿ��ʱ��ִ��,ʵ�������ڵ�һ�εõ�SQLiteDatabse�����ʱ�򣬲Ż�����������
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a Database");
		// execSQL��������ִ��SQL���
		db.execSQL("create table " + MyProviderMetaData.USER_TABLE_NAME
				+ "(" + MyProviderMetaData.UserTableMetaData._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ MyProviderMetaData.UserTableMetaData.USER_NAME
				+ " varchar(20),"
				+ MyProviderMetaData.UserTableMetaData.USER_LOGINNAME
				+ " varchar(20),"
				+ MyProviderMetaData.UserTableMetaData.USER_PASSWORD
				+ " varchar(20),"
				+ MyProviderMetaData.UserTableMetaData.USER_AGE
				+ " varchar(5),"
				+ MyProviderMetaData.UserTableMetaData.USER_EMAIL
				+ " varchar(20),"
				+ MyProviderMetaData.UserTableMetaData.USER_HOBBY
				+ " varchar(20),"
				+ MyProviderMetaData.UserTableMetaData.USER_PROVINCE
				+ " varchar(20),"
				+ MyProviderMetaData.UserTableMetaData.USER_TELEPHONE
				+ " varchar(20),"
				+ MyProviderMetaData.UserTableMetaData.USER_ICON
				+ " varchar(50),"
				+ MyProviderMetaData.UserTableMetaData.USER_SEX
				+ " varchar(5),"
				+ MyProviderMetaData.UserTableMetaData.USER_MOTTO
				+ " varchar(50),"
				+ MyProviderMetaData.UserTableMetaData.USER_PERSONKIND
				+ " varchar(10),"
				+ MyProviderMetaData.UserTableMetaData.USER_INTRODUCE
				+ " varchar(50));");
		db.execSQL("create table "+MyPhoto.MyPhotoTable.TABLE_NAME
				+ "("+MyPhoto.MyPhotoTable._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ MyPhoto.MyPhotoTable.USER_NAME
				+ " varchar(20),"
				+ MyPhoto.MyPhotoTable.PHOTO_TIME
				+ " varchar(20),"
				+ MyPhoto.MyPhotoTable.PHOTO_PATH
				+ " varchar(100),"
				+ MyPhoto.MyPhotoTable.PHOTO_DESC
				+ " varchar(30));"
				);
	}
	/**
	 * �汾���µ�ʱ�����
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP TABLE IF EXISTS "+UserTableMetaData.TABLE_NAME);
		onCreate(db);
		System.out.println("update a Database");
	}

}
