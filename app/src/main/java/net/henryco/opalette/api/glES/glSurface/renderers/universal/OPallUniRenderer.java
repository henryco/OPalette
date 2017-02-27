package net.henryco.opalette.api.glES.glSurface.renderers.universal;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.henryco.opalette.api.utils.requester.OPallRequestListener;
import net.henryco.opalette.api.utils.requester.Request;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 27/02/17.
 */

public abstract class OPallUniRenderer <T extends Context> implements GLSurfaceView.Renderer, OPallRequestListener {


	private T context;
	private long id;

	public OPallUniRenderer(T context) {
		this(context, (int) System.nanoTime());
	}
	public OPallUniRenderer(T context, int id) {
		setContext(context);
		setID(id);

	}



	protected abstract void onSurfaceCreated(GL10 gl, EGLConfig config, T context);
	protected abstract void onSurfaceChanged(GL10 gl, int width, int height, T context);
	protected abstract void onDrawFrame(GL10 gl, T context);





	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		onSurfaceCreated(gl, config, context);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		onSurfaceChanged(gl, width, height, context);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		onDrawFrame(gl, context);
	}

	@Override
	public void acceptRequest(Request request) {
		acceptRequest(request, context);
	}

	@Override
	public long getID() {
		return id;
	}





	protected void acceptRequest(Request request, T context) {}

	public OPallUniRenderer setID(long id) {
		this.id = id;
		return this;
	}

	public OPallUniRenderer setContext(T context) {
		this.context = context;
		return this;
	}
	public T getContext() {
		return this.context;
	}


}
