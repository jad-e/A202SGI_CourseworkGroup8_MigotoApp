package com.example.a202sgi_group8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HelpFeedbackActivity extends AppCompatActivity {

    //
    // constant values
    //
    private final String SP_LANGUAGE_KEY = "sp_language_key";
    private final String SP_THEME_KEY = "sp_theme_key";

    //
    // other variables
    //
    ArrayAdapter<String> mAdapter;

    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    private Context context;
    private Resources resources;

    //
    // views
    //
    ImageView mBackImgView;
    ListView mListView;
    FloatingActionButton fab_contactUs;

    private TextView mTitle;

    //
    // values
    //
    String[] listItems = {
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

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
        setContentView(R.layout.activity_help_feedback);

        //
        // find views
        //
        mBackImgView = findViewById(R.id.FAQPage_backImageView);
        mListView = findViewById(R.id.FAQ_listView);
        fab_contactUs = findViewById(R.id.contact_us_fab);

        mTitle = findViewById(R.id.FAQPage_main_title);

        //set language
        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(HelpFeedbackActivity.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mTitle.setText(resources.getString(R.string.FAQPage_main_title));
                    listItems[0] = resources.getString(R.string.ques0);
                    listItems[1] = resources.getString(R.string.ques1);
                    listItems[2] = resources.getString(R.string.ques2);
                    listItems[3] = resources.getString(R.string.ques3);
                    listItems[4] = resources.getString(R.string.ques4);
                    listItems[5] = resources.getString(R.string.ques5);
                    listItems[6] = resources.getString(R.string.ques6);
                    listItems[7] = resources.getString(R.string.ques7);
                    listItems[8] = resources.getString(R.string.ques8);
                    listItems[9] = resources.getString(R.string.ques9);
                    listItems[10] = resources.getString(R.string.ques10);
                    listItems[11] = resources.getString(R.string.ques11);
                    listItems[12] = resources.getString(R.string.ques12);

                    break;
                case 2:
                    context = LocaleHelper.setLocale(HelpFeedbackActivity.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    mTitle.setText(resources.getString(R.string.FAQPage_main_title));
                    listItems[0] = resources.getString(R.string.ques0);
                    listItems[1] = resources.getString(R.string.ques1);
                    listItems[2] = resources.getString(R.string.ques2);
                    listItems[3] = resources.getString(R.string.ques3);
                    listItems[4] = resources.getString(R.string.ques4);
                    listItems[5] = resources.getString(R.string.ques5);
                    listItems[6] = resources.getString(R.string.ques6);
                    listItems[7] = resources.getString(R.string.ques7);
                    listItems[8] = resources.getString(R.string.ques8);
                    listItems[9] = resources.getString(R.string.ques9);
                    listItems[10] = resources.getString(R.string.ques10);
                    listItems[11] = resources.getString(R.string.ques11);
                    listItems[12] = resources.getString(R.string.ques12);

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(HelpFeedbackActivity.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            mTitle.setText(resources.getString(R.string.FAQPage_main_title));
            listItems[0] = resources.getString(R.string.ques0);
            listItems[1] = resources.getString(R.string.ques1);
            listItems[2] = resources.getString(R.string.ques2);
            listItems[3] = resources.getString(R.string.ques3);
            listItems[4] = resources.getString(R.string.ques4);
            listItems[5] = resources.getString(R.string.ques5);
            listItems[6] = resources.getString(R.string.ques6);
            listItems[7] = resources.getString(R.string.ques7);
            listItems[8] = resources.getString(R.string.ques8);
            listItems[9] = resources.getString(R.string.ques9);
            listItems[10] = resources.getString(R.string.ques10);
            listItems[11] = resources.getString(R.string.ques11);
            listItems[12] = resources.getString(R.string.ques12);

        }

        //
        // listeners
        //

        mBackImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        fab_contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open feedback activity
                Intent intent = new Intent(HelpFeedbackActivity.this, FeedbackActivity.class);
                startActivity(intent);

            }
        });

        mAdapter = new ArrayAdapter<>(this, R.layout.faq_list_view_item, R.id.listView_item_txtView, listItems);
        mListView.setAdapter(mAdapter);

        //set on click listener for each list item
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //String text = mAdapter.getItem(position); //position of item which is clicked
                //Toast.makeText(HelpFeedbackActivity.this, text, Toast.LENGTH_SHORT).show();

                Intent intent = FAQItemActivity.newIntent(HelpFeedbackActivity.this, position); //pass the clicked list item's position
                startActivity(intent);

            }
        });


    }

}