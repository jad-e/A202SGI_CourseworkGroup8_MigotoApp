package com.example.a202sgi_group8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;

public class MissionSettingsActivity extends AppCompatActivity {

    //
    // constants
    //
    private final String SP_AUTO_DISMISS_KEY = "sp_auto_dismiss_key";
    private final String SP_SHAKE_DIFFICULTY_KEY = "sp_shake_difficulty_key";
    private final String SP_MATH_DIFFICULTY_KEY = "sp_math_difficulty_key";

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
    // views
    //
    private ImageView mBackArrow;

    private CardView mCardShakeDif;
    private TextView mTextShakeDifDetails;

    private CardView mCardMathDif;
    private TextView mTextMathDifDetails;


    private TextView mMainTitle;
    private TextView mDesc2;
    private TextView mShakeTitle;
    private TextView mMathTitle;

    //
    //values
    //
    private int autoDismissMin;

    private int mTempShakeDifficulty;
    private int shakeDifficulty;

    private int mTempMathDifficulty;
    private int mathDifficulty;

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
        setContentView(R.layout.activity_mission_settings);

        //
        // find views
        //
        mBackArrow = findViewById(R.id.MissionPage_backImageView);

        mCardShakeDif = findViewById(R.id.settings_mission_shakeDif_card);
        mTextShakeDifDetails = findViewById(R.id.settings_mission_shakeDif_details_txtView);

        mCardMathDif = findViewById(R.id.settings_mission_mathDif_card);
        mTextMathDifDetails = findViewById(R.id.settings_mission_mathDif_details_txtView);


        mMainTitle = findViewById(R.id.MissionPage_main_title);
        mDesc2 = findViewById(R.id.missionDifficulty);
        mShakeTitle = findViewById(R.id.settings_mission_shakeDif_textView);
        mMathTitle = findViewById(R.id.settings_mission_mathDif_textView);

        //set language
        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(MissionSettingsActivity.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mMainTitle.setText(resources.getString(R.string.MissionPage_main_title));
                    mDesc2.setText(resources.getString(R.string.missionDifficulty));
                    mShakeTitle.setText(resources.getString(R.string.settings_mission_shakeDif_textView));
                    mMathTitle.setText(resources.getString(R.string.settings_mission_mathDif_textView));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(MissionSettingsActivity.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mMainTitle.setText(resources.getString(R.string.MissionPage_main_title));
                    mDesc2.setText(resources.getString(R.string.missionDifficulty));
                    mShakeTitle.setText(resources.getString(R.string.settings_mission_shakeDif_textView));
                    mMathTitle.setText(resources.getString(R.string.settings_mission_mathDif_textView));
                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(MissionSettingsActivity.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            mMainTitle.setText(resources.getString(R.string.MissionPage_main_title));
            mDesc2.setText(resources.getString(R.string.missionDifficulty));
            mShakeTitle.setText(resources.getString(R.string.settings_mission_shakeDif_textView));
            mMathTitle.setText(resources.getString(R.string.settings_mission_mathDif_textView));

        }

        //
        // initialize values..
        //

        if (mPreferences.contains(SP_SHAKE_DIFFICULTY_KEY)) { //if got this key

            shakeDifficulty = mPreferences.getInt(SP_SHAKE_DIFFICULTY_KEY, 2);

            switch (shakeDifficulty) {

                case 1:
                    mTextShakeDifDetails.setText(resources.getString(R.string.settings_mission_shakeDif_details_txtViewEasy));
                    break;
                case 2:
                    mTextShakeDifDetails.setText(resources.getString(R.string.settings_mission_shakeDif_details_txtView));
                    break;
                case 3:
                    mTextShakeDifDetails.setText(resources.getString(R.string.settings_mission_shakeDif_details_txtViewHard));
                    break;
            }


        } else { //if can't find key (app first launch)

            shakeDifficulty = 2; //default normal

            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(SP_SHAKE_DIFFICULTY_KEY, shakeDifficulty);
            spEditor.apply();

            mTextShakeDifDetails.setText(resources.getString(R.string.settings_mission_shakeDif_details_txtView));

        }

        if (mPreferences.contains(SP_MATH_DIFFICULTY_KEY)) { //if got this key

            mathDifficulty = mPreferences.getInt(SP_MATH_DIFFICULTY_KEY, 2);

            switch (mathDifficulty) {

                case 1:
                    mTextMathDifDetails.setText(resources.getString(R.string.settings_mission_mathDif_details_txtViewEasy));
                    break;
                case 2:
                    mTextMathDifDetails.setText(resources.getString(R.string.settings_mission_mathDif_details_txtView));
                    break;
                case 3:
                    mTextMathDifDetails.setText(resources.getString(R.string.settings_mission_mathDif_details_txtViewHard));
                    break;

            }


        } else { //if can't find key (app first launch)

            mathDifficulty = 2; //default normal

            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(SP_MATH_DIFFICULTY_KEY, mathDifficulty);
            spEditor.apply();

            mTextMathDifDetails.setText(resources.getString(R.string.settings_mission_mathDif_details_txtView));

        }


        //
        // listeners..
        //

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mCardShakeDif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MissionSettingsActivity.this,
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(MissionSettingsActivity.this)
                        .inflate(R.layout.settings_mission_bottom_sheet_shake_difficulty,
                                (LinearLayout) findViewById(R.id.settingsMission_changeShakeDif_bottomSheetContainer));

                //set language
                TextView mShake = bottomSheetView.findViewById(R.id.settingsMission_changeShakeDif_label);
                TextView mTipEasy = bottomSheetView.findViewById(R.id.settingsMission_changeShakeDif_sliderTipEasy);
                TextView mTipHard = bottomSheetView.findViewById(R.id.settingsMission_changeShakeDif_sliderTipHard);
                Button mCancel = bottomSheetView.findViewById(R.id.settingsMission_changeShakeDif_btnCancel);
                Button mOK = bottomSheetView.findViewById(R.id.settingsMission_changeShakeDif_btnOK);

                mShake.setText(resources.getString(R.string.settingsMission_changeShakeDif_label));
                mTipEasy.setText(resources.getString(R.string.settingsMission_changeShakeDif_sliderTipEasy));
                mTipHard.setText(resources.getString(R.string.settingsMission_changeShakeDif_sliderTipHard));
                mCancel.setText(resources.getString(R.string.settingsMission_changeShakeDif_btnCancel));
                mOK.setText(resources.getString(R.string.settingsMission_changeShakeDif_btnOK));

                //find views
                Slider slider = bottomSheetView.findViewById(R.id.settingsMission_changeShakeDif_slider);
                TextView txtView = bottomSheetView.findViewById(R.id.settingsMission_changeShakeDif_Status);

                // initialize..
                switch (shakeDifficulty) {

                    case 1: //easy
                        txtView.setText(resources.getString(R.string.settingsMission_changeShakeDif_StatusEasy));
                        slider.setValue(0);
                        break;
                    case 2: //normal
                        txtView.setText(resources.getString(R.string.settingsMission_changeShakeDif_Status));
                        slider.setValue(20);
                        break;
                    case 3: //hard
                        txtView.setText(resources.getString(R.string.settingsMission_changeShakeDif_StatusHard));
                        slider.setValue(40);
                        break;

                }

                slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
                    @Override
                    public void onStartTrackingTouch(@NonNull Slider slider) {

                    }

                    @Override
                    public void onStopTrackingTouch(@NonNull Slider slider) {

                        switch ((int) slider.getValue()) {

                            case 0:
                                txtView.setText(resources.getString(R.string.settingsMission_changeShakeDif_StatusEasy));
                                mTempShakeDifficulty = 1;
                                break;
                            case 20:
                                txtView.setText(resources.getString(R.string.settingsMission_changeShakeDif_Status));
                                mTempShakeDifficulty = 2;
                                break;
                            case 40:
                                txtView.setText(resources.getString(R.string.settingsMission_changeShakeDif_StatusHard));
                                mTempShakeDifficulty = 3;
                                break;

                        }

                    }
                });

                bottomSheetView.findViewById(R.id.settingsMission_changeShakeDif_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();

                        //which radio button is checked??

                        switch (mTempShakeDifficulty) {

                            case 1:

                                shakeDifficulty = 1;

                                //update shake dif value in shared pref
                                if (mPreferences.contains(SP_SHAKE_DIFFICULTY_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_SHAKE_DIFFICULTY_KEY, shakeDifficulty);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextShakeDifDetails.setText(resources.getString(R.string.settings_mission_shakeDif_details_txtViewEasy));

                                break;
                            case 2:

                                shakeDifficulty = 2;

                                //update shake dif value in shared pref
                                if (mPreferences.contains(SP_SHAKE_DIFFICULTY_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_SHAKE_DIFFICULTY_KEY, shakeDifficulty);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextShakeDifDetails.setText(resources.getString(R.string.settings_mission_shakeDif_details_txtView));

                                break;
                            case 3:
                                shakeDifficulty = 3;

                                //update shake dif value in shared pref
                                if (mPreferences.contains(SP_SHAKE_DIFFICULTY_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_SHAKE_DIFFICULTY_KEY, shakeDifficulty);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextShakeDifDetails.setText(resources.getString(R.string.settings_mission_shakeDif_details_txtViewHard));

                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.settingsMission_changeShakeDif_btnCancel).setOnClickListener(new View.OnClickListener() {
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

        mCardMathDif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MissionSettingsActivity.this,
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(MissionSettingsActivity.this)
                        .inflate(R.layout.settings_mission_bottom_sheet_math_difficulty,
                                (LinearLayout) findViewById(R.id.settingsMission_changeMathDif_bottomSheetContainer));

                //set language
                TextView mMath = bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_label);
                TextView mEasyTip = bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_sliderTipEasy);
                TextView mHardTip = bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_sliderTipHard);
                Button mCancel = bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_btnCancel);
                Button mOK = bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_btnOK);

                mMath.setText(resources.getString(R.string.settingsMission_changeMathDif_label));
                mEasyTip.setText(resources.getString(R.string.settingsMission_changeMathDif_sliderTipEasy));
                mHardTip.setText(resources.getString(R.string.settingsMission_changeMathDif_sliderTipHard));
                mCancel.setText(resources.getString(R.string.settingsMission_changeMathDif_btnCancel));
                mOK.setText(resources.getString(R.string.settingsMission_changeMathDif_btnOK));

                //find views
                Slider slider = bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_slider);
                TextView txtViewStat = bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_Status);
                TextView txtViewEx = bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_Example);

                // initialize..
                switch (mathDifficulty) {

                    case 1: //easy
                        txtViewStat.setText(resources.getString(R.string.settingsMission_changeMathDif_StatusEasy));
                        txtViewEx.setText("23+17=?");
                        slider.setValue(0);
                        break;
                    case 2: //normal
                        txtViewStat.setText(resources.getString(R.string.settingsMission_changeMathDif_Status));
                        txtViewEx.setText("43+24+34=?");
                        slider.setValue(20);
                        break;
                    case 3: //hard
                        txtViewStat.setText(resources.getString(R.string.settingsMission_changeMathDif_StatusHard));
                        txtViewEx.setText("(72x6)+32=?");
                        slider.setValue(40);
                        break;

                }

                slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
                    @Override
                    public void onStartTrackingTouch(@NonNull Slider slider) {

                    }

                    @Override
                    public void onStopTrackingTouch(@NonNull Slider slider) {

                        switch ((int) slider.getValue()) {

                            case 0:
                                txtViewStat.setText(resources.getString(R.string.settingsMission_changeMathDif_StatusEasy));
                                txtViewEx.setText("23+17=?");
                                mTempMathDifficulty = 1;
                                break;
                            case 20:
                                txtViewStat.setText(resources.getString(R.string.settingsMission_changeMathDif_Status));
                                txtViewEx.setText("43+24+34=?");
                                mTempMathDifficulty = 2;
                                break;
                            case 40:
                                txtViewStat.setText(resources.getString(R.string.settingsMission_changeMathDif_StatusHard));
                                txtViewEx.setText("(72x6)+32=?");
                                mTempMathDifficulty = 3;
                                break;

                        }

                    }
                });

                bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();

                        //which radio button is checked??

                        switch (mTempMathDifficulty) {

                            case 1:
                                mathDifficulty = 1;

                                //update math dif value in shared pref
                                if (mPreferences.contains(SP_MATH_DIFFICULTY_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_MATH_DIFFICULTY_KEY, mathDifficulty);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextMathDifDetails.setText(resources.getString(R.string.settings_mission_mathDif_details_txtViewEasy));

                                break;

                            case 2:
                                mathDifficulty = 2;

                                //update math dif value in shared pref
                                if (mPreferences.contains(SP_MATH_DIFFICULTY_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_MATH_DIFFICULTY_KEY, mathDifficulty);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextMathDifDetails.setText(resources.getString(R.string.settings_mission_mathDif_details_txtView));

                                break;

                            case 3:
                                mathDifficulty = 3;

                                //update math dif value in shared pref
                                if (mPreferences.contains(SP_MATH_DIFFICULTY_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_MATH_DIFFICULTY_KEY, mathDifficulty);
                                    spEditor.apply();

                                }

                                //set Text UI
                                mTextMathDifDetails.setText(resources.getString(R.string.settings_mission_mathDif_details_txtViewHard));

                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.settingsMission_changeMathDif_btnCancel).setOnClickListener(new View.OnClickListener() {
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

    }
}