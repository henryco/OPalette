/*
 *   /*
 *    * Copyright (C) Henryk Timur Domagalski
 *    *
 *    * Licensed under the Apache License, Version 2.0 (the "License");
 *    * you may not use this file except in compliance with the License.
 *    * You may obtain a copy of the License at
 *    *
 *    *      http://www.apache.org/licenses/LICENSE-2.0
 *    *
 *    * Unless required by applicable law or agreed to in writing, software
 *    * distributed under the License is distributed on an "AS IS" BASIS,
 *    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    * See the License for the specific language governing permissions and
 *    * limitations under the License.
 *
 */

package net.henryco.opalette.api.glES.render.graphics.shaders.shapes;

import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 24/03/17.
 */

public class GridLines extends OPallShapeBuffered {

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


	private final GLESUtils.Color color;
	private float lineSize;
	private float n;
	private int type;

	private final float[] defDim = {0,0};

	public GridLines() {
		this(0, 0);
	}
	public GridLines(int w, int h) {
		super(VERTEX, FRAGMENT);
		color = new GLESUtils.Color(GLESUtils.Color.GREY);
		setGridNumber(5).setLineSize(4).create(w, h);
		setType(ColorType.NORMALIZED).setVisible(false);
	}




	public GridLines setLineSize(float lineSize) {
		this.lineSize = lineSize;
		update();
		return this;
	}

	public GridLines setGridNumber(float n) {
		update();
		this.n = n;
		return this;
	}

	public GridLines setColor(GLESUtils.Color color) {
		this.color.set(color);
		update();
		return this;
	}

	public GridLines setType(ColorType type) {
		this.type = type.type;
		update();
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


}
