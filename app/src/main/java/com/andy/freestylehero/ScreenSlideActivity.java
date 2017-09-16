package com.andy.freestylehero;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ScreenSlideActivity extends FragmentActivity {

    private static final int MILLISECONDS_IN_SECOND = 1000;

    private ViewPager mPager; // ViewPager for sliding through word fragments
    private int mCurrentPage; // current page of mPager
    private int mDuration; // Duration of each word's appearance on screen
    private Timer mSlideTimer; // Timer for automating the paging through words
    private Handler mHandler; // Handler for automating the paging through words
    private Runnable mRunnable; // Runnable for automating the paging through words
    private SeekBar mChangeDurationSeekBar; // Seek bar for choosing the value of mDuration
    private TextView mDurationTextView; // Text view to show the value of mDuration
    private ProgressBar mCountdownProgressBar; // Progress bar to show remaining time left for
    // each word on screen
    private MediaPlayer mMediaPlayer; // Media player for playing rap beat
    private ToggleButton mMusicToggleButton; // Button for toggling music on and off

    private static boolean sIsModeHard; // true if mode is hard, false if mode is easy
    private static String[] sEasyWords; // array of relatively easy words from which the program
    // will draw words at random
    private static String[] sHardWords; // array of relatively hard words from which the program
    // will draw words at random
    private static Random sRandom; // Random object for selecting words at random

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mPager = findViewById(R.id.pager);
        ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mCurrentPage = 0;

        mDuration = getIntent().getIntExtra(MainActivity.ARG_DURATION,
                MainActivity.DEFAULT_DURATION);

        mDurationTextView = findViewById(R.id.periodTextView);
        String periodText = mDuration + "s";
        mDurationTextView.setText(periodText);

        mCountdownProgressBar = findViewById(R.id.countdownProgressBar);

        mChangeDurationSeekBar = findViewById(R.id.changePeriodSeekBar);
        mChangeDurationSeekBar.setProgress(mDuration - MainActivity.MIN_DURATION);
        mChangeDurationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // change the displayed duration in mDurationTextView as the progress value in
            // mChangeDurationSeekBar is changing.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
                mDuration = mChangeDurationSeekBar.getProgress() + MainActivity.MIN_DURATION;
                String periodText = mDuration + "s";
                mDurationTextView.setText(periodText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            // When the user lets go of the seek bar, a new duration may have been selected.
            // call onPause() and onResume() so that a new TimerTask can be scheduled according to
            // the new duration value
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ScreenSlideActivity.this.onPause();
                ScreenSlideActivity.this.onResume();
            }
        });

        mMusicToggleButton = findViewById(R.id.musicToggleButton);
        mMusicToggleButton.setChecked(getIntent().getBooleanExtra(MainActivity.ARG_IS_MUSIC_ON, true));

        // Button for toggling between easy and hard modes
        ToggleButton modeToggleButton = findViewById(R.id.modeToggleButton);
        modeToggleButton.setChecked(getIntent().getBooleanExtra(MainActivity.ARG_IS_MODE_HARD,
                false));
        modeToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isModeHard) {
                sIsModeHard = isModeHard;
            }
        });
        modeToggleButton.setTypeface(MainActivity.getCustomFontTypeFace());

        sEasyWords = getResources().getStringArray(R.array.easy_words);
        sHardWords = getResources().getStringArray(R.array.hard_words);
        sRandom = new Random();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.piano_rap_beat);
        mMediaPlayer.setLooping(true);
        mMusicToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mMediaPlayer.start();
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMusicToggleButton.isChecked()) {
            mMediaPlayer.start();
        }

        // Every *mDuration* seconds, slide to a new word and dynamically display the remaining
        // screen time of that word on the progress bar mCountDownProgressBar.
        mHandler = new Handler();
        mRunnable = new Runnable() {
            public void run() {
                mPager.setCurrentItem(mCurrentPage++, true);
                ObjectAnimator mProgressAnimator = ObjectAnimator.ofInt(mCountdownProgressBar,
                        "progress", mCountdownProgressBar.getMax(), 0);
                mProgressAnimator.setDuration(mDuration * MILLISECONDS_IN_SECOND);
                mProgressAnimator.setInterpolator(new LinearInterpolator());
                mProgressAnimator.start();
            }
        };
        mSlideTimer = new Timer();
        mSlideTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                mHandler.post(mRunnable);
            }
        }, 0, mDuration * MILLISECONDS_IN_SECOND);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
        mSlideTimer.cancel();
        mSlideTimer.purge();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    // for the exit button
    public void onClickExitButton(View view) {
        startActivity(new Intent(ScreenSlideActivity.this, MainActivity.class));
    }

    // Pager adapter for the view pager
    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.newInstance(sRandom, sIsModeHard,
                    sEasyWords, sHardWords);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
    }
}
