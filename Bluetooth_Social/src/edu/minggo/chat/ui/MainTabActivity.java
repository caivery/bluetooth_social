package edu.minggo.chat.ui;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.adapters.MainAddressAdapter;
import edu.minggo.chat.adapters.MainLansiAdapter;
import edu.minggo.chat.control.BluetoothChatInterface;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.database.DataBaseOperator;
import edu.minggo.chat.database.InsertUserHealper;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;
import edu.minggo.chat.model.Task;
import edu.minggo.chat.model.User;
import edu.minggo.chat.util.OptionAlert;
import edu.minggo.chat.util.PagingFriendList;
import edu.minggo.chat.util.SideBar;
import edu.minggo.game.icecream.DropIceCreamSurfaceView;
import edu.minggo.tencent.weibo.OAuth;
/**
 * ���ǵ�������
 * @author minggo
 * @created 2013-1-28����11:43:55
 */
public class MainTabActivity extends Activity implements BluetoothChatInterface{
	/*********************��Ϸ����Ҫ�Ĳ���*******************/
	private View icecreamv;
	private View slotteryv;
	private View caiquanv;
	private View fiveGameV;
	private View chockGameV;
	private View tencentWeibov;
	/****************������ ��Ҫ�Ĳ���************************/
	private Button exitAppbt;//�Ƴ�����ť
	private static int refresh_setting_kind=1;
	private View personInfov;
	private ImageView portraitiv;
	private TextView weiboBoundtv;
	private TextView photonumberstv; 
	private View aboutLansiv;
	private View photov;
	private View weibov;
	private View cancelHistoryv;
	private View helpv;
	/****************��������Ϣ��ʷ��Ҫ�Ĳ���*****************/
	private MainLansiAdapter lansiAdapter;
	public static int refresh_lansi_kind=1;
	public final static int REFREAH_LANSI = 0;
	private ListView lansiListView;//��Ϣ��ʷ��¼listview
	private View processBar0;
	private Button searchDelete0;
	private EditText autoEditView0;//�����������
	
	/****************ͨѶ¼����Ҫ�Ĳ��� *********************/
	private MainAddressAdapter addressAdapter;
	public static int refresh_friends_kind=1;/*1 ��������ݿ���أ�2 ��ʾ����listҳ��*/
	public View process;  //������
	public static int pagenow = 1;//�ڼ�ҳ
	public static int pagesize = 7;//ÿһҳ����
	private ListView addressList;//���Ѽ����ϢListView
	public static final int REFREAH_FRIEND = 1;//ˢ��ͨѶ¼��Ϣ
	private Button searchDelete1;//����ɾ����ť
	private EditText autoEditView;//�����������
	private SideBar indexBar;//������ĸ�˵�
	private WindowManager mWindowManager;
	private TextView mDialogText;//��ʾ�����˵�
	private Button freindAddBt ;//������Ѱ�ť
	
	/****************MainTab��Ҫ�Ĳ��� *********************/
	private final ArrayList<View> views = new ArrayList<View>();
	private View view1;/*����tab*/
	private View view2;/*ͨѶ¼tab*/
	private View view3;/*��Ϸtab*/
	private View view4;/*����tab*/
	private static int registContextMenu=1;
	private ViewPager mTabPager;	
	private View progressBar;
	private ImageView mTabImg;// ����ͼƬ
	private ImageView mTab1,mTab2,mTab3,mTab4;
	private int zero = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int one;//����ˮƽ����λ��
	private int two;
	private int three;
	@SuppressWarnings("unused")
	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;	
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate++++++++++++"+refresh_lansi_kind);
        setContentView(R.layout.main_lansi);
        
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        mTab1 = (ImageView) findViewById(R.id.img_weixin);
        mTab2 = (ImageView) findViewById(R.id.img_address);
        mTab3 = (ImageView) findViewById(R.id.img_friends);
        mTab4 = (ImageView) findViewById(R.id.img_settings);
        mTabImg = (ImageView) findViewById(R.id.img_tab_now);
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));
        
        Display currDisplay = getWindowManager().getDefaultDisplay();//��ȡ��Ļ��ǰ�ֱ���
        int displayWidth = currDisplay.getWidth();
        @SuppressWarnings("unused")
		int displayHeight = currDisplay.getHeight();
        one = displayWidth/4; //����ˮƽ����ƽ�ƴ�С
        two = one*2;
        three = one*3;
        
        //��Ҫ��ҳ��ʾ��Viewװ��������
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.main_tab_lansi, null);
        view2 = mLi.inflate(R.layout.main_tab_address, null);
        view3 = mLi.inflate(R.layout.main_tab_games, null);
        view4 = mLi.inflate(R.layout.main_tab_settings, null);
        //ÿ��ҳ���view����
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
      //���ViewPager������������
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			@Override
			public int getCount() {
				return views.size();
			}
			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				
				return views.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		 //��һ��ҳ�����lansitab�ȳ�ʼ��
        if(refresh_lansi_kind==1){
        	refresh_lansi_kind = 2;
			processBar0=view1.findViewById(R.id.progress0);
			processBar0.setVisibility(View.VISIBLE);
			initDetail(0);
			
		}else {
			//lansiAdapter.notifyDataSetChanged();
		}
		BluetoothChatService.allActivity.add(this);
		
		if(BluetoothChatService.nowuser.getPicpath()!=null&&BluetoothChatService.nowuser.getPicpath()!=null&&new File(BluetoothChatService.nowuser.getPicpath()).exists()){
			BluetoothChatService.nowuser.setPhoto( BitmapFactory.decodeFile(BluetoothChatService.nowuser.getPicpath()));
		}else{
			BluetoothChatService.nowuser.setPhoto(((BitmapDrawable)(this.getResources().getDrawable(R.drawable.xiaohei))).getBitmap());
		}
    }
    /**
     * ģ�������õ�
     * @param list
     * @return
     */
    List<User> intiFriendList(List<User> list){
    	list = new ArrayList<User>();
    	
    	for(int i=0;i<3;i++){
    		User u = new User();
    		u.setUsername("minggo"+i);
        	u.setMotto("�����Ű���"+i);
        	list.add(u);
    	}
    	return list;
    }
    /**
	 * ͷ��������
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
			
		}
	};
    
	/**
	 * ��ҳtab����
	 * @author minggo
	 * @created 2013-1-29����12:28:16
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				System.out.println("0000000000000000000");
				if(refresh_lansi_kind==1){
		        	refresh_lansi_kind = 2;
					processBar0=view1.findViewById(R.id.progress0);
					processBar0.setVisibility(View.VISIBLE);
					initDetail(0);
					
				}else{
					registContextMenu = 1;
					if(addressList!=null)
						unregisterForContextMenu(addressList);
					registerForContextMenu(lansiListView);
					//lansiAdapter.notifyDataSetChanged();
				}
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				System.out.println("11111111111111111111111");
				if(refresh_friends_kind==1){
					refresh_friends_kind = 2;
					progressBar=MainTabActivity.this.findViewById(R.id.progress1);
					progressBar.setVisibility(View.VISIBLE);  
					
					new Handler().postDelayed(new Runnable(){
						@Override
						public void run(){
							initDetail(1);
						}
					}, 1000);
					
				}else{
					registContextMenu = 2;
					unregisterForContextMenu(lansiListView);
					registerForContextMenu(addressList);
					//addressAdapter.notifyDataSetChanged();
				}
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 2:
				System.out.println("22222222222222222222");
				
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				initDetail(2);
				break;
			case 3:
				System.out.println("333333333333333333333333");
				if(refresh_setting_kind==1){
					refresh_setting_kind=2;
					initDetail(3);
				}
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //��ȡ back��
    		
        	if(menu_display){         //��� Menu�Ѿ��� ���ȹر�Menu
        		menuWindow.dismiss();
        		menu_display = false;
        		}
        	else {
        		Intent intent = new Intent();
            	intent.setClass(MainTabActivity.this,ExitActivity.class);
            	startActivity(intent);
        	}
    	}
    	
    	else if(keyCode == KeyEvent.KEYCODE_MENU){   //��ȡ Menu��			
			if(!menu_display){
				//��ȡLayoutInflaterʵ��
				inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
				//�����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
				//�÷������ص���һ��View�Ķ����ǲ����еĸ�
				layout = inflater.inflate(R.layout.main_menu, null);
				
				//��������Ҫ�����ˣ����������ҵ�layout���뵽PopupWindow���أ������ܼ�
				menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); //������������width��height
				//menuWindow.showAsDropDown(layout); //���õ���Ч��
				//menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
				//��λ�ȡ����main�еĿؼ��أ�Ҳ�ܼ�
				mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);
				
				
				//�����ÿһ��Layout���е����¼���ע��ɡ�����
				//���絥��ĳ��MenuItem��ʱ�����ı���ɫ�ı�
				//����׼����һЩ����ͼƬ������ɫ
				mCloseBtn.setOnClickListener (new View.OnClickListener() {					
					@Override
					public void onClick(View arg0) {						
						//Toast.makeText(Main.this, "�˳�", Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
			        	intent.setClass(MainTabActivity.this,ExitActivity.class);
			        	startActivity(intent);
			        	menuWindow.dismiss(); //��Ӧ����¼�֮��ر�Menu
					}
				});				
				menu_display = true;				
			}else{
				//�����ǰ�Ѿ�Ϊ��ʾ״̬������������
				menuWindow.dismiss();
				menu_display = false;
				}
			
			return false;
		}
    	return false;
    }
	
	/**
	 * ����ͨѶ¼ʱ��̬�г���ض��������
	 * @author minggo
	 * @created 2013-1-30����02:42:22
	 */
	public class MyTextWatcher1 implements TextWatcher{
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			 if(s.length()!=0){
				 searchDelete1.setVisibility(View.VISIBLE);
				 List<User> userlist =DataBaseOperator.quryData(MainTabActivity.this, UserTableMetaData.USER_NAME+" like ?", new String[]{"%"+s+"%"});
				 MainAddressAdapter.friendlist  = userlist;
				 addressAdapter.refresh();
				 
			 }else{
				 searchDelete1.setVisibility(View.GONE);
				 MainAddressAdapter.friendlist  = PagingFriendList.allFriend;
				 addressAdapter.refresh();
			 }
		}
		@Override
		public void afterTextChanged(Editable s) {
			
		}
		
	}
	/**
	 * ��������ʱ��̬�г���ض��������
	 * @author minggo
	 * @created 2013-1-31����02:08:57
	 */
	public class MyTextWatcher0 implements TextWatcher{
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			 MainLansiAdapter.searchFlag = true;
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			System.out.println(s+"====="+count);
			 if(s.length()!=0){
				 searchDelete0.setVisibility(View.VISIBLE);
				 List<User> userlist =DataBaseOperator.quryData(MainTabActivity.this, UserTableMetaData.USER_NAME+" like ?", new String[]{"%"+s+"%"});
				 System.out.println("userlist����===="+userlist.size());
				 MainLansiAdapter.friendlist  = userlist;
				 lansiAdapter.initHeigh();
				 lansiAdapter.notifyDataSetChanged();
				 
			 }else{
				 searchDelete0.setVisibility(View.GONE);
				 MainLansiAdapter.searchFlag = false;
				 pagenow = 1;
				 List<User> friendsList = new PagingFriendList(MainTabActivity.this)
				 .getPagingNowFriend(pagesize,pagenow);
				 MainLansiAdapter.friendlist  = friendsList;
				 lansiAdapter.initHeigh();
				 lansiAdapter.notifyDataSetChanged();
			 }
		}
		@Override
		public void afterTextChanged(Editable s) {
			
		}
		
	}
	/**
	 * ��ʼ��ÿ��tab�ؼ����¼�
	 * @param tabNo
	 */
	public void initDetail(int tabNo){
		if(tabNo==0){
			/************ ��ʼ��"����"��һ�η�ҳ��ʾ************/
			lansiListView = (ListView) view1.findViewById(R.id.main_tab_lansi_listview);
			/*ע�������Ĳ˵�(����listview��item���ֵĸ��ֲ˵�*/
			registContextMenu=1;
			if(addressList!=null)
				this.unregisterForContextMenu(addressList);
			else registerForContextMenu(lansiListView);
			
			HashMap<String, Object> param =new HashMap<String, Object>();
			param.put("pagenow", new Integer(pagenow));
			param.put("pagesize", new Integer(pagesize));
			param.put("context", MainTabActivity.this.getApplicationContext());
			Task task = new Task(Task.TASK_GET_USER_HOMETIMEINLINE,param);
			BluetoothChatService.newTask(task);
			
			autoEditView0 = (EditText) view1.findViewById(R.id.search_lansi_ev0);
			searchDelete0 = (Button) view1.findViewById(R.id.search_clear_icon);
			searchDelete0.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					autoEditView0.setText("");
				}
			});
			autoEditView0.addTextChangedListener(new MyTextWatcher0());
			
			lansiListView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(id==-1){ //����
						/********************* ������������ʱ*********************/
						MainTabActivity.this.findViewById(R.id.textView).setVisibility(View.GONE);
						MainTabActivity.this.findViewById(R.id.tail_progressBar).setVisibility(View.VISIBLE);
						pagenow++;
						HashMap<String, Object> param =new HashMap<String, Object>();
						param.put("pagenow", new Integer(pagenow));
						param.put("pagesize", new Integer(pagesize));
						param.put("context", MainTabActivity.this.getApplicationContext());
						Task task = new Task(Task.TASK_GET_USER_HOMETIMEINLINE,param);
						BluetoothChatService.newTask(task);
					}else{
						Intent it = new Intent(MainTabActivity.this,ChattingActivity.class);
						MainTabActivity.this.startActivity(it);
					}
				}
				
			});
			
		}else if(tabNo==1){
			
			/************ ��ʼ��ͨѶ¼************/
			mWindowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
			freindAddBt = (Button) view2.findViewById(R.id.maintab_head_addbt);
			addressList = (ListView) view2.findViewById(R.id.main_tab_address_listview);
			registContextMenu=2;
			/*ע�������Ĳ˵�(����listview��item���ֵĸ��ֲ˵�*/
			if(lansiListView!=null){
				unregisterForContextMenu(lansiListView);
			}else registerForContextMenu(addressList);
			
			View listHead = LayoutInflater.from(this).inflate(R.layout.main_tab_address_listitem_head, null);
			
			if(addressAdapter==null){
				addressAdapter = new MainAddressAdapter(MainTabActivity.this, PagingFriendList.allFriend);
				addressList.addHeaderView(listHead);
				addressList.setSelectionAfterHeaderView();//Ч�����ó���û��
				//addressList.setHeaderDividersEnabled(false);//Ч�����ó���Ҳû��
				addressList.setAdapter(addressAdapter);
				
			}
			indexBar = (SideBar) findViewById(R.id.sideBar); 
			
	        indexBar.setListView(addressList); //��list�뵼��bar��
	        
	        mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.main_tab_address_listposition, null);
	        mDialogText.setVisibility(View.INVISIBLE);
	       
	        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
	                WindowManager.LayoutParams.TYPE_APPLICATION,
	                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
	                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
	                PixelFormat.TRANSLUCENT);
	        mWindowManager.addView(mDialogText, lp);
	        indexBar.setTextView(mDialogText);
	        
			progressBar.setVisibility(View.GONE);
			
			freindAddBt.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					InsertUserHealper.insertData(MainTabActivity.this);
				}
			});
			autoEditView = (EditText) listHead.findViewById(R.id.search_address_ev);
			searchDelete1 = (Button)listHead.findViewById(R.id.search_clear_icon1);
			searchDelete1.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					autoEditView.setText("");
				}
			});
			autoEditView.addTextChangedListener(new MyTextWatcher1());
			addressList.setEnabled(true);
			addressList.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//��ѯ������Ϣ��ϸ
					Intent it = new Intent();
					Bundle bd = new Bundle();
					bd.putSerializable("user", (User) addressAdapter.getItem(position-1));
					it.putExtra("user", bd);
					it.putExtra("position", position-1);
					it.setClass(MainTabActivity.this, PersonalInforAcitivity.class);
					MainTabActivity.this.startActivityForResult(it, 1);
				}
				
			});
		
		}else if(tabNo==2){
			/********************��ʼ����Ϸҳ***************/
			icecreamv = view3.findViewById(R.id.game_icecream_v);
			slotteryv = view3.findViewById(R.id.game_slottery_v);
			caiquanv = view3.findViewById(R.id.game_caiquan_v);
			tencentWeibov = view3.findViewById(R.id.game_weibo_v);
			fiveGameV = view3.findViewById(R.id.game_five_v);
			chockGameV = view3.findViewById(R.id.game_chock);
			
			caiquanv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainTabActivity.this.startActivity(new Intent(MainTabActivity.this,GameStoneScissorsClothActivity.class));
				}
			});
			
			slotteryv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainTabActivity.this.startActivity(new Intent(MainTabActivity.this,GameCrazyLottery.class));
				}
			});
			
			icecreamv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainTabActivity.this.startActivity(new Intent(MainTabActivity.this,GameIceCreamActivity.class));
				}
			});
			
			fiveGameV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainTabActivity.this.startActivity(new Intent(MainTabActivity.this,GameFiveChessActivity.class));
				}
			});
			chockGameV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent it = new Intent(MainTabActivity.this, GameShakeActivity.class);
					MainTabActivity.this.startActivity(it);
				}
			});
			tencentWeibov.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainTabActivity.this,TencentWeiboActivity.class);
					MainTabActivity.this.startActivityForResult(intent, 3);
				}
			});
		}else if(tabNo==3){
			/*******************��ʼ������ҳ**********************/
			personInfov = view4.findViewById(R.id.setting_v_personalinfo);
			exitAppbt = (Button) view4.findViewById(R.id.setting_bt_exitapp);
			portraitiv = (ImageView) view4.findViewById(R.id.setting_iv_portrait);
			aboutLansiv = view4.findViewById(R.id.setting_v_about);
			photov = view4.findViewById(R.id.setting_v_photos);
			weibov = view4.findViewById(R.id.setting_v_weibo);
			photonumberstv = (TextView) view4.findViewById(R.id.setting_tv_photonums);
			weiboBoundtv = (TextView) view4.findViewById(R.id.setting_tv_bound);
			cancelHistoryv = view4.findViewById(R.id.setting_cancel_history_v);
			helpv = view4.findViewById(R.id.setting_v_help);
			
			portraitiv.setImageBitmap(BluetoothChatService.nowuser.getPhoto());
			exitAppbt.setOnClickListener(new SettingOnclickListener());
			personInfov.setOnClickListener(new SettingOnclickListener());
			aboutLansiv.setOnClickListener(new SettingOnclickListener());
			photov.setOnClickListener(new SettingOnclickListener());
			weibov.setOnClickListener(new SettingOnclickListener());
			cancelHistoryv.setOnClickListener(new SettingOnclickListener());
			helpv.setOnClickListener(new SettingOnclickListener());
			
			SharedPreferences  prefs = PreferenceManager.getDefaultSharedPreferences(this);
		    String oauth_token = prefs.getString(OAuth.OAUTH_TOKEN, "");//��prefs��ȡ��OAuth_Token�������򸳿�ֵ
		    String oauth_token_secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		    if (oauth_token!=null&&oauth_token_secret!=null&&!oauth_token_secret.equals("")&&!oauth_token.equals("")) {
				weiboBoundtv.setText("�Ѱ�");
			}
			photonumberstv.setText("��"+DataBaseOperator.getSizePhotos(MainTabActivity.this)+"��");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){//ɾ���û�
			if(DataBaseOperator.deleteData(MainTabActivity.this, data.getLongExtra("userid", -1))){
				addressAdapter.deleItem(data.getIntExtra("position", -2));
				Toast.makeText(getApplicationContext(), "�ɹ�ɾ��", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
			}
		}else if (resultCode==2) {//����ͷ��
			portraitiv.setImageBitmap((Bitmap)data.getExtras().get("bitmap"));
		}else if(resultCode==3){//���������Ƭ��
			photonumberstv.setText("��"+data.getExtras().get("size")+"��");
		}else if (resultCode==4) {//��΢��
			weiboBoundtv.setText("�Ѱ�");
		}else if (requestCode==5) {//ȡ����
			weiboBoundtv.setText("δ��");
		}
	}
	/**
	 * �������İ�ť������
	 * @author minggo
	 * @created 2013-2-3����03:49:48
	 */
	public class SettingOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v==exitAppbt){
				Intent intent = new Intent (MainTabActivity.this,ExitFromSettingsActivity.class);			
				startActivity(intent);	
			}else if(v==personInfov){
				Intent intent = new Intent (MainTabActivity.this,PersonalInfoSettingAcitivity.class);			
				startActivityForResult(intent, 2);	
			}else if(v==aboutLansiv){
				Intent intent = new Intent(MainTabActivity.this,AppAboutActivity.class);
				MainTabActivity.this.startActivity(intent);
			}else if(v==photov){
				Intent intent = new Intent(MainTabActivity.this,MyGalleryActivity.class);
				MainTabActivity.this.startActivityForResult(intent, 3);
			}else if (v==weibov) {
				Intent intent = new Intent(MainTabActivity.this,TencentWeiboBoundActivity.class);
				MainTabActivity.this.startActivityForResult(intent, 4);
			}else if (v==cancelHistoryv) {
				OptionAlert.showAlert(MainTabActivity.this, "ɾ��ȫ����Ϣ��¼",new String[]{"ȷ��ɾ��"},null, new OptionAlert.OnAlertSelectId(){
					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case 0:
							Toast.makeText(MainTabActivity.this, "ɾ���ɹ�", 2000).show();
							break;
						default:
							break;
						}
					}
				});
			}else if (v==helpv) {
				Uri uri = Uri.parse("http://user.qzone.qq.com/1053200192"); 
		    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
		    	startActivity(intent);
			}
		}
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item){
		
		AdapterContextMenuInfo lm = (AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) {
		case 1: //����Ϣ
			if(registContextMenu == 1){
				Toast.makeText(getApplicationContext(), "�ɹ�ɾ��", Toast.LENGTH_SHORT).show();
			}else if(registContextMenu==2){
				if(DataBaseOperator.deleteData(MainTabActivity.this, lm.id)){
					addressAdapter.deleItem((lm.position-1));
					Toast.makeText(getApplicationContext(), "�ɹ�ɾ��", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
		return super.onContextItemSelected(item);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo lm = (AdapterContextMenuInfo)menuInfo;
		if(registContextMenu ==1){
			if(lm.id!=0&&lm.id!=-1){
				//MainAddressAdapter.friendlist.get(lm.position-1).getUsername()
				System.out.println(lm.position);
				menu.setHeaderTitle("wwwwwwwww");
				menu.add(1, 1, 1, "ɾ����ϵ��");
			}
		}else if(registContextMenu==2){
			if(lm.id!=0&&lm.id!=-1){
				System.out.println(lm.position+"+++++"+lm.id);
				menu.setHeaderTitle(MainAddressAdapter.username[lm.position-1].substring(0,MainAddressAdapter.username[lm.position-1].lastIndexOf(".")));
				menu.add(1, 1, 1, "ɾ����ϵ��");
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... param) {
		switch(((Integer)(param[0])).intValue()){
		case REFREAH_LANSI: //��������
			if(pagenow==1){
				if(lansiAdapter==null){
					lansiAdapter = new MainLansiAdapter(MainTabActivity.this, (List<User>)param[1],lansiListView);
			        lansiListView.setAdapter(lansiAdapter);
				}
				lansiAdapter.notifyDataSetChanged();
			}else{
				((MainLansiAdapter)lansiListView.getAdapter()).addMoreData((List<User>)param[1]);
			}
			processBar0.setVisibility(View.GONE);
			break;
	    case REFREAH_FRIEND://���������б�
			
		}
	}
	
	//���ñ������Ҳఴť������
	public void btnmainright(View v) {  
		Intent intent = new Intent (MainTabActivity.this,MainTopRightDialog.class);			
		startActivity(intent);	
		//Toast.makeText(getApplicationContext(), "����˹��ܰ�ť", Toast.LENGTH_LONG).show();
      }  	
	
	@Override
	public void init() {
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	
}
    
    

