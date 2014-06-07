package edu.minggo.chat.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import edu.minggo.chat.R;
/**
 * ��Ϣ��ʷ�����Ͻǰ�ť�����ĶԻ���
 * @author minggo
 * @created 2013-1-28����11:44:36
 */
public class MainTopRightDialog extends Activity {
	//private MyDialog dialog;
	private View layout;
	private LinearLayout chatStartview;
	private LinearLayout tingtongview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_top_right_dialog);
		//dialog=new MyDialog(this);
		layout=findViewById(R.id.main_dialog_layout);
		chatStartview = (LinearLayout) layout.findViewById(R.id.lansi_menu_view_chat);
		tingtongview = (LinearLayout) layout.findViewById(R.id.lansi_menu_view_tingtong);
		
		tingtongview.setOnClickListener(new MyOnclickListener());
		chatStartview.setOnClickListener(new MyOnclickListener());
		
		/*layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "��ʾ����������ⲿ�رմ��ڣ�", 
						Toast.LENGTH_SHORT).show();	
			}
		});*/
	}
    /**
     * 
     * @author minggo
     * @date 2013-6-6
     * @time ����12:45:51
     */
	public class MyOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (v.equals(chatStartview)) {
				Intent it = new Intent();
				it.setClass(MainTopRightDialog.this, ChattingActivity.class);
				MainTopRightDialog.this.startActivity(it);
				finish();
			}else if (v.equals(tingtongview)) {
				finish();
				Toast.makeText(MainTopRightDialog.this, "�л���Ͳģʽ", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	/*
	public void exitbutton1(View v) {  
    	this.finish();    	
      }  
	public void exitbutton0(View v) {  
    	this.finish();
    	MainWeixin.instance.finish();//�ر�Main ���Activity
      }  
	*/
}
