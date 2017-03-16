package net.henryco.opalette.application.programs.sub;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.TouchLines;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.activities.MainActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class TouchLinesProgram implements AppSubProgram<MainActivity, Texture>, AppSubProtocol {

	private final static long id = methods.genID(TouchLinesProgram.class);

	private TouchLines touchLines;


	@Override
	public void acceptRequest(Request request) {

	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void create(GL10 gl, int width, int height, MainActivity context) {
		touchLines = new TouchLines(width, height);

	}

	@Override
	public void onSurfaceChange(GL10 gl, MainActivity context, int width, int height) {
		touchLines.setScreenDim(width, height);
	}

	@Override
	public void render(GL10 gl10, MainActivity context, Camera2D camera, int w, int h) {
		touchLines.render(camera);
	}

}
