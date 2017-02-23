package net.henryco.opalette.glES.render.graphics.textures;

import android.content.Context;

import net.henryco.opalette.glES.render.graphics.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.shaders.Shader;
import net.henryco.opalette.utils.bounds.Bounds2D;
import net.henryco.opalette.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.utils.bounds.observer.OPallMultiBoundsHolder;

/**
 * Created by HenryCo on 23/02/17.
 */

public class MultiTexture extends Shader implements OPallMultiBoundsHolder <Bounds2D>, OPallTexture {


	protected static final String DEF_SHADER = "shaders/default/Default";




	public MultiTexture(Context context, String VERT, String FRAG) {
		super(context, VERT, FRAG);
	}

	public MultiTexture(Context context, String VERT, String FRAG, int coordsPerVertex) {
		super(context, VERT, FRAG, coordsPerVertex);
	}

	@Override
	protected void render(int glProgram, OPallCamera2D camera) {

	}

	@Override
	public void setScreenDim(float w, float h) {

	}







	@Override
	public MultiTexture bounds(int n, BoundsConsumer<Bounds2D> processor) {
		return this;
	}

	@Override
	public MultiTexture bounds(BoundsConsumer<Bounds2D> processor) {
		return this;
	}

	@Override
	public MultiTexture updateBounds(int n) {
		return this;
	}

	@Override
	public MultiTexture updateBounds() {
		return this;
	}
}
