package com.example.a202sgi_group8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a202sgi_group8.LocaleHelper;
import com.example.a202sgi_group8.MainActivity;
import com.example.a202sgi_group8.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class GeneralSettingsActivity extends AppCompatActivity {

    //
    // constants
    //
    private final String SP_THEME_KEY = "sp_theme_key";
    private final String SP_LANGUAGE_KEY = "sp_language_key";

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
    private ImageView mBackImgView;
    private CardView mThemeCard;
    private CardView mLanguageCard;
    private TextView mLanguageDetailsTextView;

    private TextView mMainTitle;
    private TextView mThemeTextView;
    private TextView mLanguageTextView;

    //
    // values
    //
    private int tempTheme;
    private int theme; //1-dark, 2-light
    private int tempLanguage;
    private int language; //1-English, 2-简体中文

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //SET THEME

        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE); //get sp file

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

            theme = 2; //by default its light mode

            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(SP_THEME_KEY, theme);
            spEditor.apply();

            setTheme(R.style.appTheme);

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);

        //
        //find views
        //
        mBackImgView = findViewById(R.id.generalPage_backImageView);
        mThemeCard = findViewById(R.id.settings_general_theme_card);
        mLanguageCard = findViewById(R.id.settings_general_language_card);
        mLanguageDetailsTextView = findViewById(R.id.settings_general_language_details_txtView);

        mMainTitle = findViewById(R.id.generalPage_main_title);
        mThemeTextView = findViewById(R.id.settings_general__theme_textView);
        mLanguageTextView = findViewById(R.id.settings_general_language_textView);

        //
        // initialization of the values
        //
        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(GeneralSettingsActivity.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mMainTitle.setText(resources.getString(R.string.generalPage_main_title));
                    mThemeTextView.setText(resources.getString(R.string.settings_general__theme_textView));
                    mLanguageTextView.setText(resources.getString(R.string.settings_general_language_textView));

                    mLanguageDetailsTextView.setText("English");

                    break;
                case 2:
                    context = LocaleHelper.setLocale(GeneralSettingsActivity.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mMainTitle.setText(resources.getString(R.string.generalPage_main_title));
                    mThemeTextView.setText(resources.getString(R.string.settings_general__theme_textView));
                    mLanguageTextView.setText(resources.getString(R.string.settings_general_language_textView));

                    mLanguageDetailsTextView.setText("简体中文");
                    break;

            }


        } else { //if don't have this key (app first launch)

            language = 1; //default english

            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(SP_LANGUAGE_KEY, language);
            spEditor.apply();

            context = LocaleHelper.setLocale(GeneralSettingsActivity.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            mMainTitle.setText(resources.getString(R.string.generalPage_main_title));
            mThemeTextView.setText(resources.getString(R.string.settings_general__theme_textView));
            mLanguageTextView.setText(resources.getString(R.string.settings_general_language_textView));

            mLanguageDetailsTextView.setText("English");

        }

        //
        //listeners...
        //

        mBackImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mThemeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        GeneralSettingsActivity.this,
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(GeneralSettingsActivity.this)
                        .inflate(R.layout.settings_general_bottom_sheet_theme,
                                (LinearLayout) findViewById(R.id.settingsGeneral_changeTheme_bottomSheetContainer));

                //set language
                TextView mThemeTextView = bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_label);
                TextView mDarkTextView = bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_dark_title);
                TextView mLightTextView = bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_light_title);
                Button mCancelButton = bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_btnCancel);
                Button mOKButton = bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_btnOK);


                mThemeTextView.setText(resources.getString(R.string.settingsGeneral_changeTheme_label));
                mDarkTextView.setText(resources.getString(R.string.settingsGeneral_changeTheme_dark_title));
                mLightTextView.setText(resources.getString(R.string.settingsGeneral_changeTheme_light_title));
                mCancelButton.setText(resources.getString(R.string.settingsGeneral_changeTheme_btnCancel));
                mOKButton.setText(resources.getString(R.string.settingsGeneral_changeTheme_btnOK));

                ImageView darkModeImgView = bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_dark_checked_imgView);
                ImageView lightModeImgView = bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_light_checked_imgView);

                switch (theme) {

                    case 1:
                        darkModeImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        lightModeImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        break;
                    case 2:
                        darkModeImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        lightModeImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color
                        break;

                }

                //theme cards on click listener
                CardView darkCardView = bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_dark_card);
                CardView lightCardView = bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_light_card);

                darkCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempTheme = 1;

                        darkModeImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        lightModeImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                    }
                });

                lightCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempTheme = 2;

                        darkModeImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        lightModeImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                    }
                });

                bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();

                        switch (tempTheme) {

                            case 1:
                                theme = 1; //update global value

                                //update theme in shared pref
                                if (mPreferences.contains(SP_THEME_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_THEME_KEY, theme);
                                    spEditor.apply();

                                }

                                //set theme

                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                                break;
                            case 2:
                                theme = 2; //update global value

                                //update theme in shared pref
                                if (mPreferences.contains(SP_THEME_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_THEME_KEY, theme);
                                    spEditor.apply();

                                }

                                //set theme
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.settingsGeneral_changeTheme_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(GeneralSettingsActivity.this, "CANCEL - NOT SAVED", Toast.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });

        mLanguageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        GeneralSettingsActivity.this,
                        R.style.BottomSheetDialogTheme

                );

                View bottomSheetView = LayoutInflater.from(GeneralSettingsActivity.this)
                        .inflate(R.layout.settings_general_bottom_sheet_language,
                                (LinearLayout) findViewById(R.id.settingsGeneral_changeLanguage_bottomSheetContainer));

                //set language
                TextView mBSLanguageTextView = bottomSheetView.findViewById(R.id.settingsGeneral_changeLanguage_label);
                Button mCancel = bottomSheetView.findViewById(R.id.settingsGeneral_changeLanguage_btnCancel);
                Button mOK = bottomSheetView.findViewById(R.id.settingsGeneral_changeLanguage_btnOK);

                mBSLanguageTextView.setText(resources.getString(R.string.settingsGeneral_changeLanguage_label));
                mCancel.setText(resources.getString(R.string.settingsGeneral_changeLanguage_btnCancel));
                mOK.setText(resources.getString(R.string.settingsGeneral_changeLanguage_btnOK));


                ImageView ENGImgView = bottomSheetView.findViewById(R.id.settingsGeneral_changeLanguage_eng_checked_imgView);
                ImageView CNImgView = bottomSheetView.findViewById(R.id.settingsGeneral_changeLanguage_cn_checked_imgView);

                switch (language) {

                    case 1:
                        ENGImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        CNImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                        break;
                    case 2:
                        ENGImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        CNImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color
                        break;

                }

                //theme cards on click listener
                CardView ENCardView = bottomSheetView.findViewById(R.id.settingsGeneral_changeLanguage_eng_card);
                CardView CNCardView = bottomSheetView.findViewById(R.id.settingsGeneral_changeLanguage_cn_card);

                ENCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempLanguage = 1;

                        ENGImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        CNImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                    }
                });

                CNCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempLanguage = 2;

                        ENGImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_unchecked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to checked color

                        CNImgView
                                .setColorFilter(ContextCompat.getColor(GeneralSettingsActivity.this, R.color.mission_checked_color),
                                        android.graphics.PorterDuff.Mode.SRC_IN); //set to unchecked color

                    }
                });

                bottomSheetView.findViewById(R.id.settingsGeneral_changeLanguage_btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(CreateAlarmActivity.this, "OK - SAVE", Toast.LENGTH_SHORT).show();

                        switch (tempLanguage) {

                            case 1:
                                language = 1; //update global value

                                //update language in shared pref
                                if (mPreferences.contains(SP_LANGUAGE_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_LANGUAGE_KEY, language);
                                    spEditor.apply();

                                }

                                //set the text views to wanted language

                                context = LocaleHelper.setLocale(GeneralSettingsActivity.this, "en");
                                resources = context.getResources();

                                mMainTitle.setText(resources.getString(R.string.generalPage_main_title));
                                mThemeTextView.setText(resources.getString(R.string.settings_general__theme_textView));
                                mLanguageTextView.setText(resources.getString(R.string.settings_general_language_textView));

                                mLanguageDetailsTextView.setText("English"); //update UI

                                break;
                            case 2:
                                language = 2;

                                //update language in shared pref
                                if (mPreferences.contains(SP_LANGUAGE_KEY)) {

                                    SharedPreferences.Editor spEditor = mPreferences.edit();
                                    spEditor.putInt(SP_LANGUAGE_KEY, language);
                                    spEditor.apply();

                                }

                                //set the text views to wanted language

                                context = LocaleHelper.setLocale(GeneralSettingsActivity.this, "zh");
                                resources = context.getResources();

                                mMainTitle.setText(resources.getString(R.string.generalPage_main_title));
                                mThemeTextView.setText(resources.getString(R.string.settings_general__theme_textView));
                                mLanguageTextView.setText(resources.getString(R.string.settings_general_language_textView));

                                mLanguageDetailsTextView.setText("简体中文");
                                break;

                        }

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetView.findViewById(R.id.settingsGeneral_changeLanguage_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(GeneralSettingsActivity.this, "CANCEL - NOT SAVED", Toast.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();


            }
        });

    }

    @Override
    public void onBackPressed() {

        finish();

    }
}