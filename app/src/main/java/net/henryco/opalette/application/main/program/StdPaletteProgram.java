package net.henryco.opalette.application.main.program;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.textures.MultiTexture;
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


	private Camera2D camera2D;
	private Texture texture1;

	private FrameBuffer frameBuffer;
	private FrameBuffer slimBuffer;

	private MultiTexture multiTexture1;


	public StdPaletteProgram(){
		this(OPallUnderProgram.methods.genID());
	}
	public StdPaletteProgram(long id){
		this.id = id;
	}




	@Override
	public void create(GL10 gl, int width, int height, ProtoActivity context) {
		camera2D = new Camera2D(width, height, true);
		texture1 = new Texture(context);
		frameBuffer = OPallFBOCreator.FrameBuffer(context);
		slimBuffer = OPallFBOCreator.FrameBuffer(context);
		multiTexture1 = new MultiTexture(context, 2);
	}

	@Override
	public void onSurfaceChange(GL10 gl, ProtoActivity context, int width, int height) {
		camera2D.set(width, height).update();
		frameBuffer.createFBO(width, height, false).beginFBO(() -> GLESUtils.clear(GLESUtils.Color.BLUE));
		slimBuffer.createFBO(width, 200, width, height, false).beginFBO(() -> GLESUtils.clear(GLESUtils.Color.RED));
		texture1.setScreenDim(width, height);
		multiTexture1.setScreenDim(width, height);


	}

	@Override
	public void onDraw(GL10 gl, ProtoActivity context) {



		multiTexture1.render(camera2D.update());


	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest("loadImage", () -> {
			texture1.setBitmap(request.getData());
			multiTexture1.setTexture(0, texture1);
			multiTexture1.setTexture(1, slimBuffer.getTexture());
			multiTexture1.setFocusOn(1);
		});
	}

	@Override
	public long getID() {
		return id;
	}
}
