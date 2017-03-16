package net.henryco.opalette.application.programs;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.ChessBox;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.observer.OPallObserver;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.api.utils.requester.RequestSender;
import net.henryco.opalette.application.activities.MainActivity;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.programs.sub.programs.GradientBarProgram;
import net.henryco.opalette.application.programs.sub.programs.ImageProgram;
import net.henryco.opalette.application.programs.sub.programs.PaletteBarProgram;
import net.henryco.opalette.application.programs.sub.programs.TouchLinesProgram;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */

public class ProgramPipeLine implements OPallUnderProgram<MainActivity>, AppSubProtocol, MainActivity.AppMainProtocol {


	private final long id;
	private boolean uCan = false;

	private Camera2D camera2D;
	private ChessBox chessBox;

	private OPallObserver observator;
	private RequestSender requestSender;
	private List<AppSubProgram<MainActivity>> subPrograms;

	@SuppressWarnings("unchecked")
	public static AppSubProgram<MainActivity>[] getDefaultPipeLineArray() {
		return new AppSubProgram[] {
				new ImageProgram(),
				new GradientBarProgram(),
				new TouchLinesProgram(),
				new PaletteBarProgram()
		};
	}


	public ProgramPipeLine(){
		this(OPallUnderProgram.methods.genID());
	}
	public ProgramPipeLine(long id){

		this.id = id;

		subPrograms = new ArrayList<>();
		requestSender = new RequestSender();

		AppSubProgram<MainActivity>[] pipeLine = getDefaultPipeLineArray();
		for (AppSubProgram<MainActivity> abs : pipeLine){
			requestSender.addRequestListener(abs);
			abs.setFeedBackListener(requestSender);
			subPrograms.add(abs);
		}

	}





	@Override
	public final void create(GL10 gl, int width, int height, MainActivity context) {

		System.out.println("OpenGL version is: "+ GLES20.glGetString(GLES20.GL_VERSION));
		System.out.println("GLSL version is: "+ GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION));

		FrameBuffer.debug = true;

		camera2D = new Camera2D(width, height, true);
		chessBox = new ChessBox();

		for (AppSubProgram<MainActivity> asp : subPrograms) {
			asp.create(gl, width, height, context);
		}

	}



	@Override
	public final void onSurfaceChange(GL10 gl, MainActivity context, int width, int height) {

		camera2D.set(width, height).update();
		chessBox.setScreenDim(width, height);

		for (AppSubProgram<MainActivity> asp : subPrograms) {
			asp.onSurfaceChange(gl, context, width, height);
		}

	}



	@Override
	public final void onDraw(GL10 gl, MainActivity context, int width, int height) {

		camera2D.setPosY_absolute(0).update();
		GLESUtils.clear();
		chessBox.render(camera2D);

		if (uCan) {
			for (AppSubProgram<MainActivity> asp : subPrograms) {
				asp.render(gl, context, camera2D, width, height);
			}
		}


	}


	@Override
	public void setObservator(OPallObserver observator) {
		this.observator = observator;
	}


	@Override
	public long getID() {
		return id;
	}





	@Override
	public void acceptRequest(Request request) {

		request.openRequest(send_bitmap_to_program, () -> {
			uCan = true;
			requestSender.sendRequest(new Request(set_first_image, (Bitmap)request.getData()));
			observator.update();
		});
	}



}
