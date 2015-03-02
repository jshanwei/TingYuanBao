package com.example.hsv1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ipcamer.demo1.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Ϊ���ȶ���������
 * @author Administrator
 *
 */
public class HSVAdapter extends BaseAdapter {

	private List<Map<String,Object>> list;
	private Context context;
	public HSVAdapter(Context context){
		this.context=context;
		this.list=new ArrayList<Map<String,Object>>();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Map<String,Object> getItem(int location) {
		return list.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void addObject(Map<String,Object> map){
		list.add(map);
		notifyDataSetChanged();
	}
	@Override
	public View getView(int location, View arg1, ViewGroup arg2) {
		View view = LayoutInflater.from(context).inflate(R.layout.hsv,null);
		ImageView image=(ImageView)view.findViewById(R.id.movie_image);
		Map<String,Object> map=getItem(location); //��ȡ��ǰ��Item
		//image.setBackground((Drawable)map.get("image"));
		image.setBackgroundResource((Integer) map.get("image"));
		return view;
	}

}
