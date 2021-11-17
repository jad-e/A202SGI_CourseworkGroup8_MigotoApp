package com.example.a202sgi_group8;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BedTimeFragment extends Fragment {

    //
    // constants
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

    AlarmClockAdapter mAdapter;
    DatabaseReference database;
    ArrayList mClockArrayList;

    //
    // views
    //
    private RecyclerView mAlarmClocksRecyclerView;
    private FloatingActionButton fab_addBtn;
    private TextView mNoAlarmTextView;

    //
    //values
    //
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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bedtime, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //
        // find views
        //
        fab_addBtn = view.findViewById(R.id.addBtnFab);
        mNoAlarmTextView = view.findViewById(R.id.no_alarms_set_textView);
        mAlarmClocksRecyclerView = view.findViewById(R.id.alarm_clocks_recycler_view);

        //
        // initialize
        //
        database = FirebaseDatabase.getInstance().getReference("AlarmClocks");

        mAlarmClocksRecyclerView.setHasFixedSize(true);
        mAlarmClocksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //
        // listeners
        //
        fab_addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //start create alarm activity
                Intent intent = new Intent(getActivity(), CreateAlarmActivity.class);
                startActivity(intent);

            }
        });

        mClockArrayList = new ArrayList<>();
        mAdapter = new AlarmClockAdapter(getActivity(), mClockArrayList);
        mAlarmClocksRecyclerView.setAdapter(mAdapter);

        //set language
        mPreferences = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(getContext(), "en");
                    resources = context.getResources();

                    break;
                case 2:
                    context = LocaleHelper.setLocale(getContext(), "zh");
                    resources = context.getResources();

                    break;

            }

        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(getContext(), "en");
            resources = context.getResources();

        }

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {

                    AlarmClock alarmClock = dSnapshot.getValue(AlarmClock.class);
                    mClockArrayList.add(alarmClock);

                }

                mAdapter.notifyDataSetChanged();

                //check if array list is empty
                if (mAdapter.getItemCount() == 0) {

                    mAlarmClocksRecyclerView.setVisibility(View.GONE);
                    mNoAlarmTextView.setVisibility(View.VISIBLE);

                    //set the views to the wanted language
                    mNoAlarmTextView.setText(resources.getString(R.string.no_alarm_set));


                } else {

                    mAlarmClocksRecyclerView.setVisibility(View.VISIBLE);

                    mNoAlarmTextView.setVisibility(View.GONE);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    AlarmClock alarmClock = (AlarmClock) mClockArrayList.get(viewHolder.getAdapterPosition());
                    database.child(alarmClock.getAlarmId()).setValue(null);

                    alarmClock.cancelAlarm(context);

                    //refresh activity
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(getActivity().getIntent());
                    getActivity().overridePendingTransition(0, 0);

                }
            });

        //attach helper object into recycler view
        helper.attachToRecyclerView(mAlarmClocksRecyclerView);

    }

}
