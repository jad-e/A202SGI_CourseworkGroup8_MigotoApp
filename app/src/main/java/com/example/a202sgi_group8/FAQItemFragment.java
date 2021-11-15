package com.example.a202sgi_group8;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class FAQItemFragment extends Fragment {

    //
    // constant values
    //
    private static final String ARG_FAQ_ITEM_POS = "arg_faq_item_pos";

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
    private ImageView mBackArrow;
    private TextView mFAQTitle;
    private TextView mFAQContent;

    //
    //values
    //
    private int listItemPos;

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

        listItemPos = getArguments().getInt(ARG_FAQ_ITEM_POS, 0);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //return the view to the hosting activity
        return inflater.inflate(R.layout.fragment_faq_item, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //
        // find views
        //
        mBackArrow = view.findViewById(R.id.faq_item_back_imgView);
        mFAQTitle = view.findViewById(R.id.faq_title);
        mFAQContent = view.findViewById(R.id.faq_content);

        //set language
        mPreferences = getActivity().getSharedPreferences(spFileName, Context.MODE_PRIVATE);

        String title = "";
        String content = "";

        if (mPreferences.contains(SP_LANGUAGE_KEY)) { //if got this key

            language = mPreferences.getInt(SP_LANGUAGE_KEY, 1);

            switch (language) {
                case 1:
                    context = LocaleHelper.setLocale(getContext(), "en");
                    resources = context.getResources();

                    //set the views to the wanted language

                    //
                    // set the values of the text views
                    //

                    switch (listItemPos) {

                        case 0:
                            title = resources.getString(R.string.ques0);
                            content = resources.getString(R.string.ans0);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 1:
                            title = resources.getString(R.string.ques1);
                            content = resources.getString(R.string.ans1);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 2:
                            title = resources.getString(R.string.ques2);
                            content = resources.getString(R.string.ans2);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 3:
                            title = resources.getString(R.string.ques3);
                            content = resources.getString(R.string.ans3);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 4:
                            title = resources.getString(R.string.ques4);
                            content = resources.getString(R.string.ans4);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 5:
                            title = resources.getString(R.string.ques5);
                            content = resources.getString(R.string.ans5);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 6:
                            title = resources.getString(R.string.ques6);
                            content = resources.getString(R.string.ans6);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 7:
                            title = resources.getString(R.string.ques7);
                            content = resources.getString(R.string.ans7);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 8:
                            title = resources.getString(R.string.ques8);
                            content = resources.getString(R.string.ans8);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 9:
                            title = resources.getString(R.string.ques9);
                            content = resources.getString(R.string.ans9);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 10:
                            title = resources.getString(R.string.ques10);
                            content = resources.getString(R.string.ans10);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 11:
                            title = resources.getString(R.string.ques11);
                            content = resources.getString(R.string.ans11);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 12:
                            title = resources.getString(R.string.ques12);
                            content = resources.getString(R.string.ans12);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;

                    }

                    break;
                case 2:
                    context = LocaleHelper.setLocale(getContext(), "zh");
                    resources = context.getResources();

                    //set the views to the wanted language

                    //
                    // set the values of the text views
                    //

                    switch (listItemPos) {

                        case 0:
                            title = resources.getString(R.string.ques0);
                            content = resources.getString(R.string.ans0);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 1:
                            title = resources.getString(R.string.ques1);
                            content = resources.getString(R.string.ans1);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 2:
                            title = resources.getString(R.string.ques2);
                            content = resources.getString(R.string.ans2);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 3:
                            title = resources.getString(R.string.ques3);
                            content = resources.getString(R.string.ans3);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 4:
                            title = resources.getString(R.string.ques4);
                            content = resources.getString(R.string.ans4);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 5:
                            title = resources.getString(R.string.ques5);
                            content = resources.getString(R.string.ans5);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 6:
                            title = resources.getString(R.string.ques6);
                            content = resources.getString(R.string.ans6);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 7:
                            title = resources.getString(R.string.ques7);
                            content = resources.getString(R.string.ans7);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 8:
                            title = resources.getString(R.string.ques8);
                            content = resources.getString(R.string.ans8);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 9:
                            title = resources.getString(R.string.ques9);
                            content = resources.getString(R.string.ans9);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 10:
                            title = resources.getString(R.string.ques10);
                            content = resources.getString(R.string.ans10);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 11:
                            title = resources.getString(R.string.ques11);
                            content = resources.getString(R.string.ans11);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;
                        case 12:
                            title = resources.getString(R.string.ques12);
                            content = resources.getString(R.string.ans12);

                            mFAQTitle.setText(title);
                            mFAQContent.setText(content);
                            break;

                    }

                    break;

            }


        } else { //if don't have this key (app first launch or user didn't toggle language settings yet)

            context = LocaleHelper.setLocale(getContext(), "en");
            resources = context.getResources();

            //set the views to the wanted language

            //
            // set the values of the text views
            //

            switch (listItemPos) {

                case 0:
                    title = resources.getString(R.string.ques0);
                    content = resources.getString(R.string.ans0);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 1:
                    title = resources.getString(R.string.ques1);
                    content = resources.getString(R.string.ans1);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 2:
                    title = resources.getString(R.string.ques2);
                    content = resources.getString(R.string.ans2);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 3:
                    title = resources.getString(R.string.ques3);
                    content = resources.getString(R.string.ans3);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 4:
                    title = resources.getString(R.string.ques4);
                    content = resources.getString(R.string.ans4);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 5:
                    title = resources.getString(R.string.ques5);
                    content = resources.getString(R.string.ans5);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 6:
                    title = resources.getString(R.string.ques6);
                    content = resources.getString(R.string.ans6);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 7:
                    title = resources.getString(R.string.ques7);
                    content = resources.getString(R.string.ans7);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 8:
                    title = resources.getString(R.string.ques8);
                    content = resources.getString(R.string.ans8);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 9:
                    title = resources.getString(R.string.ques9);
                    content = resources.getString(R.string.ans9);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 10:
                    title = resources.getString(R.string.ques10);
                    content = resources.getString(R.string.ans10);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 11:
                    title = resources.getString(R.string.ques11);
                    content = resources.getString(R.string.ans11);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;
                case 12:
                    title = resources.getString(R.string.ques12);
                    content = resources.getString(R.string.ans12);

                    mFAQTitle.setText(title);
                    mFAQContent.setText(content);
                    break;

            }

        }


        //
        // listeners
        //

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();

            }
        });


    }

    public static FAQItemFragment newInstance(int faqPos) {

        Bundle args = new Bundle();
        args.putInt(ARG_FAQ_ITEM_POS, faqPos);

        FAQItemFragment faqF = new FAQItemFragment();
        faqF.setArguments(args);
        return faqF;

    }
}
