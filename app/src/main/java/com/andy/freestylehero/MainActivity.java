package com.andy.freestylehero;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    public static final int MIN_DURATION = 2; // Minimum value for mDuration;
    public static final int DEFAULT_DURATION = 6; // Default value for mDuration
    private static final String CUSTOM_FONT_PATH = "fonts/GoodDog.otf"; // path of font file

    // argument keys for mDuration, mIsModeHard and mIsMusicOn
    public static final String ARG_DURATION = "duration";
    public static final String ARG_IS_MUSIC_ON = "is_music_on";
    public static final String ARG_DIFFICULTY = "difficulty";

    enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private int mDuration; // Duration of each word's appearance on screen
    private boolean mIsMusicOn; // True if music turned on, false if turned off
    private Difficulty mDifficulty; // Word difficulty
    private static Typeface sCustomFontTypeFace; // Typeface variable for the main font

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDuration = DEFAULT_DURATION;
        mIsMusicOn = true;
        mDifficulty = Difficulty.EASY;

        sCustomFontTypeFace = Typeface.createFromAsset(getAssets(), CUSTOM_FONT_PATH);

        // Text view that displays "FREESTYLE"
        TextView freestyleTextView = findViewById(R.id.freestyleTextView);
        freestyleTextView.setTypeface(sCustomFontTypeFace);

        // Text view that displays "HERO"
        TextView heroTextView = findViewById(R.id.heroTextView);
        heroTextView.setTypeface(sCustomFontTypeFace);

        // Play button
        Button playButton = findViewById(R.id.playButton);
        playButton.setTypeface(sCustomFontTypeFace);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScreenSlideActivity.class);
                intent.putExtra(ARG_DURATION, mDuration);
                intent.putExtra(ARG_IS_MUSIC_ON, mIsMusicOn);
                intent.putExtra(ARG_DIFFICULTY, mDifficulty);
                startActivity(intent);
            }
        });

        // Music toggle button
        ToggleButton musicToggleButton = findViewById(R.id.musicToggleButtonMain);
        musicToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isMusicOn) {
                mIsMusicOn = isMusicOn;
            }
        });

        // Word difficulty toggle button
        final ToggleDifficultyButton toggleDifficultyButton =
                findViewById(R.id.toggleDifficultyButtonMain);
        toggleDifficultyButton.setDifficulty(mDifficulty);
        toggleDifficultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDifficultyButton.onClick();
                mDifficulty = toggleDifficultyButton.getDifficulty();
            }
        }) ;

        // Text view displaying the currently set duration of each word's appearance on screen
        final TextView durationTextView = findViewById(R.id.durationTextViewMain);
        String periodText = mDuration + "s";
        durationTextView.setText(periodText);

        // Seek bar allowing the user to change the duration
        final SeekBar changeDurationSeekBar = findViewById(R.id.changeDurationSeekBarMain);
        changeDurationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mDuration = changeDurationSeekBar.getProgress() + MIN_DURATION;
                String periodText = mDuration + "s";
                durationTextView.setText(periodText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public static Typeface getCustomFontTypeFace() {
        return sCustomFontTypeFace;
    }
}
