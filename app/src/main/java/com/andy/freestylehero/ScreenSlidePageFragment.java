package com.andy.freestylehero;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Random;

public class ScreenSlidePageFragment extends Fragment {

    // argument keys for easyWords, hardWords, random, and isModeHard in the onCreateView method
    public static final String ARG_EASY_WORDS = "easy_words";
    public static final String ARG_HARD_WORDS = "hard_words";
    public static final String ARG_RANDOM = "random";
    public static final String ARG_IS_MODE_HARD = "is_mode_hard";

    // create a new instance of ScreenSlidePageFragment with random, isModeHard, easyWords and
    // hardWords as args.
    public static ScreenSlidePageFragment newInstance(Random random, boolean isModeHard,
                                                      String[] easyWords, String[] hardWords) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RANDOM, random);
        args.putBoolean(ARG_IS_MODE_HARD, isModeHard);
        args.putStringArray(ARG_EASY_WORDS, easyWords);
        args.putStringArray(ARG_HARD_WORDS, hardWords);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        // the text view for displaying the word on the fragment
        TextView textView = rootView.findViewById(android.R.id.text1);
        textView.setTypeface(MainActivity.getCustomFontTypeFace());
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textView,
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView, 12, 250, 1,
                TypedValue.COMPLEX_UNIT_SP);

        Bundle args = getArguments();
        String[] easyWords = args.getStringArray(ARG_EASY_WORDS);
        String[] hardWords = args.getStringArray(ARG_HARD_WORDS);
        Random random = (Random) args.getSerializable(ARG_RANDOM);
        Boolean isModeHard = args.getBoolean(ARG_IS_MODE_HARD);
        if (random == null || easyWords == null || hardWords == null)
            throw new NullPointerException("args not instantiated");

        // if in hard mode, take a word from the hardWords string array 80% of the time, and from
        // the easyWords string array 20% of the time. If in easy mode, take a word from easyWords.
        if (isModeHard && random.nextInt(5) > 0) {
            textView.setText(hardWords[random.nextInt(hardWords.length)]);
        } else {
            textView.setText(easyWords[random.nextInt(easyWords.length)]);
        }

        return rootView;
    }
}