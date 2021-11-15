package com.example.a202sgi_group8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

public class SettingsFragment extends Fragment {

    //
    // constant values
    //
    private static final String DIALOG_USERNAME = "dialog_username";
    private static final String USERNAME_KEY = "user_name_key";
    private static final String USERNAME_REQUEST_KEY = "username_request_key";

    private static final String DIALOG_BIRTHDAY = "dialog_birthday";
    private static final String BIRTH_DAY_KEY = "birth_day_key";
    private static final String BIRTH_MONTH_KEY = "birth_month_key";
    private static final String BIRTHDAY_REQUEST_KEY = "birthday_request_key";

    private final String SP_USERNAME_KEY = "sp_username_key";
    private final String SP_BIRTH_MONTH_KEY = "sp_birth_month_key";
    private final String SP_BIRTH_DAY_KEY = "sp_birth_day_key";

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
    private LinearLayout mLayoutUsername;
    private TextView mTextViewUsername;

    private LinearLayout mLayoutBirthday;
    private TextView mTextViewBirthday;

    private CardView mAlarmSettingsCard;
    private CardView mMissionCard;
    private CardView mGeneralCard;
    private CardView mFAQCard;
    private CardView mAboutCard;

    private TextView mSettingsMainTitle;
    private TextView mProfileTitle;
    private TextView mUsernameTitle;
    private TextView mBirthdayTitle;
    private TextView mAlarmSettingTitle;
    private TextView mMissionTitle;
    private TextView mGeneralTitle;
    private TextView mFAQTitle;
    private TextView mAboutTitle;

    //
    // values
    //
    private String username;
    private int birth_month;
    private int birth_day;

    private int language;
    private int theme;

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

        getParentFragmentManager().setFragmentResultListener(USERNAME_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                //get username (if empty = "User", if not empty = new user name) to update global value
                username = result.getString(USERNAME_KEY);

                //update username at UI
                mTextViewUsername.setText(username);

                //update username in shared preferences
                if (mPreferences.contains(SP_USERNAME_KEY)) { //if there is this key

                    //store in shared pref file current name
                    SharedPreferences.Editor spEditor = mPreferences.edit();
                    spEditor.putString(SP_USERNAME_KEY, username);
                    spEditor.apply();

                }

            }
        });

        getParentFragmentManager().setFragmentResultListener(BIRTHDAY_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                //get birthday to update global value
                birth_month = result.getInt(BIRTH_MONTH_KEY);
                birth_day = result.getInt(BIRTH_DAY_KEY);

                //update birthday at UI
                mTextViewBirthday.setText("" + birth_month + "/" + birth_day);

                //update birthday in shared preferences
                if (mPreferences.contains(SP_BIRTH_MONTH_KEY) && mPreferences.contains(SP_BIRTH_DAY_KEY)) { //if there is this key

                    //store in shared pref file current birthday info
                    SharedPreferences.Editor spEditor = mPreferences.edit();
                    spEditor.putInt(SP_BIRTH_MONTH_KEY, birth_month);
                    spEditor.putInt(SP_BIRTH_DAY_KEY, birth_day);
                    spEditor.apply();

                }

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //
        // find views
        //
        mLayoutUsername = view.findViewById(R.id.username_section);
        mTextViewUsername = view.findViewById(R.id.settings_profile_username_value);

        mLayoutBirthday = view.findViewById(R.id.birthday_section);
        mTextViewBirthday = view.findViewById(R.id.settings_profile_birthday_value);

        mAlarmSettingsCard = view.findViewById(R.id.settings_alarmSetting_card);
        mMissionCard = view.findViewById(R.id.settings_Mission_card);
        mGeneralCard = view.findViewById(R.id.settings_General_card);
        mFAQCard = view.findViewById(R.id.settings_FAQ_card);
        mAboutCard = view.findViewById(R.id.settings_About_card);

        mSettingsMainTitle = view.findViewById(R.id.settings_main_title);
        mProfileTitle = view.findViewById(R.id.settings_profile_card_title);
        mUsernameTitle = view.findViewById(R.id.settings_profile_username_label);
        mBirthdayTitle = view.findViewById(R.id.settings_profile_birthday_label);
        mAlarmSettingTitle = view.findViewById(R.id.settings_alarmSetting_textView);
        mMissionTitle = view.findViewById(R.id.settings_Mission_textView);
        mGeneralTitle = view.findViewById(R.id.settings_General_textView);
        mFAQTitle = view.findViewById(R.id.settings_FAQ_textView);
        mAboutTitle = view.findViewById(R.id.settings_About_textView);

        mPreferences = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        //set language
        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(getContext(), "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mSettingsMainTitle.setText(resources.getString(R.string.settings_main_title));
                    mProfileTitle.setText(resources.getString(R.string.settings_profile_card_title));
                    mUsernameTitle.setText(resources.getString(R.string.settings_profile_username_label));
                    mBirthdayTitle.setText(resources.getString(R.string.settings_profile_birthday_label));
                    mAlarmSettingTitle.setText(resources.getString(R.string.settings_alarmSetting_textView));
                    mMissionTitle.setText(resources.getString(R.string.settings_Mission_textView));
                    mGeneralTitle.setText(resources.getString(R.string.settings_General_textView));
                    mFAQTitle.setText(resources.getString(R.string.settings_FAQ_textView));
                    mAboutTitle.setText(resources.getString(R.string.settings_About_textView));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(getContext(), "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mSettingsMainTitle.setText(resources.getString(R.string.settings_main_title));
                    mProfileTitle.setText(resources.getString(R.string.settings_profile_card_title));
                    mUsernameTitle.setText(resources.getString(R.string.settings_profile_username_label));
                    mBirthdayTitle.setText(resources.getString(R.string.settings_profile_birthday_label));
                    mAlarmSettingTitle.setText(resources.getString(R.string.settings_alarmSetting_textView));
                    mMissionTitle.setText(resources.getString(R.string.settings_Mission_textView));
                    mGeneralTitle.setText(resources.getString(R.string.settings_General_textView));
                    mFAQTitle.setText(resources.getString(R.string.settings_FAQ_textView));
                    mAboutTitle.setText(resources.getString(R.string.settings_About_textView));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(getContext(), "en");
            resources = context.getResources();

            //set the views to the wanted language
            mSettingsMainTitle.setText(resources.getString(R.string.settings_main_title));
            mProfileTitle.setText(resources.getString(R.string.settings_profile_card_title));
            mUsernameTitle.setText(resources.getString(R.string.settings_profile_username_label));
            mBirthdayTitle.setText(resources.getString(R.string.settings_profile_birthday_label));
            mAlarmSettingTitle.setText(resources.getString(R.string.settings_alarmSetting_textView));
            mMissionTitle.setText(resources.getString(R.string.settings_Mission_textView));
            mGeneralTitle.setText(resources.getString(R.string.settings_General_textView));
            mFAQTitle.setText(resources.getString(R.string.settings_FAQ_textView));
            mAboutTitle.setText(resources.getString(R.string.settings_About_textView));

        }

        //
        // initialize some values
        //
        if (mPreferences.contains(SP_USERNAME_KEY)) { //if there is this key

            username = mPreferences.getString(SP_USERNAME_KEY, resources.getString(R.string.settings_profile_username_value));

            mTextViewUsername.setText(username);

        } else { //if key can't be found (app first launch)

            username = resources.getString(R.string.settings_profile_username_value); //default username

            //store in shared pref file current default name
            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putString(SP_USERNAME_KEY, username);
            spEditor.apply();

            mTextViewUsername.setText(username); //set value to UI

        }

        if (mPreferences.contains(SP_BIRTH_MONTH_KEY) && mPreferences.contains(SP_BIRTH_DAY_KEY)) { //if there is this key

            birth_month = mPreferences.getInt(SP_BIRTH_MONTH_KEY, 6);
            birth_day = mPreferences.getInt(SP_BIRTH_DAY_KEY, 25);

            mTextViewBirthday.setText("" + birth_month + "/" + birth_day);

        } else { //if key can't be found (app first launch)

            birth_month = 6;
            birth_day = 25;

            //store in shared pref file current default name
            SharedPreferences.Editor spEditor = mPreferences.edit();
            spEditor.putInt(SP_BIRTH_MONTH_KEY, birth_month);
            spEditor.putInt(SP_BIRTH_DAY_KEY, birth_day);
            spEditor.apply();

            mTextViewBirthday.setText("" + birth_month + "/" + birth_day); //set value to UI

        }

        //
        // listeners
        //
        mLayoutUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getParentFragmentManager();
                UsernameConfigurationFragment dialog = UsernameConfigurationFragment.newInstance(username); //pass the current name to the alert dialog
                dialog.show(manager, DIALOG_USERNAME);

            }
        });

        mLayoutBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getParentFragmentManager();
                BirthdayConfigurationFragment dialog = BirthdayConfigurationFragment.newInstance(birth_day, birth_month); //pass current birthDay and birthMonth
                dialog.show(manager, DIALOG_BIRTHDAY);

            }
        });

        mAlarmSettingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AlarmSettingActivity.class);
                startActivity(intent);

            }
        });

        mMissionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MissionSettingsActivity.class);
                startActivity(intent);

            }
        });

        mGeneralCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), GeneralSettingsActivity.class);
                startActivity(intent);

            }
        });

        mFAQCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), HelpFeedbackActivity.class);
                startActivity(intent);

            }
        });

        mAboutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onResume() {

        super.onResume();

        //update language
        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(getContext(), "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mSettingsMainTitle.setText(resources.getString(R.string.settings_main_title));
                    mProfileTitle.setText(resources.getString(R.string.settings_profile_card_title));
                    mUsernameTitle.setText(resources.getString(R.string.settings_profile_username_label));
                    mBirthdayTitle.setText(resources.getString(R.string.settings_profile_birthday_label));
                    mAlarmSettingTitle.setText(resources.getString(R.string.settings_alarmSetting_textView));
                    mMissionTitle.setText(resources.getString(R.string.settings_Mission_textView));
                    mGeneralTitle.setText(resources.getString(R.string.settings_General_textView));
                    mFAQTitle.setText(resources.getString(R.string.settings_FAQ_textView));
                    mAboutTitle.setText(resources.getString(R.string.settings_About_textView));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(getContext(), "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mSettingsMainTitle.setText(resources.getString(R.string.settings_main_title));
                    mProfileTitle.setText(resources.getString(R.string.settings_profile_card_title));
                    mUsernameTitle.setText(resources.getString(R.string.settings_profile_username_label));
                    mBirthdayTitle.setText(resources.getString(R.string.settings_profile_birthday_label));
                    mAlarmSettingTitle.setText(resources.getString(R.string.settings_alarmSetting_textView));
                    mMissionTitle.setText(resources.getString(R.string.settings_Mission_textView));
                    mGeneralTitle.setText(resources.getString(R.string.settings_General_textView));
                    mFAQTitle.setText(resources.getString(R.string.settings_FAQ_textView));
                    mAboutTitle.setText(resources.getString(R.string.settings_About_textView));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(getContext(), "en");
            resources = context.getResources();

            //set the views to the wanted language
            mSettingsMainTitle.setText(resources.getString(R.string.settings_main_title));
            mProfileTitle.setText(resources.getString(R.string.settings_profile_card_title));
            mUsernameTitle.setText(resources.getString(R.string.settings_profile_username_label));
            mBirthdayTitle.setText(resources.getString(R.string.settings_profile_birthday_label));
            mAlarmSettingTitle.setText(resources.getString(R.string.settings_alarmSetting_textView));
            mMissionTitle.setText(resources.getString(R.string.settings_Mission_textView));
            mGeneralTitle.setText(resources.getString(R.string.settings_General_textView));
            mFAQTitle.setText(resources.getString(R.string.settings_FAQ_textView));
            mAboutTitle.setText(resources.getString(R.string.settings_About_textView));

        }

    }
}