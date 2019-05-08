package com.sargis.kh.guardian;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class GuardianApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
//        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }


    public static Context getContext() {
        return context;
    }
}
