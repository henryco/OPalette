package net.henryco.opalette.api.glES.glSurface.renderers.solo;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.henryco.opalette.api.glES.camera.OPallCamera2D;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.shaders.Shader;
import net.henryco.opalette.api.utils.GLESUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by root on 13/02/17.
 */

public abstract class OPallSoloRenderer implements GLSurfaceView.Renderer {

	@FunctionalInterface
	public interface OnDrawAction {
		void action(GL10 gl10, OPallCamera2D camera);
	}
	private OnDrawAction onDrawAction;


	private Shader shader;
	private Context context;
	private OPallCamera2D camera;



	public OPallSoloRenderer(Context context, OPallCamera2D camera) {
		this(context, camera, (gl10, camera1) -> GLESUtils.clear(GLESUtils.Color.BLACK));
	}
	public OPallSoloRenderer(Context context, OPallCamera2D camera, OnDrawAction action) {
		this.context = context;
		this.camera = camera;
		setOnDrawAction(action);
	}
	public OPallSoloRenderer(GLSurfaceView.Renderer renderer) {
		if (renderer instanceof OPallSoloRenderer) {
			this.context = ((OPallSoloRenderer)renderer).context;
			this.camera = ((OPallSoloRenderer)renderer).camera;
			this.shader = ((OPallSoloRenderer) renderer).shader;
			if (((OPallSoloRenderer)renderer).onDrawAction != null)
				this.onDrawAction = ((OPallSoloRenderer)renderer).onDrawAction;
		}
	}

	protected abstract Shader createShader(Context context);

	@SuppressWarnings("unchecked")
	public <T extends Shader> T getShader() {
		return (T) this.shader;
	}
	public OPallSoloRenderer setShader(Shader shader) {
		this.shader = shader;
		return this;
	}


	public OPallSoloRenderer setOnDrawAction(OnDrawAction action) {
		this.onDrawAction = action;
		return this;
	}




	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		shader = createShader(context);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		FrameBuffer.debug = true;
		if (camera != null) camera.set(width, height);
		if (shader != null) shader.setScreenDim(width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
			if (camera != null) onDrawAction.action(gl, camera);
			if (shader != null) shader.render(camera);
	}

	public OPallCamera2D getCamera() {
		return camera;
	}
	public OPallSoloRenderer setCamera(OPallCamera2D camera) {
		this.camera = camera;
		return this;
	}
}
