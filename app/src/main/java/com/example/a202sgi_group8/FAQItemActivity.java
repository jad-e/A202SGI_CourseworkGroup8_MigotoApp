package com.example.a202sgi_group8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.UUID;

public class FAQItemActivity extends AppCompatActivity {

    //
    // constant values
    //
    private static final String EXTRA_FAQ_ITEM_POS = "faq_item_pos";
    private final String SP_THEME_KEY = "sp_theme_key";

    //
    // other variables
    //
    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    //
    // values
    //
    private int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q_item);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.faq_fragment_container);

        if (fragment == null) {

            int faqPosition = getIntent()
                    .getIntExtra(EXTRA_FAQ_ITEM_POS, 0);

            fragment = FAQItemFragment.newInstance(faqPosition);

            fm.beginTransaction()
                    .add(R.id.faq_fragment_container, fragment)
                    .commit();

        }

    }


    public static Intent newIntent(Context packageContext, int listItemPos) {

        Intent intent = new Intent(packageContext, FAQItemActivity.class);
        intent.putExtra(EXTRA_FAQ_ITEM_POS, listItemPos);
        return intent;

    }
}