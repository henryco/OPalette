package net.henryco.opalette.application.programs;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.ChessBox;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.lambda.consumers.OPallTriConsumer;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.api.utils.requester.RequestSender;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.programs.sub.programs.first.FirstStageProgram;
import net.henryco.opalette.application.programs.sub.programs.gradient.GradientBarProgram;
import net.henryco.opalette.application.programs.sub.programs.image.ImageProgram;
import net.henryco.opalette.application.programs.sub.programs.line.ShapeLinesProgram;
import net.henryco.opalette.application.programs.sub.programs.palette.PaletteBarProgram;
import net.henryco.opalette.application.proto.AppMainProto;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 01/03/17.
 */

public class ProgramPipeLine implements OPallUnderProgram<AppMainProto>, AppSubProtocol, AppSubProgram.AppSubProgramHolder {



	public interface AppProgramProtocol {
		int send_bitmap_to_program = 233938111;
	}


	private final long id;
	private boolean uCan = false;

	private Camera2D camera2D;
	private ChessBox chessBox;

	private OPallUpdObserver observator;
	private RequestSender requestSender;
	private List<AppSubProgram> subPrograms;
	private List<OPallTriConsumer<AppMainProto, Integer, Integer>> onDrawQueue;


	@SuppressWarnings("unchecked")
	public static AppSubProgram[] getDefaultPipeLineArray() {

		return new AppSubProgram[]{

				new FirstStageProgram(),
				new ImageProgram(),
				new ShapeLinesProgram(),
				new GradientBarProgram(),
				new PaletteBarProgram()
		};
	}


	public ProgramPipeLine(){
		this(OPallUnderProgram.methods.genID());
	}
	public ProgramPipeLine(long id){

		this.id = id;

		subPrograms = new ArrayList<>();
		onDrawQueue = new ArrayList<>();
		requestSender = new RequestSender();

		AppSubProgram[] pipeLine = getDefaultPipeLineArray();
		for (AppSubProgram abs : pipeLine) {
			addSubProgram(abs);
		}

	}


	@Override
	public void addSubProgram(AppSubProgram p) {
		requestSender.addRequestListener(p);
		p.setFeedBackListener(requestSender);
		p.setProgramHolder(this);
		p.setID(subPrograms.size());
		subPrograms.add(p);
	}

	@Override
	public void setSubProgram(AppSubProgram p, int i) {
		requestSender.addRequestListener(p);
		p.setFeedBackListener(requestSender);
		p.setProgramHolder(this);
		subPrograms.set(i, p);
		for (int k = 0; k < subPrograms.size(); k++)
			subPrograms.get(k).setID(k);
	}

	@Override
	public boolean removeSubProgram(AppSubProgram p) {
		boolean o = subPrograms.remove(p);
		if (o) for (int k = 0; k < subPrograms.size(); k++)
			subPrograms.get(k).setID(k);
		return o;
	}

	@Override
	public boolean removeSubProgram(int i) {
		try {
			subPrograms.remove(i);
		} catch (Exception e) {
			return false;
		} for (int k = 0; k < subPrograms.size(); k++)
			subPrograms.get(k).setID(k);
		return true;
	}





	@Override
	public final void create(GL10 gl, int width, int height, AppMainProto context) {

		System.out.println("OpenGL version is: "+ GLES20.glGetString(GLES20.GL_VERSION));
		System.out.println("GLSL version is: "+ GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION));

		FrameBuffer.debug = true;

		camera2D = new Camera2D(width, height, true);
		chessBox = new ChessBox();

		OPallRenderable renderData = null;
		for (AppSubProgram asp : subPrograms) {
			asp.setRenderData(renderData);
			asp.create(gl, width, height, context);
			renderData = asp.getRenderData();
		}
	}



	@Override
	public final void onSurfaceChange(GL10 gl, AppMainProto context, int width, int height) {

		camera2D.set(width, height).update();
		chessBox.setScreenDim(width, height);

		OPallRenderable renderData = null;
		for (AppSubProgram asp : subPrograms) {
			asp.setRenderData(renderData);
			asp.onSurfaceChange(gl, context, width, height);
			renderData = asp.getRenderData();
		}
	}



	@Override
	public final void onDraw(GL10 gl, AppMainProto context, int width, int height) {

		for (int i = 0; i < onDrawQueue.size(); i++) {
			onDrawQueue.remove(i).consume(context, width, height);
		}

		camera2D.setPosY_absolute(0).update();
		GLESUtils.clear();
		chessBox.render(camera2D);

		if (uCan) {
			OPallRenderable renderData = null;
			for (AppSubProgram asp : subPrograms) {
				asp.setRenderData(renderData);
				asp.render(gl, context, camera2D, width, height);
				renderData = asp.getRenderData();
			}
		}
	}


	@Override
	public void setObservator(OPallUpdObserver observator) {
		this.observator = observator;
	}


	@Override
	public long getID() {
		return id;
	}





	@Override
	public void acceptRequest(Request request) {

		request.openRequest(AppProgramProtocol.send_bitmap_to_program, () -> {
			onDrawQueue.add((appMainProto, width, height) -> {
				Bitmap bitmap = request.getData();
				Texture startImage = new Texture();
				startImage.setScreenDim(width, height);
				startImage.setBitmap(bitmap);
				bitmap.recycle();
				bitmap = null;
				float scrWidth = startImage.getScreenWidth();
				float w = startImage.getWidth();
				float h = startImage.getHeight();
				float scale = scrWidth / w;
				subPrograms.get(0).setRenderData(startImage.bounds(b -> b.setScale(scale)));
				requestSender.sendRequest(new Request(set_touch_lines_def_size, scrWidth, h * scale));
			});
			uCan = true;
			observator.update();
		});
	}



}
