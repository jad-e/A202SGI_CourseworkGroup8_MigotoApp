package com.example.a202sgi_group8;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AlarmClockAdapter extends RecyclerView.Adapter<AlarmClockAdapter.AlarmClockViewHolder> {

    //constants
    private final String SP_LANGUAGE_KEY = "sp_language_key";

    Context context;
    ArrayList<AlarmClock> mAlarmClockArrayList;

    Context forLang;

    private Resources resources;
    private SharedPreferences mPreferences;
    private String spFileName = "settingsSpFile";

    DatabaseReference database;

    //values
    private int language;


    public AlarmClockAdapter(Context context, ArrayList<AlarmClock> alarmClockArrayList) {

        this.context = context;
        mAlarmClockArrayList = alarmClockArrayList;
        forLang = context;

    }

    @NonNull
    @Override
    public AlarmClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater
                .inflate(R.layout.list_item_alarm_clock, parent, false);

        return new AlarmClockViewHolder(view);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull AlarmClockViewHolder holder, int position) {

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

        AlarmClock mAlarmClock = mAlarmClockArrayList.get(position);


        holder.mConstraintLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        holder.mWakeUpTime.setText(String.format(Locale.getDefault(), "%02d:%02d", mAlarmClock.getAlarmWakeHr(), mAlarmClock.getAlarmWakeMin()));

        holder.mIsAlarmOn.setChecked(mAlarmClock.isOnAlarm());

        if (holder.mIsAlarmOn.isChecked()) {

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(R.attr.lic_card_active_details_color, typedValue, true);
            @ColorInt int color = typedValue.data;

            String hexColor = String.format("#%06X", (0xFFFFFF & color));

            holder.mWakeUpTime.setTextColor(color);
            holder.horizontalDivider.setBackgroundColor(color);
            holder.mAlarmTitle.setTextColor(color);
            holder.mDropDown.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);

            holder.mMissionIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.mMissionLabel.setTextColor(color);
            holder.mMissionDesc.setTextColor(color);

            holder.mRingtoneIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.mRingtoneLabel.setTextColor(color);
            holder.mRingtoneDesc.setTextColor(color);

            holder.mVibrateIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.mVibrateTextView.setTextColor(color);
            holder.mVibrateDesc.setTextColor(color);

        } else {

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(R.attr.lic_card_inactive_details_color, typedValue, true);
            @ColorInt int color = typedValue.data;

            String hexColor = String.format("#%06X", (0xFFFFFF & color));

            holder.mWakeUpTime.setTextColor(color);
            holder.horizontalDivider.setBackgroundColor(color);
            holder.mAlarmTitle.setTextColor(color);
            holder.mDropDown.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);

            holder.mMissionIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.mMissionLabel.setTextColor(color);
            holder.mMissionDesc.setTextColor(color);

            holder.mRingtoneIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.mRingtoneLabel.setTextColor(color);
            holder.mRingtoneDesc.setTextColor(color);

            holder.mVibrateIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.mVibrateTextView.setTextColor(color);
            holder.mVibrateDesc.setTextColor(color);

        }

        holder.mIsAlarmOn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                boolean isOn = holder.mIsAlarmOn.isChecked();

                if (isOn) {

                    //update the alarm clock in firebase
                    database = FirebaseDatabase.getInstance().getReference("AlarmClocks");
                    database.child(mAlarmClock.getAlarmId()).child("onAlarm").setValue(true);

                    //change color of text
                    //lic_card_active_details_color

                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = context.getTheme();
                    theme.resolveAttribute(R.attr.lic_card_active_details_color, typedValue, true);
                    @ColorInt int color = typedValue.data;

                    String hexColor = String.format("#%06X", (0xFFFFFF & color));

                    holder.mWakeUpTime.setTextColor(color);
                    holder.horizontalDivider.setBackgroundColor(color);
                    holder.mAlarmTitle.setTextColor(color);
                    holder.mDropDown.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);

                    holder.mMissionIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
                    holder.mMissionLabel.setTextColor(color);
                    holder.mMissionDesc.setTextColor(color);

                    holder.mRingtoneIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
                    holder.mRingtoneLabel.setTextColor(color);
                    holder.mRingtoneDesc.setTextColor(color);

                    holder.mVibrateIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
                    holder.mVibrateTextView.setTextColor(color);
                    holder.mVibrateDesc.setTextColor(color);

                    mAlarmClock.schedule(context);

                } else {

                    //update the alarm clock in firebase
                    database = FirebaseDatabase.getInstance().getReference("AlarmClocks");
                    database.child(mAlarmClock.getAlarmId()).child("onAlarm").setValue(false);

                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = context.getTheme();
                    theme.resolveAttribute(R.attr.lic_card_inactive_details_color, typedValue, true);
                    @ColorInt int color = typedValue.data;

                    String hexColor = String.format("#%06X", (0xFFFFFF & color));

                    holder.mWakeUpTime.setTextColor(color);
                    holder.horizontalDivider.setBackgroundColor(color);
                    holder.mAlarmTitle.setTextColor(color);
                    holder.mDropDown.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);

                    holder.mMissionIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
                    holder.mMissionLabel.setTextColor(color);
                    holder.mMissionDesc.setTextColor(color);

                    holder.mRingtoneIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
                    holder.mRingtoneLabel.setTextColor(color);
                    holder.mRingtoneDesc.setTextColor(color);

                    holder.mVibrateIcon.setColorFilter(Color.parseColor(hexColor), android.graphics.PorterDuff.Mode.SRC_IN);
                    holder.mVibrateTextView.setTextColor(color);
                    holder.mVibrateDesc.setTextColor(color);

                    mAlarmClock.cancelAlarm(context);

                }

                context.startActivity(new Intent(v.getContext(), MainActivity.class));
                ((Activity) context).finish();

            }
        });

        holder.mAlarmTitle.setText(mAlarmClock.getAlarmTitle());

        holder.mDropDown.setTag(R.drawable.ic_arrow_down);
        holder.mDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageView = (ImageView) v;
                assert (R.id.drop_down_for_more == imageView.getId());

                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;

                switch (integer) {

                    case R.drawable.ic_arrow_down:
                        imageView.setImageResource(R.drawable.ic_arrow_up);
                        imageView.setTag(R.drawable.ic_arrow_up);
                        break;
                    case R.drawable.ic_arrow_up:
                        imageView.setImageResource(R.drawable.ic_arrow_down);
                        imageView.setTag(R.drawable.ic_arrow_down);
                        break;

                }

                TransitionManager.beginDelayedTransition(holder.mConstraintLayout, new AutoTransition());

                //determine the visibility
                int viMissionIcon = (holder.mMissionIcon.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                holder.mMissionIcon.setVisibility(viMissionIcon);

                int viMissionLabel = (holder.mMissionLabel.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                holder.mMissionLabel.setVisibility(viMissionLabel);

                int viMissionDesc = (holder.mMissionDesc.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                holder.mMissionDesc.setVisibility(viMissionDesc);


                int viRingtoneIcon = (holder.mRingtoneIcon.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                holder.mRingtoneIcon.setVisibility(viRingtoneIcon);

                int viRingtoneLabel = (holder.mRingtoneLabel.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                holder.mRingtoneLabel.setVisibility(viRingtoneLabel);

                int viRingtoneDesc = (holder.mRingtoneDesc.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                holder.mRingtoneDesc.setVisibility(viRingtoneDesc);


                int viVibrateIcon = (holder.mVibrateIcon.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                holder.mVibrateIcon.setVisibility(viVibrateIcon);

                int viVibrateLabel = (holder.mVibrateTextView.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                holder.mVibrateTextView.setVisibility(viVibrateLabel);

                int viVibrateDesc = (holder.mVibrateDesc.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                holder.mVibrateDesc.setVisibility(viVibrateDesc);

            }
        });

        //set language
        holder.mMissionLabel.setText(resources.getString(R.string.list_item_alarm_mission_text));
        holder.mRingtoneLabel.setText(resources.getString(R.string.list_item_alarm_ringtone_text));
        holder.mVibrateTextView.setText(resources.getString(R.string.list_item_alarm_vibrate_text));

        switch (mAlarmClock.getAlarmMission()) {

            case 1:
                holder.mMissionDesc.setText(resources.getString(R.string.list_item_alarm_mission_value1)); //list_item_alarm_mission_value1
                break;
            case 2:
                holder.mMissionDesc.setText(resources.getString(R.string.list_item_alarm_mission_value2));
                break;

        }

        switch (mAlarmClock.getAlarmRingtone()) {

            case 1:
                holder.mRingtoneDesc.setText(resources.getString(R.string.list_item_alarm_ringtone_value1));
                break;
            case 2:
                holder.mRingtoneDesc.setText(resources.getString(R.string.list_item_alarm_ringtone_value2));
                break;
            case 3:
                holder.mRingtoneDesc.setText(resources.getString(R.string.list_item_alarm_ringtone_value3));
                break;
            case 4:
                holder.mRingtoneDesc.setText(resources.getString(R.string.list_item_alarm_ringtone_value4));
                break;

        }

        if (mAlarmClock.isAlarmVibrate()) {

            holder.mVibrateDesc.setText(resources.getString(R.string.list_item_alarm_vibrate_valueOn));  //list_item_alarm_vibrate_valueOn

        } else {

            holder.mVibrateDesc.setText(resources.getString(R.string.list_item_alarm_vibrate_valueOff));

        }

        holder.getPosition(mAlarmClock);

    }

    @Override
    public int getItemCount() {
        return mAlarmClockArrayList.size();
    }

    public static class AlarmClockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AlarmClock mAlarmClock;

        //list item views

        ConstraintLayout mConstraintLayout;

        TextView mWakeUpTime;
        Switch mIsAlarmOn;

        View horizontalDivider;

        TextView mAlarmTitle;

        ImageView mDropDown;

        ImageView mMissionIcon;
        TextView mMissionLabel;
        TextView mMissionDesc;

        ImageView mRingtoneIcon;
        TextView mRingtoneLabel;
        TextView mRingtoneDesc;

        ImageView mVibrateIcon;
        TextView mVibrateTextView;
        TextView mVibrateDesc;

        public AlarmClockViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.list_item_constraintLayout);

            mWakeUpTime = (TextView) itemView.findViewById(R.id.alarm_clock_wake_up_time);
            mIsAlarmOn = (Switch) itemView.findViewById(R.id.alarm_is_on_toggle);

            horizontalDivider = (View) itemView.findViewById(R.id.line_separator_horizontal);

            mAlarmTitle = (TextView) itemView.findViewById(R.id.list_item_alarm_clock_title_text_view);

            mDropDown = (ImageView) itemView.findViewById(R.id.drop_down_for_more);

            mMissionIcon = (ImageView) itemView.findViewById(R.id.mission_icon);
            mMissionLabel = (TextView) itemView.findViewById(R.id.mission_title);
            mMissionDesc = (TextView) itemView.findViewById(R.id.mission_details);

            mRingtoneIcon = (ImageView) itemView.findViewById(R.id.ringtone_icon);
            mRingtoneLabel = (TextView) itemView.findViewById(R.id.ringtone_title);
            mRingtoneDesc = (TextView) itemView.findViewById(R.id.ringtone_details);

            mVibrateIcon = (ImageView) itemView.findViewById(R.id.vibrate_icon);
            mVibrateTextView = (TextView) itemView.findViewById(R.id.vibrate_title);
            mVibrateDesc = (TextView) itemView.findViewById(R.id.vibrate_details);

        }

        public void getPosition(AlarmClock alarmClock) {

            mAlarmClock = alarmClock;

        }

        @Override
        public void onClick(View v) {

            Intent intent = AlarmClockActivity.newIntent(v.getContext(), mAlarmClock.getAlarmId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

        }

    }

}
