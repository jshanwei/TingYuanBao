package com.ipcamer.demo1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TimerTask;
import vstc2.nativecaller.NativeCaller;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ipcamer.demo1.BridgeService.AddCameraInterface;
import com.ipcamer.demo1.BridgeService.IpcamClientInterface;
import com.ipcamer.demo1.PlayActivity.watering;

public class AddCameraActivity extends Activity implements OnClickListener,
		AddCameraInterface, OnItemSelectedListener, IpcamClientInterface {
	private EditText userEdit = null;
	private EditText pwdEdit = null;
	private EditText didEdit = null;
	private TextView textView_top_show = null;
	private Button done;
	private static final int SEARCH_TIME = 3000;
	private int option = ContentCommon.INVALID_OPTION;
	private int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
	private Button btnSearchCamera;
	private SearchListAdapter listAdapter = null;
	private ProgressDialog progressdlg = null;
	private boolean isSearched;
	private MyBroadCast receiver;
	private WifiManager manager = null;
	private ProgressBar progressBar = null;
	private static final String STR_DID = "did";
	private static final String STR_MSG_PARAM = "msgparam";
	private MyWifiThread myWifiThread = null;
	private boolean blagg = false;
	private Intent intentbrod = null;
	private WifiInfo info = null;
	boolean bthread = true;
	private Button button_play = null;
	private Button button_setting = null;
	private int tag = 0;
	private String UID;
	private static final String urlApk="http://qxz.kemao.com.cn/PlayerOCX/NiceApp.apk";
	private static final String urlVer="http://115.28.145.60:82/html/updateAndroid.xml";
	public static AddCameraActivity instance=null;
	class MyTimerTask extends TimerTask {
		public void run() {
			updateListHandler.sendEmptyMessage(100000);
		}
	};

	class MyWifiThread extends Thread {
		@Override
		public void run() {
			while (blagg == true) {
				super.run();

				updateListHandler.sendEmptyMessage(100000);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private class MyBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			AddCameraActivity.this.finish();
			Log.d("ip", "AddCameraActivity.this.finish()");
		}

	}

	class StartPPPPThread implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				StartCameraPPPP();
			} catch (Exception e) {

			}
		}
	}

	private void StartCameraPPPP() {
		try {
			Thread.sleep(100);
		} catch (Exception e) {
		}
		int result = NativeCaller.StartPPPP(SystemValue.deviceId, SystemValue.deviceName,
				SystemValue.devicePass);
		Log.i("ip", "result:"+result);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_camera);
		Intent in = getIntent();
		UID=(String) in.getSerializableExtra("cameraInfo");
		instance=this;
		//UID="VSTC383293ZYDRM";
		
		//将启动service放Addcamera里  不管用
		Intent intent = new Intent();
		intent.setClass(AddCameraActivity.this, BridgeService.class);
		startService(intent);
		
		
		
		progressdlg = new ProgressDialog(this);
		progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdlg.setMessage(getString(R.string.searching_tip));
		listAdapter = new SearchListAdapter(this);
		findView(); 
		didEdit.setText(UID);
		InitParams(); //监听按钮

		BridgeService.setAddCameraInterface(this);//注释掉没有关系
		receiver = new MyBroadCast();  //于播放没有关系
		IntentFilter filter = new IntentFilter(); //于播放没有关系	
		filter.addAction("finish");//于播放没有关系
		registerReceiver(receiver, filter);//于播放没有关系
		intentbrod = new Intent("drop");//和连接有关系。
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		blagg = true;
	}

	private void InitParams() {

		done.setOnClickListener(this);
		btnSearchCamera.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		if (myWifiThread != null) {
			blagg = false;
		}
		progressdlg.dismiss();
		NativeCaller.StopSearch();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		//NativeCaller.Free();  //这个去除掉后就能连接无数次了
		/*Intent intent = new Intent();
		intent.setClass(this, BridgeService.class);
		stopService(intent);*/
		tag = 0;
	}

	Runnable updateThread = new Runnable() {

		public void run() {
			NativeCaller.StopSearch();
			progressdlg.dismiss();
			Message msg = updateListHandler.obtainMessage();
			msg.what = 1;
			updateListHandler.sendMessage(msg);
		}
	};
	// 15576341699  WIFI
	Handler updateListHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				listAdapter.notifyDataSetChanged();
				if (listAdapter.getCount() > 0) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							AddCameraActivity.this);
					dialog.setTitle(getResources().getString(
							R.string.add_search_result));
					dialog.setPositiveButton(
							getResources().getString(R.string.refresh),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startSearch();
								}
							});
					dialog.setNegativeButton(
							getResources().getString(R.string.str_cancel), null);
					dialog.setAdapter(listAdapter,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int arg2) {
									Map<String, Object> mapItem = (Map<String, Object>) listAdapter
											.getItemContent(arg2);
									if (mapItem == null) {
										return;
									}

									String strName = (String) mapItem
											.get(ContentCommon.STR_CAMERA_NAME);
									String strDID = (String) mapItem
											.get(ContentCommon.STR_CAMERA_ID);
									String strUser = ContentCommon.DEFAULT_USER_NAME;
									String strPwd = ContentCommon.DEFAULT_USER_PWD;
									userEdit.setText(strUser);
									pwdEdit.setText(strPwd);
									didEdit.setText(strDID);

								}
							});

					dialog.show();
				} else {
					Toast.makeText(AddCameraActivity.this,
							getResources().getString(R.string.add_search_no),
							Toast.LENGTH_LONG).show();
					isSearched = false;// 
				}
			}
		}
	};

	public static String int2ip(long ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	private void startSearch() {
		listAdapter.ClearAll();
		progressdlg.setMessage(getString(R.string.searching_tip));
		progressdlg.show();
		new Thread(new SearchThread()).start();
		updateListHandler.postDelayed(updateThread, SEARCH_TIME);
	}

	private class SearchThread implements Runnable {
		@Override
		public void run() {
			Log.d("tag", "startSearch");
			NativeCaller.StartSearch();
		}
	}

	private void findView() {
		progressBar = (ProgressBar) findViewById(R.id.main_model_progressBar1);
		textView_top_show = (TextView) findViewById(R.id.login_textView1);
		button_play = (Button) findViewById(R.id.play);
		button_setting = (Button) findViewById(R.id.setting);
		done = (Button) findViewById(R.id.done);
		done.setText("连接");
		userEdit = (EditText) findViewById(R.id.editUser);
		pwdEdit = (EditText) findViewById(R.id.editPwd);
		didEdit = (EditText) findViewById(R.id.editDID);
		btnSearchCamera = (Button) findViewById(R.id.btn_searchCamera);
		button_play.setOnClickListener(this);
		button_setting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play:
			Intent intent = new Intent(AddCameraActivity.this,
					PlayActivity.class);
			Bundle mBundle=new Bundle();
			mBundle.putSerializable("cameraInfo", UID);
			intent.putExtras(mBundle);
			
			startActivity(intent);
			//AddCameraActivity.this.finish();
			break;
		case R.id.setting:
			if (tag == 1) {
				Intent intent1 = new Intent(AddCameraActivity.this,
						SettingWifiActivity.class);
				intent1.putExtra(ContentCommon.STR_CAMERA_ID,
						SystemValue.deviceId);
				intent1.putExtra(ContentCommon.STR_CAMERA_NAME,
						SystemValue.deviceName);
				intent1.putExtra(ContentCommon.STR_CAMERA_PWD, SystemValue.devicePass);
				startActivity(intent1);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			} else {
				Toast.makeText(AddCameraActivity.this,
						getResources().getString(R.string.main_setting_prompt),
						0).show();
			}
			break;
		case R.id.done:
			if (tag == 1) {
				Toast.makeText(AddCameraActivity.this, "已连接，请点击预览按钮观看正在连接，请稍等", 0)
						.show();
			} else if (tag == 2) {
				Toast.makeText(AddCameraActivity.this, "正在连接，请稍等", 0).show();
			} else {
				done();
			}

			break;
		case R.id.btn_searchCamera:
			searchCamera();
			break;

		default:
			break;
		}
	}

	private void searchCamera() {
		if (!isSearched) {
			isSearched = true;
			startSearch();
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					AddCameraActivity.this);
			dialog.setTitle(getResources()
					.getString(R.string.add_search_result));
			dialog.setPositiveButton(
					getResources().getString(R.string.refresh),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							startSearch();

						}
					});
			dialog.setNegativeButton(
					getResources().getString(R.string.str_cancel), null);
			dialog.setAdapter(listAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int arg2) {
							Map<String, Object> mapItem = (Map<String, Object>) listAdapter
									.getItemContent(arg2);
							if (mapItem == null) {
								return;
							}

							String strName = (String) mapItem
									.get(ContentCommon.STR_CAMERA_NAME);
							String strDID = (String) mapItem
									.get(ContentCommon.STR_CAMERA_ID);
							String strUser = ContentCommon.DEFAULT_USER_NAME;
							String strPwd = ContentCommon.DEFAULT_USER_PWD;
							userEdit.setText(strUser);
							pwdEdit.setText(strPwd);
							didEdit.setText(strDID);

						}
					});
			dialog.show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AddCameraActivity.this.finish();
			/*Intent intent = new Intent();
			intent.setClass(this, BridgeService.class);
			stopService(intent);*/
			
			super.onDestroy();
			
			
			return false;
		}
		return false;
	}

	public void done() {
		Intent in = new Intent();
		String strUser = userEdit.getText().toString();
		String strPwd = pwdEdit.getText().toString();
		String strDID = didEdit.getText().toString();

		//验证密码和账号是否为空
		if (strDID.length() == 0) {
			Toast.makeText(AddCameraActivity.this,
					getResources().getString(R.string.input_camera_id), 0)
					.show();
			return;
		}

		if (strUser.length() == 0) {
			Toast.makeText(AddCameraActivity.this,
					getResources().getString(R.string.input_camera_user), 0)
					.show();
			return;
		}
		// in.setAction(ContentCommon.STR_CAMERA_INFO_RECEIVER);
		if (option == ContentCommon.INVALID_OPTION) {
			option = ContentCommon.ADD_CAMERA;
		}
	/*	in.putExtra(ContentCommon.CAMERA_OPTION, option);
		in.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
		in.putExtra(ContentCommon.STR_CAMERA_USER, strUser);
		in.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);
		in.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);*/
		progressBar.setVisibility(View.VISIBLE);
		// sendBroadcast(in);
		//设置uid，用户名，密码
		SystemValue.deviceName = strUser;
		SystemValue.deviceId = strDID;
		SystemValue.devicePass = strPwd;
		BridgeService.setIpcamClientInterface(this);
		NativeCaller.Init();
		new Thread(new StartPPPPThread()).start();
		// overridePendingTransition(R.anim.in_from_right,
		// R.anim.out_to_left);// 
		// finish();
	}
	public void done2() {
		Intent in = new Intent();
		/*String strUser = userEdit.getText().toString();
		String strPwd = pwdEdit.getText().toString();
		String strDID = didEdit.getText().toString();*/
		String strUser="admin";
		String strPwd="888888";
		String strDID="VSTC440015EPMHF";
		//验证密码和账号是否为空
		if (strDID.length() == 0) {
			Toast.makeText(AddCameraActivity.this,
					getResources().getString(R.string.input_camera_id), 0)
					.show();
			return;
		}

		if (strUser.length() == 0) {
			Toast.makeText(AddCameraActivity.this,
					getResources().getString(R.string.input_camera_user), 0)
					.show();
			return;
		}
		// in.setAction(ContentCommon.STR_CAMERA_INFO_RECEIVER);
		if (option == ContentCommon.INVALID_OPTION) {
			option = ContentCommon.ADD_CAMERA;
		}
	/*	in.putExtra(ContentCommon.CAMERA_OPTION, option);
		in.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
		in.putExtra(ContentCommon.STR_CAMERA_USER, strUser);
		in.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);
		in.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);*/
		//progressBar.setVisibility(View.VISIBLE);
		// sendBroadcast(in);
		//设置uid，用户名，密码
		SystemValue.deviceName = strUser;
		SystemValue.deviceId = strDID;
		SystemValue.devicePass = strPwd;
		BridgeService.setIpcamClientInterface(this);
		NativeCaller.Init();
		new Thread(new StartPPPPThread()).start();
		// overridePendingTransition(R.anim.in_from_right,
		// R.anim.out_to_left);// 
		// finish();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			didEdit.setText(scanResult);
		}
	}

	/**
	 * BridgeService callback
	 * **/
	@Override
	public void callBackSearchResultData(int cameraType, String strMac,
			String strName, String strDeviceID, String strIpAddr, int port) {
		if (!listAdapter.AddCamera(strMac, strName, strDeviceID)) {
			return;
		}
	}

	public String getInfoSSID() {

		info = manager.getConnectionInfo();
		String ssid = info.getSSID();
		return ssid;
	}

	public int getInfoIp() {

		info = manager.getConnectionInfo();
		int ip = info.getIpAddress();
		return ip;
	}
/*protected class reboottcp extends Thread{
		
		@Override
		public void run(){
			Looper.prepare();
		try{
			Log.d("socket", "执行socket new socket 前");
			Socket socket = new Socket("115.28.145.60", 6100); 
			Log.d("socket", "执行了socket new socket");
			//String message="Web|VSTC306907PSSPY|action:0";
			String message="Web|"+UID+"|action:0";
			try{
				System.out.println("begin to send");
				PrintWriter out =new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(
								socket.getOutputStream())),true);
				out.println(message);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("在发送tcp时错误");
			}finally{
				socket.close();
				System.out.println("Client:Socket closed");  
			}
		}catch (UnknownHostException el){
			el.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		}
	}

	protected class rebootudp extends Thread{
		@Override
		public void run(){
			Looper.prepare();
			try{
				InetAddress serverAddr=InetAddress.getByName("115.28.145.60");
				DatagramSocket socket=new DatagramSocket();
				byte[]buf;
				String message="Web|"+UID+"|action:0";
				buf=message.getBytes();
				DatagramPacket packet=new DatagramPacket(buf, buf.length,serverAddr,6100);
				socket.send(packet);				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}*/
	private void showToast(int i) {
		Toast.makeText(AddCameraActivity.this, i, 0).show();

	}
	private Handler PPPPMsgHandler = new Handler() {
		public void handleMessage(Message msg) {

			Bundle bd = msg.getData();
			int msgParam = bd.getInt(STR_MSG_PARAM);
			int msgType = msg.what;
			Log.i("第一行显示连接状态栏", "===="+msgType+"--msgParam:"+msgParam);
			String did = bd.getString(STR_DID);
			switch (msgType) {
			case ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS:
				int resid;
				switch (msgParam) {
				case ContentCommon.PPPP_STATUS_CONNECTING://0
					resid = R.string.pppp_status_connecting;
					progressBar.setVisibility(View.VISIBLE);
					tag = 2;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_FAILED://3
					resid = R.string.pppp_status_connect_failed;
					progressBar.setVisibility(View.GONE);
					
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_DISCONNECT://4
					resid = R.string.pppp_status_disconnect;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_INITIALING://1
					resid = R.string.pppp_status_initialing;
					progressBar.setVisibility(View.VISIBLE);
					tag = 2;
					break;
				case ContentCommon.PPPP_STATUS_INVALID_ID://5
					resid = R.string.pppp_status_invalid_id;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_ON_LINE://2
					resid = R.string.pppp_status_online;
					progressBar.setVisibility(View.GONE);
					tag = 1;
					break;
				case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE://6
					resid = R.string.device_not_on_line;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT://7
					resid = R.string.pppp_status_connect_timeout;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_ERRER://8
					resid =R.string.pppp_status_pwd_error;
					progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				default:
					resid = R.string.pppp_status_unknown;
				}
				textView_top_show.setText(getResources().getString(resid));
				if (msgParam == ContentCommon.PPPP_STATUS_ON_LINE) {
					NativeCaller.PPPPGetSystemParams(did,
							ContentCommon.MSG_TYPE_GET_PARAMS);
				}
				if (msgParam == ContentCommon.PPPP_STATUS_INVALID_ID
						|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_FAILED
						|| msgParam == ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE
						|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT
						|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_ERRER) {
					NativeCaller.StopPPPP(did);
				}
				break;
			case ContentCommon.PPPP_MSG_TYPE_PPPP_MODE:
				break;

			}

		}
	};

	@Override
	public void BSMsgNotifyData(String did, int type, int param) {
		Log.e("ip", "type:" + type + " param:" + param);
		Bundle bd = new Bundle();
		Message msg = PPPPMsgHandler.obtainMessage();
		msg.what = type;
		bd.putInt(STR_MSG_PARAM, param);
		bd.putString(STR_DID, did);
		msg.setData(bd);
		PPPPMsgHandler.sendMessage(msg);
		if (type == ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
			intentbrod.putExtra("ifdrop", param);
			sendBroadcast(intentbrod);//注释掉也能
		}

	}

	@Override
	public void BSSnapshotNotify(String did, byte[] bImage, int len) {
		// TODO Auto-generated method stub
		Log.i("ip", "BSSnapshotNotify---len"+len);
	}

	@Override
	public void callBackUserParams(String did, String user1, String pwd1,
			String user2, String pwd2, String user3, String pwd3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void CameraStatus(String did, int status) {

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	
}
