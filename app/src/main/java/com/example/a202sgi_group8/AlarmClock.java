package com.example.a202sgi_group8;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

import static com.example.a202sgi_group8.AlarmBroadcastReceiver.CLOCK_ID;
import static com.example.a202sgi_group8.AlarmBroadcastReceiver.TITLE;
import static com.example.a202sgi_group8.AlarmBroadcastReceiver.RINGTONE;
import static com.example.a202sgi_group8.AlarmBroadcastReceiver.MISSION;
import static com.example.a202sgi_group8.AlarmBroadcastReceiver.VIBRATE;


public class AlarmClock {

    private String mAlarmId;
    private int mAlarmId2;

    private String mAlarmTitle;

    private boolean mIsOnAlarm;

    private int mAlarmWakeHr;
    private int mAlarmWakeMin;

    private int mAlarmRingtone;
    private int mAlarmMission;

    private boolean mIsAlarmVibrate;
    private boolean mStarted;

    public AlarmClock() {

    }

    public AlarmClock(String mAlarmId, int mAlarmId2, String mAlarmTitle, boolean mIsOnAlarm, int mAlarmWakeHr, int mAlarmWakeMin, int mAlarmRingtone, int mAlarmMission, boolean mIsAlarmVibrate, boolean mStarted) {

        this.mAlarmId = mAlarmId;
        this.mAlarmId2 = mAlarmId2;

        this.mAlarmTitle = mAlarmTitle;

        this.mIsOnAlarm = mIsOnAlarm;

        this.mAlarmWakeHr = mAlarmWakeHr;
        this.mAlarmWakeMin = mAlarmWakeMin;

        this.mAlarmRingtone = mAlarmRingtone;
        this.mAlarmMission = mAlarmMission;

        this.mIsAlarmVibrate = mIsAlarmVibrate;
        this.mStarted = mStarted;

    }

    public String getAlarmId() {

        return mAlarmId;

    }

    public void setAlarmId(String alarmId) {
        mAlarmId = alarmId;
    }

    public void setAlarmId2(int alarmId2) {
        mAlarmId2 = alarmId2;
    }

    public int getAlarmId2() {

        return mAlarmId2;

    }

    public String getAlarmTitle() {
        return mAlarmTitle;
    }

    public void setAlarmTitle(String alarmTitle) {
        mAlarmTitle = alarmTitle;
    }

    public boolean isOnAlarm() {
        return mIsOnAlarm;
    }

    public void setOnAlarm(boolean onAlarm) {
        mIsOnAlarm = onAlarm;
    }

    public int getAlarmWakeHr() {
        return mAlarmWakeHr;
    }

    public void setAlarmWakeHr(int alarmWakeHr) {
        mAlarmWakeHr = alarmWakeHr;
    }

    public int getAlarmWakeMin() {
        return mAlarmWakeMin;
    }

    public void setAlarmWakeMin(int alarmWakeMin) {
        mAlarmWakeMin = alarmWakeMin;
    }

    public int getAlarmRingtone() {
        return mAlarmRingtone;
    }

    public void setAlarmRingtone(int alarmRingtone) {
        mAlarmRingtone = alarmRingtone;
    }

    public int getAlarmMission() {
        return mAlarmMission;
    }

    public void setAlarmMission(int alarmMission) {
        mAlarmMission = alarmMission;
    }

    public boolean isAlarmVibrate() {
        return mIsAlarmVibrate;
    }

    public void setAlarmVibrate(boolean alarmVibrate) {
        mIsAlarmVibrate = alarmVibrate;
    }

    public void setStarted(boolean started) {mStarted = started;}

    public boolean isStarted() {return mStarted; }

    public void schedule(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, AlarmBroadcastReceiver.class);
        i.putExtra(TITLE, mAlarmTitle);
        i.putExtra(RINGTONE, mAlarmRingtone);
        i.putExtra(MISSION, mAlarmMission);
        i.putExtra(VIBRATE, mIsAlarmVibrate);
        i.putExtra(CLOCK_ID, mAlarmId);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, mAlarmId2, i, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, mAlarmWakeHr);
        cal.set(Calendar.MINUTE, mAlarmWakeMin);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        }

        String toastText = null;
        try {
            toastText = String.format("One Time Alarm \'%s\' scheduled for %s at %02d:%02d", mAlarmTitle, DayUtil.toDay(cal.get(Calendar.DAY_OF_WEEK)), mAlarmWakeHr, mAlarmWakeMin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

        alarmMgr.setExact(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                alarmPendingIntent
        );

        this.mStarted = true;
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, mAlarmId2, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        this.mStarted = false;

        String toastText = String.format("Alarm titled \'%s\' cancelled for %02d:%02d", mAlarmTitle, mAlarmWakeHr, mAlarmWakeMin);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("cancel", toastText);
    }

    public void editAlarm(Context context, String mAlarmTitle, int mAlarmWakeHr, int mAlarmWakeMin, int mRingtone, int mMission, boolean mVibrate) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, mAlarmId2, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        this.mStarted = false;

        setAlarmTitle(mAlarmTitle);
        setAlarmWakeHr(mAlarmWakeHr);
        setAlarmWakeMin(mAlarmWakeMin);
        setAlarmId2(new Random().nextInt(Integer.MAX_VALUE));
        setAlarmRingtone(mRingtone);
        setAlarmMission(mMission);
        setAlarmVibrate(mVibrate);

        scheduleNewAlarm(context);

    }

    private void scheduleNewAlarm(Context context) {
        AlarmManager am2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmBroadcastReceiver.class);
        i.putExtra(TITLE, mAlarmTitle);
        i.putExtra(RINGTONE, mAlarmRingtone);
        i.putExtra(MISSION, mAlarmMission);
        i.putExtra(VIBRATE, mIsAlarmVibrate);
        i.putExtra(CLOCK_ID, mAlarmId);

        PendingIntent api2 = PendingIntent.getBroadcast(context, mAlarmId2, i, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, mAlarmWakeHr);
        cal.set(Calendar.MINUTE, mAlarmWakeMin);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        }

        am2.setExact(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                api2
        );

        this.mStarted = true;
    }
}


