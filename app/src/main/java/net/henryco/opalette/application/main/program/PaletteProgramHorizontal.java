package net.henryco.opalette.application.main.program;

import android.content.Context;
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
import net.henryco.opalette.api.utils.observer.OPallObservator;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.main.ProtoActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */

public class PaletteProgramHorizontal implements OPallUnderProgram<ProtoActivity> {


	public static final int buffer_quantum = 5;
	public static final float step = 4.75f;



	public static final class BackBar {

		public GLESUtils.Color color = GLESUtils.Color.WHITE;


		public float height_pct = 0.2f;
		public float yPos_pct = 0.8f;
		public float cellHeight_pct = 0.65f;


		private FrameBuffer buffer;

		public BackBar(Context context) {
			buffer = OPallFBOCreator.FrameBuffer(context);
		}

		public void createBar(int scrWidth, int scrHeight, int height, GLESUtils.Color color) {
			buffer.createFBO(scrWidth, height, scrWidth, scrHeight, false);
			buffer.beginFBO(() -> GLESUtils.clear(color));
		}

		public void createBar(int scrWidth, int scrHeight) {
			createBar(scrWidth, scrHeight, (int) (scrHeight * height_pct), color);
		}

		public void render(Camera2D camera, OPallRenderable renderable) {
			float[] camYPos = camera.getPosition();
			buffer.render(camera.setPosY_absolute(-2 * yPos_pct).update());
			float cellHeight = (float)buffer.getHeight() * cellHeight_pct;
			float cellPtc = ((float)buffer.getHeight() - cellHeight) / (float) buffer.getScreenHeight();
			float margin = cellPtc * 0.5f;

			camera.translateY_absolute(-margin).update();
			drawBar(renderable, camera, (int) cellHeight, buffer_quantum, step);

			camera.setPosition(camYPos).update();

		}

		private void drawBar(OPallRenderable barLine, Camera2D camera2D, int barHeight, int buffer_quantum, float step) {
			int loss = Math.round((buffer_quantum - step) * barHeight);
			int it = Math.max(Math.round((barHeight + loss) / buffer_quantum), buffer_quantum);
			for (int i = 0; i < it; i++) barLine.render(camera2D.translateY(-step).update());
		}
	}





	public static final String VERT_FILE = OPallMultiTexture.DEF_SHADER+".vert";
	public static final String FRAG_FILE = OPallMultiTexture.FRAG_DIR+"/StdPaletteHorizontal.frag";
	public static final String u_dimension = "u_dimension";

	private final long id;


	private Camera2D camera2D;
	private Texture imageTexture;

	private FrameBuffer barImageBuffer;
	private FrameBuffer barSrcBuffer;
	private FrameBuffer imageBuffer;

	private MultiTexture multiTexture;
	private boolean uCan = false;
	private BackBar backBar;
	private final float[] bitmap_size = {0,0};

	private OPallObservator observator;

	public PaletteProgramHorizontal(){
		this(OPallUnderProgram.methods.genID());
	}
	public PaletteProgramHorizontal(long id){
		this.id = id;
	}




	@Override
	public final void create(GL10 gl, int width, int height, ProtoActivity context) {
		System.out.println("OpenGL version is: "+ GLES20.glGetString(GLES20.GL_VERSION));
		System.out.println("GLSL version is: "+ GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION));
		camera2D = new Camera2D(width, height, true);
		imageTexture = new Texture(context);
		barImageBuffer = OPallFBOCreator.FrameBuffer(context);
		barSrcBuffer = OPallFBOCreator.FrameBuffer(context);
		imageBuffer = OPallFBOCreator.FrameBuffer(context);
		multiTexture = new MultiTexture(context, VERT_FILE, FRAG_FILE, 2);
		backBar = new BackBar(context);
	}



	@Override
	public final void onSurfaceChange(GL10 gl, ProtoActivity context, int width, int height) {
		camera2D.set(width, height).update();
		barImageBuffer.createFBO(width, height, false).beginFBO(GLESUtils::clear);
		barSrcBuffer.createFBO(width, buffer_quantum, width, height, false).beginFBO(() -> GLESUtils.clear(GLESUtils.Color.PINK));
		imageBuffer.createFBO(width, height, false).beginFBO(GLESUtils::clear);
		imageTexture.setScreenDim(width, height);
		multiTexture.setScreenDim(width, height);
		backBar.createBar(width, height);
	}



	@Override
	public final void onDraw(GL10 gl, ProtoActivity context, int width, int height) {


		GLESUtils.clear(GLESUtils.Color.SILVER);

		if (uCan) {

			//RESET CAMERA
			camera2D.setPosY_absolute(0).update();



			//PREPARE TEXTURE
			imageBuffer.beginFBO(() -> {
				GLESUtils.clear(GLESUtils.Color.TRANSPARENT);
				imageTexture.render(camera2D);
			});
			imageBuffer.render(camera2D);
			multiTexture.setTexture(0, imageBuffer.getTexture());
			multiTexture.setTexture(1, barSrcBuffer.getTexture());
			multiTexture.setFocusOn(1);



			//CREATE GRADIENT BAR
			barImageBuffer.beginFBO(() -> {
				GLESUtils.clear(GLESUtils.Color.TRANSPARENT);
				multiTexture.render(camera2D, program -> {
					GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), width, height);
				});
			});


			backBar.render(camera2D, barImageBuffer);



		}


	}


	@Override
	public void setObservator(OPallObservator observator) {
		this.observator = observator;
	}


	@Override
	public void acceptRequest(Request request) {
		request.openRequest("loadImage", () -> {


			imageTexture.setBitmap(request.getData());
			float bmpWidth = ((Bitmap)request.getData()).getWidth();
			float bmpHeight = ((Bitmap)request.getData()).getHeight();
			float barWidth = barSrcBuffer.getWidth();
			float scale = barWidth / bmpWidth;
			imageTexture.bounds(b -> b.setScale(scale));

			multiTexture.setTexture(0, imageTexture);
			multiTexture.setTexture(1, barSrcBuffer.getTexture());
			multiTexture.setFocusOn(1);
			bitmap_size[0] = bmpWidth * scale;
			bitmap_size[1] = bmpHeight * scale;
			uCan = true;

		});
	}

	@Override
	public long getID() {
		return id;
	}




}
