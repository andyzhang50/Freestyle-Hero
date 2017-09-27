package com.andy.freestylehero;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageButton;

import com.andy.freestylehero.MainActivity.Difficulty;

public class ToggleDifficultyButton extends AppCompatImageButton {

    private Difficulty mDifficulty; // word difficulty

    public ToggleDifficultyButton(Context context) {
        super(context);
        setDifficulty(Difficulty.EASY);
    }

    public ToggleDifficultyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDifficulty(Difficulty.EASY);
    }

    public ToggleDifficultyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDifficulty(Difficulty.EASY);
    }

    public void setDifficulty(Difficulty difficulty) {
        mDifficulty = difficulty;
        switch (mDifficulty) {
            case EASY:
                setImageResource(R.drawable.ic_mode_easy);
                break;
            case MEDIUM:
                setImageResource(R.drawable.ic_mode_medium);
                break;
            case HARD:
                setImageResource(R.drawable.ic_mode_hard);
                break;
        }
    }

    public Difficulty getDifficulty() {
        return mDifficulty;
    }

    public void onClick() {
        int next = (mDifficulty.ordinal() + 1) % Difficulty.values().length;
        setDifficulty(Difficulty.values()[next]);
    }
}
