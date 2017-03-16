package net.henryco.opalette.application.programs.sub.programs;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.activities.MainActivity;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class ImageProgram implements AppSubProgram<MainActivity>, AppSubProtocol {

	private final static long id = methods.genID(ImageProgram.class);

	private FrameBuffer imageBuffer;
	private EdTexture imageTexture;

	private OPallRequester feedBackListener;

	@Override
	public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(set_first_image, () -> {
			imageTexture.setBitmap(request.getData());
			float scrWidth = imageTexture.getScreenWidth();
			float w = imageTexture.getWidth();
			float h = imageTexture.getHeight();
			float scale = scrWidth / w;
			imageTexture.bounds(b -> b.setScale(scale));

			feedBackListener.sendRequest(new Request(set_touch_lines_def_size, scrWidth, h * scale));
		});
	}

	@Override
	public long getID() {
		return id;
	}




	@Override
	public void create(GL10 gl, int width, int height, MainActivity context) {

		imageBuffer = OPallFBOCreator.FrameBuffer();
		imageTexture = new EdTexture();
		imageTexture.setScreenDim(width, height);
	}



	@Override
	public void onSurfaceChange(GL10 gl, MainActivity context, int width, int height) {

		imageBuffer.createFBO(width, height, false);
		imageTexture.setScreenDim(width, height);
	}



	@Override
	public void render(GL10 gl10, MainActivity context, Camera2D camera, int w, int h) {

		imageBuffer.beginFBO(() -> imageTexture.render(camera, program -> GLESUtils.clear()));
		imageBuffer.render(camera);
		feedBackListener.sendRequest(new Request(send_first_image_buffer, imageBuffer.getTexture()));
	}


}
