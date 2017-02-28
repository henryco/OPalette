package net.henryco.opalette.api.glES.glSurface.renderers.solo;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.Shader;

/**
 * Created by root on 15/02/17.
 */

public class SoloRenderer extends OPallSoloRenderer {

	@FunctionalInterface
	public interface ShaderMaker {
		Shader createShader(Context context);
	}

	private ShaderMaker shaderMaker;

	public SoloRenderer(Context context, Camera2D camera, ShaderMaker shaderMaker) {
		super(context, camera);
		this.shaderMaker = shaderMaker;
	}
	public SoloRenderer(Context context, Camera2D camera) {
		this(context, camera, context1 -> null);
	}
	public SoloRenderer(Context context, ShaderMaker shaderMaker) {
		this(context, null, shaderMaker);
	}
	public SoloRenderer(Context context) {
		this(context, context1 -> null);
	}
	public SoloRenderer(GLSurfaceView.Renderer renderer) {
		super(renderer);
		if (renderer instanceof SoloRenderer) {
			this.shaderMaker = ((SoloRenderer) renderer).shaderMaker;
		}
	}

	@Override
	protected Shader createShader(Context context) {
		return shaderMaker.createShader(context);
	}

}
