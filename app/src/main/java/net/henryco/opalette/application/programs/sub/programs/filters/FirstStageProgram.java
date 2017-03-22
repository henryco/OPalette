package net.henryco.opalette.application.programs.sub.programs.filters;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.ConvolveTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.programs.sub.programs.image.TranslationControl;
import net.henryco.opalette.application.proto.AppMainProto;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 21/03/17.
 */

public class FirstStageProgram implements AppSubProgram<AppMainProto>, AppSubProtocol {


	private long id = methods.genID(FirstStageProgram.class);
	private ProxyRenderData<ConvolveTexture> proxyRenderData = new ProxyRenderData<>();
	private FrameBuffer textureBuffer;

	private OPallRequester feedBackListener;




	@Override
	public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(update_proxy_render_state, () -> proxyRenderData.setStateUpdated());
		request.openRequest(set_filters_enable, () -> proxyRenderData.getRenderData().setEnable(true));
		request.openRequest(set_filters_disable, () -> proxyRenderData.getRenderData().setEnable(false));
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

		//m_sharpen, m_sharpen_1, m_sharpen_5 is oks
		if (feedBackListener == null) throw new RuntimeException("FeedBackListener(OPallRequester) == NULL!");
		textureBuffer = OPallFBOCreator.FrameBuffer();
		proxyRenderData.setStateUpdated().getRenderData().setFilterMatrix(ConvolveTexture.matrix.m_sharpen());
		proxyRenderData.getRenderData().setEffectScale(0);
		OPallViewInjector.inject(context.getActivityContext(), new FilterSharpnessControl(proxyRenderData));
		OPallViewInjector.inject(context.getActivityContext(), new TranslationControl(proxyRenderData));
	}

	@Override
	public void onSurfaceChange(GL10 gl, AppMainProto context, int width, int height) {
		textureBuffer.createFBO(width, height, false);
	}

	@Override
	public void render(GL10 gl10, AppMainProto context, Camera2D camera, int w, int h) {

		if (proxyRenderData.stateUpdated()) {
			textureBuffer.beginFBO(() -> proxyRenderData.getRenderData().render(camera, program -> GLESUtils.clear()));
			feedBackListener.sendRequest(new Request(update_proxy_render_state).destination(d -> d.id(this.id + 1)));
		}
	}



	@Override
	public void setRenderData(OPallRenderable data) {
		proxyRenderData.setRenderData((ConvolveTexture) data);
	}

	@Override
	public OPallRenderable getRenderData() {
		return textureBuffer.getTexture();
	}
}
