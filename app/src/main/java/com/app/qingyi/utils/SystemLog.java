package com.app.qingyi.utils;

import android.util.Log;

//Created by cjq on 17/8/23.

public class SystemLog {

	public static boolean DEBUG = true;

	public static boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean dEBUG) {
		DEBUG = dEBUG;
	}

	public static void Log(String data) {
		if (DEBUG) {
			Log.d("调试", data);
//			System.out.println("调试:" + data);
		}
	}

}
