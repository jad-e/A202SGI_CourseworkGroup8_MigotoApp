package com.example.a202sgi_group8;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Ringtone;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class CreateAlarmActivity extends AppCompatActivity {

    //
    // constants
    //
    private final String SP_DEFAULT_MISSION_KEY = "sp_default_mission_key";
    private final String SP_DEFAULT_RINGTONE_KEY = "sp_default_ringtone_key";
    private final String SP_DEFAULT_WAKE_HR_KEY = "sp_default_wake_hr_key";
    private final String SP_DEFAULT_WAKE_MIN_KEY = "sp_default_wake_min_key";

    private final String SP_THEME_KEY = "sp_theme_key";
    private final String SP_LANGUAGE_KEY = "sp_language_key";

    //
    // other variables
    //
    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    private Context context;
    private Resources resources;

    private DatabaseReference root;

    //
    // views
    //
    private ImageView mBackButton;

    private NumberPicker mNumberPickerHour;
    private NumberPicker mNumberPickerMinute;

    private CardView mCardAlarmTitle;
    private TextView mAlarmTitle;

    private CardView mCardMission;
    private TextView mMission;

    private CardView mCardRingtone;
    private TextView mRingtone;

    private CardView mCardVibrate;
    private CheckBox mVibrateCheckBox;

    private Button mSaveBtn;

    private TextView alarmL;
    private TextView missionL;
    private TextView ringtoneL;
    private TextView vibrateL;


    //values
    private int mWakeUpHour;
    private int mWakeUpMinute;

    private String mAlarmTitleUserInput;

    private int tempMissionType;
    private int mAlarmMissionType;

    private int mRingtoneChoice;

    private boolean isVibrate;


    private int language; //1-English, 2-简体中文
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
        setContentView(R.layout.activity_create_alarm);

        //
        // find views
        //

        mBackButton = findViewById(R.id.createAlarm_back_imgView);

        mNumberPickerHour = findViewById(R.id.createAlarm_numPickerHr);
        mNumberPickerMinute = findViewById(R.id.createAlarm_numPickerMin);

        mCardAlarmTitle = findViewById(R.id.createAlarm_card_label);
        mAlarmTitle = findViewById(R.id.createAlarm_label_details);

        mCardMission = findViewById(R.id.createAlarm_card_mission);
        mMission = findViewById(R.id.createAlarm_mission_details);

        mCardRingtone = findViewById(R.id.createAlarm_card_ringtone);
        mRingtone = findViewById(R.id.createAlarm_ringtone_details);

        mCardVibrate = findViewById(R.id.createAlarm_card_vibrate);
        mVibrateCheckBox = findViewById(R.id.createAlarm_vibrate_checkbox);

        mSaveBtn = findViewById(R.id.createAlarm_save_alarm_changes_button);


        alarmL = findViewById(R.id.createAlarm_label_title);
        missionL = findViewById(R.id.createAlarm_mission_title);
        ringtoneL = findViewById(R.id.createAlarm_ringtone_title);
        vibrateL = findViewById(R.id.createAlarm_vibrate_title);


        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE); //get sp file

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(CreateAlarmActivity.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    alarmL.setText(resources.getString(R.string.caa_alarm_title_text));
                    missionL.setText(resources.getString(R.string.caa_mission_title_text));
                    ringtoneL.setText(resources.getString(R.string.caa_ringtone_title_text));
                    vibrateL.setText(resources.getString(R.string.caa_vibrate_title_text));
                    mSaveBtn.setText(resources.getString(R.string.caa_save_btn_text));

                    break;

                case 2:
                    context = LocaleHelper.setLocale(CreateAlarmActivity.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    alarmL.setText(resources.getString(R.string.caa_alarm_title_text));
                    missionL.setText(resources.getString(R.string.caa_mission_title_text));
                    ringtoneL.setText(resources.getString(R.string.caa_ringtone_title_text));
                    vibrateL.setText(resources.getString(R.string.caa_vibrate_title_text));
                    mSaveBtn.setText(resources.getString(R.string.caa_save_btn_text));
                    break;

            }


        } else { //if don't have this key (app first launch)

            context = LocaleHelper.setLocale(CreateAlarmActivity.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            alarmL.setText(resources.getString(R.string.caa_alarm_title_text));
            missionL.setText(resources.getString(R.string.caa_mission_title_text));
            ringtoneL.setText(resources.getString(R.string.caa_ringtone_title_text));
            vibrateL.setText(resources.getString(R.string.caa_vibrate_title_text));
            mSaveBtn.setText(resources.getString(R.string.caa_save_btn_text));

        }


        //
        // initialization of the values
        //

        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        //wake up time
        mNumberPickerHour.setMinValue(0);
        mNumberPickerHour.setMaxValue(23);
        mNumberPickerHour.setWrapSelectorWheel(true);

        mNumberPickerMinute.setMinValue(0);
        mNumberPickerMinute.setMaxValue(59);
        mNumberPickerMinute.setWrapSelectorWheel(true);

        if (mPreferences.contains(SP_DEFAULT_WAKE_HR_KEY) && mPreferences.contains(SP_DEFAULT_WAKE_MIN_KEY)) {

            mWakeUpHour = mPreferences.getInt(SP_DEFAULT_WAKE_HR_KEY, 7);
            mWakeUpMinute = mPreferences.getInt(SP_DEFAULT_WAKE_MIN_KEY, 30);

            mNumberPickerHour.setValue(mWakeUpHour);
            mNumberPickerMinute.setValue(mWakeUpMinute);

        } else {

            mWakeUpHour = 7;
            mWakeUpMinute = 30;

            mNumberPickerHour.setValue(mWakeUpHour);
            mNumberPickerMinute.setValue(mWakeUpMinute);

        }

        // alarm title
        mAlarmTitle.setText("-");
        mAlarmTitleUserInput = "-";

        //mission
        if (mPreferences.contains(SP_DEFAULT_MISSION_KEY)) {

            mAlarmMissionType = mPreferences.getInt(SP_DEFAULT_MISSION_KEY, 1);

            switch (mAlarmMissionType) {

                case 1:
                    mMission.setText(resources.getString(R.string.caa_mission_details_text1));
                    break;
                case 2:
                    mMission.setText(resources.getString(R.string.caa_mission_details_text2));
                    break;

            }

        } else {

            mAlarmMissionType = 1;

            mMission.setText(R.string.caa_mission_details_text1);

        }

        //ringtone
        if (mPreferences.contains(SP_DEFAULT_RINGTONE_KEY)) {

            mRingtoneChoice = mPreferences.getInt(SP_DEFAULT_RINGTONE_KEY, 1);

            switch (mRingtoneChoice) {

                case 1:
                    mRingtone.setText(resources.getString(R.string.caa_ringtone_details_text1));
                    break;
                case 2:
                    mRingtone.setText(resources.getString(R.string.caa_ringtone_details_text2));
                    break;
                case 3:
                    mRingtone.setText(resources.getString(R.string.caa_ringtone_details_text3));
                    break;
                case 4:
                    mRingtone.setText(resources.getString(R.string.caa_ringtone_details_text4));
                    break;

            }

        } else {

            mRingtoneChoice = 1; //ringtone_1

            mRingtone.setText(resources.getString(R.string.caa_ringtone_details_text1));

        }

        //vibrate
        mVibrateCheckBox.setChecked(false);
        isVibrate = false; //no vibrate

        //
        // listeners..
        //

        mNumberPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                mWakeUpHour = picker.getValue();

            }
        });

        mNumberPickerMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                mWakeUpMinute = picker.getValue();

            }
        });

        mCardAlarmTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        CreateAlarmActivity.this,
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(CreateAlarmActivity.this)
                        .inflate(R.layout.create_alarm_bottom_sheet_alarm_title,
                                (LinearLayout) findViewById(R.id.createAlarm_changeAlarmTitle_bottomSheetContainer));

                //set language
                TextView label =  bottomSheetView.findViewById(R.id.createAlarm_changeAlarmTitle_label);
                EditText editText = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmTitle_userInput_editText);
                Button cancel = bottomSheetView.findViewById(R.id.createAlarm_changeTitle_btnCancel);
                Button OK = bottomSheetView.findViewById(R.id.createAlarm_changeTitle_btnOK);

                label.setText(resources.getString(R.string.cabsat_label_title));
                editText.setHint(resources.getString(R.string.cabsat_edit_text_hint));
                cancel.setText(resources.getString(R.string.cabsat_btnCancel_text));
                OK.setText(resources.getString(R.string.cabsat_btnOk_text));


                if (!mAlarmTitleUserInput.equals("-")) {

                    editText.setText(mAlarmTitleUserInput);

                }

                bottomSheetView.findViewById(R.id.createAlarm_changeTitle_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();

                        //if edit text is empty
                        if (TextUtils.isEmpty(editText.getText())) {

                            mAlarmTitle.setText("-");
                            mAlarmTitleUserInput = "-";

                        } else { //edit text is not empty

                            String str = editText.getText().toString();

                            mAlarmTitle.setText(str);
                            mAlarmTitleUserInput = str;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.createAlarm_changeTitle_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "CANCEL - NOT SAVED", Toast.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });

        mCardMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        CreateAlarmActivity.this,
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(CreateAlarmActivity.this)
                        .inflate(R.layout.create_alarm_bottom_sheet_alarm_mission,
                                (LinearLayout) findViewById(R.id.createAlarm_changeAlarmMission_bottomSheetContainer));

                //set language
                TextView label = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmMission_label);
                TextView shakeL = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmMission_mission_shake_title);
                TextView mathL = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmMission_mission_math_title);
                Button cancel = bottomSheetView.findViewById(R.id.createAlarm_changeMission_btnCancel);
                Button Ok =  bottomSheetView.findViewById(R.id.createAlarm_changeMission_btnOK);

                label.setText(resources.getString(R.string.cabsam_mission_title));
                shakeL.setText(resources.getString(R.string.cabsam_shake_title));
                mathL.setText(resources.getString(R.string.cabsam_math_title));
                cancel.setText(resources.getString(R.string.cabsam_btnCancel_text));
                Ok.setText(resources.getString(R.string.cabsam_btnOk_text));


                ImageView shakeImageView = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmMission_mission_shake_checked_imgView);
                ImageView mathImageView = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmMission_mission_math_checked_imgView);


                switch (mAlarmMissionType) {

                    case 1:
                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(CreateAlarmActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(CreateAlarmActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        break;

                    case 2:

                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(CreateAlarmActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(CreateAlarmActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        break;

                }

                //mission cards on click listener
                CardView shakeCardView = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmMission_mission_shake_card);
                CardView mathCardView = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmMission_mission_math_card);

                shakeCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempMissionType = 1;

                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(CreateAlarmActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(CreateAlarmActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color


                    }
                });


                mathCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempMissionType = 2;

                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(CreateAlarmActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(CreateAlarmActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                    }
                });

                bottomSheetView.findViewById(R.id.createAlarm_changeMission_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();

                        switch (tempMissionType) {

                            case 1:
                                mAlarmMissionType = 1;
                                mMission.setText(resources.getString(R.string.cabsam_shake_title));  //UI
                                break;
                            case 2:
                                mAlarmMissionType = 2;
                                mMission.setText(resources.getString(R.string.cabsam_math_title)); //UI
                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.createAlarm_changeMission_btnCancel).setOnClickListener(new View.OnClickListener() {
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
                        CreateAlarmActivity.this,
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(CreateAlarmActivity.this)
                        .inflate(R.layout.create_alarm_bottom_sheet_alarm_ringtone,
                                (LinearLayout) findViewById(R.id.createAlarm_changeAlarmRingtone_bottomSheetContainer));


                //set language
                TextView label = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_label);
                TextView r1L = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_rb1_text);
                TextView r2L = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_rb2_text);
                TextView r3L = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_rb3_text);
                TextView r4L = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_rb4_text);
                Button cancel = bottomSheetView.findViewById(R.id.createAlarm_changeRingtone_btnCancel);
                Button Ok = bottomSheetView.findViewById(R.id.createAlarm_changeRingtone_btnOK);

                label.setText(resources.getString(R.string.cabsar_ringtone_title));
                r1L.setText(resources.getString(R.string.cabsar_ringtone1_title));
                r2L.setText(resources.getString(R.string.cabsar_ringtone2_title));
                r3L.setText(resources.getString(R.string.cabsar_ringtone3_title));
                r4L.setText(resources.getString(R.string.cabsar_ringtone4_title));
                cancel.setText(resources.getString(R.string.cabsar_btnCancel_text));
                Ok.setText(resources.getString(R.string.cabsar_btnOk_text));


                //find views
                RadioGroup radioGroup = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_rg);
                RadioButton ringtone1Rb = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_rb1);
                RadioButton ringtone2Rb = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_rb2);
                RadioButton ringtone3Rb = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_rb3);
                RadioButton ringtone4Rb = bottomSheetView.findViewById(R.id.createAlarm_changeAlarmRingtone_rb4);

                //set initial values for bottom sheet dialog
                if (mRingtoneChoice == 1) { //user first opens it or prev choice is ringtone 1

                    ringtone1Rb.setChecked(true);
                    ringtone2Rb.setChecked(false);
                    ringtone3Rb.setChecked(false);
                    ringtone4Rb.setChecked(false);

                } else {

                    switch (mRingtoneChoice) {

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

                }

                bottomSheetView.findViewById(R.id.createAlarm_changeRingtone_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();


                        //which radio button is checked??

                        int radioId = radioGroup.getCheckedRadioButtonId();

                        switch (radioId) {

                            case R.id.createAlarm_changeAlarmRingtone_rb1:

                                //set Text UI
                                mRingtone.setText(resources.getString(R.string.cabsar_ringtone1_title));

                                //update the selected ringtone value
                                mRingtoneChoice = 1;

                                break;

                            case R.id.createAlarm_changeAlarmRingtone_rb2:

                                //set Text UI
                                mRingtone.setText(resources.getString(R.string.cabsar_ringtone2_title));

                                //update the selected ringtone value
                                mRingtoneChoice = 2;
                                break;

                            case R.id.createAlarm_changeAlarmRingtone_rb3:

                                //set Text UI
                                mRingtone.setText(resources.getString(R.string.cabsar_ringtone3_title));

                                //update the selected ringtone value
                                mRingtoneChoice = 3;
                                break;

                            case R.id.createAlarm_changeAlarmRingtone_rb4:

                                //set Text UI
                                mRingtone.setText(resources.getString(R.string.cabsar_ringtone4_title));

                                //update the selected ringtone value
                                mRingtoneChoice = 4;
                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.createAlarm_changeRingtone_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "CANCEL - NOT SAVED", Toast.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });

        //vibrate option
        mVibrateCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //see if the checkbox is checked or not
                boolean isChecked = mVibrateCheckBox.isChecked();

                if (isChecked) {

                    isVibrate = true;

                } else {

                    isVibrate = false;

                }

            }

        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                root = FirebaseDatabase.getInstance().getReference("AlarmClocks");

                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
                String id = ft.format(dNow);

                AlarmClock alarmClock = new AlarmClock(id, new Random().nextInt(Integer.MAX_VALUE), mAlarmTitleUserInput, true, mWakeUpHour, mWakeUpMinute, mRingtoneChoice, mAlarmMissionType, isVibrate, false);

                root.child(alarmClock.getAlarmId()).setValue(alarmClock);

                alarmClock.schedule(CreateAlarmActivity.this);

                //finish this activity, return to BedtimeFragment

                startActivity(new Intent(CreateAlarmActivity.this, MainActivity.class));
                finish();

            }
        });

        //"up button"

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //changes made will be gone!
                //will not be added into list!

                finish();

            }
        });

    }

    //override system back button
    @Override
    public void onBackPressed() {

        //changes made will be gone!
        //will not be added into list !

        super.onBackPressed();

    }
}
