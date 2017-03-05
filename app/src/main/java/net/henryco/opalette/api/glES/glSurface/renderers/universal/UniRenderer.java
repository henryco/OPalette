package net.henryco.opalette.api.glES.glSurface.renderers.universal;

import android.content.Context;

import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.utils.observer.OPallObservator;
import net.henryco.opalette.api.utils.requester.Request;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */

public class UniRenderer<T extends Context> extends OPallUniRenderer<T> implements OPallObservator {


	private OPallUnderProgram<T> underProgram;
	private int[] dimension = {0, 0};

	public UniRenderer(T context) {
		super(context);
	}
	public UniRenderer(T context, int id) {
		super(context, id);
	}
	public UniRenderer(T context, OPallSurfaceView view) {
		super(context, view);
	}
	public UniRenderer(T context, int id, OPallSurfaceView view) {
		super(context, id, view);
	}

	public UniRenderer(T context, OPallUnderProgram<T> program) {
		this(context);
		setProgram(program);
	}
	public UniRenderer(T context, int id, OPallUnderProgram<T> program) {
		this(context, id);
		setProgram(program);
	}
	public UniRenderer(T context, OPallSurfaceView view, OPallUnderProgram<T> program) {
		this(context, view);
		setProgram(program);
	}
	public UniRenderer(T context, int id, OPallSurfaceView view, OPallUnderProgram<T> program) {
		this(context, id, view);
		setProgram(program);
	}


	/**
	 * Optional method can be override
	 * @param request - delegated request
	 */
	protected void acceptDelegatedRequest(Request request) {
		// DO NOTHING
	}

	/**
	 * Final method, if u want handle requests, override {acceptDelegatedRequest}
	 * @param request
	 */
	@Override
	public final void acceptRequest(Request request) {
		forceUpDate(() -> addToGLContextQueue((gl10, t) -> {
			super.acceptRequest(request);
			underProgram.acceptRequest(request);
			acceptDelegatedRequest(request);
		}));
	}


	@Override
	protected void onSurfaceCreated(GL10 gl, EGLConfig config, int width, int height, T context) {
		if (underProgram != null) underProgram.create(gl, width, height, context);
		dimension = new int[]{width, height};
	}

	@Override
	protected void onSurfaceChanged(GL10 gl, int width, int height, T context) {
		if (underProgram != null) underProgram.onSurfaceChange(gl, context, width, height);
		dimension = new int[]{width, height};
	}

	@Override
	protected void onDrawFrame(GL10 gl, T context) {
		if (underProgram != null) underProgram.onDraw(gl, context, dimension[0], dimension[1]);
	}


	public UniRenderer<T> setRenderProgram(OPallUnderProgram<T> program) {
		setProgram(program);
		forceUpDate(()
				-> addToGLContextQueue((gl10, context)
				-> underProgram.create(gl10, getWidth(), getHeight(), context))
		);
		return this;
	}

	private UniRenderer<T> setProgram(OPallUnderProgram<T> program) {
		this.underProgram = program;
		program.setObservator(this);
		return this;
	}

	@Override
	public void update() {
		forceUpDate();
	}
}
