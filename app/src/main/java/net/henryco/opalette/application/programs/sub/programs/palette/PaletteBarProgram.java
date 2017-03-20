package net.henryco.opalette.application.programs.sub.programs.palette;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.glES.render.graphics.units.bar.BarHorizontal;
import net.henryco.opalette.api.glES.render.graphics.units.bar.OPallBar;
import net.henryco.opalette.api.glES.render.graphics.units.palette.CellPaletter;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.MainActivity;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class PaletteBarProgram implements AppSubProgram<MainActivity>, AppSubProtocol {

	private long id = methods.genID(PaletteBarProgram.class);


	private CellPaletter cellPaletter;
	private OPallBar backBar;

	private Texture externalRenderSource;

	private int buffer_quantum = 5;

	private OPallRequester feedBackListener;


	@Override
	public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(set_buffer_quantum, () -> this.buffer_quantum = request.getData());
		request.openRequest(send_bar_gradient_buffer, () -> this.externalRenderSource = request.getData());
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public void create(GL10 gl, int width, int height, MainActivity context) {

		cellPaletter = new CellPaletter();
		backBar = new BarHorizontal();
		senBarHeightInfo();
	}

	@Override
	public void onSurfaceChange(GL10 gl, MainActivity context, int width, int height) {

		backBar.createBar(width, height);
		cellPaletter.setScreenDim(width, height);
		senBarHeightInfo();
	}

	@Override
	public void render(GL10 gl10, MainActivity context, Camera2D camera, int w, int h) {

		cellPaletter.generate(externalRenderSource, camera);
		backBar.render(camera, cellPaletter, buffer_quantum);
	}


	private void senBarHeightInfo() {
		feedBackListener.sendRequest(new Request(send_back_bar_height, backBar.getHeight()));
	}
}
