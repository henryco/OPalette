package net.henryco.opalette.api.glES.glSurface.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.solo.OPallSoloRenderer;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUniRenderer;
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
				surfaceInfo.surfaceCreated.set(false);
				renderer.onSurfaceCreated(gl, config);
				drawAction.onDrawFrameAction(gl);
				surfaceInfo.surfaceCreated.set(true);
			}
			@Override
			public void onSurfaceChanged(GL10 gl, int width, int height) {
				surfaceInfo.surfaceChanged.set(false);
				renderer.onSurfaceChanged(gl, width, height);
				drawAction.onDrawFrameAction(gl);
				surfaceInfo.surfaceChanged.set(true);
			}
			@Override
			public void onDrawFrame(GL10 gl) {
				surfaceInfo.surfaceDrawing.set(true);
				renderer.onDrawFrame(gl);
				drawAction.onDrawFrameAction(gl);
				surfaceInfo.surfaceDrawing.set(false);
			}
		});
		this.renderer = renderer;
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



	/**
	 * Area where code will execute after context creation.
	 * @param action action to execute
	 * @return OPallSurfaceView
	 */
	public final OPallSurfaceView startWhenReady(OPallConsumer<Renderer> action) {
		new Thread(() -> {
			while (!surfaceInfo.isSurfaceCreated()
					|| !surfaceInfo.isSurfaceChanged()) {
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
