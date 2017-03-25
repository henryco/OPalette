package net.henryco.opalette.application.programs.sub.programs.line;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.TouchLines;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.proto.AppMainProto;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class ShapeLinesProgram implements AppSubProgram<AppMainProto>, AppSubProtocol {

	private long id = methods.genID(ShapeLinesProgram.class);
	private ProxyRenderData<OPallRenderable> proxyRenderData = new ProxyRenderData<>();

	private TouchLines touchLines;
	private OPallRequester feedBackListener;
	private AppSubProgramHolder holder;

	@Override
	public void setProgramHolder(AppSubProgramHolder holder) {
		this.holder = holder;
	}

	@Override
	public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(update_proxy_render_state, () -> proxyRenderData.setStateUpdated());
		request.openRequest(set_touch_lines_def_size, () -> {
			touchLines.reset();
			sendCoeffInfo();
		});
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
		touchLines = new TouchLines(width, height);
		sendCoeffInfo();
	}

	@Override
	public void onSurfaceChange(GL10 gl, AppMainProto context, int width, int height) {
		touchLines.setScreenDim(width, height);
		proxyRenderData.setStateUpdated();
		sendCoeffInfo();
	}

	@Override
	public void render(GL10 gl10, AppMainProto context, Camera2D camera, int w, int h) {
		touchLines.render(camera);
	}




	@Override
	public void setRenderData(OPallRenderable data) {
		proxyRenderData.setRenderData(data);
	}

	@Override
	public OPallRenderable getRenderData() {
		return proxyRenderData.getRenderData();
	}

	private void sendCoeffInfo() {
		feedBackListener.sendRequest(new Request(send_line_coeffs, touchLines.getCoefficients()));
	}
}
