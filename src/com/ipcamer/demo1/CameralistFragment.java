package com.ipcamer.demo1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vstc2.nativecaller.NativeCaller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ipcamer.demo1.AddCameraActivity.StartPPPPThread;
import com.ipcamer.demo1.BridgeService.AddCameraInterface;
import com.ipcamer.demo1.BridgeService.IpcamClientInterface;
import com.ipcamer.demo1.PlayActivity.watering;



public class CameralistFragment extends Fragment {
	private String uid;
	private ListView listview;
	private SimpleAdapter adapter;
	private ImageButton addCamera;
	//public List<Map<String,Object>> cameraList=new ArrayList<Map<String,Object>>();
	//private AddCameraActivity newAddCameraActivity;
	
	private ConnectCameraListener connectcamera;
	public interface ConnectCameraListener{
		public void ConnectCamera(String id,int num);
		public void linksecond(String id);
	}
	
	 
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState){
		View cameralistLayout=inflater.inflate(R.layout.main_camera_gridview,container,false);
		uid=((MainActivity)getActivity()).getUid();
		System.out.println("uid is "+uid);
		createAdapterData();
		listview=(ListView)cameralistLayout.findViewById(R.id.listviewCamera);
		addCamera=(ImageButton)cameralistLayout.findViewById(R.id.imgAddDevice);
		adapter=new SimpleAdapter(getActivity(), SystemValue.cameraList, R.layout.mian_camera_listview,
				new String[]{"uid","img","cameraName","status"},
				new int[]{R.id.camera_info_uid,R.id.imagecamer,R.id.camera_info_name,
			R.id.camera_info_status});				
		listview.setAdapter(adapter);
		
		
		
		listview.setOnItemClickListener(new OnItemClickListener(){			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ListView listView=(ListView)parent;
				HashMap<String,String> map=(HashMap<String,String>) listView.getItemAtPosition(position);
				String id1=new String();
				id1=map.get("uid");
				String status=new String();
				status=map.get("status");
				if(status.equals("在线")){
					
					Intent intent = new Intent(getActivity(),
							PlayActivity.class);
					Bundle mBundle=new Bundle();
					mBundle.putSerializable("cameraInfo", id1);
					intent.putExtras(mBundle);					
					getActivity().startActivity(intent);
					
				}else{
				/*Intent i=new Intent();
				i.setClass(getActivity(), AddCameraActivity.class);
				Bundle mBundle=new Bundle();
				mBundle.putSerializable("cameraInfo", id1);
				i.putExtras(mBundle);
				getActivity().startActivity(i);	*/
				connectcamera.ConnectCamera(id1,position);	
				}
				
				
				
			}
			
		});
		addCamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
		
		return cameralistLayout;
				
	}
	
	public void createAdapterData(){
		SystemValue.cameraList.clear();
		Map<String,Object> map4=new HashMap<String,Object>();
		map4.put("uid","VSTC510355PLMDN");
		map4.put("cameraName",getResources().getString(R.string.ipcam_name_1));
		map4.put("status", getResources().getString(R.string.click_to_connect));
		SystemValue.cameraList.add(map4);
		
		Map<String,Object> map5=new HashMap<String,Object>();
		map5.put("uid","VSTC204017UCCVJ");
		map5.put("cameraName",getResources().getString(R.string.ipcam_name_2));
		map5.put("status", getResources().getString(R.string.click_to_connect));
		SystemValue.cameraList.add(map5);
		
		Map<String,Object> map6=new HashMap<String,Object>();
		map6.put("uid","VSTC509968JDLFF");
		map6.put("cameraName",getResources().getString(R.string.ipcam_name_3));
		map6.put("status", getResources().getString(R.string.click_to_connect));
		SystemValue.cameraList.add(map6);
		
		Map<String,Object> map7=new HashMap<String,Object>();
		map7.put("uid","VSTC510372DTJBC");
		map7.put("cameraName",getResources().getString(R.string.ipcam_name_4));
		map7.put("status", getResources().getString(R.string.click_to_connect));
		SystemValue.cameraList.add(map7);
		
		Map<String,Object> map8=new HashMap<String,Object>();
		map8.put("uid","VSTC484181CBEJJ");
		map8.put("cameraName",getResources().getString(R.string.ipcam_name_5));
		map8.put("status", getResources().getString(R.string.click_to_connect));
		SystemValue.cameraList.add(map8);
		
	}
	
	public void refresh(){
		adapter.notifyDataSetChanged();
		Log.d("CameralistFragment refresh()", "excute refresh()");
	}
	
	private void changeStatus(String s){
		
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			connectcamera=(ConnectCameraListener)activity;
			
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+"must implement ConnectCammeraListener");
		}
	}
	

	
	
	
	
}
