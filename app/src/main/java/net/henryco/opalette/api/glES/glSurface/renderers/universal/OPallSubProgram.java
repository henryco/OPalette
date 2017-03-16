package net.henryco.opalette.api.glES.glSurface.renderers.universal;

import android.content.Context;

import net.henryco.opalette.api.utils.requester.OPallRequestListener;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public interface OPallSubProgram <T extends Context> extends OPallRequestListener {

	void create(GL10 gl, int width, int height, T context);
	void onSurfaceChange(GL10 gl, T context, int width, int height);

	final class methods  {
		public static long genID() {
			return (long) (System.nanoTime() * 0.5 - 1);
		}
		public static long genID(Class inst) {
			return inst.getName().hashCode();
		}
	}
}