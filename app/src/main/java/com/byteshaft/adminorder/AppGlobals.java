package com.byteshaft.adminorder;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseInstallation;


public class AppGlobals extends Application {

    private static Context sContext;
    public static final String APP_ID = "EieYzwuvJCqr2xFlwy8n7MAuofRj4LHa1F1MCudO";
    public static final String CLIENT_ID = "9sIi4bCDTLYXzd5y0JG5pBaAwaqzJVzlZnfiTvdk";
    public static final int NOTIFICATION_ID = 2112;
    public static final String PUSH_RESPONSE = "we have received  your request";
    public static String sSenderId;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Parse.initialize(this, APP_ID , CLIENT_ID);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("admin", "test");
        installation.put("test", "test");
        installation.saveInBackground();
    }

    public static Context getContext() {
        return sContext;
    }

    public static String getLogTag(Class aClass) {
        return  aClass.getName();

    }
}
