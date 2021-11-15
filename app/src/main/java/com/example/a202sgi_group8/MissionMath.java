package com.example.a202sgi_group8;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class MissionMath extends AppCompatActivity {
    private final String SP_MATH_DIFFICULTY_KEY = "sp_math_difficulty_key";

    private final String SP_LANGUAGE_KEY = "sp_language_key";
    private final String SP_THEME_KEY = "sp_theme_key";

    private Button mDismiss;
    private Button mSolve;
    private ImageView mClock;

//    private Switch mSwitch;

    private TextView mainTitle;

    private TextView mQuestion;
    private EditText mAnswer;

    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    private Context context;
    private Resources resources;

    private int difficulty;
    private int n1 = 0;
    private int n2 = 0;
    private int n3 = 0;
    private int qAnswer = 0;
    private String ques;
    private int ans;

    private int language;
    private int theme;

//    private TextView mWakeUpTime;
//    private View horizontalDivider;
//    private TextView mAlarmTitle;
//    private ImageView mDropDown;
//    private ImageView mMissionIcon;
//    private TextView mMissionLabel;
//    private TextView mMissionDesc;
//    private ImageView mRingtoneIcon;
//    private TextView mRingtoneLabel;
//    private TextView mRingtoneDesc;
//    private ImageView mVibrateIcon;
//    private TextView mVibrateTextView;
//    private TextView mVibrateDesc;

    DatabaseReference database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

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
        setContentView(R.layout.activity_mission_math);


        mDismiss = findViewById(R.id.dismiss_math_btn);
        mSolve = findViewById(R.id.solve_math_btn);
        mClock = findViewById(R.id.activity_ring_clock);

        mainTitle = findViewById(R.id.textView);

        mQuestion = findViewById(R.id.mission_math_question);
        mAnswer = findViewById(R.id.mission_math_answer);

        mPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(MissionMath.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mainTitle.setText(resources.getString(R.string.mission_math_desc));
                    mAnswer.setHint(resources.getString(R.string.mission_math_hint));
                    mSolve.setText(resources.getString(R.string.solve_btn));
                    mDismiss.setText(resources.getString(R.string.dismiss_btn));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(MissionMath.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mainTitle.setText(resources.getString(R.string.mission_math_desc));
                    mAnswer.setHint(resources.getString(R.string.mission_math_hint));
                    mSolve.setText(resources.getString(R.string.solve_btn));
                    mDismiss.setText(resources.getString(R.string.dismiss_btn));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(MissionMath.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            mainTitle.setText(resources.getString(R.string.mission_math_desc));
            mAnswer.setHint(resources.getString(R.string.mission_math_hint));
            mSolve.setText(resources.getString(R.string.solve_btn));
            mDismiss.setText(resources.getString(R.string.dismiss_btn));

        }


        if (mPreferences.contains(SP_MATH_DIFFICULTY_KEY)) {

            difficulty = mPreferences.getInt(SP_MATH_DIFFICULTY_KEY, 2);

        } else { //if don't have this key (app first launch)

            difficulty = 2;

        }

        switch (difficulty) {
            case 1:
                n1 = new Random().nextInt(99) + 1;
                n2 = new Random().nextInt(99) + 1;
                ques = String.format("%d+%d=?", n1, n2);
                qAnswer = n1 + n2;
                break;
            case 2:
                n1 = new Random().nextInt(99) + 1;
                n2 = new Random().nextInt(99) + 1;
                n3 = new Random().nextInt(99) + 1;
                ques = String.format("%d+%d+%d=?", n1, n2, n3);
                qAnswer = n1 + n2 + n3;
                break;
            case 3:
                n1 = new Random().nextInt(99) + 1;
                n2 = new Random().nextInt(99) + 1;
                n3 = new Random().nextInt(99) + 1;
                ques = String.format("(%dx%d)+%d=?", n1, n2, n3);
                qAnswer = (n1 * n2) + n3;
                break;
        }

        mQuestion.setText(ques);

//        mSwitch = findViewById(R.id.alarm_is_on_toggle);
//
//        mWakeUpTime = findViewById(R.id.alarm_clock_wake_up_time);
//        horizontalDivider = findViewById(R.id.line_separator_horizontal);
//        mAlarmTitle = findViewById(R.id.list_item_alarm_clock_title_text_view);
//        mDropDown = findViewById(R.id.drop_down_for_more);
//        mMissionIcon = findViewById(R.id.mission_icon);
//        mMissionLabel = findViewById(R.id.mission_title);
//        mMissionDesc = findViewById(R.id.mission_details);
//        mRingtoneIcon = findViewById(R.id.ringtone_icon);
//        mRingtoneLabel = findViewById(R.id.ringtone_title);
//        mRingtoneDesc = findViewById(R.id.ringtone_details);
//        mVibrateIcon = findViewById(R.id.vibrate_icon);
//        mVibrateTextView = findViewById(R.id.vibrate_title);
//        mVibrateDesc = findViewById(R.id.vibrate_details);

        mSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans = Integer.parseInt(mAnswer.getText().toString());

                if (ans == qAnswer) {
                    Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                    getApplicationContext().stopService(intentService);

                    finish();
                    Toast.makeText(MissionMath.this, resources.getString(R.string.mission_completed), Toast.LENGTH_SHORT).show();

                    succeedChallenge();
                } else {
                    Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                    getApplicationContext().stopService(intentService);

                    finish();
                    Toast.makeText(MissionMath.this, resources.getString(R.string.mission_failed), Toast.LENGTH_SHORT).show();

                    failChallenge();
                }
            }
        });

        mDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);

//                //turn of toggle switch
//                mSwitch.setChecked(false);
//
//                //gray out alarm
//                TypedValue typedValue = new TypedValue();
//                Resources.Theme theme = getApplicationContext().getTheme();
//                theme.resolveAttribute(R.attr.lic_card_inactive_details_color, typedValue, true);
//                @ColorInt int color = typedValue.data;
//
//                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//
//                mWakeUpTime.setTextColor(color);
//                horizontalDivider.setBackgroundColor(color);
//                mAlarmTitle.setTextColor(color);
//                mDropDown.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
//
//                mMissionIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
//                mMissionLabel.setTextColor(color);
//                mMissionDesc.setTextColor(color);
//
//                mRingtoneIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
//                mRingtoneLabel.setTextColor(color);
//                mRingtoneDesc.setTextColor(color);
//
//                mVibrateIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
//                mVibrateTextView.setTextColor(color);
//                mVibrateDesc.setTextColor(color);

                finish();

                Toast.makeText(MissionMath.this, resources.getString(R.string.mission_dismissed), Toast.LENGTH_SHORT).show();

                failChallenge();
            }
        });

        animateClock();
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