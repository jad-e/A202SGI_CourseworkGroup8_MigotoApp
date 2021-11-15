package com.example.a202sgi_group8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

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

    private BottomNavigationView bottomNav;
    private Menu menu;
    private MenuItem nav_bedTime;
    private MenuItem nav_timeline;
    private MenuItem nav_settings;

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
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNavigationView);

        //set language
        menu = bottomNav.getMenu();

        nav_bedTime = menu.findItem(R.id.alarm);
        nav_timeline = menu.findItem(R.id.timeline);
        nav_settings = menu.findItem(R.id.settings);

        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(MainActivity.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    nav_bedTime.setTitle(resources.getString(R.string.menu_alarm));
                    nav_timeline.setTitle(resources.getString(R.string.menu_timeline));
                    nav_settings.setTitle(resources.getString(R.string.menu_settings));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(MainActivity.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    nav_bedTime.setTitle(resources.getString(R.string.menu_alarm));
                    nav_timeline.setTitle(resources.getString(R.string.menu_timeline));
                    nav_settings.setTitle(resources.getString(R.string.menu_settings));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(MainActivity.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            nav_bedTime.setTitle(resources.getString(R.string.menu_alarm));
            nav_timeline.setTitle(resources.getString(R.string.menu_timeline));
            nav_settings.setTitle(resources.getString(R.string.menu_settings));

        }


        bottomNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BedTimeFragment()).commit();

    }

    @Override
    protected void onResume() {

        super.onResume();

        //update language

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(MainActivity.this, "en");
                    resources = context.getResources();

                    //set the views to the wanted language
                    nav_bedTime.setTitle(resources.getString(R.string.menu_alarm));
                    nav_timeline.setTitle(resources.getString(R.string.menu_timeline));
                    nav_settings.setTitle(resources.getString(R.string.menu_settings));

                    break;
                case 2:
                    context = LocaleHelper.setLocale(MainActivity.this, "zh");
                    resources = context.getResources();

                    //set the views to the wanted language
                    nav_bedTime.setTitle(resources.getString(R.string.menu_alarm));
                    nav_timeline.setTitle(resources.getString(R.string.menu_timeline));
                    nav_settings.setTitle(resources.getString(R.string.menu_settings));

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(MainActivity.this, "en");
            resources = context.getResources();

            //set the views to the wanted language
            nav_bedTime.setTitle(resources.getString(R.string.menu_alarm));
            nav_timeline.setTitle(resources.getString(R.string.menu_timeline));
            nav_settings.setTitle(resources.getString(R.string.menu_settings));

        }

    }

    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.alarm:
                            selectedFragment = new BedTimeFragment();
                            break;
                        case R.id.timeline:
                            selectedFragment = new TimelineFragment();
                            break;
                        case R.id.settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };
}