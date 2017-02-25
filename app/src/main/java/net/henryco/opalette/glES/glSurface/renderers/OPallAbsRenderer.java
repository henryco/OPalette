package net.henryco.opalette.glES.glSurface.renderers;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.henryco.opalette.glES.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.glES.render.graphics.shaders.Shader;
import net.henryco.opalette.glES.render.graphics.textures.Texture;
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


	private Shader shader;
	private Context context;
	private OPallCamera2D camera;

	private FrameBuffer testBuffer = new FrameBuffer();


	public OPallAbsRenderer(Context context, OPallCamera2D camera) {
		this(context, camera, (gl10, camera1) -> GLESUtils.clear(GLESUtils.Color.BLACK));
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

	protected abstract Shader createShader(Context context);

	@SuppressWarnings("unchecked")
	public <T extends Shader> T getShader() {
		return (T) this.shader;
	}
	public OPallAbsRenderer setShader(Shader shader) {
		this.shader = shader;
		return this;
	}


	public OPallAbsRenderer setOnDrawAction(OnDrawAction action) {
		this.onDrawAction = action;
		return this;
	}




	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		shader = createShader(context);
		testBuffer.setTargetTexture(new Texture(null, context));
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		FrameBuffer.debug = true;
		if (camera != null) camera.set(width, height);
		if (shader != null) shader.setScreenDim(width, height);
		testBuffer.createFBO(width, height, false);
	}

	@Override
	public void onDrawFrame(GL10 gl) {

	//	testBuffer.beginFBO(() -> {
			if (camera != null) onDrawAction.action(gl, camera);
			if (shader != null) shader.render(camera);
	//	}).render(camera);

	}

	public OPallCamera2D getCamera() {
		return camera;
	}
	public OPallAbsRenderer setCamera(OPallCamera2D camera) {
		this.camera = camera;
		return this;
	}
}
