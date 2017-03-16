package net.henryco.opalette.application.programs.sub;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.glES.render.graphics.units.bar.BarHorizontal;
import net.henryco.opalette.api.glES.render.graphics.units.bar.OPallBar;
import net.henryco.opalette.api.glES.render.graphics.units.palette.CellPaletter;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.activities.MainActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class PaletteBarProgram implements AppSubProgram<MainActivity, Texture>, AppSubProtocol {

	private static final long id = methods.genID(PaletteBarProgram.class);


	private CellPaletter cellPaletter;
	private OPallBar backBar;

	private Texture externalRenderSource;

	private int buffer_quantum = 5;

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(set_buffer_quantum, () -> this.buffer_quantum = request.getData());
		request.openRequest(set_palette_bar_source, () -> this.externalRenderSource = request.getData());
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void create(GL10 gl, int width, int height, MainActivity context) {

		cellPaletter = new CellPaletter();
		backBar = new BarHorizontal();
	}

	@Override
	public void onSurfaceChange(GL10 gl, MainActivity context, int width, int height) {

		backBar.createBar(width, height);
		cellPaletter.setScreenDim(width, height);
	}

	@Override
	public void render(GL10 gl10, MainActivity context, Camera2D camera, int w, int h) {

		cellPaletter.generate(externalRenderSource, camera);
		backBar.render(camera, cellPaletter, buffer_quantum);
	}


}
