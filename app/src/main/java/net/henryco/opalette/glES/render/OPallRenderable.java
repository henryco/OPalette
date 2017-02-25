package net.henryco.opalette.glES.render;

import net.henryco.opalette.glES.camera.OPallCamera2D;

/**
 * Created by HenryCo on 26/02/17.
 */

public interface OPallRenderable {

	void render(OPallCamera2D camera);
	int getWidth();
	int getHeight();
}
