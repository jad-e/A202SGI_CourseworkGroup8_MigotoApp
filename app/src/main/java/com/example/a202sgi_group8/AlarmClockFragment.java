package com.example.a202sgi_group8;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class AlarmClockFragment extends Fragment {

    //
    // constant values
    //
    private static final String ARG_ALARM_CLOCK_ID = "alarm_clock_id";

    private final String SP_LANGUAGE_KEY = "sp_language_key";
    private final String SP_THEME_KEY = "sp_theme_key";

    //
    // other variables
    //
    private AlarmClock mAlarmClock;
    DatabaseReference database;

    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    private Context context;
    private Resources resources;


    //
    // layout views
    //
    private ImageView mBackArrow;

    private NumberPicker mNumberPickerHr;
    private NumberPicker mNumberPickerMin;

    private CardView mCardViewAlarmTitle;
    private TextView mAlarmTitle;

    private CardView mCardViewAlarmMission;
    private TextView mAlarmMission;

    private CardView mCardViewAlarmRingtone;
    private TextView mAlarmRingtone;

    private CardView mCardViewVibrate;
    private CheckBox mVibrateCheckBox;

    private Button mSaveButton;

    private TextView alarmLabel;
    private TextView missionLabel;
    private TextView ringtoneLabel;
    private TextView vibrateLabel;


    //
    // other values
    //
    private String alarmClockId;

    private int language;
    private int theme;


    //
    // values to be used in final stage
    //
    private int mWakeUpHr;
    private int mWakeUpMin;

    private String mAlarmTitleUserInput;

    private int tempMissionType;
    private int mAlarmMissionType;

    private int mRingtoneChoice;

    private boolean mIsOnAlarm;
    private boolean mStarted;

    private boolean isVibrate;

    private int alarmClockId2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        mPreferences = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE); //get sp file

        if (mPreferences.contains(SP_THEME_KEY)) { //if got this key

            theme = mPreferences.getInt(SP_THEME_KEY, 2);

            switch(theme){

                case 1: //dark
                    getActivity().setTheme(R.style.darkTheme);
                    break;
                case 2: //light
                    getActivity().setTheme(R.style.appTheme);
                    break;
            }

        } else { //if don't have this key (app first launch)

            getActivity().setTheme(R.style.appTheme);

        }


        super.onCreate(savedInstanceState);

        alarmClockId = getArguments().getString(ARG_ALARM_CLOCK_ID);

        //Toast.makeText(getActivity(), alarmClockId, Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //return the view to the hosting activity
        return inflater.inflate(R.layout.fragment_alarm_clock, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //
        // find views
        //
        mBackArrow = view.findViewById(R.id.editAlarm_back_imgView);

        mNumberPickerHr = view.findViewById(R.id.editAlarm_numPickerHr);
        mNumberPickerMin = view.findViewById(R.id.editAlarm_numPickerMin);

        mCardViewAlarmTitle = view.findViewById(R.id.editAlarm_card_label);
        mAlarmTitle = view.findViewById(R.id.editAlarm_label_details);

        mCardViewAlarmMission = view.findViewById(R.id.editAlarm_card_mission);
        mAlarmMission = view.findViewById(R.id.editAlarm_mission_details);

        mCardViewAlarmRingtone = view.findViewById(R.id.editAlarm_card_ringtone);
        mAlarmRingtone = view.findViewById(R.id.editAlarm_ringtone_details);

        mCardViewVibrate = view.findViewById(R.id.editAlarm_card_vibrate);
        mVibrateCheckBox = view.findViewById(R.id.editAlarm_vibrate_checkbox);

        mSaveButton = view.findViewById(R.id.editAlarm_save_alarm_changes_button);

        alarmLabel = view.findViewById(R.id.editAlarm_label_title);
        missionLabel = view.findViewById(R.id.editAlarm_mission_title);
        ringtoneLabel = view.findViewById(R.id.editAlarm_ringtone_title);
        vibrateLabel = view.findViewById(R.id.editAlarm_vibrate_title);



        mPreferences = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE); //get sp file

        //set language
        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(getContext(), "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    alarmLabel.setText(resources.getString(R.string.fac_alarm_title_text));
                    missionLabel.setText(resources.getString(R.string.fac_mission_title_text));
                    ringtoneLabel.setText(resources.getString(R.string.fac_ringtone_title_text));
                    vibrateLabel.setText(resources.getString(R.string.fac_vibrate_title_text));

                    mSaveButton.setText(resources.getString(R.string.fac_save_btn_text));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(getContext(), "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    alarmLabel.setText(resources.getString(R.string.fac_alarm_title_text));
                    missionLabel.setText(resources.getString(R.string.fac_mission_title_text));
                    ringtoneLabel.setText(resources.getString(R.string.fac_ringtone_title_text));
                    vibrateLabel.setText(resources.getString(R.string.fac_vibrate_title_text));

                    mSaveButton.setText(resources.getString(R.string.fac_save_btn_text));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(getContext(), "en");
            resources = context.getResources();

            //set the views to the wanted language
            alarmLabel.setText(resources.getString(R.string.fac_alarm_title_text));
            missionLabel.setText(resources.getString(R.string.fac_mission_title_text));
            ringtoneLabel.setText(resources.getString(R.string.fac_ringtone_title_text));
            vibrateLabel.setText(resources.getString(R.string.fac_vibrate_title_text));

            mSaveButton.setText(resources.getString(R.string.fac_save_btn_text));

        }


        //
        // initialization of the values
        //

        database = FirebaseDatabase.getInstance().getReference("AlarmClocks");
        database.child(alarmClockId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        DataSnapshot dataSnapshot = task.getResult();

                        mWakeUpHr = Integer.parseInt(String.valueOf(dataSnapshot.child("alarmWakeHr").getValue()));
                        mWakeUpMin = Integer.parseInt(String.valueOf(dataSnapshot.child("alarmWakeMin").getValue()));
                        mAlarmTitleUserInput = String.valueOf(dataSnapshot.child("alarmTitle").getValue());
                        mAlarmMissionType = Integer.parseInt(String.valueOf(dataSnapshot.child("alarmMission").getValue()));
                        mRingtoneChoice = Integer.parseInt(String.valueOf(dataSnapshot.child("alarmRingtone").getValue()));
                        isVibrate = Boolean.valueOf(String.valueOf(dataSnapshot.child("alarmVibrate").getValue()));

                        mNumberPickerHr.setMinValue(0);
                        mNumberPickerHr.setMaxValue(23);
                        mNumberPickerHr.setWrapSelectorWheel(true);
                        mNumberPickerHr.setValue(mWakeUpHr);

                        mNumberPickerMin.setMinValue(0);
                        mNumberPickerMin.setMaxValue(59);
                        mNumberPickerMin.setWrapSelectorWheel(true);
                        mNumberPickerMin.setValue(mWakeUpMin);

                        mAlarmTitle.setText(mAlarmTitleUserInput);

                        switch (mAlarmMissionType) {

                            case 1:
                                mAlarmMission.setText(resources.getString(R.string.fac_mission_details_text1));
                                break;
                            case 2:
                                mAlarmMission.setText(resources.getString(R.string.fac_mission_details_text2));
                                break;

                        }

                        switch (mRingtoneChoice) {

                            case 1:
                                mAlarmRingtone.setText(resources.getString(R.string.fac_ringtone_details_text1));
                                break;
                            case 2:
                                mAlarmRingtone.setText(resources.getString(R.string.fac_ringtone_details_text2));
                                break;
                            case 3:
                                mAlarmRingtone.setText(resources.getString(R.string.fac_ringtone_details_text3));
                                break;
                            case 4:
                                mAlarmRingtone.setText(resources.getString(R.string.fac_ringtone_details_text4));
                                break;

                        }

                        if (isVibrate) {

                            mVibrateCheckBox.setChecked(true);

                        } else {

                            mVibrateCheckBox.setChecked(false);

                        }

                    } else {

                        Toast.makeText(getActivity(), "Alarm doesn't exist.", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });

        //
        // listeners..
        //

        mNumberPickerHr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                mWakeUpHr = picker.getValue();

            }
        });

        mNumberPickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                mWakeUpMin = picker.getValue();

            }
        });

        mCardViewAlarmTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(R.layout.edit_alarm_bottom_sheet_alarm_title,
                                (LinearLayout) view.findViewById(R.id.editAlarm_changeAlarmTitle_bottomSheetContainer));


                //set language
                TextView label = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmTitle_label);
                EditText editText = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmTitle_userInput_editText);
                Button OK = bottomSheetView.findViewById(R.id.editAlarm_changeTitle_btnOK);
                Button cancel = bottomSheetView.findViewById(R.id.editAlarm_changeTitle_btnCancel);

                label.setText(resources.getString(R.string.eabsat_label_title));
                editText.setHint(resources.getString(R.string.eabsat_edit_text_hint));
                OK.setText(resources.getString(R.string.eabsat_btnOk_text));
                cancel.setText(resources.getString(R.string.eabsat_btnCancel_text));


                if (!mAlarmTitleUserInput.equals("-")) {

                    editText.setText(mAlarmTitleUserInput);

                }

                bottomSheetView.findViewById(R.id.editAlarm_changeTitle_btnOK).setOnClickListener(new View.OnClickListener() {
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

                bottomSheetView.findViewById(R.id.editAlarm_changeTitle_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(getContext(), "CANCEL - NOT SAVED", Toast.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });

        mCardViewAlarmMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(R.layout.edit_alarm_bottom_sheet_alarm_mission,
                                (LinearLayout) view.findViewById(R.id.editAlarm_changeAlarmMission_bottomSheetContainer));


                //set language
                TextView label = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmMission_label);
                TextView shakeL = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmMission_mission_shake_title);
                TextView mathL = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmMission_mission_math_title);
                Button cancel = bottomSheetView.findViewById(R.id.editAlarm_changeMission_btnCancel);
                Button OK = bottomSheetView.findViewById(R.id.editAlarm_changeMission_btnOK);

                label.setText(resources.getString(R.string.eabsam_mission_title));
                shakeL.setText(resources.getString(R.string.eabsam_shake_title));
                mathL.setText(resources.getString(R.string.eabsam_math_title));
                cancel.setText(resources.getString(R.string.eabsam_btnCancel_text));
                OK.setText(resources.getString(R.string.eabsam_btnOk_text));


                ImageView shakeImageView = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmMission_mission_shake_checked_imgView);
                ImageView mathImageView = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmMission_mission_math_checked_imgView);


                switch (mAlarmMissionType) {

                    case 1:

                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(getContext(), R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(getContext(), R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        break;

                    case 2:

                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(getContext(), R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(getContext(), R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        break;

                }

                //mission cards on click listener
                CardView shakeCardView = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmMission_mission_shake_card);
                CardView mathCardView = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmMission_mission_math_card);

                shakeCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempMissionType = 1;

                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(getContext(), R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(getContext(), R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                    }
                });

                mathCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempMissionType = 2;

                        shakeImageView
                                .setColorFilter(ContextCompat.getColor(getContext(), R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        mathImageView
                                .setColorFilter(ContextCompat.getColor(getContext(), R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                    }
                });

                bottomSheetView.findViewById(R.id.editAlarm_changeMission_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();

                        switch (tempMissionType) {

                            case 1:
                                mAlarmMissionType = 1;
                                mAlarmMission.setText(resources.getString(R.string.eabsam_shake_title));  //UI
                                break;
                            case 2:
                                mAlarmMissionType = 2;
                                mAlarmMission.setText(resources.getString(R.string.eabsam_math_title)); //UI
                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.editAlarm_changeMission_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(getContext(), "CANCEL - NOT SAVED", Toast.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();


            }
        });


        mCardViewAlarmRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(R.layout.edit_alarm_bottom_sheet_alarm_ringtone,
                                (LinearLayout) view.findViewById(R.id.createAlarm_changeAlarmRingtone_bottomSheetContainer));

                //set language
                TextView label = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_label);
                TextView ring1L = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_rb1_text);
                TextView ring2L = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_rb2_text);
                TextView ring3L = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_rb3_text);
                TextView ring4L = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_rb4_text);
                Button cancel = bottomSheetView.findViewById(R.id.editAlarm_changeRingtone_btnCancel);
                Button OK = bottomSheetView.findViewById(R.id.editAlarm_changeRingtone_btnOK);

                label.setText(resources.getString(R.string.eabsar_ringtone_title));
                ring1L.setText(resources.getString(R.string.eabsar_ringtone1_title));
                ring2L.setText(resources.getString(R.string.eabsar_ringtone2_title));
                ring3L.setText(resources.getString(R.string.eabsar_ringtone3_title));
                ring4L.setText(resources.getString(R.string.eabsar_ringtone4_title));
                cancel.setText(resources.getString(R.string.eabsar_btnCancel_text));
                OK.setText(resources.getString(R.string.eabsar_btnOk_text));

                //find views
                RadioGroup radioGroup = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_rg);
                RadioButton ringtone1Rb = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_rb1);
                RadioButton ringtone2Rb = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_rb2);
                RadioButton ringtone3Rb = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_rb3);
                RadioButton ringtone4Rb = bottomSheetView.findViewById(R.id.editAlarm_changeAlarmRingtone_rb4);

                switch (mRingtoneChoice) {

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


                bottomSheetView.findViewById(R.id.editAlarm_changeRingtone_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();


                        //which radio button is checked??

                        int radioId = radioGroup.getCheckedRadioButtonId();

                        switch (radioId) {

                            case R.id.editAlarm_changeAlarmRingtone_rb1:

                                //set Text UI
                                mAlarmRingtone.setText(resources.getString(R.string.eabsar_ringtone1_title));

                                //update the selected ringtone value
                                mRingtoneChoice = 1;

                                break;

                            case R.id.editAlarm_changeAlarmRingtone_rb2:

                                //set Text UI
                                mAlarmRingtone.setText(resources.getString(R.string.eabsar_ringtone2_title));

                                //update the selected ringtone value
                                mRingtoneChoice = 2;
                                break;

                            case R.id.editAlarm_changeAlarmRingtone_rb3:

                                //set Text UI
                                mAlarmRingtone.setText(resources.getString(R.string.eabsar_ringtone3_title));

                                //update the selected ringtone value
                                mRingtoneChoice = 3;
                                break;

                            case R.id.editAlarm_changeAlarmRingtone_rb4:

                                //set Text UI
                                mAlarmRingtone.setText(resources.getString(R.string.eabsar_ringtone4_title));

                                //update the selected ringtone value
                                mRingtoneChoice = 4;
                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.editAlarm_changeRingtone_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(getContext(), "CANCEL - NOT SAVED", Toast.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();


            }
        });


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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap hashMap = new HashMap();
                hashMap.put("alarmTitle", mAlarmTitleUserInput);
                hashMap.put("alarmWakeHr", mWakeUpHr);
                hashMap.put("alarmWakeMin", mWakeUpMin);
                hashMap.put("alarmRingtone", mRingtoneChoice);
                hashMap.put("alarmMission", mAlarmMissionType);
                hashMap.put("alarmVibrate", isVibrate);

                database.child(alarmClockId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                        Toast.makeText(getActivity(), resources.getString(R.string.edit_alarm_success_msg), Toast.LENGTH_SHORT).show();

                    }
                });

                database = FirebaseDatabase.getInstance().getReference("AlarmClocks");
                database.child(alarmClockId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<DataSnapshot> task) {

                         if (task.isSuccessful()) {

                             if (task.getResult().exists()) {

                                 DataSnapshot dataSnapshot = task.getResult();

                                 alarmClockId2 = Integer.parseInt(String.valueOf(dataSnapshot.child("alarmId2").getValue()));
                                 mWakeUpHr = Integer.parseInt(String.valueOf(dataSnapshot.child("alarmWakeHr").getValue()));
                                 mWakeUpMin = Integer.parseInt(String.valueOf(dataSnapshot.child("alarmWakeMin").getValue()));
                                 mAlarmTitleUserInput = String.valueOf(dataSnapshot.child("alarmTitle").getValue());
                                 mAlarmMissionType = Integer.parseInt(String.valueOf(dataSnapshot.child("alarmMission").getValue()));
                                 mRingtoneChoice = Integer.parseInt(String.valueOf(dataSnapshot.child("alarmRingtone").getValue()));
                                 isVibrate = Boolean.valueOf(String.valueOf(dataSnapshot.child("alarmVibrate").getValue()));
                                 mIsOnAlarm = Boolean.valueOf(String.valueOf(dataSnapshot.child("onAlarm").getValue()));
                                 mStarted = Boolean.valueOf(String.valueOf(dataSnapshot.child("started").getValue()));

                                 AlarmClock temp = new AlarmClock(alarmClockId, alarmClockId2, mAlarmTitleUserInput, mIsOnAlarm, mWakeUpHr, mWakeUpMin, mRingtoneChoice, mAlarmMissionType, isVibrate, mStarted);
                                 temp.editAlarm(context, mAlarmTitleUserInput, mWakeUpHr, mWakeUpMin, mRingtoneChoice, mAlarmMissionType, isVibrate);
                             }
                         }
                     }
                 });

                //finish this fragment activity, return to BedtimeFragment

                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();

            }
        });

        //"up button"
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //changes made will be gone!
                //will not be added into list!

                getActivity().finish();

            }
        });

    }

    public static AlarmClockFragment newInstance(String alarmClockId, int alarmClockId2) {

        Bundle args = new Bundle();
        args.putString(ARG_ALARM_CLOCK_ID, alarmClockId);

        AlarmClockFragment acf = new AlarmClockFragment();
        acf.setArguments(args);
        return acf;

    }

}
