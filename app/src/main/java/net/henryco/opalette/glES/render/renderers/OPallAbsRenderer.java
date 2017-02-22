package net.henryco.opalette.glES.render.renderers;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.henryco.opalette.glES.render.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.shaders.OPallShader;
import net.henryco.opalette.utils.GLESUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by root on 13/02/17.
 */

public abstract class OPallAbsRenderer implements GLSurfaceView.Renderer {

	@FunctionalInterface
	public interface OnDrawAction {
		void action(GL10 gl10, OPallCamera2D camera);
	}
	private OnDrawAction onDrawAction;


	private OPallShader shader;
	private Context context;
	private OPallCamera2D camera;




	public OPallAbsRenderer(Context context, OPallCamera2D camera) {
		this(context, camera, (gl10, camera1) -> GLESUtils.clear(0.9f, 0.1f, 0.5f, 1f));
	}
	public OPallAbsRenderer(Context context, OPallCamera2D camera, OnDrawAction action) {
		this.context = context;
		this.camera = camera;
		setOnDrawAction(action);
	}
	public OPallAbsRenderer(GLSurfaceView.Renderer renderer) {
		if (renderer instanceof OPallAbsRenderer) {
			this.context = ((OPallAbsRenderer)renderer).context;
			this.camera = ((OPallAbsRenderer)renderer).camera;
			this.shader = ((OPallAbsRenderer) renderer).shader;
			if (((OPallAbsRenderer)renderer).onDrawAction != null)
				this.onDrawAction = ((OPallAbsRenderer)renderer).onDrawAction;
		}
	}

	protected abstract OPallShader createShader(Context context);

	public OPallShader getShader() {
		return this.shader;
	}
	public OPallAbsRenderer setShader(OPallShader shader) {
		this.shader = shader;
		return this;
	}


	public GLSurfaceView.Renderer setOnDrawAction(OnDrawAction action) {
		this.onDrawAction = action;
		return this;
	}




	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		shader = createShader(context);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (camera != null) camera.set(width, height);
		if (shader != null) shader.setScrDim(width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		if (camera != null) onDrawAction.action(gl, camera);
		if (shader != null) shader.render(camera);
	}

	public OPallCamera2D getCamera() {
		return camera;
	}
	public void setCamera(OPallCamera2D camera) {
		this.camera = camera;
	}
}
