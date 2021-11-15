package com.example.a202sgi_group8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String TITLE = "TITLE";
    public static final String RINGTONE = "RINGTONE";
    public static final String MISSION = "MISSION";
    public static final String VIBRATE = "VIBRATE";
    public static final String CLOCK_ID = "ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            startRescheduleAlarmsService(context);
        }
        else {
            startAlarmService(context, intent);
        }
    }


    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE));
        intentService.putExtra(RINGTONE, intent.getIntExtra(RINGTONE, 1));
        intentService.putExtra(MISSION, intent.getIntExtra(MISSION, 1));
        intentService.putExtra(VIBRATE, intent.getBooleanExtra(VIBRATE, false));
        intentService.putExtra(CLOCK_ID, intent.getStringExtra(CLOCK_ID));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}
