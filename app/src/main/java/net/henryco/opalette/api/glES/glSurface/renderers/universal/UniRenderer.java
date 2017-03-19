package net.henryco.opalette.api.glES.glSurface.renderers.universal;

import android.content.Context;

import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.api.utils.requester.Request;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */

public class UniRenderer extends OPallUniRenderer implements OPallUpdObserver {


	private OPallUnderProgram underProgram;
	private int[] dimension = {0, 0};

	public UniRenderer(Context context) {
		super(context);
	}
	public UniRenderer(Context context, int id) {
		super(context, id);
	}
	public UniRenderer(Context context, OPallSurfaceView view) {
		super(context, view);
	}
	public UniRenderer(Context context, int id, OPallSurfaceView view) {
		super(context, id, view);
	}

	public UniRenderer(Context context, OPallUnderProgram program) {
		this(context);
		setProgram(program);
	}
	public UniRenderer(Context context, int id, OPallUnderProgram program) {
		this(context, id);
		setProgram(program);
	}
	public UniRenderer(Context context, OPallSurfaceView view, OPallUnderProgram program) {
		this(context, view);
		setProgram(program);
	}
	public UniRenderer(Context context, int id, OPallSurfaceView view, OPallUnderProgram program) {
		this(context, id, view);
		setProgram(program);
	}


	/**
	 * Optional method can be override
	 * @param request - delegated request
	 */
	protected void acceptDelegatedRequest(Request request) {}

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
	protected void onSurfaceCreated(GL10 gl, EGLConfig config, int width, int height, Context context) {
		if (underProgram != null) underProgram.create(gl, width, height, context);
		dimension = new int[]{width, height};
	}

	@Override
	protected void onSurfaceChanged(GL10 gl, int width, int height, Context context) {
		if (underProgram != null) underProgram.onSurfaceChange(gl, context, width, height);
		dimension = new int[]{width, height};
	}

	@Override
	protected void onDrawFrame(GL10 gl, Context context) {
		if (underProgram != null) underProgram.onDraw(gl, context, dimension[0], dimension[1]);
	}


	public UniRenderer setRenderProgram(OPallUnderProgram program) {
		setProgram(program);
		forceUpDate(()
				-> addToGLContextQueue((gl10, context)
				-> underProgram.create(gl10, getWidth(), getHeight(), context))
		);
		return this;
	}

	private UniRenderer setProgram(OPallUnderProgram program) {
		this.underProgram = program;
		program.setObservator(this);
		return this;
	}

	@Override
	public void update() {
		forceUpDate();
	}
}
