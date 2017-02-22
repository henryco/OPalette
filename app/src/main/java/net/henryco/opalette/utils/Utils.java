package net.henryco.opalette.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by root on 13/02/17.
 */

public class Utils {

	public static final class activity {
		public static final int REQUEST_PICK_IMAGE = 2137;
	}


	public static final class StopHold {

		private volatile AtomicBoolean stop = new AtomicBoolean(false);
		public synchronized StopHold stop(){
			stop.set(true);
			return this;
		}
		public synchronized StopHold start() {
			stop.set(false);
			return this;
		}
		public boolean get() {return stop.get();}
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
        }
        return "";
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

    public static void loopStart(long sleep, StopHold stop, Runnable runnable) {
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


	public static void loadImageActivity(FragmentActivity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		activity.startActivityForResult(intent, Utils.activity.REQUEST_PICK_IMAGE);
	}
}
