package com.petcare.demo1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import org.xml.sax.InputSource;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateVer extends Activity {
	private static final String TAG = "DOWNLOADAPK";
	private String PastVersion;
	private String NowVersion;
	public ProgressDialog pBar;
	private String currentFilePath = "";
	private String fileEx = "";
	private String fileNa = "";
	private String strURL = "";
	private String VersionUri = "";
	private Context mContext;
	private final String fileVer = "ver.cfg";
	DownloadManager dManager;
	

	public UpdateVer(String urlapk, String urlver, final Context context,DownloadManager dManager) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String ver = "?ver=" + df.format(new Date());// ��Ҫ�Ǳܿ��ֻ��Ļ���
		strURL = urlapk + ver;
		VersionUri = urlver;
		mContext = context;
		this.dManager=dManager;
	}

	public void checkVer() {
		// ����Version��ҳ����ȡ�汾��
		getVersionxml(VersionUri);
	}

	private void compareVer() {
		load();//��õ�ǰ�汾��

		// �������°汾��ʱ��
		if (PastVersion != null && !PastVersion.equals(NowVersion)) {
			Dialog dialog = new AlertDialog.Builder(mContext)
					.setTitle("ϵͳ����")
					.setMessage(
							String.format("�����°汾%s��Ŀǰ�汾Ϊ%s������£�", NowVersion,
									PastVersion))
					// ��������
					// ����ȷ����ť
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								
									//��������
									
									Uri uri = Uri.parse("http://qxz.kemao.com.cn/PlayerOCX/tingyuan.apk?ver=20140729154049");
									DownloadManager.Request request = new Request(uri);
									// ��������·�����ļ���
									request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "NiceApp.apk");
									request.setDescription("");
									request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
									request.setMimeType("application/vnd.android.package-archive");
									// ����Ϊ�ɱ�ý��ɨ�����ҵ�
									request.allowScanningByMediaScanner();
									// ����Ϊ�ɼ��Ϳɹ���
									request.setVisibleInDownloadsUi(true);
									long refernece = dManager.enqueue(request);
									// �ѵ�ǰ���ص�ID��������
									SharedPreferences sPreferences = mContext.getSharedPreferences("downloadcomplete", 0);
									sPreferences.edit().putLong("refernece", refernece).commit();
									dialog.dismiss();
									
								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// ���"ȡ��"��ť֮���˳�����
								}
							}).create();// ����
			// ��ʾ�Ի���
			dialog.show();
		} else {
			/*Toast.makeText(mContext, String.format("��ǰΪ���°汾%s", PastVersion),
					Toast.LENGTH_LONG).show();*/
		}
	}




	

	/* �ж��ļ�MimeType��method */
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* ȡ����չ�� */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* ����չ�������;���MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		/* ����޷�ֱ�Ӵ򿪣�����������嵥��ʹ����ѡ�� */
		if (!end.equals("apk")) {
			type += "/*";
		}
		return type;
	}

	private void getVersionxml(String resourceUrl) {
		GetVer gv = new GetVer();
		gv.execute(resourceUrl);
	}

	private boolean load() {
		Properties properties = new Properties();
		try {
			InputStream stream = mContext.getAssets().open(fileVer);
			// FileInputStream stream = mContext.openFileInput(fileVer);
			// ��ȡ�ļ�����
			properties.load(stream);
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
		PastVersion = String.valueOf(properties.get("Version").toString());
		return true;
	}

	// ==========================================================================
	// GetVer
	// ==========================================================================
	class GetVer extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... urlVer) {
			String db = null;
			URL url = null;

			try {
				url = new URL(urlVer[0]);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputSource is = null;
			try {
				is = new InputSource(url.openStream());
				is.setEncoding("UTF-8");
				db = SAXGetVersionService.readRssXml(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return db;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			NowVersion = result;
			compareVer();
		}
	}
}
