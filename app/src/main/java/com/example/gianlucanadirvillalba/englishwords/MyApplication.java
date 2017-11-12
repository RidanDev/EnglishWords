package com.example.gianlucanadirvillalba.englishwords;

import android.app.Application;
import android.content.Context;

/**
 * Created by gianlucanadirvillalba on 12/11/2017.
 */

public class MyApplication extends Application
{
    private static MyApplication sInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance()
    {
        return sInstance;
    }

    public static Context getAppContext()
    {
        return sInstance.getApplicationContext();
    }
}
