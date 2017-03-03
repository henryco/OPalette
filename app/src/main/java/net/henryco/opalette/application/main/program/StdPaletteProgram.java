package net.henryco.opalette.application.main.program;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.textures.MultiTexture;
import net.henryco.opalette.api.glES.render.graphics.textures.OPallMultiTexture;
import net.henryco.opalette.api.glES.render.graphics.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.Utils;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.main.ProtoActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */

public class StdPaletteProgram implements OPallUnderProgram<ProtoActivity> {


	public static final String VERT_FILE = OPallMultiTexture.DEF_SHADER+".vert";
	public static final String FRAG_FILE = OPallMultiTexture.FRAG_DIR+"/StdPalette.frag";

	private final long id;
	private final int buffer_quantum = 5;
	private final float step = 4.75f;

	private Camera2D camera2D;
	private Texture texture1;

	private FrameBuffer frameBuffer;
	private FrameBuffer onePxBuffer;
	private FrameBuffer twoPxBuffer;


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
		onePxBuffer = OPallFBOCreator.FrameBuffer(context);
		twoPxBuffer = OPallFBOCreator.FrameBuffer(context);
		multiTexture1 = new MultiTexture(context, VERT_FILE, FRAG_FILE, 2);
	}

	@Override
	public void onSurfaceChange(GL10 gl, ProtoActivity context, int width, int height) {
		camera2D.set(width, height).update();
		frameBuffer.createFBO(width, height, false).beginFBO(GLESUtils::clear);
		onePxBuffer.createFBO(width, buffer_quantum, width, height, false).beginFBO(() -> GLESUtils.clear(GLESUtils.Color.PINK));
		twoPxBuffer.createFBO(width, height, false).beginFBO(GLESUtils::clear);
		texture1.setScreenDim(width, height);
		multiTexture1.setScreenDim(width, height);

	}


	private boolean uCan = false;

	@Override
	public void onDraw(GL10 gl, ProtoActivity context, int width, int height) {

		GLESUtils.clear();

		if (uCan) {

			camera2D.setPosY_absolute(0).update();
			//texture1.setSize(ww, hh);

			twoPxBuffer.beginFBO(() -> texture1.render(camera2D));
			twoPxBuffer.render(camera2D);
			multiTexture1.setTexture(0, twoPxBuffer.getTexture());
			multiTexture1.setTexture(1, onePxBuffer.getTexture());
			multiTexture1.setFocusOn(1);

			//*

			frameBuffer.beginFBO(() -> {
				GLESUtils.clear(0,0,0,0);
				multiTexture1.render(camera2D, program
						-> GLES20.glUniform2f(GLES20.glGetUniformLocation(program, "u_dimension"), width, height)
				);
			});
			camera2D.setPosY_absolute(-1.6f).update();


			float time = Utils.secTimer(() -> drawBar(frameBuffer, camera2D, 75, buffer_quantum, step));


			System.out.println("<<<"+time+">>>");

			//*/

		}
	}

	private int ww = 0;
	private int hh = 0;

	@Override
	public void acceptRequest(Request request) {
		request.openRequest("loadImage", () -> {
			texture1.setBitmap(request.getData());
			multiTexture1.setTexture(0, texture1);
			multiTexture1.setTexture(1, onePxBuffer.getTexture());
			multiTexture1.setFocusOn(1);
			ww = ((Bitmap) request.getData()).getWidth();
			hh = ((Bitmap) request.getData()).getHeight();

			uCan = true;
		});
	}

	@Override
	public long getID() {
		return id;
	}




	private void drawBar(OPallRenderable renderable, Camera2D camera2D, int barHeight, int buffer_quantum, float step) {

		int loss = Math.round((buffer_quantum - step) * barHeight);
		int it = Math.max(Math.round((barHeight + loss) / buffer_quantum), buffer_quantum);
		for (int i = 0; i < it; i++) renderable.render(camera2D.translateY(-step).update());
	}

}
