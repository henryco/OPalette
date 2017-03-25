package net.henryco.opalette.application.programs.sub.programs.canvas;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.programs.sub.programs.aImage.ImageProgram;
import net.henryco.opalette.application.proto.AppMainProto;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 25/03/17.
 */

public class CanvasProgram implements AppSubProgram<AppMainProto>, AppSubProtocol {


	private long id = methods.genID(ImageProgram.class);
	private ProxyRenderData<OPallRenderable> proxyRenderData = new ProxyRenderData<>();


	private OPallRequester feedBackListener;
	private AppSubProgramHolder holder;
	private final float[] defDim = {0,0};
	private final float[] translation = {0,0};

	@Override
	public void acceptRequest(Request request) {

	}



	@Override
	public void create(GL10 gl, int width, int height, AppMainProto context) {

		OPallViewInjector.inject(context.getActivityContext(), new CanvasSizeControl(width, height, feedBackListener));
		defDim[0] = width;
		defDim[1] =height;
	}

	@Override
	public void onSurfaceChange(GL10 gl, AppMainProto context, int width, int height) {

		synchronized (this) {
			proxyRenderData.setStateUpdated();
			translation[0] = (defDim[0] - width);// - translation[0];
			translation[1] = (defDim[1] - height);// - translation[1];
		}
	}

	@Override
	public void render(GL10 gl10, AppMainProto context, Camera2D camera, int w, int h) {
		if (proxyRenderData.stateUpdated()) {
			//
		}
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
	public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override
	public void setProgramHolder(AppSubProgramHolder holder) {
		this.holder = holder;
	}

	@Override
	public void setRenderData(OPallRenderable data) {
		this.proxyRenderData.setRenderData(data);
	}

	@Override
	public OPallRenderable getRenderData() {
		return proxyRenderData.getRenderData();
	}
}
