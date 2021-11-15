package com.example.a202sgi_group8;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

public class App extends Application {

    private final String SP_THEME_KEY = "sp_theme_key";
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";

    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    private int theme;

    @Override
    public void onCreate() {
        super.onCreate();

        mPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        if (mPreferences.contains(SP_THEME_KEY)) { //if got this key

            theme = mPreferences.getInt(SP_THEME_KEY, 2);

            switch(theme){

                case 1: //dark
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case 2: //light
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }

        } else { //if don't have this key (app first launch)

            theme = 2; //by default its light mode

            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(SP_THEME_KEY, theme);
            spEditor.apply();

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

