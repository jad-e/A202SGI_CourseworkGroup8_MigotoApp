package com.example.a202sgi_group8;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.a202sgi_group8.AlarmBroadcastReceiver.CLOCK_ID;
import static com.example.a202sgi_group8.AlarmBroadcastReceiver.TITLE;
import static com.example.a202sgi_group8.AlarmBroadcastReceiver.RINGTONE;
import static com.example.a202sgi_group8.AlarmBroadcastReceiver.MISSION;
import static com.example.a202sgi_group8.AlarmBroadcastReceiver.VIBRATE;

import static com.example.a202sgi_group8.App.CHANNEL_ID;

public class AlarmService extends Service {
    private final String SP_AUTO_DISMISS_KEY = "sp_auto_dismiss_key";
    private final String SP_LANGUAGE_KEY = "sp_language_key";

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    private int timeOut;

    DatabaseReference database;

    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    private Context context;
    private Resources resources;

    private int language;

    @Override
    public void onCreate() {
        super.onCreate();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone_1);
//        mediaPlayer.setLooping(true);
//
//        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        //set language
        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(this, "en");
                    resources = context.getResources();
                    break;
                case 2:
                    context = LocaleHelper.setLocale(this, "zh");
                    resources = context.getResources();
                    break;

            }

        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(this, "en");
            resources = context.getResources();

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ringtonePref = intent.getIntExtra(RINGTONE, 1);
        int missionPref = intent.getIntExtra(MISSION, 1);
        boolean vibratePref = intent.getBooleanExtra(VIBRATE, false);
        String alarmId = intent.getStringExtra(CLOCK_ID);

        switch(ringtonePref) {
            case 1:
                mediaPlayer = MediaPlayer.create(this, R.raw.ringtone_1);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this, R.raw.ringtone_2);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this, R.raw.ringtone_3);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this, R.raw.ringtone_4);
                break;
        }

        mediaPlayer.setLooping(true);

        mediaPlayer.start();

        if (vibratePref) {
            long[] pattern = { 0, 100, 1000 };
            vibrator.vibrate(pattern, 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground(intent, missionPref);
        else
            startForeground(1, new Notification());

        database = FirebaseDatabase.getInstance().getReference("AlarmClocks");
        HashMap hashMap = new HashMap();
        hashMap.put("onAlarm", false);

        database.child(alarmId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startMyOwnForeground(Intent intent, int mission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chan);
        }

        Intent notificationIntent;

        switch (mission) {
            case 1:
                notificationIntent = new Intent(this, MissionShake.class);
                break;
            case 2:
                notificationIntent = new Intent(this, MissionMath.class);
                break;
            default:
                notificationIntent = new Intent(this, MissionShake.class);
                break;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        String alarmTitle = String.format("%s", intent.getStringExtra(TITLE));

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText(resources.getString(R.string.alarm_ring_notification))
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(2, notification);
    }
}
