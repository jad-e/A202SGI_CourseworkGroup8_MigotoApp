package com.example.a202sgi_group8;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

public class AboutActivity extends AppCompatActivity {

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
    private ImageView mImgViewBack;
    private Button mBtnRate;
    private Button mBtnShare;

    private TextView mMainTitle;
    private TextView mAppName;
    private TextView mRights1;
    private TextView mRights2;

    //
    //values
    //
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
        setContentView(R.layout.activity_about);

        //
        // find views
        //
        mImgViewBack = findViewById(R.id.aboutPage_backImageView);
        mBtnRate = findViewById(R.id.about_rate_app_button);
        mBtnShare = findViewById(R.id.about_share_button);

        mMainTitle = findViewById(R.id.aboutPage_main_title);
        mAppName = findViewById(R.id.about_app_name);
        mRights1 = findViewById(R.id.about_copyright_textView1);
        mRights2 = findViewById(R.id.about_copyright_textView2);

        //set language
        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(AboutActivity.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mMainTitle.setText(resources.getString(R.string.aboutPage_main_title));
                    mAppName.setText(resources.getString(R.string.about_app_name));
                    mBtnRate.setText(resources.getString(R.string.about_rate_app_button));
                    mBtnShare.setText(resources.getString(R.string.about_share_button));
                    mRights1.setText(resources.getString(R.string.about_copyright_textView1));
                    mRights2.setText(resources.getString(R.string.about_copyright_textView2));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(AboutActivity.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mMainTitle.setText(resources.getString(R.string.aboutPage_main_title));
                    mAppName.setText(resources.getString(R.string.about_app_name));
                    mBtnRate.setText(resources.getString(R.string.about_rate_app_button));
                    mBtnShare.setText(resources.getString(R.string.about_share_button));
                    mRights1.setText(resources.getString(R.string.about_copyright_textView1));
                    mRights2.setText(resources.getString(R.string.about_copyright_textView2));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(AboutActivity.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            mMainTitle.setText(resources.getString(R.string.aboutPage_main_title));
            mAppName.setText(resources.getString(R.string.about_app_name));
            mBtnRate.setText(resources.getString(R.string.about_rate_app_button));
            mBtnShare.setText(resources.getString(R.string.about_share_button));
            mRights1.setText(resources.getString(R.string.about_copyright_textView1));
            mRights2.setText(resources.getString(R.string.about_copyright_textView2));

        }


        //listeners...

        mImgViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mBtnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setPackage("com.android.vending");
                    i.setData(Uri.parse("market://details?id=" + "com.microsoft.office.onenote"));
                    startActivity(i);

                } catch (android.content.ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + "com.microsoft.office.onenote")));

                }

            }
        });


        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = resources.getString(R.string.share_content);

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                startActivity(Intent.createChooser(intent, ""));

            }
        });

    }


}