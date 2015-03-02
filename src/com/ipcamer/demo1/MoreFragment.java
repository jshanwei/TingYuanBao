package com.ipcamer.demo1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MoreFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState){
		View moreLayout=inflater.inflate(R.layout.more_fragment,container,false);
		return moreLayout;
				
	}
}
