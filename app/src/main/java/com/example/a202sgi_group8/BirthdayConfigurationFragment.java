package com.example.a202sgi_group8;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BirthdayConfigurationFragment extends DialogFragment {

    //
    // constants
    //
    private static final String ARG_BIRTH_MONTH = "birth_month";
    private static final String ARG_BIRTH_DAY = "birth_day";
    private static final String BIRTH_DAY_KEY = "birth_day_key";
    private static final String BIRTH_MONTH_KEY = "birth_month_key";
    private static final String BIRTHDAY_REQUEST_KEY = "birthday_request_key";

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
    private NumberPicker mBirthMonth;
    private NumberPicker mBirthDay;
    private Button mBtnOK;
    private Button mBtnCancel;

    private TextView mBirthdayTitle;
    private TextView mBirthdayDesc;
    private TextView mMonth;
    private TextView mDay;

    //
    // values
    //
    private int newMonth;
    private int newDay;

    private int language;

    public static BirthdayConfigurationFragment newInstance(int day, int month) {

        Bundle args = new Bundle();
        args.putInt(ARG_BIRTH_MONTH, month);
        args.putInt(ARG_BIRTH_DAY, day);

        BirthdayConfigurationFragment fragment = new BirthdayConfigurationFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //get previous last set birth date
        int month = getArguments().getInt(ARG_BIRTH_MONTH);
        int day = getArguments().getInt(ARG_BIRTH_DAY);

        //find content layout
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_birthday, null);

        //create alert dialog
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(v) //set content layout
                .create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.settings_dialogs_background);

        //
        // find views inside content layout
        //
        mBirthMonth = v.findViewById(R.id.np_birth_month);
        mBirthDay = v.findViewById(R.id.np_birth_day);
        mBtnOK = v.findViewById(R.id.settings_birthday_btnOK);
        mBtnCancel = v.findViewById(R.id.settings_birthday_btnCancel);

        mBirthdayTitle = v.findViewById(R.id.birthday_textView);
        mBirthdayDesc = v.findViewById(R.id.birthday_textView2);
        mMonth = v.findViewById(R.id.birthMonth_label);
        mDay = v.findViewById(R.id.birthDay_label);

        //set language
        mPreferences = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(getContext(), "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mBirthdayTitle.setText(resources.getString(R.string.birthday_textView));
                    mBirthdayDesc.setText(resources.getString(R.string.birthday_textView2));
                    mMonth.setText(resources.getString(R.string.birthMonth_label));
                    mDay.setText(resources.getString(R.string.birthDay_label));
                    mBtnOK.setText(resources.getString(R.string.settings_birthday_btnOK));
                    mBtnCancel.setText(resources.getString(R.string.settings_birthday_btnCancel));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(getContext(), "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mBirthdayTitle.setText(resources.getString(R.string.birthday_textView));
                    mBirthdayDesc.setText(resources.getString(R.string.birthday_textView2));
                    mMonth.setText(resources.getString(R.string.birthMonth_label));
                    mDay.setText(resources.getString(R.string.birthDay_label));
                    mBtnOK.setText(resources.getString(R.string.settings_birthday_btnOK));
                    mBtnCancel.setText(resources.getString(R.string.settings_birthday_btnCancel));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(getContext(), "en");
            resources = context.getResources();

            //set the views to the wanted language
            mBirthdayTitle.setText(resources.getString(R.string.birthday_textView));
            mBirthdayDesc.setText(resources.getString(R.string.birthday_textView2));
            mMonth.setText(resources.getString(R.string.birthMonth_label));
            mDay.setText(resources.getString(R.string.birthDay_label));
            mBtnOK.setText(resources.getString(R.string.settings_birthday_btnOK));
            mBtnCancel.setText(resources.getString(R.string.settings_birthday_btnCancel));

        }

        //initialize view with previous values
        mBirthMonth.setMinValue(1);
        mBirthMonth.setMaxValue(12);
        mBirthMonth.setWrapSelectorWheel(true);
        mBirthMonth.setValue(month);

        mBirthDay.setMinValue(1);
        mBirthDay.setMaxValue(31);
        mBirthDay.setWrapSelectorWheel(true);
        mBirthDay.setValue(day);

        //listeners....

        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newMonth = mBirthMonth.getValue();
                newDay = mBirthDay.getValue();

                Bundle result = new Bundle();
                result.putInt(BIRTH_MONTH_KEY, newMonth);
                result.putInt(BIRTH_DAY_KEY, newDay);
                getParentFragmentManager().setFragmentResult(BIRTHDAY_REQUEST_KEY, result);

                alertDialog.cancel(); //close the dialog

            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.cancel(); //close the dialog

            }
        });

        return alertDialog;
    }
}
