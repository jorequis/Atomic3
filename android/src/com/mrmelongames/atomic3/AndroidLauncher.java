package com.mrmelongames.atomic3;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	MainScene mainScene;

	private VelocityTracker mVelocityTracker = null;
	private float velocityX;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		RelativeLayout lg = (RelativeLayout) findViewById(R.id.MainLayout);

		config.useImmersiveMode = true;
		config.useGLSurfaceView20API18 = true;

		mainScene = new MainScene();
		View mainSceneView = initializeForView(mainScene, config);
		mainSceneView.setFocusable(false);

		TouchView touchView = new TouchView(this);
		touchView.setFocusable(true);

		lg.addView(mainSceneView);
		lg.addView(touchView);
	}

	public void touchEvent(MotionEvent event){
		int index = event.getActionIndex();
		int pointerId = event.getPointerId(index);

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (mVelocityTracker == null) {

					// Retrieve a new VelocityTracker object to watch the velocity
					// of a motion.
					mVelocityTracker = VelocityTracker.obtain();
				} else {

					// Reset the velocity tracker back to its initial state.
					mVelocityTracker.clear();
				}
				mVelocityTracker.addMovement(event);
				break;
			case MotionEvent.ACTION_MOVE:
				mVelocityTracker.addMovement(event);
				// When you want to determine the velocity, call
				// computeCurrentVelocity(). Then call getXVelocity()
				// and getYVelocity() to retrieve the velocity for each pointer ID.
				mVelocityTracker.computeCurrentVelocity(1);

				// Log velocity of pixels per second
				// Best practice to use VelocityTrackerCompat where possible.
				velocityX = mVelocityTracker.getXVelocity(pointerId) * 0.0125f;
				//velocityX = VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId) * 0.0125f;
				//velocityY = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId) * 0.0125f;

				velocityX = Math.abs(velocityX)<0.005f?0:velocityX;
				//velocityY = Math.abs(velocityY)<0.005f?0:velocityY;

				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_CANCEL:
				// Return a VelocityTracker object back to be re-used by others.
				mVelocityTracker.recycle();
				break;
		}

		mainScene.inputManager(event.getAction(), event.getX(), event.getY(), velocityX);
	}
/*
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus)
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
				getWindow().getDecorView().setSystemUiVisibility(
						View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				| View.SYSTEM_UI_FLAG_FULLSCREEN);
	}*/
}
