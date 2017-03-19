package net.henryco.opalette.api.glES.glSurface.renderers.universal;

import net.henryco.opalette.api.utils.observer.OPallUpdObserved;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */
public interface OPallUnderProgram<T> extends OPallSubProgram<T>, OPallUpdObserved {

	void onDraw(GL10 gl, T context, int width, int height);
}
