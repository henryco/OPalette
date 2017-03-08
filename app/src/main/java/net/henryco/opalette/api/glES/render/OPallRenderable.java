package net.henryco.opalette.api.glES.render;

import net.henryco.opalette.api.glES.camera.Camera2D;

/**
 * Created by HenryCo on 26/02/17.
 */

public interface OPallRenderable {

	void render(Camera2D camera);
	void setScreenDim(float w, float h);
	int getWidth();
	int getHeight();
}
