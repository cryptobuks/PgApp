package com.app.qingyi.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

import es.dmoral.toasty.Toasty;

public class DownLoadBR extends BroadcastReceiver {
	/**
	 * Created with IntelliJ IDEA. User: Administrator Date: 15-10-14 Time:
	 * 15:30 To change this template use File | Settings | File Templates.
	 */
	public void onReceive(Context context, Intent intent) {
		Intent install = new Intent(Intent.ACTION_VIEW);
		String pathString = intent.getStringExtra("downloadFile");
		File apkFile = new File(pathString);
		Log.e("e","-----log-----");
		Utils.setPermission(pathString);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri contentUri = FileProvider.getUriForFile(context, context.getApplicationInfo().processName+".install.fileProvider", apkFile);
			install.setDataAndType(contentUri, "application/vnd.android.package-archive");
		} else {
			install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
			install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(install);
	}

}
