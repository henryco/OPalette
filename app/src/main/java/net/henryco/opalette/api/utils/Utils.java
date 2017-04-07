/*
 *   /*
 *    * Copyright (C) Henryk Timur Domagalski
 *    *
 *    * Licensed under the Apache License, Version 2.0 (the "License");
 *    * you may not use this file except in compliance with the License.
 *    * You may obtain a copy of the License at
 *    *
 *    *      http://www.apache.org/licenses/LICENSE-2.0
 *    *
 *    * Unless required by applicable law or agreed to in writing, software
 *    * distributed under the License is distributed on an "AS IS" BASIS,
 *    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    * See the License for the specific language governing permissions and
 *    * limitations under the License.
 *
 */

package net.henryco.opalette.api.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by root on 13/02/17.
 */

public class Utils {

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
				Utils.activity.REQUEST_PICK_IMAGE);
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


	public static File saveBitmapAction(Bitmap bitmap, String filename, Context activity) {

		File dest = new File(Environment.getExternalStorageDirectory().toString(), filename + ".png");
		try {
			dest.createNewFile();
			FileOutputStream fos = new FileOutputStream(dest);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return updGallery(dest, activity);
	}

	public static File updGallery(File outputFile, Context activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			final Uri contentUri = Uri.fromFile(outputFile);
			scanIntent.setData(contentUri);
			activity.sendBroadcast(scanIntent);
		} else activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		return outputFile;
	}

	public static void shareBitmapAction(Bitmap bitmap, String filename, Context activity, boolean saveAfter) {


		try {
			File cachePath = new File(activity.getCacheDir(), "images"); // see: res/xml/filepaths.xml

			if (cachePath.exists()) deleteRecursive(cachePath);
			cachePath.mkdirs();

			FileOutputStream stream = new FileOutputStream(cachePath + "/"+filename + ".png"); // overwrites this image every time
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File imagePath = new File(activity.getCacheDir(), "images");
		File newFile = new File(imagePath, filename + ".png");
		Uri contentUri = FileProvider.getUriForFile(activity, "net.henryco.opalette.fileprovider", newFile);

		if (contentUri != null) {
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
			shareIntent.setType("image/png");
			shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
			activity.startActivity(Intent.createChooser(shareIntent, "Share"));
		}
		if (saveAfter) saveBitmapAction(bitmap, filename, activity);
	}


	private static void deleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteRecursive(child);
		fileOrDirectory.delete();
	}

}

