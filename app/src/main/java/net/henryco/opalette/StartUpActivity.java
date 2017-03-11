package net.henryco.opalette;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.henryco.opalette.application.main.ProtoActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartUpActivity extends AppCompatActivity {

	public static final long SPLASH_LOADING_DELAY = 3500;

	/**
	 * Some older devices needs a small delay between UI widget updates
	 * and a change of the status and navigation bar.
	 */
	private static final int UI_ANIMATION_DELAY = 0;
	private final Handler mHideHandler = new Handler();
	private View mContentView;
	private final Runnable mHidePart2Runnable = () ->
			mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN
			| View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	private View mControlsView;
	private final Runnable mHideRunnable = this::hide;
	private final Runnable mShowPart2Runnable = () -> {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) actionBar.show();
		mControlsView.setVisibility(View.VISIBLE);
	};



	private void hide() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) actionBar.hide();
		mControlsView.setVisibility(View.GONE);
		mHideHandler.removeCallbacks(mShowPart2Runnable);
		mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
	}


	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}



	private void init() {
		new Thread(() -> {
			long t0 = System.currentTimeMillis() + SPLASH_LOADING_DELAY;
			while (System.currentTimeMillis() < t0) {
				synchronized (this) {
					try {
						wait(t0 - System.currentTimeMillis());
					} catch (Exception ignored) {}
				}
			}
			startActivity(new Intent(this, ProtoActivity.class));
		}).start();
	}




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start_up_activiy);

		mControlsView = findViewById(R.id.fullscreen_content_controls);
		mContentView = findViewById(R.id.fullscreen_content);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		delayedHide(0);
		init();
	}







}
