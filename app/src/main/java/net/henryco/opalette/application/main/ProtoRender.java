package net.henryco.opalette.application.main;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUniRenderer;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.requester.Request;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 27/02/17.
 */

public class ProtoRender extends OPallUniRenderer<ProtoActivity> {


	private Camera2D camera2D;
	private Texture texture1;
	private FrameBuffer frameBuffer;

	public ProtoRender(ProtoActivity context) {
		super(context);
	}
	public ProtoRender(ProtoActivity context, int id) {
		super(context, id);
	}


	@Override
	protected void onSurfaceCreated(GL10 gl, EGLConfig config, int width, int height, ProtoActivity context) {

		camera2D = new Camera2D(width, height, true);
		texture1 = new Texture(context);
		frameBuffer = new FrameBuffer(width, height, false).setTargetTexture(new Texture(context));
	}





	@Override
	protected void onSurfaceChanged(GL10 gl, int width, int height, ProtoActivity context) {

		camera2D.set(width, height).update();
		frameBuffer.createFBO(width, height, false);
	}




	@Override
	protected void onDrawFrame(GL10 gl, ProtoActivity context) {

		frameBuffer.beginFBO(() -> {

			camera2D.update();
			GLESUtils.clear(GLESUtils.Color.PIKNY);
			texture1.render(camera2D);

		}).render(camera2D);


	}





	@Override
	protected void acceptRequest(Request request, ProtoActivity context) {
		request.openRequest("LoadImage", ()
				-> addToGLContextQueue((gl10, protoActivity)
				-> texture1.setBitmap(request.getData())).forceUpDate()
		);


	}


}
