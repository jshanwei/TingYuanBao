package com.ipcamer.demo1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vstc2.nativecaller.NativeCaller;

import com.ipcamer.demo1.BridgeService.AddCameraInterface;
import com.ipcamer.demo1.BridgeService.IpcamClientInterface;
import com.ipcamer.demo1.CameralistFragment.ConnectCameraListener;
import com.petcare.demo1.UpdateVer;



import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;



/**
 * 项目的主Activity，所有的Fragment都嵌入在这里
 * 
 * @author hanwei
 */
public class MainActivity extends Activity  implements 
OnClickListener,AddCameraInterface,OnItemSelectedListener,IpcamClientInterface,ConnectCameraListener{
	



	private AlarmFragment alarmFragment;
	private CameralistFragment cameralistFragment;
	private MoreFragment moreFragment;
	private PictureFragment pictureFragment;
	private View cameraLayout;
	private View pictureLayout;
	private View alarmLayout;
	private View moreLayout;
	private ImageView cameraImage;
	private ImageView pictureImage;
	private ImageView alarmImage;
	private ImageView moreImage;
	private TextView cameraText;
	private TextView pictureText;
	private TextView alarmText;
	private TextView moreText;
	private TextView ordertime;
	public String uid;
	private static final String STR_DID = "did";
	private WifiManager manager = null;
	private int tag = 0;
	private static final String STR_MSG_PARAM = "msgparam";
	private Intent intentbrod = null;
	private static final String urlApk="http://qxz.kemao.com.cn/PlayerOCX/tingyuan.apk";
	private static final String urlVer="http://115.28.145.60:82/html/updateAndroid_tingyuan.xml";
	private static final String TAG="MainActivity";
	public int Num;
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//Log.e(TAG, "start onCreate~~~");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buttom_main);
		//Intent in = getIntent();
		//uid=(String) in.getSerializableExtra("user");
		//uid="VSTC383293ZYDRM";
		
		// 初始化布局
		initViews();
		//下载新版本
		DownloadManager dManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
		UpdateVer uv=new UpdateVer(urlApk,urlVer,MainActivity.this,dManager);
		manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		uv.checkVer();
		
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, BridgeService.class);
		startService(intent);
		
		intentbrod = new Intent("drop");
		
		
		fragmentManager = getFragmentManager();
		// 第一次启动时选中第一个tab
		setTabSelection(0);
	}
	
	
	public String getUid(){
		return this.uid;
	}
	/**
	 * 在这里获取到每个用到的控件的实例，并给它们设置好必要的点击事件
	 */
	private void initViews() {
		cameraLayout = findViewById(R.id.main_layout_vidicon);
		pictureLayout = findViewById(R.id.main_layout_pic);
		alarmLayout = findViewById(R.id.main_layout_vid);
		moreLayout = findViewById(R.id.main_layout_about);
		cameraImage = (ImageView) findViewById(R.id.main_img_vidicon);
		pictureImage = (ImageView) findViewById(R.id.main_img_picture);
		alarmImage = (ImageView) findViewById(R.id.main_img_vid);
		moreImage = (ImageView) findViewById(R.id.main_img_about);
		cameraText = (TextView) findViewById(R.id.main_tv_vidicon);
		pictureText = (TextView) findViewById(R.id.main_tv_picture);
		alarmText = (TextView) findViewById(R.id.main_tv_vid);
		moreText = (TextView) findViewById(R.id.main_tv_about);
		ordertime=(TextView)findViewById(R.id.edtstarttime);
		cameraLayout.setOnClickListener(this);
		pictureLayout.setOnClickListener(this);
		alarmLayout.setOnClickListener(this);
		moreLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_layout_vidicon:
			// 当点击了消息tab时，选中这个tab
			setTabSelection(0);
			break;
		case R.id.main_layout_pic:
			// 当点击了联系人tab时，选中这个tab
			setTabSelection(1);
			break;
		case R.id.main_layout_vid:
			// 当点击了动�?tab时，选中这个tab
			setTabSelection(2);
			break;
		case R.id.main_layout_about:
			// 当点击了设置tab时，选中这个tab
			setTabSelection(3);
			break;
		default:
			break;
		}
	}

	/**
	 * 根据传入的index参数来设置�?中的tab页�?
	 * 
	 * @param index
	 *            每个tab页对应的下标�?表示消息�?表示联系人，2表示动�?�?表示设置�?
	 */
	private void setTabSelection(int index) {
		// 每次选中之前先清除掉上次的状态
		clearSelection();
		// 开启Fragment事务
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			// 当点击了摄像机tab时，改变控件的图片和文字颜色
			cameraImage.setImageResource(R.drawable.buttom_camera_press);
			cameraText.setTextColor(getResources().getColor(R.color.color_black));
			if (cameralistFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				cameralistFragment = new CameralistFragment();
				transaction.add(R.id.contentfragment, cameralistFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(cameralistFragment);
			}
			break;
		case 1:
			// 当点击了图片tab时，改变控件的图片和文字颜色
			pictureImage.setImageResource(R.drawable.buttom_picture_press);
			pictureText.setTextColor(getResources().getColor(R.color.color_black));
			if (pictureFragment == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				pictureFragment = new PictureFragment();
				transaction.add(R.id.contentfragment, pictureFragment);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出�?
				transaction.show(pictureFragment);
			}
			break;
		case 2:
			// 当点击了警告tab时，改变控件的图片和文字颜色
			alarmImage.setImageResource(R.drawable.buttom_message_press);
			alarmText.setTextColor(getResources().getColor(R.color.color_black));
			if (alarmFragment == null) {
				// 如果NewsFragment为空，则创建�?��并添加到界面�?
				alarmFragment = new AlarmFragment();
				transaction.add(R.id.contentfragment, alarmFragment);
			} else {
				// 如果NewsFragment不为空，则直接将它显示出�?
				transaction.show(alarmFragment);
			}
			break;
		case 3:
		default:
			// 当点击了设置tab时，改变控件的图片和文字颜色
			moreImage.setImageResource(R.drawable.buttom_more_press);
			moreText.setTextColor(getResources().getColor(R.color.color_black));
			if (moreFragment == null) {
				// 如果SettingFragment为空，则创建�?��并添加到界面�?
				moreFragment = new MoreFragment();
				transaction.add(R.id.contentfragment, moreFragment);
			} else {
				// 如果SettingFragment不为空，则直接将它显示出�?
				transaction.show(moreFragment);
			}
			break;
		}
		transaction.commit();
	}
		
		
		
		
		
		
		
		
	

	/**
	 * 清除掉所有的选中状态
	 */
	private void clearSelection() {
		cameraImage.setImageResource(R.drawable.buttom_camera);
		cameraText.setTextColor(getResources().getColor(R.color.color_main_buttom_textcolor));
		pictureImage.setImageResource(R.drawable.buttom_picture);
		pictureText.setTextColor(getResources().getColor(R.color.color_main_buttom_textcolor));
		alarmImage.setImageResource(R.drawable.buttom_message);
		alarmText.setTextColor(getResources().getColor(R.color.color_main_buttom_textcolor));
		moreImage.setImageResource(R.drawable.buttom_more);
		moreText.setTextColor(getResources().getColor(R.color.color_main_buttom_textcolor));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	@SuppressLint("NewApi")
	private void hideFragments(FragmentTransaction transaction) {
		if (cameralistFragment != null) {
			transaction.hide(cameralistFragment);
		}
		if (pictureFragment != null) {
			transaction.hide(pictureFragment);
		}
		if (alarmFragment != null) {
			transaction.hide(alarmFragment);
		}
		if (moreFragment != null) {
			transaction.hide(moreFragment);
		}
	}
	
 
    @Override
    public void ConnectCamera(String id,int num){
    	done2(id,num);
    	
    }
    @Override
    public void linksecond(String id){
    	
    }
    
    
    public void done2(String id,int num) {
		Intent in = new Intent();
		/*String strUser = userEdit.getText().toString();
		String strPwd = pwdEdit.getText().toString();
		String strDID = didEdit.getText().toString();*/
		String strUser="admin";
		String strPwd="888888";
		String strDID=id;
		Num=num;
		Log.d("done2(String,num)","id="+id+"    mun="+Num);
		
		//验证密码和账号是否为空		
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
		//BridgeService.setIpcamClientInterface(this);
		//NativeCaller.Init();
		new Thread(new StartPPPPThread()).start();
		// overridePendingTransition(R.anim.in_from_right,
		// R.anim.out_to_left);// 
		// finish();
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
		BridgeService.setIpcamClientInterface(this);
		NativeCaller.Init();
		
		int result = NativeCaller.StartPPPP(SystemValue.deviceId, SystemValue.deviceName,
				SystemValue.devicePass);
		Log.i("ip", "result:"+result);
	}
    
	@Override  
	   public void onBackPressed() {  
	new AlertDialog.Builder(this).setTitle("确认退出吗？")  
	    .setIcon(android.R.drawable.ic_dialog_info)  
	    .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	  
	        public void onClick(DialogInterface dialog, int which) {  
	        // 点击“确认”后的操作  
	       MainActivity.this.finish();  
	  
	        }  
	    })  
	    .setNegativeButton("返回", new DialogInterface.OnClickListener() {  
	  
	        public void onClick(DialogInterface dialog, int which) {  
	        // 点击“返回”后的操作,这里不设置没有任何操作  
	        }  
	    }).show();  
	// super.onBackPressed();  
	   } 
	
	
	/*@Override
    protected void onStart() {
        super.onStart();
       Log.e(TAG, "start onStart~~~");
    }
     
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "start onRestart~~~");
    }
     
    @Override
    protected void onResume() {
        super.onResume();
        for(int i=0;i<3;i++){
        Map<String,Object> map=new HashMap<String,Object>();
				map=SystemValue.cameraList.get(i);
				map.put("status", "点击连接");
				SystemValue.cameraList.set(i, map);
        }
        cameralistFragment.refresh();
        Log.e(TAG, "start onResume~~~");
    }
     
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "start onPause~~~");
    }
     
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "start onStop~~~");
    }
     
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "start onDestroy~~~");
    }
   
    @Override
    protected void onSaveInstanceState(Bundle outState){
    	 Log.e(TAG, "start onDestroy~~~");
    }
    
    */
    
    
    private Handler PPPPMsgHandler = new Handler() {
		public void handleMessage(Message msg) {

			Bundle bd = msg.getData();
			int msgParam = bd.getInt(STR_MSG_PARAM);
			int msgType = msg.what;
			Log.i("PPPPMsgHandler", "===="+msgType+"--msgParam:"+msgParam);
			String did = bd.getString(STR_DID);
			switch (msgType) {
			case ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS:
				int resid;
				switch (msgParam) {
				case ContentCommon.PPPP_STATUS_CONNECTING://0
					resid = R.string.pppp_status_connecting;
					//progressBar.setVisibility(View.VISIBLE);
					tag = 2;
					
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_FAILED://3
					resid = R.string.pppp_status_connect_failed;
					//progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_DISCONNECT://4
					resid = R.string.pppp_status_disconnect;
					//progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_INITIALING://1
					resid = R.string.pppp_status_initialing;
					//progressBar.setVisibility(View.VISIBLE);
					tag = 2;
					break;
				case ContentCommon.PPPP_STATUS_INVALID_ID://5
					resid = R.string.pppp_status_invalid_id;
					//progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_ON_LINE://2
					resid = R.string.pppp_status_online;
					//progressBar.setVisibility(View.GONE);
					tag = 1;
					break;
				case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE://6
					resid = R.string.device_not_on_line;
					//progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT://7
					resid = R.string.pppp_status_connect_timeout;
					//progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_ERRER://8
					resid =R.string.pppp_status_pwd_error;
					//progressBar.setVisibility(View.GONE);
					tag = 0;
					break;
				default:
					resid = R.string.pppp_status_unknown;
				}
				//textView_top_show.setText(getResources().getString(resid));
				Log.e("PPPPMsgHandler","Num="+Num+"   "+getResources().getString(resid));
				Map<String,Object> map=new HashMap<String,Object>();
				map=SystemValue.cameraList.get(Num);
				map.put("status", getResources().getString(resid));
				SystemValue.cameraList.set(Num, map);
				
				
				cameralistFragment.refresh();
				
				
				//
				
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
					Log.e("PPPPMsgHandler", "NativeCaller.stoppppp");
				}
				break;
			case ContentCommon.PPPP_MSG_TYPE_PPPP_MODE:
				break;

			}

		}
	};
	
	public void changeStatus(int status){
		
	}
	
	@Override
	public void BSMsgNotifyData(String did, int type, int param) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void callBackSearchResultData(int cameraType, String strMac,
			String strName, String strDeviceID, String strIpAddr, int port) {
		// TODO Auto-generated method stub
		
	}

}
