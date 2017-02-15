package net.henryco.opalette.graphicsCore.glES.render.renderers;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.shaders.pure.OPallShader;
import net.henryco.opalette.utils.GLESUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by root on 13/02/17.
 */

public abstract class OPallAbsRenderer implements GLSurfaceView.Renderer {

	private OPallShader shader;
	private Context context;

	private OPallCamera camera;

	public OPallAbsRenderer(Context context, OPallCamera camera) {
		this.context = context;
		setCamera(camera);
	}

	protected abstract OPallShader createShader(Context context);


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		shader = createShader(context);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		camera.set(width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLESUtils.clear(0.9f, 0.1f, 0.5f, 1f);
		shader.render(camera);
	}

	public OPallAbsRenderer setCamera(OPallCamera camera) {
		this.camera = camera;
		return this;
	}
}
