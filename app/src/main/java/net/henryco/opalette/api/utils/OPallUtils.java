package net.henryco.opalette.api.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;

import net.henryco.opalette.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by root on 13/02/17.
 */

public class OPallUtils {

	public interface ImageLoadable {
		void onActivityResult(int requestCode, int resultCode, Intent data);
		AppCompatActivity getActivity();
	}


	public static final class activity {
		public static final int REQUEST_PICK_IMAGE = 2137;
	}


	public static final class HoldXYZ {
		public float x;
		public float y;
		public float z;

		public HoldXYZ(float x, float y, float z) {
			setXYZ(x, y, z);
		}

		public HoldXYZ setXYZ(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}

		@Override
		public String toString() {
			return "HoldXYZ{" +
					"x=" + x +
					", y=" + y +
					", z=" + z +
					'}';
		}
	}


	public static final class Switcher {

		private volatile AtomicBoolean stop = new AtomicBoolean(false);
		public synchronized Switcher stop(){
			stop.set(true);
			return this;
		}
		public synchronized Switcher start() {
			stop.set(false);
			return this;
		}
		public boolean get() {return stop.get();}
	}


	@SuppressWarnings("unchecked")
	public static <T> T arrayFlatCopy(Object a) {
		int l = Array.getLength(a);
		Object arr = java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), l);
		System.arraycopy(a, 0, arr, 0, l);
		return (T) arr;
	}


    public static String getSourceAssetsText(String file, Context context) {
        try {
            InputStream is = context.getAssets().open(file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
			return "";
        }
    }

	public static Bitmap loadAssetsBitmap(Context context, boolean inScaled, int resID) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = inScaled;
		return BitmapFactory.decodeResource(context.getResources(), resID, options);
	}

	public static Bitmap loadIntentBitmap(Context context, Intent intent) {
		try {
			return MediaStore.Images.Media.getBitmap(context.getContentResolver(), intent.getData());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

    public static void loopStart(long sleep, Switcher stop, Runnable runnable) {
		new Thread(() -> {
			while (stop == null || !stop.get()) {
				runnable.run();
				if (sleep > 0) {
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}


	public static void loadGalleryImageActivity(ImageLoadable activity) {
		activity.getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
				OPallUtils.activity.REQUEST_PICK_IMAGE);
	}

	public static long millisTimer(Runnable runnable) {
		long t0 = System.currentTimeMillis();
		runnable.run();
		return System.currentTimeMillis() - t0;
	}

	public static long nanosTimer(Runnable runnable) {
		long t0 = System.nanoTime();
		runnable.run();
		return System.nanoTime() - t0;
	}


	public static float secTimer(Runnable runnable) {
		return millisTimer(runnable) * 0.001f;
	}


	@SuppressWarnings("unchecked")
	public static <T extends View> T getViewFromHeader(NavigationView navigationView, int headerIndex, int viewID)  {
		View headerView = navigationView.getHeaderView(headerIndex);
		return (T) headerView.findViewById(viewID);
	}

	public static void pressButton75_225(Context c, View v, Runnable action) {
		v.startAnimation(AnimationUtils.loadAnimation(c, R.anim.press_75pct_225ms));
		new Handler().postDelayed(action, 180);
	}

}

