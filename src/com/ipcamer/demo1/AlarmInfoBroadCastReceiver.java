package com.ipcamer.demo1;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmInfoBroadCastReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (!paramIntent.getAction().equals("com.intent.ALARM"))
      return;
    String str1 = paramIntent.getStringExtra("camera_name");
    String str2 = paramIntent.getStringExtra("content");
    Toast.makeText(paramContext, str1 + "        " + str2, 3000).show();
  }
}
