package net.henryco.opalette.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by root on 13/02/17.
 */

public class Utils {

	public static final class Stopper {

		private volatile AtomicBoolean stop;
		public synchronized Stopper stop(){
			stop.set(true);
			return this;
		}
		public synchronized Stopper start() {
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


    public static void loopStart(long sleep, Stopper stop, Runnable runnable) {
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

}
