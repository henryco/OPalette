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

package net.henryco.opalette.api.glES.glSurface.renderers.universal;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.utils.lambda.consumers.OPallBiConsumer;
import net.henryco.opalette.api.utils.requester.OPallRequestListener;
import net.henryco.opalette.api.utils.requester.Request;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 27/02/17.
 */

public abstract class OPallUniRenderer implements GLSurfaceView.Renderer, OPallRequestListener {


	private Context context;
	private long id;
	private OPallSurfaceView surfaceView = null;

	public OPallUniRenderer(Context context) {
		this(context, (int) System.nanoTime());
	}
	public OPallUniRenderer(Context context, int id) {
		setContext(context);
		setID(id);
	}
	public OPallUniRenderer(Context context, OPallSurfaceView view) {
		this(context);
		setSuperSurface(view);
	}
	public OPallUniRenderer(Context context, int id, OPallSurfaceView view) {
		this(context, id);
		setSuperSurface(view);
	}

	protected abstract void onSurfaceCreated(GL10 gl, EGLConfig config, int width, int height, Context context);
	protected abstract void onSurfaceChanged(GL10 gl, int width, int height, Context context);
	protected abstract void onDrawFrame(GL10 gl, Context context);





	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		if (surfaceView == null) throw new RuntimeException("Super surface: <OPallSurfaceView> lost!");
		onSurfaceCreated(gl, config, getWidth(), getHeight(), context);
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





	protected void acceptRequest(Request request, Context context) {}

	public OPallUniRenderer setID(long id) {
		this.id = id;
		return this;
	}

	public OPallUniRenderer setContext(Context context) {
		this.context = context;
		return this;
	}
	public Context getContext() {
		return this.context;
	}


	public OPallUniRenderer setSuperSurface(OPallSurfaceView view) {
		this.surfaceView = view;
		return this;
	}


	public OPallUniRenderer forceUpDate() {
		surfaceView.update();
		return this;
	}

	public OPallUniRenderer forceUpDate(Runnable runnable) {
		surfaceView.update(runnable);
		return this;
	}

	public OPallUniRenderer addToGLContextQueue(OPallBiConsumer<GL10, Context> action) {
		surfaceView.addToGLContextQueue(gl -> action.consume(gl, context));
		return this;
	}


	protected int getWidth() {
		return surfaceView.getWidth();
	}
	protected int getHeight() {
		return surfaceView.getHeight();
	}

}
