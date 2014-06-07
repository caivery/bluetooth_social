package edu.minggo.chat.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import edu.minggo.chat.adapters.LoginUserAdapter;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;
import edu.minggo.chat.model.Task;
import edu.minggo.chat.model.User;
import edu.minggo.chat.ui.ChattingActivity;
import edu.minggo.chat.ui.ExitActivity;
import edu.minggo.chat.ui.GameFiveChessActivity;
import edu.minggo.chat.ui.LoginActivity;
import edu.minggo.chat.ui.MainTabActivity;
import edu.minggo.chat.util.ClippingPicture;
import edu.minggo.chat.util.ClippingSounds;
import edu.minggo.chat.util.MessageLen;
import edu.minggo.chat.util.PagingFriendList;

public class BluetoothChatService extends Service implements Runnable {
	public static String connectDeviceAdd=null;
	public static File soundFile;
	public static Context context = null;
	public static boolean isrun = true;
	public static User serviceExist= null;
	public static User nowuser = null;
	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();
	public static ArrayList<Task> allTask = new ArrayList<Task>();
	public static int lastActivityId; // ǰ�˵�Activity�ı��
	public static HashMap<Integer, BitmapDrawable> alluserIcon = new HashMap<Integer, BitmapDrawable>();// ��Ѷͷ��

	// ����ָ����ʾ
	private static final String TAG = "BluetoothChatService";

	// �����������ڷ�������
	private static final String NAME_SECURE = "LansiServer";
	@SuppressWarnings("unused")
	private static final String NAME_INSECURE = "LansiServer";

	// �����������ڷ������Ψһuuid 00001101-0000-1000-8000-00805F9B34FB
	//private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public final static BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter(); // ����������
	private static AcceptThread mSecureAcceptThread; // ��ȫ�����������߳�
	private static AcceptThread mInsecureAcceptThread; // �ǰ�ȫ���ӵ��߳�
	private static ConnectThread mConnectThread; // �����豸�����е��߳�
	private static ConnectedThread mConnectedThread; // �����豸���Ӻ���߳�
	private static int mState ; // ������������״̬

	// ��ʾ��ǰ������״̬
	public static final int STATE_NONE = 0; // ʲôҲ������״̬
	public static final int STATE_LISTEN = 1; // ����������״̬
	public static final int STATE_CONNECTING = 2; // ��ʼ��һ����������ׯ��
	public static final int STATE_CONNECTED = 3; // �Ѿ�����һ��Զ���豸״̬
	public static final int STATE_ONRESUME = 4; // �������»ص���ǰ
	public static final int STATE_SEND_MESSAGE = 5; // ������Ϣ
	public static final int STATE_SEND_IMAGE = 11; // ������Ϣ
	public static final int STATE_CONNECT_THE_SAME = 8; // ���������ӵ��豸
	public static final int STATE_REFRESH_MAINACTIVITY = 12; // ˢ��Mainactivity
	
	//��ΪChattingActivities ��������Handler
	private static Handler mHandler;
	//��ΪGameFiveChessActivity.java ��������Handler
    @SuppressWarnings("unused")
	private static Handler gameHandler;
	/**
	 * ���õ�ǰ��״̬
	 * @param state ��ǰ���ӵ�״̬
	 */
	private synchronized static void setState(int state) {
		mState = state;
		if (mHandler!=null) {
			mHandler.obtainMessage(ChattingActivity.MESSAGE_STATE_CHANGE, state,-1).sendToTarget();
		}else{
			System.out.println("handlerΪ��");
		}
	}
	/**
	 * ���ص�ǰ������װ̬ Return the current connection state.
	 */
	public synchronized static int getState() {
		return mState;
	}
	/**
	 * �Ӽ����и������ֻ�ȡActivity����
	 * @param name
	 * @return
	 */
	public static Activity getActivitybyName(String name) {
		for (Activity ac : allActivity) {
			if (ac.getClass().getName().indexOf(name) >= 0) {
				return ac;
			}
		}
		return null;
	}

	/**
	 * ���һ������
	 * @param task
	 */
	public static void newTask(Task task) {
		allTask.add(task);
	}

	/**
	 * ִ�������ʱ��
	 */
	@Override
	public void run() {
		while (isrun == true) {
			Task lastTask = null;
			synchronized (allTask) {
				if (allTask.size() > 0) {
					lastTask = allTask.get(0);
					doTask(lastTask);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ִ������
	 * @param task
	 */
	public void doTask(Task task) {
		Message message = hand.obtainMessage();
		message.what = task.getTaskID();// ����ˢ��ui�ı仯
		try {
			switch (task.getTaskID()) {
			// TASK_DETECT_DEVICE������final
			case Task.TASK_REFREAH_GALLERY:
				message.obj = Task.TASK_REFREAH_GALLERY;
				break;
			case Task.TASK_GET_MY_INFORMATION:
				message.obj = (Context)(task.getTaskParam().get("context"));
				break;
			case Task.TASK_GET_USER_HOMETIMEINLINE:
				PagingFriendList paging0 = new PagingFriendList((Context)(task.getTaskParam().get("context")));
				List<User> friendList0 = paging0.getPagingNowFriend((Integer)task.getTaskParam().get("pagesize")
						,(Integer)(task.getTaskParam().get("pagenow")));
				message.obj = friendList0;
				break;
			case Task.TASK_GET_USER_ADDRESS:
				message.obj = PagingFriendList.allFriend;
				break;
			case Task.TASK_LOGIN_SUCCESS:
				nowuser = (User)task.getTaskParam().get("loginUser");
				message.obj = nowuser;
				break;
			case Task.TASK_SEND_MESSAGE://�������ֻ����
				write((byte[]) task.getTaskParam().get(BluetoothChatService.STATE_SEND_MESSAGE));
				break;
			case Task.TASK_SEND_IMAGE: //��ͼƬ
				File image = new File((String)task.getTaskParam().get("talkPicPath"));
				write(image);
				break;
			case Task.TASK_SEND_SOUND://������
				write((String)task.getTaskParam().get("talkSoundPath"),(Integer)task.getTaskParam().get("recordTime"));
				break;
			case Task.TASK_SEND_LOCATION://��λ��
				write((String)task.getTaskParam().get("location"));
				break;
			case Task.TASK_CHESS_NEXT:
				write((String)task.getTaskParam().get("chess_location"),true);
				break;
			case Task.TASK_CONECT_DEVICE: //����Զ���豸
				System.out.println("�����豸��"+((BluetoothDevice) task.getTaskParam().get("device")).toString()+(Boolean) task.getTaskParam().get("secure"));
				mHandler = (Handler)task.getTaskParam().get("mHandler");
				connect((BluetoothDevice) task.getTaskParam().get("device"),(Boolean) task.getTaskParam().get("secure"));
				break;
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.what = -100;
		}
		hand.sendMessage(message);
		allTask.remove(task);// ִ��������
	}
	public static BluetoothChatInterface bc1 = null;
	/**
	 * ����ui
	 */
	public static Handler hand = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			System.out.println("���Handler��ִ��");
			switch (msg.what) {
			case Task.TASK_REFREAH_GALLERY:
				bc1 = (BluetoothChatInterface) BluetoothChatService.getActivitybyName("MyGalleryActivity");
				bc1.refresh(Task.TASK_REFREAH_GALLERY);
				break;
			case Task.TASK_GET_USER_HOMETIMEINLINE: //���������б�
				bc1 = (BluetoothChatInterface) BluetoothChatService.getActivitybyName("MainTabActivity");
				bc1.refresh(MainTabActivity.REFREAH_LANSI,msg.obj);
				break;
			case Task.TASK_GET_USER_ADDRESS:
				bc1 = (BluetoothChatInterface) BluetoothChatService.getActivitybyName("MainTabActivity");
				bc1.refresh(MainTabActivity.REFREAH_FRIEND,msg.obj);
				break;
			case Task.TASK_LOGIN_SUCCESS: //��½�ɹ�
				// ����ui�еĵ�¼��
				bc1 = (BluetoothChatInterface)BluetoothChatService.getActivitybyName("LoginActivity");
				bc1.refresh(LoginActivity.REFRESH_LOGIN, msg.obj);
				break;
			}
		}

	};

	/**
	 * �˳�����Activity
	 * 
	 * @param context
	 */
	public static void exitApplication(Context context) {
		for (Activity activity : allActivity) {
			activity.finish();
		}
		System.exit(0);
	}
	
	/**
	 * �˳�����
	 * @param context
	 */
	public static void promptExit(final Context context) {
		Intent it  = new Intent(context,ExitActivity.class);
		context.startActivity(it);
	}
	
	public static void delete( final Context context , final Uri uri ,final String id, final LoginUserAdapter loginUserAdapter,final int position) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setPositiveButton(edu.minggo.chat.R.string.confirm,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						context.getContentResolver().delete(uri, UserTableMetaData._ID+"="+"?", new String[]{id});
						loginUserAdapter.refresh(position);
					}
					
				});
		ab.setNegativeButton(edu.minggo.chat.R.string.no, null);
		ab.show();
	}
	/**
	 * ��������
	 * @param context
	 */
	public static void promptQuestConect(final Context context) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setPositiveButton(edu.minggo.chat.R.string.exit_confirm,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					//	connected(socket, socket.getRemoteDevice(),mSocketType);
					}
				});
		ab.setNegativeButton(edu.minggo.chat.R.string.cancel, null);
		ab.show();
	}
	@Override
	public void onCreate() {
		super.onCreate();
		isrun = true;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void onDestroy() {
		if (this != null) 
			super.stopSelf(); //ֹͣ��������
		isrun = false;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		if (intent.getExtras()!=null) {
			start();
		}
	}
	/**
	 * ��������������������
	 */
	public synchronized void start() {
		if (mConnectThread != null) {// �ر����г������ӵ��߳�
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {// �ر����е�ǰ�������ӵ��߳�
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		setState(STATE_LISTEN);

		if (mSecureAcceptThread == null) { // ��ȫ�����߳��Ƿ�Ϊ��
			mSecureAcceptThread = new AcceptThread(true);
			mSecureAcceptThread.start();
		}
		/*if (mInsecureAcceptThread == null) { // �ǰ�ȫ�Ľ����߳��Ƿ�Ϊ��
			mInsecureAcceptThread = new AcceptThread(false);
			mInsecureAcceptThread.start();
		}*/
	}
	
	/**
	 * ����һ�����ӵ��̳߳�ʼ����Զ���豸������
	 * @param device Ҫ���ӵ������豸
	 * @param secure �߼����͵Ĳ��� ����ȫ��
	 */
	public synchronized void connect(BluetoothDevice device, boolean secure) {
		System.out.println(device.getAddress());
		
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		if (connectDeviceAdd!=null&&connectDeviceAdd.equals(device.getAddress())) {
			mHandler.obtainMessage(STATE_CONNECT_THE_SAME).sendToTarget();
			//��ʷ��Ϣ�ó�����������
		}else{
			System.out.println("�����߳̽�����");
			if (mConnectedThread != null) {
				mConnectedThread.cancel();
				mConnectedThread = null;
			}
			mConnectThread = new ConnectThread(device, secure);
			mConnectThread.start();
			setState(STATE_CONNECTING);
		}
		
	}

	/**
	 * ����һ�������ӵ��߳�ȥ��ʼ����һ�����������ӡ�
	 * @param socket���Ӻõ�BluetoothSocket
	 * @param device���Ӻõ��豸
	 */
	public synchronized void connected(BluetoothSocket socket,BluetoothDevice device, final String socketType) {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mSecureAcceptThread != null) { // ��ȫ�߳��Ƿ�Ϊ��
			mSecureAcceptThread.cancel();
			mSecureAcceptThread = null;
		}
		if (mInsecureAcceptThread != null) { // �ǰ�ȫ�豸�Ƿ�Ϊ��
			mInsecureAcceptThread.cancel();
			mInsecureAcceptThread = null;
		}
		connectDeviceAdd = device.getAddress();
		// ����һ���߳�ȥ�������Ӻ�ִ�д�������
		mConnectedThread = new ConnectedThread(socket, socketType);
		mConnectedThread.start();

		// ����һ�������ӵ��豸���Ʒ���ui����
		mHandler.obtainMessage(ChattingActivity.MESSAGE_STATE_CHANGE,STATE_CONNECTED,-1).sendToTarget();
		// �����Ѿ������豸״̬
		setState(STATE_CONNECTED);
	}

	/**
	 * ֹͣ��ǰ�ػ�����������߳�
	 */
	public synchronized void cancelCurrent() {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		start();
		setState(STATE_LISTEN);
	}
	/**
	 * ֹͣ��������
	 */
	public synchronized static void stopService() {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mSecureAcceptThread != null) {
			mSecureAcceptThread.cancel();
			mSecureAcceptThread = null;
		}
		if (mInsecureAcceptThread != null) {
			mInsecureAcceptThread.cancel();
			mInsecureAcceptThread = null;
		}
		setState(STATE_NONE);
		
	}
	/**
	 * д�������̵߳�����һ����ͬ���ķ���
	 * @param out �ֽ����͵�out
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(out);
	}
	/**
	 * д�������̵߳�����һ����ͬ���ķ���
	 * @param image
	 */
	public void write(File image) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(image);
	}
	/**
	 * д�������̵߳�����һ����ͬ���ķ���
	 * @param image
	 */
	public void write(String soundPath,int recordTime) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(soundPath,recordTime);
	}
	/**
	 * д�������̵߳�����һ����ͬ���ķ���
	 * @param image
	 */
	public void write(String location) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(location);
	}
	/**
	 * д�������̵߳�����һ����ͬ���ķ���
	 * @param image
	 */
	public void write(String location,boolean flag) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(location,flag);
	}
	/**
	 * ��ʾ������ͼʧ�ܺ�ui������ʾ
	 */
	private void connectionFailed() {
		connectDeviceAdd = null;
		mHandler.obtainMessage(ChattingActivity.MESSAGE_TOAST,1,-1).sendToTarget();
		BluetoothChatService.this.start();
	}

	/**
	 * ��ʾ�����жϺ�ui������ʾ
	 */
	private void connectionLost() {
		connectDeviceAdd = null;
		mHandler.obtainMessage(ChattingActivity.MESSAGE_TOAST,2,-1).sendToTarget();
		BluetoothChatService.this.start();
	}

	/**
	 * ����̵߳��г��������������ӡ�������Ϊһ���������ߵĿͻ��ˡ�
	 * ��������ֱ�������� �����̱߳�ɾ���Ż�ֹͣ
	 */
	private class AcceptThread extends Thread {
		// ���������socket
		private final BluetoothServerSocket mmServerSocket;
		private String mSocketType;
		BluetoothSocket socket = null;
		public AcceptThread(boolean secure) {
			BluetoothServerSocket tmp = null;
			mSocketType = secure ? "Secure" : "Insecure";

			// �����µļ�����������socket
			try {
				tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
			} catch (IOException e) {
				Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
			}
			
			// ʵ����������socket
			mmServerSocket = tmp;
		}

		public void run() {
			setName("AcceptThread" + mSocketType);
			// ������������socket�Ƿ�����
			while (mState != STATE_CONNECTED) {
				try {
					if(mmServerSocket==null){
						System.out.println("��Ϊ��tmp");
					}else{
						System.out.println(mmServerSocket.toString()+"====mmServerSocket");
					}
					// ���û�����ӵĻ�������
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}

				// ���Ӳ�Ϊ�յ����
				if (socket != null) {
					synchronized (BluetoothChatService.this) {
						switch (mState) {
						case STATE_LISTEN:
							
						case STATE_CONNECTING:  //���������ز������ӵ��豸��
							
							//System.out.println("��׽�����￪ʼ����"+context.toString()+"Context�ģ�����");
							// �����������ӵ�״̬�Ϳ�ʼһ�����ӵ��߳�
							
							//bc1.refresh(BluetoothChatActivity.PROMPT_DEVICE_CONECT,socket,socket.getRemoteDevice(),mSocketType);
							connected(socket, socket.getRemoteDevice(),mSocketType);
							
							break;
						case STATE_NONE:
							// ʲôҲ����
						case STATE_CONNECTED:
							// ׼�������Ѿ����ӣ���ֹ�µ�socket����
							try {
								socket.close();
							} catch (IOException e) {
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
			
		}
		
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ����̻߳��Ȿ�豸��ͼ����������Χ�豸��ʱ�� ֱ�����У���������ʧ�ܻ��߳ɹ�
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private String mSocketType;

		/*
		 * ����������Ҫ���ӵ��豸�����ӵķ�ʽ
		 */
		public ConnectThread(BluetoothDevice device, boolean secure) {
			mmDevice = device;
			BluetoothSocket tmp = null;
			mSocketType = secure ? "Secure" : "Insecure";

			try {
				if (secure) {
					System.out.println("��������secure");
					tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
				} else {
					System.out.println("��������insecure");
					tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mmSocket = tmp;
		}

		public void run() {
			setName("ConnectThread" + mSocketType);

			mAdapter.cancelDiscovery();

			// ��BluetoothSocket������
			try {
				// ����Ƕ�������ֻ�гɹ����ӻ����쳣�Ż᷵��
				mmSocket.connect();
			} catch (IOException e) {
				/*try {
					//mmSocket.close(); // �ر�socket
				} catch (IOException e2) {
					e.printStackTrace();
				}*/
				connectionFailed();
				return;
			}

			// ��������
			synchronized (BluetoothChatService.this) {
				// ����Ϊ��
				mConnectThread = null;
			}
			// ��������
			connected(mmSocket, mmDevice, mSocketType);
		}

		/*
		 * �ر�socket
		 */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect " + mSocketType
						+ " socket failed", e);
			}
		}
	}

	/**
	 * ����̻߳������Ӻ�һ��Զ���豸��ʱ�����������ٿؽ������ȥ������
	 */
	private class ConnectedThread extends Thread {
		public int messagekind = -1;
		private final BluetoothSocket mmSocket; // �������
		private InputStream mmInStream; // ������Ϣ��
		private final OutputStream mmOutStream; // �����Ϣ��
		private int lenght = 0;
		public ConnectedThread(BluetoothSocket socket, String socketType) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
			
		}
		
		/**
		 * ��׽������
		 */
		public void run() {
			String shead = null;
			String subname1 = null;
			int recordTime = 0;
			// �����Ӻ󱣳ּ���������
			while (true) {
				byte[] head = new byte[10];
				byte[] len = new byte[8];
				try {
					if(mmInStream!=null){
						if (messagekind==-1) {
							mmInStream.read(head,0,10);
							shead = new String(head);
							mmInStream.read(len,0,8);
							lenght = Integer.parseInt(new String(len));
							System.out.println(shead+"----"+new String(len));
							if (shead.equals("[MessageA]")) {
								byte[] msgByte = new byte[lenght];
								mmInStream.read(msgByte);
								System.out.println(new String(msgByte));
								mHandler.obtainMessage(ChattingActivity.MESSAGE_READ,0, -1, new String(msgByte)).sendToTarget();
								System.out.println("���Խ���Ϣ");
								messagekind = -1;
							}else if(shead.equals("[MessageB]")){
								byte[] subname2 = new byte[4];
								mmInStream.read(subname2,0,4);
								System.out.println(new String(subname2)+"<-------��׺��");
								subname1 =  new String(subname2);
								messagekind = 1;
							}else if (shead.equals("[MessageC]")) {
								byte[] recordTime0 = new byte[3];
								mmInStream.read(recordTime0,0,3);
								recordTime = Integer.parseInt(new String(recordTime0));
								messagekind = 2;
							}else if (shead.equals("[MessageD]")) {
								byte[] msgByte = new byte[lenght];
								mmInStream.read(msgByte);
								mHandler.obtainMessage(ChattingActivity.MESSAGE_READ,3, -1, new String(msgByte)).sendToTarget();
								messagekind = -1;
							}else if (shead.equals("[MessageF]")) {
								byte[] msgByte = new byte[lenght];
								mmInStream.read(msgByte);
								mHandler.obtainMessage(GameFiveChessActivity.MESSAGE_READ,0, 0, new String(msgByte)).sendToTarget();
								messagekind = -1;
							}
						}
						if(messagekind == 1){//��ͼƬ
							InputStream tempimage = mmInStream;
							File picFile = ClippingPicture.createTalkImage(subname1);            
					        FileOutputStream os = new FileOutputStream(picFile);
							try {
								byte[] buffer1 = new byte[1024*1024];
								int len1 = 0;
								int bytes1 = 0;
								while((len1)!=lenght){
									bytes1=tempimage.read(buffer1);
									len1 = len1+bytes1;
									os.write(buffer1,0,bytes1); 
								}
								os.flush();
								os.close();
								
							} catch (IOException e) {
								e.printStackTrace();
							}
							mHandler.obtainMessage(ChattingActivity.MESSAGE_READ,1, 1, picFile.getAbsolutePath()).sendToTarget();
							messagekind = -1;
								
						}else if(messagekind==2){//��¼��
							InputStream tempimage = mmInStream;
							File soundFile = new File(ClippingSounds.saveSounds());            
					        FileOutputStream os;
							try {
								os = new FileOutputStream(soundFile);
								byte[] buffer1 = new byte[1024*1024];
								int len1 = 0;
								int bytes1 = 0;
								while((len1)!=lenght){
									bytes1=tempimage.read(buffer1);
									len1 = len1+bytes1;
									os.write(buffer1,0,bytes1); 
								}
								os.flush();
								os.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							mHandler.obtainMessage(ChattingActivity.MESSAGE_READ,2, recordTime, soundFile.getAbsolutePath()).sendToTarget();
							messagekind = -1;
						}
					}	
				} catch (IOException e) {
					if (flag==0) {
						connectionLost();
					}
					break;
				}
			}
		}

		/**
		 * д���������
		 * @param buffer
		 * �ֽ�����д��
		 */
		public void write(byte[] buffer) {
			try {
				String head = "[MessageA]"+MessageLen.getLenght(buffer);
				byte[] allcontent = head.getBytes();
				allcontent = ArrayUtils.addAll(allcontent, buffer);
		        mmOutStream.write(allcontent);
		        mmOutStream.flush();
		       
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * д��������з�����Ƶ�ļ�
		 * @param buffer
		 */
		public void write(String soundPath,int recordTime) {
			try {
				File soundf = new File(soundPath);
				System.out.println(soundPath+"<----------------");
				byte[] content = getBytesFromFile(soundf);
				if (content.length<=1024*1024) {
					String head = "[MessageC]"+MessageLen.getLenght(content)+MessageLen.getRecordTime(recordTime);
					byte[] headbyte = head.getBytes();
					
					mmOutStream.write(headbyte);
					mmOutStream.flush();
					mmOutStream.write(content);
			        mmOutStream.flush();
			       
					mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
				}else{
					mHandler.obtainMessage(ChattingActivity.MESSAGE_WRITE_BOUND_MAX, -1, -1,"0").sendToTarget();
				}
				
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * д���������
		 * @param buffer �ļ�����
		 */
		public void write(File image) {
			try {
				byte[] content = getBytesFromFile(image);
				if (content.length<=1024*1024) {
					String fileName = image.getName();
					String subName = ".jpg";//����Ϊ4
			        if(fileName.lastIndexOf(".")>0){
			        	subName = fileName.substring(fileName.lastIndexOf("."));
			        }
					String head = "[MessageB]"+MessageLen.getLenght(content)+subName;
					byte[] headbyte = head.getBytes();
					//�ȷ��ͱ���ͷ
					mmOutStream.write(headbyte);
					mmOutStream.flush();
					//�ڷ�������
					mmOutStream.write(content);
					mmOutStream.flush();
					mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
				
				}else{
					mHandler.obtainMessage(ChattingActivity.MESSAGE_WRITE_BOUND_MAX, -1, -1,"0").sendToTarget();
				}
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * ����λ��
		 * @param location
		 */
		public void write(String location) {
			System.out.println(location+"<-------λ��ƹƹ����");
			byte[] content = location.getBytes();
			String head = "[MessageD]"+MessageLen.getLenght(content)+location;
			byte[] allcontent = head.getBytes();
			try {
				mmOutStream.write(allcontent);
				mmOutStream.flush();
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * ��������λ��
		 * @param location
		 */
		public void write(String location,boolean flag) {
			System.out.println(location+"<-------λ������");
			byte[] content = location.getBytes();
			String head = "[MessageF]"+MessageLen.getLenght(content)+location;
			byte[] allcontent = head.getBytes();
			try {
				mmOutStream.write(allcontent);
				mmOutStream.flush();
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * ��ͼƬת����byte[]
		 * @param file
		 * @return
		 * @throws IOException
		 */
		public  byte[] getBytesFromFile(File file) throws IOException {

	        InputStream is = new FileInputStream(file);
	        // ��ȡ�ļ���С
	        long length = file.length();
	        
	        if (length > Integer.MAX_VALUE) {
	        // �ļ�̫���޷���ȡ
	        throw new IOException("File is to large "+file.getName());
	        }
	        // ����һ�������������ļ�����
	        byte[] bytes = new byte[(int)length];
	        // ��ȡ���ݵ�byte������
	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	            offset += numRead;
	        }
	        // ȷ���������ݾ�����ȡ
	        if (offset < bytes.length) {
	            throw new IOException("Could not completely read file "+file.getName());
	        }
	        is.close();
	        return bytes;
	    }
		private int flag = 0;
		public void cancel() {
			try {
				flag = 1;
				mmOutStream.close();
				mmInStream.close();
				mmSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	

}
