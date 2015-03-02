package com.ipcamer.demo1;

import java.io.File;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;


public class PictureFragment extends Fragment{
	private com.example.hsv1.HSVLayout picGallery;
	//public ImageSwitcher imageSwitcher = null; 
	private TextView filename;
	public ImageView bigpicture;
	private int screenwidth;
	private int height;
	private String filetime;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState){
		View picturelayout=inflater.inflate(R.layout.picture_fragment,container,false);
		picGallery=(com.example.hsv1.HSVLayout)picturelayout.findViewById(R.id.movieLayout);
		bigpicture=(ImageView)picturelayout.findViewById(R.id.image_swithcer);
		filename=(TextView)picturelayout.findViewById(R.id.picname);
		String targetPath="/mnt/sdcard/DCIM/Camera/takepic/";
        Log.d("图片锟斤拷址",targetPath);
        
        //Toast.makeText(getActivity(), targetPath,Toast.LENGTH_LONG).show();
        File targetDirector=new File(targetPath);
        
        if(!targetDirector.exists()){
        	filename.setText("锟斤拷锟斤拷锟斤拷锟斤拷锟酵计�");
        }else{
        File[] files=targetDirector.listFiles();
        Log.d("图片锟侥硷拷锟斤拷锟斤拷",files.length+"");
       // DisplayMetrics dm1 = new DisplayMetrics();
        DisplayMetrics dm =getResources().getDisplayMetrics(); 
        screenwidth=dm.widthPixels;
        System.out.println("width is"+screenwidth);
        height=(int) (screenwidth*0.75);
        /*for(File file:files){
        	picGallery.addView(insertPhoto(file.getAbsolutePath()));
        }*/
        for(int i=0;i<files.length;i++){
        	
        	picGallery.addView(insertPhoto(files[i].getAbsolutePath()));
        	        	       	
        }
      /*  picGallery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("锟揭碉拷锟斤拷锟�");
				
				
			}
		});*/
        
        }
        
		return picturelayout;				
	}
	
	
	
	
	private View insertPhoto(final String absolutePath) {
		// TODO Auto-generated method stub
		Bitmap bm=decodeSampleBitmapFromUri(absolutePath,200,200);
		LinearLayout layout=new LinearLayout(getActivity());
		layout.setLayoutParams(new LayoutParams(250,250));
		layout.setGravity(Gravity.CENTER);
		
		ImageView imageView=new ImageView(getActivity());
		imageView.setLayoutParams(new LayoutParams(220,220));
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageBitmap(bm);
		
		layout.addView(imageView);
		
		layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("锟揭碉拷锟斤拷锟�"+absolutePath);
				String temp[]=null;
				temp=absolutePath.split("takepic");
				filetime=temp[1];
				filename.setText(filetime);
				
				Bitmap bitmap=decodeSampleBitmapFromUri(absolutePath, screenwidth, height);
				bigpicture.setImageBitmap(bitmap);
				
			}
		});
		return layout;
	}
	private Bitmap decodeSampleBitmapFromUri(String absolutePath, int reqWidth, int reqHeight) {
		// TODO Auto-generated method stub
		Bitmap bm=null;
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final Options options=new Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(absolutePath,options);
		
		// Calculate inSampleSize
		options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds=false;
		bm=BitmapFactory.decodeFile(absolutePath,options);
		return bm;
	}
	private int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		// TODO Auto-generated method stub
		// Raw height and width of image
		final int height=options.outHeight;
		final int width=options.outWidth;
		int inSampleSize=1;
		
		if(height>reqHeight||width>reqWidth){
			if(width>height){
				inSampleSize=Math.round((float)height/(float)reqHeight);
			}else{
				inSampleSize = Math.round((float)width / (float)reqWidth);
			}
		}
		return inSampleSize;
	}
	
	
}
