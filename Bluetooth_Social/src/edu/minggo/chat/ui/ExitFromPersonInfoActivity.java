package edu.minggo.chat.ui;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.minggo.chat.R;
/**
 * ������ϸ��Ϣ���Ƴ��˵�
 * @author minggo
 * @created 2013-2-3����03:35:57
 */
public class ExitFromPersonInfoActivity extends Activity {
	private Button remarkNamebt;
	private Button deletebt;
	private Button cancelbt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_dialog_from_personinfo);
		deletebt = (Button)findViewById(R.id.personal_bt_delete);
		cancelbt = (Button)findViewById(R.id.personal_bt_cancel);
		remarkNamebt = (Button)findViewById(R.id.personal_bt_remark);
		
		deletebt.setOnClickListener(new MenuOnclickListener());
		cancelbt.setOnClickListener(new MenuOnclickListener());
		remarkNamebt.setOnClickListener(new MenuOnclickListener());
		
	}
	/**
	 * ��ϸ��Ϣ�еİ�ť�ļ�����
	 * @author minggo
	 * @created 2013-2-3����03:37:41
	 */
	public class MenuOnclickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(deletebt==v){
				setResult(1);
				finish();
			}else if(cancelbt==v){
				finish();
			}else if(remarkNamebt==v){
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
