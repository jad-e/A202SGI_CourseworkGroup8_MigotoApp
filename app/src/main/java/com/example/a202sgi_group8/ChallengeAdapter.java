package com.example.a202sgi_group8;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {

    //constants
    private final String SP_LANGUAGE_KEY = "sp_language_key";

    // variables
    Context context;
    ArrayList<Challenge> mChallengeArrayList;

    Context forLang;

    DatabaseReference database;

    private Resources resources;
    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    //values
    private int language;

    public ChallengeAdapter(Context context, ArrayList<Challenge> challengeArrayList) {

        this.context = context;
        mChallengeArrayList = challengeArrayList;
        forLang = context;

    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_challenge, parent, false);

        return new ChallengeViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {

        mPreferences = forLang.getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        //set language
        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    forLang = LocaleHelper.setLocale(forLang, "en");
                    resources = forLang.getResources();

                    break;
                case 2:
                    forLang = LocaleHelper.setLocale(forLang, "zh");
                    resources = forLang.getResources();

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            forLang = LocaleHelper.setLocale(forLang, "en");
            resources = forLang.getResources();

        }

        Challenge mChallenge = mChallengeArrayList.get(position);

        holder.mDate.setText(mChallenge.getFinalizedDate());

        switch (mChallenge.getStatus()) {

            case 1: //success
                holder.mStatusText.setText(resources.getString(R.string.complete_challenge_status_text));
                holder.mStatusImage.setImageResource(R.drawable.sucess);
                break;
            case 2: //fail
                holder.mStatusText.setText(resources.getString(R.string.fail_challenge_status_text));
                holder.mStatusImage.setImageResource(R.drawable.dismiss_from_app);
                break;

        }

        holder.mAlarmTime.setText(mChallenge.getAlarmTime());

    }

    @Override
    public int getItemCount() {

        return mChallengeArrayList.size();

    }

    public static class ChallengeViewHolder extends RecyclerView.ViewHolder {

        TextView mDate;
        TextView mStatusText;
        ImageView mStatusImage;
        TextView mAlarmTime;

        public ChallengeViewHolder(@NonNull View itemView) {

            super(itemView);

            mDate = itemView.findViewById(R.id.challenge_finalize_date_time);
            mStatusImage = itemView.findViewById(R.id.challenge_status_image);
            mStatusText = itemView.findViewById(R.id.challenge_status);
            mAlarmTime = itemView.findViewById(R.id.challenge_alarm_wake_sleep_time);

        }

    }

}
