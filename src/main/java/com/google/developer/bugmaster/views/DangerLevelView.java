package com.google.developer.bugmaster.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.google.developer.bugmaster.R;

public class DangerLevelView extends TextView {

    private String[] mAllDangerColors;
    private int mDangerLevel;

    public DangerLevelView(Context context) {

        super(context);
        init();
    }

    public DangerLevelView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init();
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        setGravity(Gravity.CENTER);
        mAllDangerColors = getContext().getResources().getStringArray(R.array.dangerColors);
    }

    public void setDangerLevel(int dangerLevel) {

        mDangerLevel = dangerLevel;

        String dangerLevelColor = mAllDangerColors[dangerLevel - 1];
        int colorAsInt = Color.parseColor(dangerLevelColor);
        getBackground().setColorFilter(colorAsInt, PorterDuff.Mode.SRC_IN);

        setText(String.valueOf(dangerLevel));
    }

    public int getDangerLevel() {

        return mDangerLevel;
    }
}
