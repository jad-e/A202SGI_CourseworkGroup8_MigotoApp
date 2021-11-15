package com.example.a202sgi_group8;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

public class AlarmClockActivity extends AppCompatActivity {

    private static final String EXTRA_ALARM_CLOCK_ID = "com.example.a202sgi_group8.alarmClock_id";
    private static final String EXTRA_ALARM_CLOCK_ID2 = "com.example.a202sgi_group8.alarmClock_id2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.alarm_clock_fragment_container);

        if (fragment == null) {

            String alarmClockId = getIntent()
                    .getStringExtra(EXTRA_ALARM_CLOCK_ID);

            int alarmClockId2 = getIntent().getIntExtra(EXTRA_ALARM_CLOCK_ID2, 0);

            fragment = AlarmClockFragment.newInstance(alarmClockId, alarmClockId2);

            fm.beginTransaction()
                    .add(R.id.alarm_clock_fragment_container, fragment)
                    .commit();

        }

    }

    //override system back button
    @Override
    public void onBackPressed() {

        //changes made will be gone!
        //will not be added into list !

        super.onBackPressed();

    }

    public static Intent newIntent(Context packageContext, String AlarmClockId) {

        Intent intent = new Intent(packageContext, AlarmClockActivity.class);
        intent.putExtra(EXTRA_ALARM_CLOCK_ID, AlarmClockId);
        return intent;

    }

}