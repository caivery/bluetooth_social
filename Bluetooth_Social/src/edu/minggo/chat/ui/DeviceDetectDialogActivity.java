package edu.minggo.chat.ui;

import edu.minggo.chat.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * �����豸��̽�������������ѡ��˵��Ի���
 * @author minggo
 * @date 2013-6-6
 * @time ����10:47:09
 */
public class DeviceDetectDialogActivity extends Activity {
	private Button detectbt;
	private Button connetbt;
	private Button cancelbt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_dialog_from_chat_detect);
		detectbt = (Button)findViewById(R.id.chat_bt_detect);
		cancelbt = (Button)findViewById(R.id.chat_bt_cancel);
		connetbt = (Button)findViewById(R.id.chat_bt_connet);
		
		connetbt.setOnClickListener(new MenuOnclickListener());
		cancelbt.setOnClickListener(new MenuOnclickListener());
		detectbt.setOnClickListener(new MenuOnclickListener());
		
	}
	/**
	 * ��ϸ��Ϣ�еİ�ť�ļ�����
	 * @author minggo
	 * @created 2013-2-3����03:37:41
	 */
	public class MenuOnclickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(detectbt==v){
				setResult(1);
				finish();
			}else if(cancelbt==v){
				finish();
			}else if(connetbt==v){
				setResult(2);
				finish();
			}
		}
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
}
