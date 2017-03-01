package net.henryco.opalette.application.main.program;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.main.ProtoActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */

public class StdPaletteProgram implements OPallUnderProgram<ProtoActivity> {


	private final long id;


	private static long genID() {
		return (long) (System.currentTimeMillis() * 0.5 - 1);
	}


	private Camera2D camera2D;
	private Texture texture1;
	private FrameBuffer frameBuffer;



	public StdPaletteProgram(){
		this(genID());
	}
	public StdPaletteProgram(long id){
		this.id = id;
	}




	@Override
	public void create(GL10 gl, int width, int height, ProtoActivity context) {
		camera2D = new Camera2D(width, height, true);
		texture1 = new Texture(context);
		frameBuffer = new FrameBuffer(width, height, false).setTargetTexture(new Texture(context));
	}

	@Override
	public void onSurfaceChange(GL10 gl, ProtoActivity context, int width, int height) {
		camera2D.set(width, height).update();
		frameBuffer.createFBO(width, height, false);
	}

	@Override
	public void onDraw(GL10 gl, ProtoActivity context) {

		frameBuffer.beginFBO(() -> {
			camera2D.update();
			GLESUtils.clear(GLESUtils.Color.PIKNY);
			texture1.render(camera2D);

		}).render(camera2D);

	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest("loadImage", () -> texture1.setBitmap(request.getData()));
	}

	@Override
	public long getID() {
		return id;
	}
}
