package com.ipcamer.demo1;
/*package com.ipcamer.demo;

import android.widget.BaseAdapter;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraListAdapter extends BaseAdapter{
	public static List<Map<String,Object>> statuslist=new ArrayList();
	private MainActivity ipcamClientActivity;
	private Context context=null;
	private LayoutInflater listContainer=null;
	public ArrayList<Map<String,Object>> listItems=new ArrayList();
	private int picNumber;
	private DatabaseUtils dbUtil;
	public int j;
	
	
	public CameraListAdapter(Context paramContext,MainActivity paramIpcamClientActivity){
		this.ipcamClientActivity=paramIpcamClientActivity;
		this.context=paramContext;
		this.listContainer=LayoutInflater.from(paramContext);
		//this.dbUtil=new DatabaseUtils();  ���ݿ��ʹ��
		statuslist.clear();
	 
	}
	
	public void AddCamera(String paramString1,String paramString2,String paramString3,String paramString4){
		HashMap localHashMap =new HashMap();
		localHashMap.put("camera_snapshot", null);
		localHashMap.put("camera_name",paramString1);
		localHashMap.put("cameraid", paramString2);
		localHashMap.put("camera_user", paramString3);
		localHashMap.put("camera_pwd", paramString4);
		this.listItems.add(localHashMap);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		// TODO Auto-generated method stub
		if(paramView==null){
		CameraListItem localCameraListItem2=new  CameraListItem();
		paramView=this.listContainer.inflate(R.layout.camera_list_item, null);
		localCameraListItem2.imgSnapShot=(ImageView)paramView.findViewById(R.id.imgSnapshot);
		localCameraListItem2.devName=(TextView)paramView.findViewById(R.id.cameraDevName);
		localCameraListItem2.devID=(TextView)paramView.findViewById(R.id.cameraDevID);
		localCameraListItem2.devStatus=(TextView)paramView.findViewById(R.id.textPPPPStatus);
		localCameraListItem2.imgBtnSetting=(ImageButton)paramView.findViewById(R.id.imgBtnPPPPSetting);
		//paramView.setTag(localCameraListItem2);
		//CameraListItem localcameraListItem2=localCameraListItem2;
		//MyOnTouchListener localMyOnTouchListener =new MyOnTouchListener(paramint);
		Map localMap;
		localMap=(Map)this.listItems.get(paramInt);
		localCameraListItem2.devName.setText((String)localMap.get("camera_name"));
		localCameraListItem2.devID.setText((String)localMap.get("cameraid"));
		switch (((Integer) localMap.get("pppp_status")).intValue()){
		case 0:
			 j=R.string.pppp_status_unknown;
			HashMap localHashMap10=new HashMap();
			localHashMap10.put("camera_name",(Map)this.listItems.get(paramInt).get("camera_name"));
			localHashMap10.put("cameraid",(Map)this.listItems.get(paramInt).get("cameraid"));
			localHashMap10.put("camera_user",(Map)this.listItems.get(paramInt).get("camera_user"));
			localHashMap10.put("camera_pwd",(Map)this.listItems.get(paramInt).get("camera_pwd"));
			localHashMap10.put("camera_type",(Map)this.listItems.get(paramInt).get("camera_type"));
			if(statuslist.contains(localHashMap10)){
				statuslist.remove(localHashMap10);
				System.out.println("statuslist.size()"+"****************");				
			}else{
				localCameraListItem2.devStatus.setText(j);
				//����һЩ����ͼƬ�ġ�
			}
			break;
			
		case 1:
			 j=R.string.pppp_status_connect_failed;
			 break;
		case 2:
			j=R.string.pppp_status_connect_timeout;
			break;
		case 3:
			j=R.string.pppp_status_connecting;
			break;
		case 4:
			j=R.string.pppp_status_disconnect;
			break;
		case 5:
			j=R.string.pppp_status_initialing;
			break;
		case 6:
			j=R.string.pppp_status_invalid_id;
			break;
		case 7:
			j=R.string.pppp_status_online;
			break;			
		case 8:
			j=R.string.pppp_status_pwd_error;
			break;
		default:
			break;
			
		}
		localCameraListItem2.devStatus.setText(j);
		
		}
		
		return paramView;
	}
	public final class CameraListItem{
		public TextView devID;
		public TextView devName;
		public TextView devStatus;
		public TextView devType;
		public ImageButton imgBtnSetting;
		public ImageView imgSnapShot;
		public CameraListItem(){
			
		}
	}
	public class MyOnTouchListener implements View.OnTouchListener{
		public ImageButton imgBtn;
		private Map<String,Object> mapItem;
		private int position;
		public MyOnTouchListener(int arg2){
			int i;
			this.position=arg2;
		}
		@Override
		public boolean onTouch(View paramView, MotionEvent paraMotionEvent) {
			
			// TODO Auto-generated method stub
			this.imgBtn=((ImageButton)paramView);
			CameraListAdapter.this.ipcamClientActivity.showSettingPopWindow(this.position);
			
		}
		
	}
	
	
}
*/