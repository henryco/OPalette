package net.henryco.opalette.glES.render.renderers;

import android.content.Context;

import net.henryco.opalette.glES.render.camera.OPallCamera2D;
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
	public OPallRenderer(Context context) {
		this(context, null, c -> null);
	}

	@Override
	protected OPallShader createShader(Context context) {
		return shaderMaker.createShader(context);
	}
}
