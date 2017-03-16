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
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.activities.MainActivity;
import net.henryco.opalette.application.programs.controlls.PPHTranslation;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.programs.sub.GradientBarProgram;
import net.henryco.opalette.application.programs.sub.ImageProgram;
import net.henryco.opalette.application.programs.sub.PaletteBarProgram;
import net.henryco.opalette.application.programs.sub.TouchLinesProgram;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */

public class PaletteProgramHorizontal implements OPallUnderProgram<MainActivity>, AppSubProtocol {


	private final long id;
	private boolean uCan = false;

	private Camera2D camera2D;
	private ChessBox chessBox;

	private OPallObserver observator;

	private RequestSender requestSender;

	private List<AppSubProgram<MainActivity>> subPrograms;


	public PaletteProgramHorizontal(){
		this(OPallUnderProgram.methods.genID());
	}
	public PaletteProgramHorizontal(long id){
		this.id = id;

		subPrograms = new ArrayList<>();

		requestSender = new RequestSender();

		ImageProgram imageProgram = new ImageProgram();
		requestSender.addRequestListener(imageProgram);
		imageProgram.setFeedBackListener(requestSender);
		subPrograms.add(imageProgram);

		GradientBarProgram gradientBarProgram = new GradientBarProgram();
		requestSender.addRequestListener(gradientBarProgram);
		gradientBarProgram.setFeedBackListener(requestSender);
		subPrograms.add(gradientBarProgram);

		TouchLinesProgram touchLinesProgram = new TouchLinesProgram();
		requestSender.addRequestListener(touchLinesProgram);
		touchLinesProgram.setFeedBackListener(requestSender);
		subPrograms.add(touchLinesProgram);

		PaletteBarProgram paletteBarProgram = new PaletteBarProgram();
		requestSender.addRequestListener(paletteBarProgram);
		paletteBarProgram.setFeedBackListener(requestSender);
		subPrograms.add(paletteBarProgram);




	}





	@Override
	public final void create(GL10 gl, int width, int height, MainActivity context) {

		OPallViewInjector.inject(context, new PPHTranslation());


		System.out.println("OpenGL version is: "+ GLES20.glGetString(GLES20.GL_VERSION));
		System.out.println("GLSL version is: "+ GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION));

		FrameBuffer.debug = true;

		camera2D = new Camera2D(width, height, true);
		chessBox = new ChessBox();

		for (AppSubProgram<MainActivity> asp : subPrograms)
			asp.create(gl, width, height, context);


	}



	@Override
	public final void onSurfaceChange(GL10 gl, MainActivity context, int width, int height) {

		camera2D.set(width, height).update();
		chessBox.setScreenDim(width, height);

		for (AppSubProgram<MainActivity> asp : subPrograms)
			asp.onSurfaceChange(gl, context, width, height);

	}



	@Override
	public final void onDraw(GL10 gl, MainActivity context, int width, int height) {

		//RESET CAMERA
		camera2D.setPosY_absolute(0).update();

		GLESUtils.clear(GLESUtils.Color.TRANSPARENT);
		chessBox.render(camera2D);

		if (uCan) {
			for (AppSubProgram<MainActivity> asp : subPrograms)
				asp.render(gl, context, camera2D, width, height);
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
		request.openRequest("loadImage", () -> {

			Bitmap image = request.getData();
			requestSender.sendRequest(new Request(set_first_image, image));

			uCan = true;
		});
	}



}
