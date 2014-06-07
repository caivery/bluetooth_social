package edu.minggo.chat.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;

public class InsertUserHealper {
	 /**
     * �ǳ�
     */
    private static String[] nicks = {"����","����","��ɽ","����","ŷ����","����","����","���","���","ܽ�ؽ��","������","ӣľ����","������","������","÷����"};
   
	public static void insertData(Context context){
		Uri uri = UserTableMetaData.CONTENT_URI;
		for(int i = 0 ; i<nicks.length; i++){
			ContentValues values = new ContentValues();
			values.put(UserTableMetaData.USER_NAME, nicks[i]);
			values.put(UserTableMetaData.USER_MOTTO, "���������õģ�"+i);
			values.put(UserTableMetaData.USER_PERSONKIND, "friend");
			uri = context.getContentResolver().insert(UserTableMetaData.CONTENT_URI, values);
			System.out.println("uri----->"+uri.toString() );
		}
		Toast.makeText(context, "�ɹ�ע��1-�û�", Toast.LENGTH_LONG).show();
	}

}
