package com.andy.freestylehero;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Random;

import com.andy.freestylehero.MainActivity.Difficulty;

public class ScreenSlidePageFragment extends Fragment {

    // argument keys for easyWords, mediumWords, hardWords, random, and difficulty in the
    // onCreateView method
    public static final String ARG_EASY_WORDS = "easy_words";
    public static final String ARG_MEDIUM_WORDS = "medium_words";
    public static final String ARG_HARD_WORDS = "hard_words";
    public static final String ARG_RANDOM = "random";
    public static final String ARG_DIFFICULTY = "difficulty";

    // create a new instance of ScreenSlidePageFragment with random, difficulty, easyWords,
    // mediumWords and hardWords as args.
    public static ScreenSlidePageFragment newInstance(Random random, Difficulty difficulty,
                                                      String[] easyWords, String[] mediumWords,
                                                      String[] hardWords) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RANDOM, random);
        args.putSerializable(ARG_DIFFICULTY, difficulty);
        args.putStringArray(ARG_EASY_WORDS, easyWords);
        args.putStringArray(ARG_MEDIUM_WORDS, mediumWords);
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
        String[] mediumWords = args.getStringArray(ARG_MEDIUM_WORDS);
        String[] hardWords = args.getStringArray(ARG_HARD_WORDS);
        Random random = (Random) args.getSerializable(ARG_RANDOM);
        Difficulty difficulty = (Difficulty) args.getSerializable(ARG_DIFFICULTY);
        if (random == null || easyWords == null || mediumWords == null || hardWords == null
                || difficulty == null)
            throw new NullPointerException("args not instantiated");

        switch (difficulty) {
            case EASY:
                textView.setText(easyWords[random.nextInt(easyWords.length)]);
                textView.setTextColor(getResources().getColor(R.color.orange));
                break;
            case MEDIUM:
                textView.setText(mediumWords[random.nextInt(mediumWords.length)]);
                textView.setTextColor(getResources().getColor(R.color.stronger_orange));
                break;
            case HARD:
                textView.setText(hardWords[random.nextInt(hardWords.length)]);
                textView.setTextColor(getResources().getColor(R.color.red));
        }

        return rootView;
    }
}