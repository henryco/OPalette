package net.henryco.opalette.application.programs.sub.programs.color;

import android.support.annotation.Nullable;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.GridLines;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.proto.AppMainProto;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class ColorProgram implements AppSubProgram<AppMainProto>, AppSubProtocol {

	private long id = methods.genID(ColorProgram.class);

	private FrameBuffer imageBuffer;
	private EdTexture imageTexture;
	private GridLines gridLines;

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
		request.openRequest(set_filters_enable, () -> gridLines.setVisible(false));
		request.openRequest(set_filters_disable, () -> gridLines.setVisible(true));
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
	public void create(@Nullable GL10 gl, int width, int height, AppMainProto context) {

		if (feedBackListener == null) throw new RuntimeException("FeedBackListener(OPallRequester) == NULL!");

		gridLines = new GridLines(width, height);

		imageBuffer = OPallFBOCreator.FrameBuffer(width, height, false);
		imageTexture = new EdTexture();
		imageTexture.setScreenDim(width, height);

		OPallViewInjector.inject(context.getActivityContext(), new MaxColorControl(imageTexture));
		OPallViewInjector.inject(context.getActivityContext(), new MinColorControl(imageTexture));
		OPallViewInjector.inject(context.getActivityContext(), new BrightnessControl(imageTexture));
		OPallViewInjector.inject(context.getActivityContext(), new AddColorControl(imageTexture));
	}



	@Override
	public void onSurfaceChange(@Nullable GL10 gl, AppMainProto context, int width, int height) {

		gridLines.setScreenDim(width, height);
	}



	@Override
	public void render(@Nullable GL10 gl10, AppMainProto context, Camera2D camera, int w, int h) {
		imageBuffer.beginFBO(() -> imageTexture.render(camera, program -> GLESUtils.clear()));
		imageBuffer.render(camera);
		gridLines.render(camera);
	}



	@Override
	public void setRenderData(OPallRenderable data) {
		if (imageTexture != null) imageTexture.set((Texture) data);
	}

	@Override
	public OPallRenderable getRenderData() {
		return imageBuffer.getTexture();
	}
}
