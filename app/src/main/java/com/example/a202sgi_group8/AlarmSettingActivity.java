package com.example.a202sgi_group8;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class AlarmSettingActivity extends AppCompatActivity {

    //
    // constants
    //
    private final String SP_DEFAULT_MISSION_KEY = "sp_default_mission_key";
    private final String SP_DEFAULT_RINGTONE_KEY = "sp_default_ringtone_key";
    private final String SP_DEFAULT_WAKE_HR_KEY = "sp_default_wake_hr_key";
    private final String SP_DEFAULT_WAKE_MIN_KEY = "sp_default_wake_min_key";

    private final String SP_LANGUAGE_KEY = "sp_language_key";
    private final String SP_THEME_KEY = "sp_theme_key";

    //
    // other variables
    //
    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    private Context context;
    private Resources resources;

    //
    //views
    //
    private ImageView mBackArrow;

    private CardView mCardMission;
    private TextView mTextMission;

    private CardView mCardRingtone;
    private TextView mTextRingtone;

    private CardView mCardWakeTime;
    private TextView mTextWakeTime;


    private TextView mainTitle;
    private TextView subtitle;
    private TextView missionT;
    private TextView ringtoneT;
    private TextView wakeT;


    //
    //values
    //
    private int tempDefMission;
    private int defMission;

    private int defRingtone;

    private int defWakeHr;
    private int defWakeMin;

    private int language;
    private int theme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        if (mPreferences.contains(SP_THEME_KEY)) { //if got this key

            theme = mPreferences.getInt(SP_THEME_KEY, 2);

            switch(theme){

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
        setContentView(R.layout.activity_alarm_setting);

        //
        //find views
        //
        mBackArrow = findViewById(R.id.AlarmSettingPage_backImageView);

        mCardMission = findViewById(R.id.settings_alarmSetting_Mission_card);
        mTextMission = findViewById(R.id.settings_alarmSetting_Mission_details_txtView);

        mCardRingtone = findViewById(R.id.settings_alarmSetting_Ringtone_card);
        mTextRingtone = findViewById(R.id.settings_alarmSetting_Ringtone_details_txtView);

        mCardWakeTime = findViewById(R.id.settings_alarmSetting_wakeTime_card);
        mTextWakeTime = findViewById(R.id.settings_alarmSetting_wakeTime_details_txtView);


        mainTitle = findViewById(R.id.AlarmSettingPage_main_title);
        subtitle = findViewById(R.id.newAlarmDefaultSetting);
        missionT = findViewById(R.id.settings_alarmSetting_Mission_textView);
        ringtoneT = findViewById(R.id.settings_alarmSetting_Ringtone_textView);
        wakeT = findViewById(R.id.settings_alarmSetting_wakeTime_textView);

        //set language
        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(AlarmSettingActivity.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mainTitle.setText(resources.getString(R.string.AlarmSettingPage_main_title));
                    subtitle.setText(resources.getString(R.string.newAlarmDefaultSetting));
                    missionT.setText(resources.getString(R.string.settings_alarmSetting_Mission_textView));
                    ringtoneT.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_textView));
                    wakeT.setText(resources.getString(R.string.settings_alarmSetting_wakeTime_textView));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(AlarmSettingActivity.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mainTitle.setText(resources.getString(R.string.AlarmSettingPage_main_title));
                    subtitle.setText(resources.getString(R.string.newAlarmDefaultSetting));
                    missionT.setText(resources.getString(R.string.settings_alarmSetting_Mission_textView));
                    ringtoneT.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_textView));
                    wakeT.setText(resources.getString(R.string.settings_alarmSetting_wakeTime_textView));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(AlarmSettingActivity.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            mainTitle.setText(resources.getString(R.string.AlarmSettingPage_main_title));
            subtitle.setText(resources.getString(R.string.newAlarmDefaultSetting));
            missionT.setText(resources.getString(R.string.settings_alarmSetting_Mission_textView));
            ringtoneT.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_textView));
            wakeT.setText(resources.getString(R.string.settings_alarmSetting_wakeTime_textView));

        }


        //
        // initialize some values
        //
        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        if (mPreferences.contains(SP_DEFAULT_MISSION_KEY)) { //if got this key

            defMission = mPreferences.getInt(SP_DEFAULT_MISSION_KEY, 1);

            switch (defMission) {

                case 1:
                    mTextMission.setText(resources.getString(R.string.settings_alarmSetting_Mission_details_txtView));
                    break;
                case 2:
                    mTextMission.setText(resources.getString(R.string.settings_alarmSetting_Mission_details_txtViewMath));
                    break;

            }

        } else { //if can't find key (app first launch)

            defMission = 1; //default shake

            //store in shared pref file current default mission
            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(SP_DEFAULT_MISSION_KEY, defMission);
            spEditor.apply();

            mTextMission.setText(resources.getString(R.string.settings_alarmSetting_Mission_details_txtView));

        }

        if (mPreferences.contains(SP_DEFAULT_RINGTONE_KEY)) {//if got this key

            defRingtone = mPreferences.getInt(SP_DEFAULT_RINGTONE_KEY, 1);

            switch (defRingtone) {

                case 1:
                    mTextRingtone.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_details_txtView));
                    break;
                case 2:
                    mTextRingtone.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_details_txtViewRing2));
                    break;
                case 3:
                    mTextRingtone.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_details_txtViewRing3));
                    break;
                case 4:
                    mTextRingtone.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_details_txtViewRing4));
                    break;

            }

        } else {//if can't find key (app first launch)

            defRingtone = 1; //default ringtone 1

            //store in shared pref file current default mission
            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(SP_DEFAULT_RINGTONE_KEY, defRingtone);
            spEditor.apply();

            mTextRingtone.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_details_txtView));

        }

        if (mPreferences.contains(SP_DEFAULT_WAKE_HR_KEY) && mPreferences.contains(SP_DEFAULT_WAKE_MIN_KEY)) {//if got this key

            defWakeHr = mPreferences.getInt(SP_DEFAULT_WAKE_HR_KEY, 7);
            defWakeMin = mPreferences.getInt(SP_DEFAULT_WAKE_MIN_KEY, 30);

            mTextWakeTime.setText(String.format(Locale.getDefault(), "%02d:%02d", defWakeHr, defWakeMin));

        } else {//if can't find key (app first launch)

            defWakeHr = 7;
            defWakeMin = 30;

            //store in shared pref file current default wake time
            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(SP_DEFAULT_WAKE_HR_KEY, defWakeHr);
            spEditor.putInt(SP_DEFAULT_WAKE_MIN_KEY, defWakeMin);
            spEditor.apply();

            mTextWakeTime.setText(String.format(Locale.getDefault(), "%02d:%02d", defWakeHr, defWakeMin));

        }

        //
        //listeners..
        //
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mCardMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        AlarmSettingActivity.this,
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(AlarmSettingActivity.this)
                        .inflate(R.layout.default_alarm_bottom_sheet_alarm_mission,
                                (LinearLayout) findViewById(R.id.defaultAlarm_changeAlarmMission_bottomSheetContainer));

                //set language
                TextView label = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmMission_label);
                TextView shake = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmMission_mission_shake_title);
                TextView math = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmMission_mission_math_title);
                Button cancel = bottomSheetView.findViewById(R.id.defaultAlarm_changeMission_btnCancel);
                Button ok = bottomSheetView.findViewById(R.id.defaultAlarm_changeMission_btnOK);

                label.setText(resources.getString(R.string.defaultAlarm_changeAlarmMission_label));
                shake.setText(resources.getString(R.string.defaultAlarm_changeAlarmMission_mission_shake_title));
                math.setText(resources.getString(R.string.defaultAlarm_changeAlarmMission_mission_math_title));
                cancel.setText(resources.getString(R.string.defaultAlarm_changeMission_btnCancel));
                ok.setText(resources.getString(R.string.defaultAlarm_changeMission_btnOK));


                ImageView shakeImageView = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmMission_mission_shake_checked_imgView);
                ImageView mathImageView = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmMission_mission_math_checked_imgView);

                switch (defMission) {

                    case 1:
                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(AlarmSettingActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(AlarmSettingActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        break;

                    case 2:
                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(AlarmSettingActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(AlarmSettingActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        break;
                }

                //mission cards on click listener
                CardView shakeCardView = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmMission_mission_shake_card);
                CardView mathCardView = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmMission_mission_math_card);

                shakeCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempDefMission = 1;

                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(AlarmSettingActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(AlarmSettingActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                    }
                });

                mathCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempDefMission = 2;

                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(AlarmSettingActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(AlarmSettingActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                    }
                });

                bottomSheetView.findViewById(R.id.defaultAlarm_changeMission_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();

                        switch (tempDefMission) {

                            case 1:
                                defMission = 1;

                                //update def mission value in shared pref
                                if (mPreferences.contains(SP_DEFAULT_MISSION_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_DEFAULT_MISSION_KEY, defMission);
                                    spEditor.apply();

                                }

                                mTextMission.setText(resources.getString(R.string.settings_alarmSetting_Mission_details_txtView));  //UI

                                break;
                            case 2:
                                defMission = 2;

                                //update def mission value in shared pref
                                if (mPreferences.contains(SP_DEFAULT_MISSION_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_DEFAULT_MISSION_KEY, defMission);
                                    spEditor.apply();

                                }

                                mTextMission.setText(resources.getString(R.string.settings_alarmSetting_Mission_details_txtViewMath));

                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.defaultAlarm_changeMission_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });

        mCardRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        AlarmSettingActivity.this,
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(AlarmSettingActivity.this)
                        .inflate(R.layout.default_alarm_bottom_sheet_alarm_ringtone,
                                (LinearLayout) findViewById(R.id.defaultAlarm_changeAlarmRingtone_bottomSheetContainer));


                //set language
                TextView label = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_label);
                TextView ring1 = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_rb1_text);
                TextView ring2 = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_rb2_text);
                TextView ring3 = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_rb3_text);
                TextView ring4 = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_rb4_text);
                Button cancel = bottomSheetView.findViewById(R.id.defaultAlarm_changeRingtone_btnCancel);
                Button ok = bottomSheetView.findViewById(R.id.defaultAlarm_changeRingtone_btnOK);

                label.setText(resources.getString(R.string.defaultAlarm_changeAlarmRingtone_label));
                ring1.setText(resources.getString(R.string.defaultAlarm_changeAlarmRingtone_rb1_text));
                ring2.setText(resources.getString(R.string.defaultAlarm_changeAlarmRingtone_rb2_text));
                ring3.setText(resources.getString(R.string.defaultAlarm_changeAlarmRingtone_rb3_text));
                ring4.setText(resources.getString(R.string.defaultAlarm_changeAlarmRingtone_rb4_text));
                cancel.setText(resources.getString(R.string.defaultAlarm_changeRingtone_btnCancel));
                ok.setText(resources.getString(R.string.defaultAlarm_changeRingtone_btnOK));


                //find views
                RadioGroup radioGroup = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_rg);
                RadioButton ringtone1Rb = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_rb1);
                RadioButton ringtone2Rb = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_rb2);
                RadioButton ringtone3Rb = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_rb3);
                RadioButton ringtone4Rb = bottomSheetView.findViewById(R.id.defaultAlarm_changeAlarmRingtone_rb4);

                switch (defRingtone) {

                    case 1:
                        ringtone1Rb.setChecked(true);
                        ringtone2Rb.setChecked(false);
                        ringtone3Rb.setChecked(false);
                        ringtone4Rb.setChecked(false);
                        break;
                    case 2:
                        ringtone1Rb.setChecked(false);
                        ringtone2Rb.setChecked(true);
                        ringtone3Rb.setChecked(false);
                        ringtone4Rb.setChecked(false);
                        break;
                    case 3:
                        ringtone1Rb.setChecked(false);
                        ringtone2Rb.setChecked(false);
                        ringtone3Rb.setChecked(true);
                        ringtone4Rb.setChecked(false);
                        break;
                    case 4:
                        ringtone1Rb.setChecked(false);
                        ringtone2Rb.setChecked(false);
                        ringtone3Rb.setChecked(false);
                        ringtone4Rb.setChecked(true);
                        break;

                }

                bottomSheetView.findViewById(R.id.defaultAlarm_changeRingtone_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();

                        //which radio button is checked??

                        int radioId = radioGroup.getCheckedRadioButtonId();

                        switch (radioId) {

                            case R.id.defaultAlarm_changeAlarmRingtone_rb1:

                                //update the selected ringtone value
                                defRingtone = 1;

                                //update def ringtone value in shared pref
                                if (mPreferences.contains(SP_DEFAULT_RINGTONE_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_DEFAULT_RINGTONE_KEY, defRingtone);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextRingtone.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_details_txtView));

                                break;

                            case R.id.defaultAlarm_changeAlarmRingtone_rb2:

                                //update the selected ringtone value
                                defRingtone = 2;

                                //update def ringtone value in shared pref
                                if (mPreferences.contains(SP_DEFAULT_RINGTONE_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_DEFAULT_RINGTONE_KEY, defRingtone);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextRingtone.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_details_txtViewRing2));

                                break;

                            case R.id.defaultAlarm_changeAlarmRingtone_rb3:

                                //update the selected ringtone value
                                defRingtone = 3;

                                //update def ringtone value in shared pref
                                if (mPreferences.contains(SP_DEFAULT_RINGTONE_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_DEFAULT_RINGTONE_KEY, defRingtone);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextRingtone.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_details_txtViewRing3));

                                break;

                            case R.id.defaultAlarm_changeAlarmRingtone_rb4:

                                //update the selected ringtone value
                                defRingtone = 4;

                                //update def ringtone value in shared pref
                                if (mPreferences.contains(SP_DEFAULT_RINGTONE_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_DEFAULT_RINGTONE_KEY, defRingtone);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextRingtone.setText(resources.getString(R.string.settings_alarmSetting_Ringtone_details_txtViewRing4));

                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.defaultAlarm_changeRingtone_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(AlarmSettingActivity.this, "CANCEL - NOT SAVED", Toast.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });

        mCardWakeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //time picker

                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        defWakeHr = hourOfDay;
                        defWakeMin = minute;

                        //update wake time value in shared pref
                        if (mPreferences.contains(SP_DEFAULT_WAKE_HR_KEY) && mPreferences.contains(SP_DEFAULT_WAKE_MIN_KEY)) {

                            SharedPreferences.Editor spEditor = mPreferences.edit();
                            spEditor.putInt(SP_DEFAULT_WAKE_HR_KEY, defWakeHr);
                            spEditor.putInt(SP_DEFAULT_WAKE_MIN_KEY, defWakeMin);
                            spEditor.apply();

                        }

                        //set UI
                        mTextWakeTime.setText(String.format(Locale.getDefault(), "%02d:%02d", defWakeHr, defWakeMin));

                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmSettingActivity.this, R.style.timePickerTheme, onTimeSetListener, defWakeHr, defWakeMin, true);

                timePickerDialog.show();


            }
        });


    }
}