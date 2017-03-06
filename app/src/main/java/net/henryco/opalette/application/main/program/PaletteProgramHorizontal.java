package net.henryco.opalette.application.main.program;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shapes.ChessBox;
import net.henryco.opalette.api.glES.render.graphics.shapes.bar.BarHorizontal;
import net.henryco.opalette.api.glES.render.graphics.shapes.bar.OPallBar;
import net.henryco.opalette.api.glES.render.graphics.textures.MultiTexture;
import net.henryco.opalette.api.glES.render.graphics.textures.OPallMultiTexture;
import net.henryco.opalette.api.glES.render.graphics.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.geom.OPallGeometry;
import net.henryco.opalette.api.utils.observer.OPallObservator;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.main.ProtoActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */

public class PaletteProgramHorizontal implements OPallUnderProgram<ProtoActivity> {


	public static final int buffer_quantum = 5;
	public static final String VERT_FILE = OPallMultiTexture.DEF_SHADER+".vert";
	public static final String FRAG_FILE = OPallMultiTexture.FRAG_DIR+"/StdPaletteHorizontal.frag";
	public static final String u_dimension = "u_dimension";

	private final long id;

	private Camera2D camera2D;
	private Texture imageTexture;

	private FrameBuffer barImageBuffer;
	private FrameBuffer barSrcBuffer;
	private FrameBuffer imageBuffer;
	private ChessBox chessBox;

	private MultiTexture multiTexture;
	private boolean uCan = false;
	private OPallBar backBar;
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
		backBar = new BarHorizontal(context);
		chessBox = new ChessBox(context);
	}



	@Override
	public final void onSurfaceChange(GL10 gl, ProtoActivity context, int width, int height) {
		camera2D.set(width, height).update();
		barImageBuffer.createFBO(width, height, false).beginFBO(GLESUtils::clear);
		barSrcBuffer.createFBO(width, buffer_quantum, width, height, false).beginFBO(GLESUtils::clear);
		imageBuffer.createFBO(width, height, false).beginFBO(GLESUtils::clear);
		imageTexture.setScreenDim(width, height);
		multiTexture.setScreenDim(width, height);
		backBar.createBar(width, height);
		chessBox.setScreenDim(width, height);

		float[] p1 = new float[]{100,0};
		float[] p2 = new float[]{200, 100};


		float a = OPallGeometry.lineAx(p1, p2);
		float b = OPallGeometry.lineBy(p1, p2);
		float c = OPallGeometry.lineC(p1, p2);

		System.out.println("COEFF: "+a+" | "+b+" | "+c);

	}



	@Override
	public final void onDraw(GL10 gl, ProtoActivity context, int width, int height) {

		//RESET CAMERA
		camera2D.setPosY_absolute(0).update();


		chessBox.render(camera2D);

		if (uCan) {


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



			//RENDER GRADIENT BAR
			backBar.render(camera2D, barImageBuffer, buffer_quantum);



		}


	}


	@Override
	public void setObservator(OPallObservator observator) {
		this.observator = observator;
	}


	@Override
	public long getID() {
		return id;
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






}
