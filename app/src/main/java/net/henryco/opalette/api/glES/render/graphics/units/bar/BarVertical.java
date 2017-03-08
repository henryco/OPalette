package net.henryco.opalette.api.glES.render.graphics.units.bar;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 06/03/17.
 */
//TODO
public class BarVertical implements OPallBar {




	@Override
	public void createBar(int scrWidth, int scrHeight) {

	}

	@Override
	public void render(Camera2D camera, OPallRenderable renderable, int buffer_quantum) {

	}

	@Override
	public OPallBar setColor(GLESUtils.Color color) {
		return null;
	}

	@Override
	public OPallBar setRelativeSize(float size_pct) {
		return null;
	}

	@Override
	public OPallBar setRelativePosition(float pos_pct) {
		return null;
	}

	@Override
	public OPallBar setRelativeContentSize(float size_pct) {
		return null;
	}


	@Override
	public float getWidth() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 0;
	}
}
