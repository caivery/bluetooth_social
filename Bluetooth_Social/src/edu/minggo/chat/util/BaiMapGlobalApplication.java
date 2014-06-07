package edu.minggo.chat.util;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
/**
 * �ٶȵ�ͼ 
 * �����ַ��http://dev.baidu.com/wiki/static/imap/key/
 * @author minggo
 * @created 2013-2-23����04:24:38
 */
public class BaiMapGlobalApplication extends Application {
	
	public static BaiMapGlobalApplication mDemoApp;
	
	public BMapManager mBMapMan = null;//�ٶ�MapAPI�Ĺ�����
	public String mStrKey = "CC785368D20134344C5863CF259A6939E9F60314";
	boolean m_bKeyRight = true;	// ��ȨKey��ȷ����֤ͨ��
	
	public static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Log.d("MyGeneralListener", "onGetNetworkState error is "+ iError);
			Toast.makeText(BaiMapGlobalApplication.mDemoApp.getApplicationContext(), "���������������",Toast.LENGTH_LONG).show();
		}
		@Override
		public void onGetPermissionState(int iError) {
			Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				Toast.makeText(BaiMapGlobalApplication.mDemoApp.getApplicationContext(), 
						"����BaiMapApiUtil�ļ�������ȷ����ȨKey��",Toast.LENGTH_LONG).show();
				BaiMapGlobalApplication.mDemoApp.m_bKeyRight = false;
			}
		}
		
	}
	@Override
    public void onCreate() {
		mDemoApp = this;
		mBMapMan = new BMapManager(BaiMapGlobalApplication.this);
		mBMapMan.init(this.mStrKey, new MyGeneralListener());
		super.onCreate();
	}
	@Override
	public void onTerminate() {
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}
}
