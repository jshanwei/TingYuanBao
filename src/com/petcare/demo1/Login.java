package com.petcare.demo1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.ipcamer.demo1.MainActivity;
import com.ipcamer.demo1.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Login extends Activity {
	public Handler handler_chk_user;
	public static final String appName = "锟斤拷锟斤拷通";
	  public static Handler handler;
	  public static int login;
	  public static int logins = 0;
	  // private UpdateManager mUpdateManager;
	  private String methodName;
	  private String methodPwd;
	  private String nameSpace;
	  private String params;
	  //private CustomProgressDialog progressDialog;
	  private String result = null;
	  String suc = null;
	  String value = null;
	  EditText account;
	  EditText pwd;
	  private int check=0;
	  private String tel;
	  private CheckBox mcheck;
	  private RelativeLayout login_window;
	  private RelativeLayout progressbar;
	  private LinearLayout two;
	  private String receivemsg;
	  private String UID;
	  private String reslut;
	  private String resultStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Button login;
		
		account=(EditText)findViewById(R.id.edtmail);
		pwd=(EditText)findViewById(R.id.edtpwd);
		login=(Button)findViewById(R.id.loginButton);
		mcheck=(CheckBox)findViewById(R.id.cbo_rem_pwd);
		login_window=(RelativeLayout)findViewById(R.id.one);
		progressbar=(RelativeLayout)findViewById(R.id.progressbar1);
		two=(LinearLayout)findViewById(R.id.two);
		login.setOnClickListener(new ButtonClick());
		
		
		mcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					check=1;
				}else{
					check=0;
				}
				
			}
		});
	}
	
	
	
	
	class ButtonClick implements OnClickListener
	{
		public void onClick(View v)
		{
			switch(v.getId()){
			case R.id.loginButton:
				//锟斤拷录锟侥达拷锟斤拷
				login_window.setVisibility(8);
				progressbar.setVisibility(0);
				RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				        ViewGroup.LayoutParams.WRAP_CONTENT);
				p.addRule(RelativeLayout.BELOW, R.id.progressbar1);
				two.setLayoutParams(p);
				methodName=account.getText().toString();
				methodPwd=pwd.getText().toString();	
				
				//Toast.makeText(Login.this,"锟斤拷录锟斤拷.......",Toast.LENGTH_SHORT).show();
				new Thread(new LoginThread(0, methodName, methodPwd)).start();
				handler_chk_user = new Handler() {
					// Handler锟斤拷锟秸碉拷锟斤拷息时锟侥达拷锟斤拷
					@Override
					public void handleMessage(Message msg) {
						Log.d("system", String.valueOf(msg.what));
						// 锟截憋拷ProgressDialog
						//progressDialog.dismiss();
						String recmsg=(String) msg.getData().get("user");
						System.out.println("recmsg is "+recmsg);
						String []temp=null;
						temp=recmsg.split("\\|");
						try{
						UID=temp[1];
						}catch(Exception e){
							System.out.println(e);
						}
						result=temp[0];
						System.out.println("UID is "+UID);
						System.out.println("Result is "+temp[0]);
						
						if(result.equals("Login Success")){
							System.out.println("锟矫伙拷锟斤拷锟斤拷锟斤拷锟斤拷确");
							Intent i = new Intent();
							i.setClass(Login.this, MainActivity.class);
							Bundle mBundle = new Bundle(); 
							mBundle.putSerializable("user",UID);
							i.putExtras(mBundle);
							///
							Login.this.startActivity(i);
							Login.this.finish();															
						}else{
							login_window.setVisibility(0);
							progressbar.setVisibility(8);	
							RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							        ViewGroup.LayoutParams.WRAP_CONTENT);
							p.addRule(RelativeLayout.BELOW, R.id.one);
							two.setLayoutParams(p);
							Toast.makeText(Login.this,"锟剿伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�",Toast.LENGTH_LONG).show();
						}
						
						
					}
				};				
				break;
			default:
				break;
			}
		}

	}
	
	/*public void clickRegister(View v){
		Intent intent=new Intent(Login.this,ConfirmTel.class);
		startActivity(intent);
	}*/
		
	
		// 锟斤拷证锟缴癸拷锟斤拷锟斤拷一锟斤拷锟铰碉拷Activity
		public void checkSuccess(User u) {
			if(u.getType().equals("2")||u.getType().equals("0")){
				Intent i = new Intent();
				//i.setClass(Login.this, HelloUser.class);
				Bundle mBundle = new Bundle(); 
				mBundle.putSerializable("user",u);
				i.putExtras(mBundle);
				Login.this.startActivity(i);
				Login.this.finish();				
			}else{
				login_window.setVisibility(0);
				progressbar.setVisibility(8);	
				RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				        ViewGroup.LayoutParams.WRAP_CONTENT);
				p.addRule(RelativeLayout.BELOW, R.id.one);
				two.setLayoutParams(p);
				Toast.makeText(Login.this,"锟剿号伙拷锟斤拷锟斤拷锟斤拷锟�",Toast.LENGTH_SHORT).show();
			}
			
				
		}
		
		
		public void writeXml(String tel,String pwd,int check,SharedPreferences sp){
			Editor editor=sp.edit();
			editor.putString("TEL_PWD",pwd);
			System.out.println("tel is"+tel);
			editor.putString("TEL_KEY", tel);
			editor.putInt("CHECK_KEY", check);
			editor.commit();
			
		}
		
		public class LoginThread extends Thread {
			int times = 0;// 为锟剿斤拷锟絊ocket锟斤拷锟斤拷锟解，锟斤拷锟斤拷锟斤拷锟皆达拷锟斤拷
			String username;
			String psw;
			String postValue;

			public LoginThread(int times, String username, String psw) {
				this.times = times;
				this.username = username;
				this.psw = psw;
				this.postValue="Login"+"|"+username+"|"+psw+"|";
			}

			@Override
			public void run() {
				Looper.prepare(); // 锟斤拷为锟斤拷要锟斤拷锟斤拷锟斤拷UI锟斤拷锟斤拷执锟叫碉拷锟竭程ｏ拷锟斤拷浠帮拷潜锟揭拷锟斤拷锟�
				
				User user = null;				
				try {
					Socket socket=new Socket("115.28.145.60",6100);
					try{
						PrintWriter out =new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										socket.getOutputStream())),true);
						out.println(postValue);
						
						
						out.flush();
						
						System.out.println("TCP锟斤拷锟酵成癸拷");
						//锟斤拷锟秸凤拷锟斤拷锟斤拷锟斤拷息
						BufferedReader in=new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						System.out.println("锟矫碉拷锟斤拷锟斤拷锟斤拷锟斤拷息");
						//锟矫碉拷锟斤拷锟斤拷锟斤拷锟斤拷息
						char[] charArray=new char[100];
						int read_rst=in.read(charArray);
						resultStr = new String(charArray, 0, read_rst); 
						//receivemsg=in.readLine();
						System.out.println("锟斤拷锟斤拷息转锟斤拷为String锟斤拷式");
						System.out.println("TCP锟秸碉拷锟斤拷锟斤拷息锟斤拷"+resultStr);
						
						
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("锟节凤拷锟斤拷tcp时锟斤拷锟斤拷");
					}finally{
						socket.close();
						System.out.println("Client:Socket closed");  
					}					
				}
				catch (Exception e) {
					e.printStackTrace();
					System.out.println("锟斤拷锟斤拷锟届常");
					return;
				}
				
				Message message = new Message();
				Bundle data = new Bundle();
				data.putSerializable("user", resultStr);
				message.setData(data);
				// 锟斤拷目锟斤拷handler锟斤拷锟斤拷锟斤拷息
				handler_chk_user.sendMessage(message);//锟斤拷应锟竭筹拷锟斤拷息锟斤拷handler锟斤拷锟斤拷锟斤拷锟斤拷User锟斤拷锟斤拷锟竭硷拷锟斤拷锟斤拷也锟斤拷锟斤拷锟角革拷handler锟叫ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷			
				super.run();
				Looper.loop();// 锟斤拷为锟斤拷要锟斤拷锟斤拷锟斤拷UI锟斤拷锟斤拷执锟叫碉拷锟竭程ｏ拷锟斤拷浠帮拷潜锟揭拷锟斤拷锟�
			}
		}
		
		
		
	}
	

