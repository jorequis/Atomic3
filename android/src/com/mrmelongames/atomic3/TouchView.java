package com.mrmelongames.atomic3;

import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View {

    AndroidLauncher mainScene;

    public TouchView(AndroidLauncher c) {
        super(c);
        mainScene = c;
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mainScene.touchEvent(event);
        return true;
    }

}