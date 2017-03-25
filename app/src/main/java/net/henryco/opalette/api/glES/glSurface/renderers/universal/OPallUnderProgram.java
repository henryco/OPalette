package net.henryco.opalette.api.glES.glSurface.renderers.universal;

import android.support.annotation.Nullable;

import net.henryco.opalette.api.utils.observer.OPallUpdObserved;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */
public interface OPallUnderProgram<T> extends OPallSubProgram<T>, OPallUpdObserved {

	void onDraw(@Nullable GL10 gl, T context, int width, int height);
}
