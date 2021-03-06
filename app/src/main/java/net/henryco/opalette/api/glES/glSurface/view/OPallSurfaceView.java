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

package net.henryco.opalette.api.glES.glSurface.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.solo.OPallSoloRenderer;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUniRenderer;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OPallSurfaceView extends GLSurfaceView {


	public static final SurfaceInfo surfaceInfo = new SurfaceInfo();
	public static final class SurfaceInfo {
		private final AtomicBoolean surfaceCreated = new AtomicBoolean(false);
		private final AtomicBoolean surfaceChanged = new AtomicBoolean(false);
		private final AtomicBoolean surfaceDrawing = new AtomicBoolean(false);

		public boolean isSurfaceCreated() {
			return surfaceCreated.get();
		}
		public boolean isSurfaceChanged() {
			return surfaceChanged.get();
		}
		public boolean isSurfaceDrawing() {
			return surfaceDrawing.get();
		}
	}




	public static final class DimensionProcessors {
		public static final SurfaceDimension DEFAULT = (w, h) -> new int[]{w, h};
		public static final SurfaceDimension RELATIVE_SQUARE = (w, h) -> {
			int width = MeasureSpec.getSize(w);
			int height = MeasureSpec.getSize(h);
			int size = width > height ? height : width;
			return new int[]{size, size};
		};
	}



	private List<OnTouchEventListener> touchEventListeners = new ArrayList<>();
	public interface OnTouchEventListener {
		void onTouchEvent(MotionEvent event);
	}


	private SurfaceDimension dimProcessor = DimensionProcessors.DEFAULT;
	public interface SurfaceDimension {
		int[] onMeasure(int widthMeasureSpec, int heightMeasureSpec);
	}



	private DrawAction drawAction;
	public interface DrawAction {
		void onDrawFrameAction(GL10 gl);
	}
	private List<DrawAction> actionQueue;



	private Renderer renderer;


    public OPallSurfaceView(Context context) {
        super(context);
        init(context);
	}

    public OPallSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
		actionQueue = new ArrayList<>();
		drawAction = gl -> {
			int size = actionQueue.size();
			for (int i = 0; i < size; i++) actionQueue.remove(0).onDrawFrameAction(gl);
		};

    }


    private SurfaceDimension backUpDimProcessor = null;
	public OPallSurfaceView setSize(int w, int h) {
		resetSize();
		backUpDimProcessor = dimProcessor;
		this.dimProcessor = (width, height) -> new int[]{w, h};
		getHolder().setFixedSize(w, h);
		return this;
	}
	public OPallSurfaceView resetSize() {
		if (backUpDimProcessor != null)
			dimProcessor = backUpDimProcessor;
		return this;
	}


    public OPallSurfaceView update() {
        requestRender();
		return this;
    }
	public OPallSurfaceView update(Runnable runnable) {
		runnable.run();
		return update();
	}


	public OPallSurfaceView setDimProportions(SurfaceDimension dimProcessor) {
		this.dimProcessor = dimProcessor;
		return this;
	}


	public OPallSurfaceView addToGLContextQueue(DrawAction action) {
		actionQueue.add(action);
		return this;
	}





	@Override
	public void setRenderer(Renderer renderer) {

		if (renderer instanceof OPallSoloRenderer)
			if (((OPallSoloRenderer) renderer).getCamera() == null)
				((OPallSoloRenderer) renderer).setCamera(new Camera2D(getWidth(), getHeight(), true));
		if (renderer instanceof OPallUniRenderer) ((OPallUniRenderer) renderer).setSuperSurface(this);

		super.setRenderer(new Renderer() {
			@Override
			public void onSurfaceCreated(GL10 gl, EGLConfig config) {
				GLESUtils.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, () -> {
					surfaceInfo.surfaceCreated.set(false);
					drawAction.onDrawFrameAction(gl);
					renderer.onSurfaceCreated(gl, config);
					surfaceInfo.surfaceCreated.set(true);
				});
			}
			@Override
			public void onSurfaceChanged(GL10 gl, int width, int height) {
				GLESUtils.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, () -> {
					surfaceInfo.surfaceChanged.set(false);
					drawAction.onDrawFrameAction(gl);
					renderer.onSurfaceChanged(gl, width, height);
					surfaceInfo.surfaceChanged.set(true);
				});
			}
			@Override
			public void onDrawFrame(GL10 gl) {
				GLESUtils.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, () -> {
					surfaceInfo.surfaceDrawing.set(true);
					drawAction.onDrawFrameAction(gl);
					renderer.onDrawFrame(gl);
					surfaceInfo.surfaceDrawing.set(false);
				});
			}
		});
		this.renderer = renderer;
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}


	@SuppressWarnings("unchecked")
	public <T extends Renderer> T getRenderer() {
		try {
			return (T) this.renderer;
		} catch (Exception e) {
			throw new ClassCastException();
		}
	}


	/**
	 * Square area with percent relative size<br>
	 * http://stackoverflow.com/a/8981478
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int[] dim = dimProcessor.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(dim[0], dim[1]);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (OnTouchEventListener l : touchEventListeners) l.onTouchEvent(event);
		return true;
	}


	private OnTouchEventListener backUPedListener;
	public void addBackUpedTouchEventListener(OnTouchEventListener listener) {
		backUPedListener = getLastTouchEventListener();
		addOnTouchEventListener(listener);
	}
	public void removeAndRestoreOnTouchEventListener(OnTouchEventListener listener) {
		removeTouchEventListener(listener);
		addOnTouchEventListener(backUPedListener);
	}

	public void addOnTouchEventListener(OnTouchEventListener listener) {
		if (listener != null && !touchEventListeners.contains(listener)) {
			touchEventListeners.clear();
			touchEventListeners.add(listener);
		}
	}

	public void removeTouchEventListener(OnTouchEventListener listener) {
		if (listener != null) touchEventListeners.remove(listener);
	}

	public OnTouchEventListener getLastTouchEventListener() {
		if (touchEventListeners.size() > 0)
			return touchEventListeners.get(0);
		return null;
	}

	/**
	 * Area where code will execute after context creation.
	 * @param action action to execute
	 * @return OPallSurfaceView
	 */
	public final OPallSurfaceView startWhenReady(OPallConsumer<Renderer> action) {
		new Thread(() -> {
			while (!surfaceInfo.isSurfaceCreated() || !surfaceInfo.isSurfaceChanged()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			action.consume(renderer);
		}).start();
		return this;
	}

}
