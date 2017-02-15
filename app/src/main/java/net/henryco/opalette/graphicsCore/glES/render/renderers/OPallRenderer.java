package net.henryco.opalette.graphicsCore.glES.render.renderers;

import android.content.Context;

import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.shaders.pure.OPallShader;

/**
 * Created by root on 15/02/17.
 */

public class OPallRenderer extends OPallAbsRenderer {

	@FunctionalInterface
	public interface ShaderMaker {
		OPallShader createShader(Context context);
	}

	private ShaderMaker shaderMaker;

	public OPallRenderer(Context context, OPallCamera camera, ShaderMaker shaderMaker) {
		super(context, camera);
		this.shaderMaker = shaderMaker;
	}

	@Override
	protected OPallShader createShader(Context context) {
		return shaderMaker.createShader(context);
	}
}
