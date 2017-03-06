package net.henryco.opalette.api.glES.render.graphics.shapes.bar;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 06/03/17.
 */
//TODO
public class BackBarVertical implements OPallBackBar {




	@Override
	public void createBar(int scrWidth, int scrHeight) {

	}

	@Override
	public void render(Camera2D camera, OPallRenderable renderable, int buffer_quantum) {

	}

	@Override
	public OPallBackBar setColor(GLESUtils.Color color) {
		return null;
	}

	@Override
	public OPallBackBar setRelativeSize(float size_pct) {
		return null;
	}

	@Override
	public OPallBackBar setRelativePosition(float pos_pct) {
		return null;
	}

	@Override
	public OPallBackBar setRelativeContentSize(float size_pct) {
		return null;
	}
}
