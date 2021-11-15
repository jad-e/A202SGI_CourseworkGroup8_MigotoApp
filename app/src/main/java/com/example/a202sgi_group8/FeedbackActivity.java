package com.example.a202sgi_group8;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    //
    // constant values
    //
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
    ImageView mBackBtn;
    RadioGroup mRbGroup;
    RadioButton mbugCrash;
    RadioButton mFunctionImprove;
    EditText mUserContact;
    EditText mFeedbackContent;
    Button mSendBtn;

    private TextView mMainTitle;
    private TextView mFeedbackTypeTitle;


    //
    //values
    //
    private String problemDomain;

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
        setContentView(R.layout.activity_feedback);

        //
        // find views
        //
        mBackBtn = findViewById(R.id.FeedbackPage_backImageView);
        mRbGroup = findViewById(R.id.radioGroup);
        mbugCrash = findViewById(R.id.bug_crash_RB);
        mFunctionImprove = findViewById(R.id.function_improve_RB);
        mUserContact = findViewById(R.id.feedback_userContactDetails);
        mFeedbackContent = findViewById(R.id.feedback_userFeedbackContent);
        mSendBtn = findViewById(R.id.feedback_send_btn);

        mMainTitle = findViewById(R.id.FeedbackPage_main_title);
        mFeedbackTypeTitle = findViewById(R.id.feedback_selectFeedBackType_title);

        //set language
        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(FeedbackActivity.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mMainTitle.setText(resources.getString(R.string.FeedbackPage_main_title));
                    mFeedbackTypeTitle.setText(resources.getString(R.string.feedback_selectFeedBackType_title));
                    mbugCrash.setText(resources.getString(R.string.bug_crash_RB));
                    mFunctionImprove.setText(resources.getString(R.string.function_improve_RB));
                    mUserContact.setHint(resources.getString(R.string.feedback_userContactDetails));
                    mFeedbackContent.setHint(resources.getString(R.string.feedback_userFeedbackContent));
                    mSendBtn.setText(resources.getString(R.string.feedback_send_btn));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(FeedbackActivity.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mMainTitle.setText(resources.getString(R.string.FeedbackPage_main_title));
                    mFeedbackTypeTitle.setText(resources.getString(R.string.feedback_selectFeedBackType_title));
                    mbugCrash.setText(resources.getString(R.string.bug_crash_RB));
                    mFunctionImprove.setText(resources.getString(R.string.function_improve_RB));
                    mUserContact.setHint(resources.getString(R.string.feedback_userContactDetails));
                    mFeedbackContent.setHint(resources.getString(R.string.feedback_userFeedbackContent));
                    mSendBtn.setText(resources.getString(R.string.feedback_send_btn));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(FeedbackActivity.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            mMainTitle.setText(resources.getString(R.string.FeedbackPage_main_title));
            mFeedbackTypeTitle.setText(resources.getString(R.string.feedback_selectFeedBackType_title));
            mbugCrash.setText(resources.getString(R.string.bug_crash_RB));
            mFunctionImprove.setText(resources.getString(R.string.function_improve_RB));
            mUserContact.setHint(resources.getString(R.string.feedback_userContactDetails));
            mFeedbackContent.setHint(resources.getString(R.string.feedback_userFeedbackContent));
            mSendBtn.setText(resources.getString(R.string.feedback_send_btn));

        }

        //
        //initialization of values...
        //

        mbugCrash.setChecked(true); //by default checked
        problemDomain = mbugCrash.getText().toString();

        //
        // listeners
        //

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mRbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.bug_crash_RB:
                        problemDomain = mbugCrash.getText().toString();
                        break;
                    case R.id.function_improve_RB:
                        problemDomain = mFunctionImprove.getText().toString();
                        break;

                }
            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //send email to developers

                String userContact = "";
                String feedbackContent = "";

                userContact = mUserContact.getText().toString();
                feedbackContent = mFeedbackContent.getText().toString();

                //check if user contact if empty
                if (userContact.isEmpty()) {

                    userContact = "Not provided.";

                }

                //check if feedback content is empty
                if (feedbackContent.isEmpty()) {

                    Toast.makeText(FeedbackActivity.this, resources.getString(R.string.feedback_content_empty_toast), Toast.LENGTH_SHORT).show();

                } else { //create email

                    String subject = "Migoto User Feedback - " + problemDomain;

                    String email = "tanseowsean@yahoo.com";

                    String emailBody = feedbackContent
                            + "\n\n"
                            + "User Phone Number: "
                            + userContact;


                    Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                    selectorIntent.setData(Uri.parse("mailto:"));

                    final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    emailIntent.setSelector(selectorIntent);

                    startActivity(Intent.createChooser(emailIntent, ""));

                }

            }
        });

    }

}