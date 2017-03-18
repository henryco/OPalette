package net.henryco.opalette.api.glES.glSurface.renderers.universal;

import android.content.Context;

import net.henryco.opalette.api.utils.observer.OPallUpdObserved;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */
public interface OPallUnderProgram<T extends Context>
		extends OPallSubProgram<T>, OPallUpdObserved {

	void onDraw(GL10 gl, T context, int width, int height);
}
