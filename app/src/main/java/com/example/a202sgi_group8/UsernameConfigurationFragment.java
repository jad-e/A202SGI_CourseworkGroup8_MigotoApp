package com.example.a202sgi_group8;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class UsernameConfigurationFragment extends DialogFragment {

    //
    // constants
    //
    private static final String ARG_USERNAME = "username";
    private static final String USERNAME_KEY = "user_name_key";
    private static final String USERNAME_REQUEST_KEY = "username_request_key";

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
    private EditText mUsernameEditText;
    private Button mBtnOK;
    private Button mBtnCancel;

    private TextView mUsernameTitle;
    private TextView mUsernameDesc;

    //values
    private String mNewUsername;

    private int language;

    public static UsernameConfigurationFragment newInstance(String username) {

        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);

        UsernameConfigurationFragment fragment = new UsernameConfigurationFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String username = getArguments().getString(ARG_USERNAME); //get prev username

        //find content layout for alert dialog
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_username, null);

        //create alert dialog
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(v) //set content layout
                .create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.settings_dialogs_background);

        //
        // find views inside content layout
        //
        mUsernameEditText = v.findViewById(R.id.username_input);
        mBtnOK = v.findViewById(R.id.settings_username_btnOK);
        mBtnCancel = v.findViewById(R.id.settings_username_btnCancel);

        mUsernameTitle = v.findViewById(R.id.usernameTextView);
        mUsernameDesc = v.findViewById(R.id.username_desc_textView2);

        //set language
        mPreferences = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(getContext(), "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mUsernameTitle.setText(resources.getString(R.string.usernameTextView));
                    mUsernameDesc.setText(resources.getString(R.string.username_desc_textView2));
                    mUsernameEditText.setHint(resources.getString(R.string.username_input));
                    mBtnOK.setText(resources.getString(R.string.settings_username_btnOK));
                    mBtnCancel.setText(resources.getString(R.string.settings_username_btnCancel));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(getContext(), "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mUsernameTitle.setText(resources.getString(R.string.usernameTextView));
                    mUsernameDesc.setText(resources.getString(R.string.username_desc_textView2));
                    mUsernameEditText.setHint(resources.getString(R.string.username_input));
                    mBtnOK.setText(resources.getString(R.string.settings_username_btnOK));
                    mBtnCancel.setText(resources.getString(R.string.settings_username_btnCancel));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(getContext(), "en");
            resources = context.getResources();

            //set the views to the wanted language
            mUsernameTitle.setText(resources.getString(R.string.usernameTextView));
            mUsernameDesc.setText(resources.getString(R.string.username_desc_textView2));
            mUsernameEditText.setHint(resources.getString(R.string.username_input));
            mBtnOK.setText(resources.getString(R.string.settings_username_btnOK));
            mBtnCancel.setText(resources.getString(R.string.settings_username_btnCancel));

        }


        //initialize username with previous last set username
        mUsernameEditText.setText(username);

        //listeners....

        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //what if empty?
                if (TextUtils.isEmpty(mUsernameEditText.getText().toString())) { //if empty (default username - user)

                    mNewUsername = resources.getString(R.string.settings_profile_username_value);

                } else { //edit text is not empty

                    mNewUsername = mUsernameEditText.getText().toString();

                }

                Bundle result = new Bundle();
                result.putString(USERNAME_KEY, mNewUsername);
                getParentFragmentManager().setFragmentResult(USERNAME_REQUEST_KEY, result);

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
