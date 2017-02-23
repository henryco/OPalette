package net.henryco.opalette.glES.render.renderers;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.henryco.opalette.glES.render.graphics.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.shaders.OPallShader;

/**
 * Created by root on 15/02/17.
 */

public class OPallRenderer extends OPallAbsRenderer {

	@FunctionalInterface
	public interface ShaderMaker {
		OPallShader createShader(Context context);
	}

	private ShaderMaker shaderMaker;

	public OPallRenderer(Context context, OPallCamera2D camera, ShaderMaker shaderMaker) {
		super(context, camera);
		this.shaderMaker = shaderMaker;
	}
	public OPallRenderer(Context context, OPallCamera2D camera) {
		this(context, camera, context1 -> null);
	}
	public OPallRenderer(Context context, ShaderMaker shaderMaker) {
		this(context, null, shaderMaker);
	}
	public OPallRenderer(Context context) {
		this(context, context1 -> null);
	}
	public OPallRenderer(GLSurfaceView.Renderer renderer) {
		super(renderer);
		if (renderer instanceof OPallRenderer) {
			this.shaderMaker = ((OPallRenderer) renderer).shaderMaker;
		}
	}

	@Override
	protected OPallShader createShader(Context context) {
		return shaderMaker.createShader(context);
	}

}
