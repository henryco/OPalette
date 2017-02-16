package net.henryco.opalette.glES.layouts;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

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



    public OPallSurfaceView(Context context) {
        super(context);
        reInit(context);
    }

    public OPallSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        reInit(context);
    }




    public void reInit(Context context) {
        setEGLContextClientVersion(2);
    }
    public void update() {
        requestRender();
    }
	public void update(Runnable runnable) {
		runnable.run();
		update();
	}




	@Override
	public void setRenderer(Renderer renderer) {
		super.setRenderer(new Renderer() {
			@Override
			public void onSurfaceCreated(GL10 gl, EGLConfig config) {
				surfaceInfo.surfaceCreated.set(false);
				renderer.onSurfaceCreated(gl, config);
				surfaceInfo.surfaceCreated.set(true);
			}
			@Override
			public void onSurfaceChanged(GL10 gl, int width, int height) {
				surfaceInfo.surfaceChanged.set(false);
				renderer.onSurfaceChanged(gl, width, height);
				surfaceInfo.surfaceChanged.set(true);
			}
			@Override
			public void onDrawFrame(GL10 gl) {
				surfaceInfo.surfaceDrawing.set(true);
				renderer.onDrawFrame(gl);
				surfaceInfo.surfaceDrawing.set(false);
			}
		});
	}




	public OPallSurfaceView executeWhenReady(Runnable action) {
		new Thread(() -> {
			while (!surfaceInfo.isSurfaceCreated()
					|| !surfaceInfo.isSurfaceChanged()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			action.run();
		}).start();
		return this;
	}
}
