package edu.minggo.chat.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import edu.minggo.tencent.weibo.CommonsHttpOAuthConsumer;
import edu.minggo.tencent.weibo.CommonsHttpOAuthProvider;
import edu.minggo.tencent.weibo.Constants;
import edu.minggo.tencent.weibo.OAuthConsumer;
import edu.minggo.tencent.weibo.OAuthProvider;
import edu.minggo.tencent.weibo.OAuthRequestTokenTask;
import edu.minggo.tencent.weibo.RetrieveAccessTokenTask;
/**
 * RequestToken����ǰ��׼��
 * @author minggo
 * @created 2013-3-1����01:51:43
 */
public class PrepareRequestTokenActivity extends Activity {

	private OAuthConsumer consumer;
	private OAuthProvider provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.setProperty("debug", "true");
		consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,
				Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
		new OAuthRequestTokenTask(this, consumer, provider).execute();
	}

	//��AndroidManifest.xml�ļ�������android:launchMode="singleTask"���ã�����
	//��ִ�лص�url��Activity�ڶ�����������ʱ����ִ��onCreate��������ֱ��ִ�д˷���
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//��Ҫ���ڴ�ȡ�� �ļ�ֵ��
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		System.out.println("uri--->"+uri.toString());
		if (uri != null
				&& uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			//��ȡAccess_Token������Access_Token�ŵ�prefs��
			new RetrieveAccessTokenTask(this, consumer, provider, prefs)
					.execute(uri);
			finish();
		}
	}
}
