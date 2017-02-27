package net.henryco.opalette.api.glES.render;

import net.henryco.opalette.api.glES.camera.OPallCamera2D;

/**
 * Created by HenryCo on 26/02/17.
 */

public interface OPallRenderable {

	void render(OPallCamera2D camera);
	int getWidth();
	int getHeight();
}
