package net.henryco.opalette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
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


	public static final long SPLASH_LOADING_DELAY = 3000;
	public static final long PICKBUTTON_ROTATION_TIME = 500;
	public static final int PICKBUTTON_ROTATION_NUMB = 1;
	public static final long PICKBUTTON_SLEEP_TIME = 2;
	public static final float PICKBUTTON_RADIUS = 2/3;

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









	private void showAllItems() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) actionBar.show();
		findViewById(R.id.fullscreen_content).setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		);
	}


	private void initSplash() {
		ImageView logo = (ImageView) findViewById(R.id.logoImageVIew);
		new Thread(() -> {
			long t0 = System.currentTimeMillis();
			while (System.currentTimeMillis() < t0 + SPLASH_LOADING_DELAY) {
				synchronized (this) {
					try {
						float nt = ((float) (System.currentTimeMillis() - t0) / SPLASH_LOADING_DELAY);
						logo.setAlpha((float) Math.min(1., Math.sin(Math.PI * nt) * 1.25));
						Thread.sleep(1);
					} catch (Exception ignored) {}
				}
			} runOnUiThread(() -> {
				logo.setVisibility(View.GONE);
				showAllItems();
				findViewById(R.id.firstPickLayout).setVisibility(View.VISIBLE);
			});
		}).start();
	}



	private synchronized void imagePickAction(View view) {

		ImageButton pickButton = (ImageButton) findViewById(R.id.imageButtonGall);

		float hlW = findViewById(R.id.firstPickLayout).getWidth() * 0.5f;
		float s_pi = (float) (hlW / Math.PI);
		float r = pickButton.getWidth() * 0.5f * PICKBUTTON_RADIUS;



		long t_spin_ms = PICKBUTTON_ROTATION_TIME / PICKBUTTON_ROTATION_NUMB;
		float deg_per_ms = 360f / (float)t_spin_ms;
		new Thread(() -> {
			long t0 = System.currentTimeMillis();
			while(System.currentTimeMillis() < t0 + PICKBUTTON_ROTATION_TIME) {
				synchronized (this) {
					try {
						pickButton.setRotation(((float)(System.currentTimeMillis() - t0)) * deg_per_ms);
						Thread.sleep(PICKBUTTON_SLEEP_TIME);
					} catch (Exception ignore) {}
				}
			}
			pickButton.setRotation(0);
			AppCompatDialogFragment pickImageDialog = new PickImageDialog();
			pickImageDialog.show(getSupportFragmentManager(), "pickImageDialog");
		}).start();
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

		findViewById(R.id.fullscreen_content_controls).setOnClickListener(this::imagePickAction);
		findViewById(R.id.imageButtonGall).setOnClickListener(this::imagePickAction);
		findViewById(R.id.textView).setOnClickListener(this::imagePickAction);

		initSplash();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == OPallUtils.activity.REQUEST_PICK_IMAGE) {
				Intent intent = new Intent(this, ProtoActivity.class);
				BitmapPack.pushUpBitmap = OPallUtils.loadIntentBitmap(this, data);
				startActivity(intent);
				finish();
			}
		}
	}


	@Override
	public void dialogSelectedCamera(PickImageDialog dialog) {
		//TODO
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
