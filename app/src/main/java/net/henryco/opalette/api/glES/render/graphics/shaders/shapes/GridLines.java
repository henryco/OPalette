package net.henryco.opalette.api.glES.render.graphics.shaders.shapes;

import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.bounds.Bounds2D;
import net.henryco.opalette.api.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

/**
 * Created by HenryCo on 24/03/17.
 */

public class GridLines extends OPallShape {

	private static final String VERTEX = "#version 100\n" +
			"\n" +
			"attribute vec4 a_Position;\n" +
			"uniform mat4 u_MVPMatrix;\n" +
			"\n" +
			"void main() {\n" +
			"    gl_Position = u_MVPMatrix * a_Position;\n" +
			"}";
	private static final String FRAGMENT = "#version 100\n" +
			"precision mediump float;\n" +
			"\n" +
			"uniform float u_halfLineSize;\n" +
			"uniform float u_cellW;\n" +
			"uniform float u_cellH;\n" +
			"uniform vec4 u_gridColor;\n" +
			"uniform vec2 u_dimension;\n" +
			"uniform int u_type;\n" +
			"\n" +
			"\n" +
			"vec4 colorize(vec2 position) {\n" +
			"    if (u_type == 1) return vec4(normalize(vec3(position.x, 1, position.y)), 1.);\n" +
			"    if (u_type == 2) {\n" +
			"        vec2 xy = position / u_dimension;\n" +
			"        return vec4(normalize(vec3(xy, 1. - length(xy) / sqrt(2.))), 1.);\n" +
			"    }   return u_gridColor;\n" +
			"}\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"  vec2 pos = gl_FragCoord.xy;\n" +
			"  float nx = floor(pos.x / u_cellW);\n" +
			"  float ny = floor(pos.y / u_cellH);\n" +
			"\n" +
			"  float left_x = (nx * u_cellW) + u_halfLineSize;\n" +
			"  float right_x = ((nx + 1.) * u_cellW) - u_halfLineSize;\n" +
			"\n" +
			"  float bot_y = (ny * u_cellH) + u_halfLineSize;\n" +
			"  float top_y = ((ny + 1.) * u_cellH) - u_halfLineSize;\n" +
			"\n" +
			"  if (pos.x <= left_x || pos.x >= right_x)\n" +
			"    gl_FragColor = colorize(pos);\n" +
			"  else if (pos.y <= bot_y || pos.y >= top_y)\n" +
			"    gl_FragColor = colorize(pos);\n" +
			"  else gl_FragColor = vec4(0.);\n" +
			"}";
	private static final String u_halfLineSize = "u_halfLineSize";
	private static final String u_gridColor = "u_gridColor";
	private static final String u_dimension = "u_dimension";
	private static final String u_cellW = "u_cellW";
	private static final String u_cellH = "u_cellH";
	private static final String u_type = "u_type";


	private final FrameBuffer imageBuffer;
	private final GLESUtils.Color color;
	private boolean needUpDate;
	private boolean visible;
	private float lineSize;
	private float n;
	private int type;


	public GridLines() {
		this(0, 0);
	}
	public GridLines(int w, int h) {
		super(VERTEX, FRAGMENT);
		imageBuffer = OPallFBOCreator.FrameBuffer();
		color = new GLESUtils.Color(GLESUtils.Color.GREY);
		setGridNumber(5).setLineSize(4).setScreenDim(w, h);
		setVisible(false).setType(ColorType.NORMALIZED);
	}


	public GridLines create(int scr_width, int scr_height) {
		super.setScreenDim(scr_width, scr_height);
		if (scr_width != 0 && scr_height != 0) {
			imageBuffer.createFBO(scr_width, scr_height, false);
			needUpDate = true;
		}	return this;
	}

	public GridLines setLineSize(float lineSize) {
		this.lineSize = lineSize;
		needUpDate = true;
		return this;
	}

	public GridLines setGridNumber(float n) {
		needUpDate = true;
		this.n = n;
		return this;
	}

	public GridLines setColor(GLESUtils.Color color) {
		this.color.set(color);
		needUpDate = true;
		return this;
	}

	public GridLines setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public GridLines setType(ColorType type) {
		this.type = type.type;
		needUpDate = true;
		return this;
	}

	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform4f(GLES20.glGetUniformLocation(program, u_gridColor), color.r, color.g, color.b, color.a);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_halfLineSize), lineSize * 0.5f);
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), getScreenWidth(), getScreenHeight());
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_cellW), getScreenWidth() / n);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_cellH), getScreenHeight() / n);
		GLES20.glUniform1i(GLES20.glGetUniformLocation(program, u_type), type);
	}

	@Override
	protected void render(int glProgram, Camera2D camera, OPallConsumer<Integer> setter) {

		if (visible) {
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
	}

	@Override
	public GridLines bounds(BoundsConsumer<Bounds2D> processor) {
		super.bounds(processor);
		needUpDate = true;
		return this;
	}

	@Override
	public GridLines updateBounds() {
		super.updateBounds();
		needUpDate = true;
		return this;
	}

	@Override
	public void setScreenDim(float w, float h) {
		create((int) w, (int)h);
	}
}
