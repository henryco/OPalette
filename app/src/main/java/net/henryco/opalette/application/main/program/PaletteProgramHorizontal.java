package net.henryco.opalette.application.main.program;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
import net.henryco.opalette.api.glES.render.graphics.bar.BarHorizontal;
import net.henryco.opalette.api.glES.render.graphics.bar.OPallBar;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.ChessBox;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.TouchLines;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.MultiTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallMultiTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
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
	public static final String VERT_FILE = OPallMultiTexture.DEF_SHADER+".vert";
	public static final String FRAG_FILE = OPallMultiTexture.FRAG_DIR+"/StdPaletteHorizontal.frag";
	public static final String u_dimension = "u_dimension";
	public static final String u_line = "u_line";

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

	private TouchLines touchLines;

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
		touchLines = new TouchLines(width, height);
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
		touchLines.setScreenDim(width, height);
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
					GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), width, height - backBar.getHeight());
					GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, u_line), 2, touchLines.getCoefficients(), 0);
				});
			});


			touchLines.render(camera2D);

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
			uCan = true;

			touchLines.setVisible(true).setDefaultSize(barWidth, bmpHeight * scale).reset();
		});
	}








	public static final String FRAG_PROGRAM =
			"#version 100\n" +
			"precision mediump float;\n" +
			"\n" +
			"varying vec4 v_Position;\n" +
			"varying vec4 v_WorldPos;\n" +
			"varying vec2 v_TexCoordinate[5];\n" +
			"\n" +
			"uniform sampler2D u_Texture0;\n" +
			"uniform sampler2D u_Texture1;\n" +
			"uniform sampler2D u_Texture2;\n" +
			"uniform sampler2D u_Texture3;\n" +
			"uniform sampler2D u_Texture4;\n" +
			"\n" +
			"uniform vec2 u_dimension;\n" +
			"uniform int u_texNumb;\n" +
			"uniform vec3 u_line[2]; // Ax + By + C = 0\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    float trueHeight = 0.0;\n" +
			"    vec3 p_col = vec3(0.0);\n" +
			"\n" +
			"    for (float y = 0.0; y < u_dimension.y; y += 1.0) {\n" +
			"\n" +
			"        vec2 pointNormed = vec2(v_TexCoordinate[1].s, y / u_dimension.y);\n" +
			"        vec2 point = vec2(pointNormed.x * u_dimension.x, u_dimension.y * (1. - pointNormed.y));\n" +
			"        vec4 pointColor = texture2D(u_Texture0, pointNormed).rgba;\n" +
			"\n" +
			"        if (pointColor.a != 0.0) {\n" +
			"\n" +
			"            float py1 = (-1.) * ((u_line[0].x * point.x) + u_line[0].z) / u_line[0].y;\n" +
			"            float py2 = (-1.) * ((u_line[1].x * point.x) + u_line[1].z) / u_line[1].y;\n" +
			"\n" +
			"            if ((point.y > py1 && point.y < py2) || (point.y > py2 && point.y < py1)) {\n" +
			"                p_col += pointColor.rgb;\n" +
			"                trueHeight += 1.0;\n" +
			"            }\n" +
			"        }\n" +
			"    }\n" +
			"\n" +
			"    vec3 color = p_col / max(trueHeight, 1.0);\n" +
			"    gl_FragColor = vec4(color, 1.0);\n" +
			"}";

}
