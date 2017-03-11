package net.henryco.opalette;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.henryco.opalette.application.main.ProtoActivity;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartUpActivity extends AppCompatActivity {

	public static final long SPLASH_LOADING_DELAY = 2500;

	private View mContentView;
	private View mControlsView;


	private void hide() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) actionBar.hide();
		mControlsView.setVisibility(View.GONE);
		if (actionBar != null) actionBar.show();
		mControlsView.setVisibility(View.VISIBLE);

		mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		);
	}




	private void init() {
		new Thread(() -> {
			long t0 = System.currentTimeMillis() + SPLASH_LOADING_DELAY;
			while (System.currentTimeMillis() < t0)
				synchronized (this) {
					try {
						wait(t0 - System.currentTimeMillis());
					} catch (Exception ignored) {}
				}
			startActivity(new Intent(this, ProtoActivity.class));
			finish();
		}).start();
	}




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start_up_activiy);

		mControlsView = findViewById(R.id.fullscreen_content_controls);
		mContentView = findViewById(R.id.fullscreen_content);
		hide();
		init();
	}










}
