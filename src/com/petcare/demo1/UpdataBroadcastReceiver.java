package com.petcare.demo1;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class UpdataBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint("NewApi")
    public void onReceive(Context context, Intent intent) {
        long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        SharedPreferences sPreferences = context.getSharedPreferences("downloadcomplete", 0);
        long refernece = sPreferences.getLong("refernece", 0);
        if (refernece == myDwonloadID) {
            String serviceString = Context.DOWNLOAD_SERVICE;
            DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);      }
    }

}
