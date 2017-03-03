package net.henryco.opalette.application.main.program;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.textures.MultiTexture;
import net.henryco.opalette.api.glES.render.graphics.textures.OPallMultiTexture;
import net.henryco.opalette.api.glES.render.graphics.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;
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
	private Texture imageTexture;

	private FrameBuffer barImageBuffer;
	private FrameBuffer barSrcBuffer;
	private FrameBuffer imageBuffer;


	private MultiTexture multiTexture;
	private boolean uCan = false;


	public StdPaletteProgram(){
		this(OPallUnderProgram.methods.genID());
	}
	public StdPaletteProgram(long id){
		this.id = id;
	}




	@Override
	public void create(GL10 gl, int width, int height, ProtoActivity context) {
		camera2D = new Camera2D(width, height, true);
		imageTexture = new Texture(context);
		barImageBuffer = OPallFBOCreator.FrameBuffer(context);
		barSrcBuffer = OPallFBOCreator.FrameBuffer(context);
		imageBuffer = OPallFBOCreator.FrameBuffer(context);
		multiTexture = new MultiTexture(context, VERT_FILE, FRAG_FILE, 2);
	}



	@Override
	public void onSurfaceChange(GL10 gl, ProtoActivity context, int width, int height) {
		camera2D.set(width, height).update();
		barImageBuffer.createFBO(width, height, false).beginFBO(GLESUtils::clear);
		barSrcBuffer.createFBO(width, buffer_quantum, width, height, false).beginFBO(() -> GLESUtils.clear(GLESUtils.Color.PINK));
		imageBuffer.createFBO(width, height, false).beginFBO(GLESUtils::clear);
		imageTexture.setScreenDim(width, height);
		multiTexture.setScreenDim(width, height);

	}



	@Override
	public void onDraw(GL10 gl, ProtoActivity context, int width, int height) {

		GLESUtils.clear();

		if (uCan) {

			camera2D.setPosY_absolute(0).update();
			prepareTexture();


			barImageBuffer.beginFBO(() -> {
				GLESUtils.clear(0,0,0,0);
				multiTexture.render(camera2D, program
						-> GLES20.glUniform2f(GLES20.glGetUniformLocation(program, "u_dimension"), width, height)
				);
			});



			camera2D.setPosY_absolute(-1.6f).update();
			drawBar(barImageBuffer, camera2D, 75, buffer_quantum, step);



		}
	}




	private void prepareTexture() {
		imageBuffer.beginFBO(() -> imageTexture.render(camera2D));
		imageBuffer.render(camera2D);
		multiTexture.setTexture(0, imageBuffer.getTexture());
		multiTexture.setTexture(1, barSrcBuffer.getTexture());
		multiTexture.setFocusOn(1);
	}



	private void drawBar(OPallRenderable renderable, Camera2D camera2D, int barHeight, int buffer_quantum, float step) {

		int loss = Math.round((buffer_quantum - step) * barHeight);
		int it = Math.max(Math.round((barHeight + loss) / buffer_quantum), buffer_quantum);
		for (int i = 0; i < it; i++) renderable.render(camera2D.translateY(-step).update());
	}






	@Override
	public void acceptRequest(Request request) {
		request.openRequest("loadImage", () -> {

			imageTexture.setBitmap(request.getData());
			float bmpWidth = ((Bitmap)request.getData()).getWidth();
			float barWidth = barSrcBuffer.getWidth();
			imageTexture.bounds(b -> b.setScale(barWidth / bmpWidth));

			multiTexture.setTexture(0, imageTexture);
			multiTexture.setTexture(1, barSrcBuffer.getTexture());
			multiTexture.setFocusOn(1);

			uCan = true;
		});
	}

	@Override
	public long getID() {
		return id;
	}




}
