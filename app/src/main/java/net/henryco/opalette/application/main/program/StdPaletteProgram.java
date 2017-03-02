package net.henryco.opalette.application.main.program;

import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
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


	private Camera2D camera2D;
	private Texture texture1;

	private FrameBuffer frameBuffer;
	private FrameBuffer onePxBuffer;

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
		multiTexture1 = new MultiTexture(context, VERT_FILE, FRAG_FILE, 2);
	}

	@Override
	public void onSurfaceChange(GL10 gl, ProtoActivity context, int width, int height) {
		camera2D.set(width, height).update();
		frameBuffer.createFBO(width, height, false).beginFBO(GLESUtils::clear);
		onePxBuffer.createFBO(width, 2, width, height, false).beginFBO(() -> GLESUtils.clear(GLESUtils.Color.RED));
		texture1.setScreenDim(width, height);
		multiTexture1.setScreenDim(width, height);


	}


	private boolean uCan = false;

	@Override
	public void onDraw(GL10 gl, ProtoActivity context, int width, int height) {

		draw1(width, height);

		if (uCan) {
			String out = "";
			long t = 0;

			for (int i = 0; i < 100; i++) {
				GLESUtils.clear();


				long time = draw1(width, height);


				String o = "DRAW 2 | I: "+i+" | time: "+time+" ns\n";
				System.out.println(o);
				out += o;

				t += time;
			}

			long av = t / 100;
			String o = "Average: " + av + "ns\n";
			out += o;
			System.out.println(o);

			uCan = false;
		}




	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest("loadImage", () -> {
			texture1.setBitmap(request.getData());
			multiTexture1.setTexture(0, texture1);
			multiTexture1.setTexture(1, onePxBuffer.getTexture());
			multiTexture1.setFocusOn(1);
			uCan = true;
		});
	}

	@Override
	public long getID() {
		return id;
	}




	private long draw1(int width, int height) {
		long t0 = System.nanoTime();
		frameBuffer.beginFBO(()
				-> multiTexture1.render(camera2D.setPosY_absolute(0).update(), program
				-> GLES20.glUniform2f(GLES20.glGetUniformLocation(program, "u_dimension"), width, height)
		));
		camera2D.setPosY_absolute(-1).update();
		for (int i = 0; i < 100; i++) frameBuffer.render(camera2D.translateY(-1).update());
		return System.nanoTime() - t0;
	}

	private long draw2(int width, int height) {
		long t0 = System.nanoTime();
		frameBuffer.beginFBO(()
				-> multiTexture1.render(camera2D.setPosY_absolute(0).update(), program
				-> GLES20.glUniform2f(GLES20.glGetUniformLocation(program, "u_dimension"), width, height)
		));
		frameBuffer.render(camera2D.setPosY_absolute(-1f).update());
		return System.nanoTime() - t0;
	}
}
