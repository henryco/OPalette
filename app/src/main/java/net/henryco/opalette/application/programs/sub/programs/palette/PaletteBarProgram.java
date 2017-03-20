package net.henryco.opalette.application.programs.sub.programs.palette;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.glES.render.graphics.units.bar.BarHorizontal;
import net.henryco.opalette.api.glES.render.graphics.units.bar.OPallBar;
import net.henryco.opalette.api.glES.render.graphics.units.palette.CellPaletter;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.proto.AppMainProto;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class PaletteBarProgram implements AppSubProgram<AppMainProto>, AppSubProtocol {

	private long id = methods.genID(PaletteBarProgram.class);
	private ProxyRenderData<Texture> proxyRenderData = new ProxyRenderData<>();


	private CellPaletter cellPaletter;
	private OPallBar backBar;
	private int buffer_quantum = 5;
	private OPallRequester feedBackListener;


	@Override
	public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(set_buffer_quantum, () -> this.buffer_quantum = request.getData());
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
	public void create(GL10 gl, int width, int height, AppMainProto context) {

		cellPaletter = new CellPaletter();
		backBar = new BarHorizontal();
		sendBarHeightInfo();
	}

	@Override
	public void onSurfaceChange(GL10 gl, AppMainProto context, int width, int height) {

		backBar.createBar(width, height);
		cellPaletter.setScreenDim(width, height);
		sendBarHeightInfo();
	}

	@Override
	public void render(GL10 gl10, AppMainProto context, Camera2D camera, int w, int h) {

		cellPaletter.generate(getRenderData(), camera);
		backBar.render(camera, cellPaletter, buffer_quantum);
	}


	@Override
	public void setRenderData(OPallRenderable data) {
		proxyRenderData.setRenderData((Texture) data);
	}

	@Override
	public Texture getRenderData() {
		return proxyRenderData.getRenderData();
	}

	private void sendBarHeightInfo() {
		feedBackListener.sendRequest(new Request(send_back_bar_height, backBar.getHeight()));
	}
}
