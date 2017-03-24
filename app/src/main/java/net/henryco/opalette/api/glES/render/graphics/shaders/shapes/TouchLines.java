package net.henryco.opalette.api.glES.render.graphics.shaders.shapes;


import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.OPallGeometry;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

/**
 * Created by HenryCo on 07/03/17.
 */

public class TouchLines extends OPallShape {


	private static final String VERT_PROGRAM =
			"#version 100\n" +
			"\n" +
			"attribute vec4 a_Position;\n" +
			"uniform mat4 u_MVPMatrix;\n" +
			"\n" +
			"void main() {\n" +
			"    gl_Position = u_MVPMatrix * a_Position;\n" +
			"}";

	private static final String FRAG_PROGRAM =
			"#version 100\n" +
			"\n" +
			"precision mediump float;\n" +
			"\n" +
			"uniform vec2 u_dimension;\n" +
			"uniform vec3 u_line[2]; // Ax + By + C = 0\n" +
			"uniform vec3 u_color;\n" +
			"uniform float u_dx;\n" +
			"uniform float u_transparency;\n" +
			"uniform float u_lines_a;\n" +
			"uniform int u_type;\n" +
			"\n" +
			"float line(float n, float x) {\n" +
			"  return (u_lines_a * (x - (u_dx * n)));\n" +
			"}\n" +
			"\n" +
			"vec3 colorize(vec2 position) {\n" +
			"    if (u_type == 1) return vec3(normalize(vec3(position.x, 1, position.y)));\n" +
			"    if (u_type == 2) {\n" +
			"        vec2 xy = position / u_dimension;\n" +
			"        return normalize(vec3(xy, 1. - length(xy) / sqrt(2.)));\n" +
			"    }\n" +
			"    return u_color;\n" +
			"}\n" +
			"\n" +
			"void main() {\n" +
			"  \n" +
			"  vec2 pos = vec2(gl_FragCoord.x, u_dimension.y - gl_FragCoord.y);\n" +
			"  float py1 = (-1.) * ((u_line[0].x * pos.x) + u_line[0].z) / u_line[0].y;\n" +
			"  float py2 = (-1.) * ((u_line[1].x * pos.x) + u_line[1].z) / u_line[1].y;\n" +
			"\n" +
			"  if (!((pos.y > py1 && pos.y < py2) || (pos.y > py2 && pos.y < py1))) {\n" +
			"\n" +
			"    vec3 color = colorize(pos);\n" +
			"\n" +
			"    float n1 = ceil(pos.x / u_dx);\n" +
			"    float dy = line(1.,0.);\n" +
			"    float n2 = ceil(pos.y / dy);\n" +
			"    float n = (n1 + n2) - 1.;\n" +
			"    float rest1 = mod(n1, 2.);\n" +
			"    float rest2 = mod(n2, 2.);\n" +
			"    float y_n = line(n, pos.x);\n" +
			"\n" +
			"    if (((rest1 != 0. && rest2 != 0.) || (rest1 == 0. && rest2 == 0.)) && (pos.y > y_n))\n" +
			"        gl_FragColor = vec4(color, u_transparency);\n" +
			"    else if (((rest1 != 0. && rest2 == 0.) || (rest1 == 0. && rest2 != 0.)) && (pos.y < y_n))\n" +
			"        gl_FragColor = vec4(color, u_transparency);\n" +
			"  }\n" +
			"  else gl_FragColor = vec4(0.);\n" +
			"}";


	private static final String u_dimension = "u_dimension";
	private static final String u_dx = "u_dx";
	private static final String u_transparency = "u_transparency";
	private static final String u_lines_a = "u_lines_a";
	private static final String u_line = "u_line";
	private static final String u_type = "u_type";
	private static final String u_color = "u_color";


	public enum TouchType {
		NORMALS(1), GRADIENT(2), COLOR(0);
		public int type;
		TouchType(int type) {
			this.type = type;
		}
	}


	private float defaultWidth = 0;
	private float defaultHeight = 0;

	private final GLESUtils.Color color;

	private float[] linesCoefficients;

	private float lines_dx = 10;
	private float lines_transparency = 0.65f;
	private float lines_a = -1;
	private int type = 1;
	private boolean visible = false;


	public TouchLines() {
		this(0,0);
	}
	public TouchLines(int w, int h) {
		super(VERT_PROGRAM, FRAG_PROGRAM, 2);
		color = new GLESUtils.Color();
		setScreenDim(w, h);
		reset();
	}

	@Override
	protected void render(int glProgram, Camera2D camera, OPallConsumer<Integer> setter) {
		if (visible) super.render(glProgram, camera, setter);
	}

	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform1i(GLES20.glGetUniformLocation(program, u_type), type);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_dx), lines_dx);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_lines_a), lines_a);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_transparency), lines_transparency);
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), getWidth(), getHeight());
		GLES20.glUniform3f(GLES20.glGetUniformLocation(program, u_color), color.r, color.g, color.b);
		GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, u_line), 2, linesCoefficients, 0);
	}

	/**
	 * Calculate region bounded by 2 perpendicular lines between 2 fingers position.<br>
	 * (finger1) <----------> (finger2) <br>
	 * @param p1 point where is first finger
	 * @param p2 point where is second finger
	 */
	public TouchLines setPoints(float[] p1, float[] p2) {

		float x1 = p1[0] == p2[0] ? p1[0] + 0.01f : p1[0];
		float y1 = p1[1] == p2[1] ? p1[1] + 0.01f : p1[1];
		float x2 = p2[0];
		float y2 = p2[1];

		float[] c1 = OPallGeometry.lineABC_cmnPerp(x1, y1, x2, y2);
		float[] c2 = OPallGeometry.lineABC_cmnPerp(x2, y2, x1, y1);

		linesCoefficients = new float[] {
				c1[0], c1[1], c1[2], c2[0], c2[1], c2[2]};
		return this;
	}

	public float[] getCoefficients() {
		return linesCoefficients;
	}

	public TouchLines reset(float width, float height) {
		return setPoints(
				new float[]{0, 0}, new float[]{0, height}
		);
	}

	public TouchLines reset() {

		return reset(
				defaultWidth > 0 ? defaultWidth : getWidth(),
				defaultHeight > 0 ? defaultHeight : getHeight()
		);
	}


	public TouchLines setDefaultSize(float w, float h) {
		defaultWidth = w;
		defaultHeight = h;
		return this;
	}


	public TouchLines setLines_dx(float lines_dx) {
		this.lines_dx = lines_dx;
		return this;
	}

	public TouchLines setLines_transparency(float lines_transparency) {
		this.lines_transparency = lines_transparency;
		return this;
	}

	public TouchLines setLines_a(float lines_a) {
		this.lines_a = lines_a;
		return this;
	}

	public TouchLines setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public TouchLines setColor(GLESUtils.Color color) {
		this.color.set(color);
		return this;
	}

	public TouchLines setType(TouchType type) {
		this.type = type.type;
		return this;
	}
}
