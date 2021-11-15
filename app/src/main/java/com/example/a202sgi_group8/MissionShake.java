package com.example.a202sgi_group8;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class MissionShake extends AppCompatActivity {
    private ProgressBar progBar;
    private Button dismissBtn;
    private ImageView mClock;
    private TextView mainTitle;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private double accelerationCurrentValue;
    private double accelerationPreviousValue;

    private final String SP_SHAKE_DIFFICULTY_KEY = "sp_shake_difficulty_key";
    private final String SP_LANGUAGE_KEY = "sp_language_key";
    private final String SP_THEME_KEY = "sp_theme_key";

    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    private Context context;
    private Resources resources;

    private int difficulty = 2;

    //
    // values
    //
    private int progr = 0;
    private int maxVal = 2500;

    private int language;
    private int theme;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        if (mPreferences.contains(SP_THEME_KEY)) { //if got this key

            theme = mPreferences.getInt(SP_THEME_KEY, 2);

            switch (theme) {

                case 1: //dark
                    setTheme(R.style.darkTheme);
                    break;
                case 2: //light
                    setTheme(R.style.appTheme);
                    break;
            }

        } else { //if don't have this key (app first launch)

            setTheme(R.style.appTheme);

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_shake);

        progBar = findViewById(R.id.progress_bar);
        dismissBtn = findViewById(R.id.dismiss_shake_btn);
        mClock = findViewById(R.id.activity_ring_clock);
        mainTitle = findViewById(R.id.textView);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        //set language
        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(MissionShake.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mainTitle.setText(resources.getString(R.string.mission_shake_desc));
                    dismissBtn.setText(resources.getString(R.string.dismiss_btn));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(MissionShake.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mainTitle.setText(resources.getString(R.string.mission_shake_desc));
                    dismissBtn.setText(resources.getString(R.string.dismiss_btn));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(MissionShake.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            mainTitle.setText(resources.getString(R.string.mission_shake_desc));
            dismissBtn.setText(resources.getString(R.string.dismiss_btn));

        }

        if (mPreferences.contains(SP_SHAKE_DIFFICULTY_KEY)) {

            difficulty = mPreferences.getInt(SP_SHAKE_DIFFICULTY_KEY, 2);

        } else {

            difficulty = 2;

        }

        switch (difficulty) {
            case 1:
                maxVal = 2500;
                break;
            case 2:
                maxVal = 4500;
                break;
            case 3:
                maxVal = 7500;
                break;

        }

        progBar.setMax(maxVal);
        progBar.setProgress(progr); //initialize progress bar

        dismissBtn.setOnClickListener(new View.OnClickListener() {  //dismiss mission
            @Override
            public void onClick(View v) {
                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);

                finish();
                Toast.makeText(MissionShake.this, resources.getString(R.string.mission_dismissed), Toast.LENGTH_SHORT).show();

                failChallenge();

            }
        });

        animateClock();

    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            //
            // get raw values
            //
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            //
            // perform calculations on raw values
            //
            accelerationCurrentValue = Math.sqrt(((x * x) + (y * y) + (z * z)));
            double changeInAccelleration = Math.abs(accelerationCurrentValue - accelerationPreviousValue);
            accelerationPreviousValue = accelerationCurrentValue;

            //
            // values to be used
            //
            int change = (int) changeInAccelleration;

            //
            // set progress bar
            //
            if (progr < maxVal) {  //shaking but progress bar still not full yet, continue add

                progr += change;
                progBar.setProgress(progr);

            } else { //shaking but progress bar full, mission complete!
                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);

                finish();
                Toast.makeText(MissionShake.this, resources.getString(R.string.mission_completed), Toast.LENGTH_SHORT).show();

                succeedChallenge();

            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(sensorEventListener);

    }

    private void animateClock() {
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(mClock, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }

    private void succeedChallenge() {
        // add a new challenge
        database = FirebaseDatabase.getInstance().getReference("Challenges");

        // challenge id
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String id = ft.format(dNow);

        // 1: success, 2: fail
        int status = 1;

        // finalizedDate (get current date)
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date();
        String dateStr = sdf.format(date);

        // get current time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.now();
        String alarmTime = dtf.format(localTime);

        //push to database
        Challenge mChallenge = new Challenge(id, status, dateStr, alarmTime);
        database.child(mChallenge.getChallengeId()).setValue(mChallenge);
    }

    private void failChallenge() {
        // add a new challenge
        database = FirebaseDatabase.getInstance().getReference("Challenges");

        // challenge id
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String id = ft.format(dNow);

        // 1: success, 2: fail
        int status = 2;

        // finalizedDate (get current date)
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date();
        String dateStr = sdf.format(date);

        // get current time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.now();
        String alarmTime = dtf.format(localTime);

        //push to database
        Challenge mChallenge = new Challenge(id, status, dateStr, alarmTime);
        database.child(mChallenge.getChallengeId()).setValue(mChallenge);
    }

}