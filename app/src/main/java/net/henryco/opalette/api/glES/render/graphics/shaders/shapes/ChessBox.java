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
import net.henryco.opalette.api.glES.render.graphics.shaders.OPallShader;
import net.henryco.opalette.api.utils.GLESUtils;


/**
 * Created by HenryCo on 05/03/17.
 */

public class ChessBox extends OPallShapeBuffered {

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




	private final GLESUtils.Color[] colors;

	private float[] colorsArray;
	private float cellSize;

	private final float[] defDim = {0,0};

	public ChessBox(int w, int h) {
		this();
		create(w, h);
	}

	public ChessBox() {
		super(VERT_FILE, FRAG_FILE);
		cellSize = 25;
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
		update();
		return this;
	}


	private void colorsUpDate() {
		colorsArray = new float[]{
				colors[0].r, colors[0].g, colors[0].b, colors[0].a,
				colors[1].r, colors[1].g, colors[1].b, colors[1].a,
		};
		update();
	}



	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_cellSize), cellSize);
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, u_color), 2, colorsArray, 0);
	}





}
