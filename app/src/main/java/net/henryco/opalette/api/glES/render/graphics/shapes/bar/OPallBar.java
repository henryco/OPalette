package net.henryco.opalette.api.glES.render.graphics.shapes.bar;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 06/03/17.
 */

public interface OPallBar {

	void createBar(int scrWidth, int scrHeight);
	void render(Camera2D camera, OPallRenderable renderable, int buffer_quantum);

	OPallBar setColor(GLESUtils.Color color);
	OPallBar setRelativeSize(float size_pct);
	OPallBar setRelativePosition(float pos_pct);
	OPallBar setRelativeContentSize(float size_pct);
}
