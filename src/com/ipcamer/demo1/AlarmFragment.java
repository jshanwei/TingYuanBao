package com.ipcamer.demo1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AlarmFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState){
		View alarmLayout=inflater.inflate(R.layout.alarm_fragment,container,false);
		return alarmLayout;
				
	}
}
