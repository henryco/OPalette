package net.henryco.opalette.api.glES.render.graphics.shaders.shapes;

import android.content.Context;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.OPallShader;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.bounds.Bounds2D;
import net.henryco.opalette.api.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;


/**
 * Created by HenryCo on 05/03/17.
 */

public class ChessBox extends OPallShape {

	private static final String FRAG_FILE =
			"#version 100\n" +
			"\n" +
			"precision mediump float;\n" +
			"\n" +
			"uniform vec4 u_color[2];\n" +
			"uniform float u_cellSize;\n" +
			"\n" +
			"void main() {\n" +
			"    vec2 position = gl_FragCoord.st / u_cellSize;\n" +
			"    float px_m = (mod(ceil(position.x), 2.));\n" +
			"    float py_m = (mod(ceil(position.y), 2.));\n" +
			"\n" +
			"    if ((px_m != 0. && py_m != 0.) || (px_m == 0. && py_m == 0.))\n" +
			"        gl_FragColor = u_color[0];\n" +
			"    else\n" +
			"        gl_FragColor = u_color[1];\n" +
			"}";
	private static final String VERT_FILE =
			"#version 100\n" +
			"\n" +
			"attribute vec4 a_Position;\n" +
			"uniform mat4 u_MVPMatrix;\n" +
			"\n" +
			"void main() {\n" +
			"    gl_Position = u_MVPMatrix * a_Position;\n" +
			"}";

	private static final String FILE = OPallShader.SHADERS_DIR+"/shapes/chessBox/ChessBox";
	private static final String u_cellSize = "u_cellSize";
	private static final String u_color = "u_color";




	private final FrameBuffer imageBuffer;
	private final GLESUtils.Color[] colors;

	private float[] colorsArray;
	private float cellSize;
	private boolean needUpDate;



	public ChessBox(Context context) {
		//super(context, FILE+".vert", FILE+".frag", 2);
		super(VERT_FILE, FRAG_FILE, 2);
		cellSize = 25;
		needUpDate = false;
		imageBuffer = OPallFBOCreator.FrameBuffer(context);
		colors = new GLESUtils.Color[]{GLESUtils.Color.GREY, GLESUtils.Color.SILVER};
		colorsUpDate();
	}


	public ChessBox setColor(int n, GLESUtils.Color color) {
		colors[n].set(color);
		colorsUpDate();
		return this;
	}

	public ChessBox setColor(GLESUtils.Color color1, GLESUtils.Color color2) {
		colors[0].set(color1);
		colors[1].set(color2);
		colorsUpDate();
		return this;
	}

	public ChessBox setCellSize(float size) {
		this.cellSize = size;
		needUpDate = true;
		return this;
	}


	private void colorsUpDate() {
		colorsArray = new float[]{
				colors[0].r, colors[0].g, colors[0].b, colors[0].a,
				colors[1].r, colors[1].g, colors[1].b, colors[1].a,
		};
		needUpDate = true;
	}


	public ChessBox create(int scr_width, int scr_height) {
		super.setScreenDim(scr_width, scr_height);
		if (scr_width != 0 && scr_height != 0) {
			imageBuffer.createFBO(scr_width, scr_height, false);
			needUpDate = true;
		}
		return this;
	}




	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_cellSize), cellSize);
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, u_color), 2, colorsArray, 0);
	}


	@Override
	protected void render(int glProgram, Camera2D camera, OPallConsumer<Integer> setter) {

		if (needUpDate) {
			imageBuffer.beginFBO(() -> {
				GLESUtils.clear(GLESUtils.Color.TRANSPARENT);
				super.render(glProgram, camera, setter);
			});
			needUpDate = false;
		}
		camera.backTranslate(() -> {
			camera.setPosXY_absolute(0,0);
			imageBuffer.render(camera);
		});

	}




	@Override
	public ChessBox bounds(BoundsConsumer<Bounds2D> processor) {
		super.bounds(processor);
		needUpDate = true;
		return this;
	}

	@Override
	public ChessBox updateBounds() {
		super.updateBounds();
		needUpDate = true;
		return this;
	}




	@Override
	public void setScreenDim(float w, float h) {
		create((int) w, (int)h);
	}



}
