package com.mrmelongames.atomic3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.splash);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.z_blanco);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription("Zalou", bm, argb(255,25,118,210));

            setTaskDescription(td);
            bm.recycle();

        }*/

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        if(hasFocus){
            Intent mainIntent = new Intent().setClass(Splash.this, AndroidLauncher.class);
            //mainIntent.putExtra("Splash", this);
            startActivity(mainIntent);
        } else {
            Handler handlerTimer = new Handler();
            handlerTimer.postDelayed(new Runnable() {
                public void run() {
                    finish();
                }
            }, 250);
        }
/*
        if(hasFocus)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_FULLSCREEN);*/
    }

/*
    public int argb(int A, int R, int G, int B){
        byte[] colorByteArr = { (byte) A, (byte) R, (byte) G, (byte) B };
        return byteArrToInt(colorByteArr);
    }

    public int byteArrToInt(byte[] colorByteArr) {
        return (colorByteArr[0] << 24) + ((colorByteArr[1] & 0xFF) << 16) + ((colorByteArr[2] & 0xFF) << 8) + (colorByteArr[3] & 0xFF);
    }
*/
}
