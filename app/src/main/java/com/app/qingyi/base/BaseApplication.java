package com.app.qingyi.base;

import android.app.Activity;
import android.app.Application;

import com.app.qingyi.http.httputils.HttpsUtil;
import com.app.qingyi.utils.CrashHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ylkjcjq on 2017/12/4.
 */

public class BaseApplication extends Application {

    /**
     * activity栈保存
     */
    public List<Activity> activityStack = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
//        HttpsUtil.initHttpsUrlConnection(this);
//        CrashHandler.getInstance().init(this);
    }

    public void addActivity(Activity activity){
        activityStack.add(activity);
    }

    public void exitApp(){
        for(Activity activity : activityStack){
            activity.finish();
        }
    }
}
