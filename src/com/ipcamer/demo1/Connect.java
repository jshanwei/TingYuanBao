package com.ipcamer.demo1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;
import java.util.TimerTask;

import vstc2.nativecaller.NativeCaller;

import android.app.AlertDialog;
import android.app.DownloadManager;
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
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.Toast;

import com.petcare.demo1.*;
import com.ipcamer.demo1.AddCameraActivity.StartPPPPThread;
import com.ipcamer.demo1.BridgeService.AddCameraInterface;
import com.ipcamer.demo1.BridgeService.IpcamClientInterface;

public class Connect extends Activity implements AddCameraInterface, 
OnItemSelectedListener, IpcamClientInterface{
	private Button connect;
	private TextView connectstatus;
	private Button testplay;
	private int option = ContentCommon.INVALID_OPTION;
	private Intent intentbrod = null;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);
		
		testplay=(Button)findViewById(R.id.testplay);
		connect=(Button)findViewById(R.id.testconnect);
		connectstatus=(TextView)findViewById(R.id.Connectstatus);
		BridgeService.setAddCameraInterface(this);
		intentbrod = new Intent("drop");//�������й�ϵ��
		
		
		connect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				connectcamera();
			}
		});
		
	}
	
	private void connectcamera(){
		Intent i=new Intent();
		String strUser="admin";
		String strPwd="888888";
		String strDID="VSTC-383293-ZYDRM";
		if (option == ContentCommon.INVALID_OPTION) {
			option = ContentCommon.ADD_CAMERA;
		}
		SystemValue.deviceName = strUser;
		SystemValue.deviceId = strDID;
		SystemValue.devicePass = strPwd;
		BridgeService.setIpcamClientInterface(this);
		NativeCaller.Init();
		new Thread(new StartPPPPThread()).start();
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
		
		Log.i("deviceId",SystemValue.deviceId);
		Log.i("deviceName",SystemValue.deviceName);
		Log.i("devicePass",SystemValue.devicePass);
		Log.i("ip", "result:"+result);
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public void BSMsgNotifyData(String did, int type, int param) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void BSSnapshotNotify(String did, byte[] bImage, int len) {
		// TODO Auto-generated method stub
		
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
