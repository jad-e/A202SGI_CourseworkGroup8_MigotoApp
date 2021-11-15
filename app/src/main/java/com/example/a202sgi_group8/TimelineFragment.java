package com.example.a202sgi_group8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.spec.IvParameterSpec;

public class TimelineFragment extends Fragment {

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

    DatabaseReference database;

    private ChallengeAdapter mAdapter;
    private ArrayList mChallengeArrayList;

    //
    // views
    //
    private RecyclerView mChallengesRecyclerView;
    private TextView mNoChallengeRecordTextView;

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

        return inflater.inflate(R.layout.fragment_timeline, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //
        // find views
        //
        mChallengesRecyclerView = view.findViewById(R.id.challenge_recycler_view);
        mNoChallengeRecordTextView = view.findViewById(R.id.no_challenges_record_textView);

        //initialize
        mChallengesRecyclerView.setHasFixedSize(true);
        mChallengesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mChallengeArrayList = new ArrayList<>();
        mAdapter = new ChallengeAdapter(getActivity(), mChallengeArrayList);
        mChallengesRecyclerView.setAdapter(mAdapter);


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

        //get data from database
        database = FirebaseDatabase.getInstance().getReference("Challenges");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {

                    Challenge challenge = dSnapshot.getValue(Challenge.class);
                    mChallengeArrayList.add(challenge);

                }

                //invert array list to display latest on top of recycler view
                Collections.reverse(mChallengeArrayList);

                mAdapter.notifyDataSetChanged();

                //check if array list is empty
                if (mAdapter.getItemCount() == 0) {

                    mChallengesRecyclerView.setVisibility(View.GONE);
                    mNoChallengeRecordTextView.setVisibility(View.VISIBLE);

                    //set the views to the wanted language
                    mNoChallengeRecordTextView.setText(resources.getString(R.string.no_challenges_record));


                } else {

                    mChallengesRecyclerView.setVisibility(View.VISIBLE);

                    mNoChallengeRecordTextView.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}