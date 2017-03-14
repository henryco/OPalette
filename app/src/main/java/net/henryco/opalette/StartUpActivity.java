package net.henryco.opalette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.henryco.opalette.api.utils.OPallUtils;
import net.henryco.opalette.application.activities.ProtoActivity;
import net.henryco.opalette.application.fragments.dialogs.PickImageDialog;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartUpActivity extends AppCompatActivity
		implements OPallUtils.ImageLoadable, PickImageDialog.PickImageDialogListener {


	public static final long PICKBUTTON_SLEEP_TIME = 2;
	public static final long SPLASH_LOADING_TIME = 3000;
	public static final long ANIMATION_TIME = 250;

	public static final long AFTER_SPLASH_DELAY = 225;
	public static final long NEW_ACTIVITY_DELAY = 60;

	public static final float PICKBUTTON_RADIUS = 0.71f;


	public static final class BitmapPack {
		private static Bitmap pushUpBitmap = null;
		public static Bitmap get() {
			return pushUpBitmap;
		}
		public static void close(){
			pushUpBitmap.recycle();
			pushUpBitmap = null;
		}
	}





	private void showAfterSplash() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) actionBar.show();
		findViewById(R.id.fullscreen_content).setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		findViewById(R.id.firstPickLayout).setVisibility(View.VISIBLE);

		new Handler().postDelayed(() -> animation(1, -1, ANIMATION_TIME, () -> {
			View text = findViewById(R.id.textView);
			text.setVisibility(View.VISIBLE);
			text.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
		}), AFTER_SPLASH_DELAY);
	}



	private void animation(float direction, float corner, float animTime_ms, Runnable afterAction) {

		ImageButton pickButton = (ImageButton) findViewById(R.id.imageButtonGall);


		float r = pickButton.getWidth() * 0.5f * PICKBUTTON_RADIUS;
		float hlW = (findViewById(R.id.firstPickLayout).getWidth() * 0.5f) + r;
		float s_pi = (float) (hlW / (Math.PI * r * 2f));

		long t_spin_ms = (long)(animTime_ms / s_pi);
		float deg_per_ms = 360f / (float)t_spin_ms;
		float x_per_ms = hlW / animTime_ms;

		new Thread(() -> {
			long t0 = System.currentTimeMillis();
			while (System.currentTimeMillis() < t0 + (long)animTime_ms)
				synchronized (this) {

					long t1 = System.currentTimeMillis() - t0;
					float start = corner * hlW;
					float trx = ((float)t1 * x_per_ms * direction) + start;
					float rot = (float)t1 * deg_per_ms * direction;

					try {
						pickButton.setVisibility(View.VISIBLE);
						pickButton.setRotation(rot);
						pickButton.setTranslationX(trx);
						Thread.sleep(PICKBUTTON_SLEEP_TIME);
					} catch (Exception ignore) {}
				}
			runOnUiThread(() -> {
				pickButton.setRotation(0);
				afterAction.run();
			});
		}).start();
	}




	private void initSplash() {
		ImageView logo = (ImageView) findViewById(R.id.logoImageVIew);
		new Thread(() -> {
			long t0 = System.currentTimeMillis();
			while (System.currentTimeMillis() < t0 + SPLASH_LOADING_TIME) {
				synchronized (this) {
					try {
						float nt = ((float) (System.currentTimeMillis() - t0) / SPLASH_LOADING_TIME);
						logo.setAlpha((float) Math.min(1., Math.sin(Math.PI * nt) * 1.25));
						Thread.sleep(1);
					} catch (Exception ignored) {}
				}
			} runOnUiThread(() -> {
				logo.setVisibility(View.GONE);
				findViewById(R.id.fullscreen_content_controls).setOnClickListener(this::imagePickAction);
				findViewById(R.id.imageButtonGall).setOnClickListener(this::imagePickAction);
				findViewById(R.id.textView).setOnClickListener(this::imagePickAction);
				showAfterSplash();
			});
		}).start();
	}



	private synchronized void imagePickAction(View view) {
		AppCompatDialogFragment pickImageDialog = new PickImageDialog();
		pickImageDialog.show(getSupportFragmentManager(), "pickImageDialog");
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_up_activiy);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.DARK)));
			actionBar.setElevation(60);
			actionBar.hide();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.DARK));
		}
		initSplash();
	}




	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == OPallUtils.activity.REQUEST_PICK_IMAGE) {
				Intent intent = new Intent(this, ProtoActivity.class);
				BitmapPack.pushUpBitmap = OPallUtils.loadIntentBitmap(this, data);

				findViewById(R.id.textView).setVisibility(View.GONE);

				new Handler().postDelayed(() ->
						animation(1, 0, ANIMATION_TIME, () -> {
							findViewById(R.id.imageButtonGall).setVisibility(View.GONE);
							new Handler().postDelayed(() ->
									runOnUiThread(() -> {
										startActivity(intent);
										finish();
									}), NEW_ACTIVITY_DELAY);
						}), AFTER_SPLASH_DELAY
				);
			}
		}
	}


	@Override
	public void dialogSelectedCamera(PickImageDialog dialog) {
		//TODO ADD CAMERA SOURCE
	}

	@Override
	public void dialogSelectedGallery(PickImageDialog dialog) {
		OPallUtils.loadGalleryImageActivity(this);
	}


	@Override
	public AppCompatActivity getActivity() {
		return this;
	}


}
