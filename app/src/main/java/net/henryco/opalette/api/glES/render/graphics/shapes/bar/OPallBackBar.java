package net.henryco.opalette.api.glES.render.graphics.shapes.bar;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 06/03/17.
 */

public interface OPallBackBar {

	void createBar(int scrWidth, int scrHeight);
	void render(Camera2D camera, OPallRenderable renderable, int buffer_quantum);

	OPallBackBar setColor(GLESUtils.Color color);
	OPallBackBar setRelativeSize(float size_pct);
	OPallBackBar setRelativePosition(float pos_pct);
	OPallBackBar setRelativeContentSize(float size_pct);
}
