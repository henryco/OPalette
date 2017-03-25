package net.henryco.opalette.application.programs.sub.programs.aImage;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.ConvolveTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.proto.AppMainProto;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 21/03/17.
 */

public class ImageProgram implements AppSubProgram<AppMainProto>, AppSubProtocol {



	private long id = methods.genID(ImageProgram.class);
	private ProxyRenderData<ConvolveTexture> proxyRenderData = new ProxyRenderData<>();
	private FrameBuffer textureBuffer;


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
		request.openRequest(set_filters_enable, () -> proxyRenderData.getRenderData().setFilterEnable(true));
		request.openRequest(set_filters_disable, () -> proxyRenderData.getRenderData().setFilterEnable(false));
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

		if (feedBackListener == null) throw new RuntimeException("FeedBackListener(OPallRequester) == NULL!");

		textureBuffer = OPallFBOCreator.FrameBuffer(width, height, false);
		proxyRenderData.setRenderData(new ConvolveTexture());
		proxyRenderData.getRenderData().setScreenDim(width, height);
		proxyRenderData.setStateUpdated().getRenderData().setFilterMatrix(ConvolveTexture.matrix.m_sharpen1());
		proxyRenderData.getRenderData().setEffectScale(0);
		proxyRenderData.getRenderData().setScreenDim(width, height);

		OPallViewInjector.inject(context.getActivityContext(), new FilterSharpnessControl(proxyRenderData));
		OPallViewInjector.inject(context.getActivityContext(), new RotationControl(proxyRenderData));
		OPallViewInjector.inject(context.getActivityContext(), new TranslationControl(proxyRenderData));
	}




	@Override
	public void onSurfaceChange(GL10 gl, AppMainProto context, int width, int height) {
		proxyRenderData.setStateUpdated();
	}



	@Override
	public void render(GL10 gl10, AppMainProto context, Camera2D camera, int w, int h) {

		if (proxyRenderData.stateUpdated()) {
			boolean e = proxyRenderData.getRenderData().isFilterEnable();
			feedBackListener.sendRequest(new Request(e ? set_filters_enable : set_filters_disable).destination(d -> d.except(id)));
			feedBackListener.sendRequest(new Request(update_proxy_render_state).destination(d -> d.except(id)));
			textureBuffer.beginFBO(() -> proxyRenderData.getRenderData().render(camera, program -> GLESUtils.clear()));
		}
		textureBuffer.render(camera);
	}



	@Override
	public void setRenderData(OPallRenderable data) {
		if (data != null && data instanceof Texture) {
			proxyRenderData.getRenderData().set((Texture) data);
			proxyRenderData.setStateUpdated();
		}
	}

	@Override
	public OPallRenderable getRenderData() {
		return textureBuffer.getTexture();
	}
}
