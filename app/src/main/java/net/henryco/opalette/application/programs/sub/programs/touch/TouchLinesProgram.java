package net.henryco.opalette.application.programs.sub.programs.touch;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.TouchLines;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.MainActivity;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class TouchLinesProgram implements AppSubProgram<MainActivity>, AppSubProtocol {

	private final static long id = methods.genID(TouchLinesProgram.class);

	private TouchLines touchLines;

	private OPallRequester feedBackListener;


	@Override
	public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(set_touch_lines_def_size, () -> {
			float w = request.getData(0);
			float h = request.getData(1);
			touchLines.setDefaultSize(w, h).setVisible(true).reset();
		});
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void create(GL10 gl, int width, int height, MainActivity context) {
		touchLines = new TouchLines(width, height);
		sendCoeffInfo();
	}

	@Override
	public void onSurfaceChange(GL10 gl, MainActivity context, int width, int height) {
		touchLines.setScreenDim(width, height);
		sendCoeffInfo();
	}

	@Override
	public void render(GL10 gl10, MainActivity context, Camera2D camera, int w, int h) {
		touchLines.render(camera);
	}

	@Override
	public void setObservator(OPallUpdObserver observator) {

	}

	private void sendCoeffInfo() {
		feedBackListener.sendRequest(new Request(send_line_coeffs, touchLines.getCoefficients()));
	}
}
